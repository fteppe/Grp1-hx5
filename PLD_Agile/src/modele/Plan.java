package modele;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import tsp.TSPPlages;

public class Plan extends Observable {
    private HashMap<Integer, Intersection> listeIntersections; // Liste des
							       // intersections
							       // du plan
							       // classees selon
							       // leur
							       // identifiant
    private HashMap<Integer, List<Troncon>> listeTroncons; // Liste des troncons
							   // du plan classes
							   // selon
							   // l'identifiant de
							   // leur origine
    private DemandeDeLivraison demandeDeLivraison;
    private Tournee tournee;
    private ArrayList<Integer> idSommets;
    private AlgoDijkstra algo;
    private boolean calculTourneeEnCours;
    private int tpsAttente = 1;

    /**
     * Cree un plan ne possedant aucune intersection et aucun troncon
     */
    public Plan() {
	this.listeIntersections = new HashMap<Integer, Intersection>();
	this.listeTroncons = new HashMap<Integer, List<Troncon>>();
	this.calculTourneeEnCours = false;
    }

    /**
     * @return Renvoie l'Heure de départ de la demande/tournée sous forme de
     *         String
     */
    public String afficherHeureDepart() {
	if (tournee != null)
	    return tournee.getHeureDebut().toString();
	if (demandeDeLivraison != null)
	    return demandeDeLivraison.getHeureDepart().toString();
	return "NC";
    }

    /**
     * @return Renvoie l'Heure de retour de la tournée sous forme de String
     */
    public String afficherHeureRetour() {
	if (tournee != null) {
	    return tournee.getHeureFin().toString();
	}
	return "NC";
    }

    /**
     * Arrête le calcul de la tournée en cours si il existe
     */
    public void arreterCalculTournee() {
	this.calculTourneeEnCours = false;
    }

    /**
     * Calcule la tournee (algo Dijkstra et TSP) si possible et la cree
     * 
     * @param tpsLimite
     *            Temps maximum en millisecondes pour le calcul du parcours
     *            optimal
     * @return true Si une tournee a ete trouvee, false si aucune tournee n'a
     *         ete trouvee
     * @throws ExceptionTournee
     */
    public boolean calculerTournee() throws ExceptionTournee {
	if (this.demandeDeLivraison == null) {
	    throw new ExceptionTournee("Aucune demande de livraison n'a été chargée");
	}
	// On initialise l'algo de Dijkstra
	algo = AlgoDijkstra.getInstance();
	algo.chargerAlgo(listeIntersections, listeTroncons);
	// On recupere la liste des identifiants des sommets devant constituer
	// le graphe complet analyse par le TSP
	idSommets = completionTableauLivraison();
	// On constitue un graphe complet grace a l'algorithme de Dijkstra
	Object[] resultDijkstra = algo.calculerDijkstra(idSommets);
	// On initialise les variables à fournir au TSP
	TSPPlages tsp = new TSPPlages();
	int[] durees = recupererDurees(idSommets);
	int[][] couts = (int[][]) resultDijkstra[0];

	int[] plageDepart = new int[idSommets.size()];
	int[] plageFin = new int[idSommets.size()];

	plageDepart[0] = 0;
	plageFin[0] = Integer.MAX_VALUE;

	for (int i = 1; i < idSommets.size(); i++) {

	    if (this.getHashMapLivraisonsDemande().get(idSommets.get(i)).possedePlage()) {
		plageDepart[i] = this.getHashMapLivraisonsDemande().get(idSommets.get(i)).getDebutPlage().toSeconds();
		plageFin[i] = this.getHashMapLivraisonsDemande().get(idSommets.get(i)).getFinPlage().toSeconds();
	    } else {
		plageDepart[i] = 0;
		plageFin[i] = Integer.MAX_VALUE;
	    }
	}

	// On verifie que tous les sommets sont atteignables à partir de
	// l'entrepot
	for (int i = 1; i < idSommets.size(); i++) {
	    if (couts[0][i] == Integer.MAX_VALUE) {
		throw new ExceptionTournee("L'intersection d'identifiant " + idSommets.get(i)
			+ "n'est pas atteignable à partir de l'entrepôt");
	    }
	}

	System.out.println("Entree calcul tournée");
	this.calculTourneeEnCours = true;

	// On lance le calcul de la tournee dans un nouveau thread
	Callable<Boolean> calculTournee = () -> {
	    // On cherche l'itineraire optimal via l'utilisation du TSP
	    tsp.chercheSolution(idSommets.size(), couts, durees, plageDepart, plageFin,
		    this.demandeDeLivraison.getHeureDepart().toSeconds());
	    return tsp.getCoutMeilleureSolution() != Integer.MAX_VALUE;
	};

	ExecutorService executorCalculTournee = Executors.newFixedThreadPool(2);

	Future<Boolean> futureCalculTournee = executorCalculTournee.submit(calculTournee);

	// On recupere la meilleure tournee calculee a intervalle de temps
	// regulier dans un autre thread
	tournee = new Tournee(demandeDeLivraison.getHeureDepart());
	Callable<Boolean> recuperationMeilleurResultat = () -> {
	    boolean tourneeTrouvee = false;
	    while (this.calculTourneeEnCours == true) {
		try {
		    TimeUnit.SECONDS.sleep(tpsAttente);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		boolean calculTermine = futureCalculTournee.isDone();
		tsp.lock();
		int dureeTournee = tsp.getCoutMeilleureSolution();
		if (this.tournee.getDuree() != dureeTournee) {
		    System.out.println("Meilleur résultat");
		    System.out.println(dureeTournee);
		    tourneeTrouvee = true;
		    int[] ordreTournee = new int[idSommets.size()];
		    for (int i = 0; i < idSommets.size(); i++) {
			ordreTournee[i] = tsp.getMeilleureSolution(i);
		    }
		    tsp.unlock();
		    Itineraire[][] trajets = (Itineraire[][]) resultDijkstra[1];
		    tournee.mettreAJourTournee(dureeTournee, ordreTournee, trajets,
			    this.demandeDeLivraison.getHashMapLivraisons(), idSommets);
		    setChanged();
		    notifyObservers(this.tournee);
		} else {
		    tsp.unlock();
		}
		// Si le calcul est termine, on arrête de chercher une
		// nouvelle tournee
		if (calculTermine) {
		    System.out.println("Calcul terminé");
		    this.calculTourneeEnCours = false;
		    try {
			tourneeTrouvee = futureCalculTournee.get();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    } catch (ExecutionException e) {
			e.printStackTrace();
		    }
		    System.out.println(tourneeTrouvee);
		}
	    }
	    tsp.setCalculEnCours(false);
	    return tourneeTrouvee;
	};

	Future<Boolean> futureRecuperationMeilleurResultat = executorCalculTournee.submit(recuperationMeilleurResultat);

	executorCalculTournee.shutdown();
	boolean tourneeTrouvee = false;
	// On attend la fin de l'execution des deux thread lances
	try {
	    executorCalculTournee.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
	    tourneeTrouvee = futureRecuperationMeilleurResultat.get();
	    System.out.println(tourneeTrouvee);
	    /*
	     * if (this.tournee.getDuree() == Integer.MAX_VALUE &&
	     * tpsLimiteAtteint == false) { throw new ExceptionTournee(
	     * "Aucune tournée n'a été trouvée. " +
	     * "Veuillez recommencer avec de nouvelles plages horaires"); }
	     */
	} catch (InterruptedException | ExecutionException e) {
	    e.printStackTrace();
	}
	return tourneeTrouvee;

    }

    /**
     * Renvoie l'ObjetGraphique positionné au coordonnées du point précisé
     * 
     * @param p
     *            Position de l'ObjetGraphique à rechercher
     * @param tolerance
     *            Intervalle de tolerance de la recherche
     * @return ObjetGraphique aux coordonnées p si il existe, null sinon
     */
    public ObjetGraphique cherche(Point p, int tolerance) {
	ObjetGraphique objGraph = null;
	// On teste le clic sur la liste d'intersections
	for (Intersection inter : listeIntersections.values()) {
	    if (inter.contient(p, tolerance)) {
		objGraph = inter;
		Livraison livAssociee = this.getLivraisonParAdresse(inter.getId());
		if (livAssociee != null)
		    objGraph = livAssociee;
	    }
	}

	return objGraph;
    }

    /**
     * Creation d'un tableau faisant correspondre l'identifiant de chaque
     * intersection avec sa place dans le tableau des couts et des itineraires
     * resultant des calculs de plus court chemin
     * 
     * @return Identifiants des intersections constituant la tournee finale
     */
    private ArrayList<Integer> completionTableauLivraison() {
	ArrayList<Integer> sommets = new ArrayList<>();
	sommets.add(demandeDeLivraison.getEntrepot().getId());
	Set<Integer> cles = this.getHashMapLivraisonsDemande().keySet();
	Iterator<Integer> it = cles.iterator();
	while (it.hasNext()) {
	    Integer cle = it.next();
	    sommets.add(cle);
	}
	return sommets;
    }

    /**
     * Cree et ajoute une demande de livraison au plan
     * 
     * @param heureDepart
     *            Heure de depart de l'entrepot
     * @param entrepot
     *            Identifiant de l'intersection correspondant a l'entrepot
     * @throws ModeleException
     *             Renvoie une exception si la demande ne peut pas être créée
     */
    public void creerDemandeDeLivraison(Heure heureDepart, int entrepot) throws ModeleException {
	if (listeIntersections.get(entrepot) == null)
	    throw new ModeleException("L'adresse de l'entrepôt ne correspond pas à une intersection.");
	this.demandeDeLivraison = new DemandeDeLivraison(heureDepart, this.listeIntersections.get(entrepot));
	if (this.tournee != null) {
	    this.tournee = null;
	}
	setChanged();
	notifyObservers(demandeDeLivraison);
    }

    /**
     * Cree et ajoute une intersection au plan courant
     * 
     * @param id
     *            Identifiant de l'intersection a ajouter
     * @param longitude
     *            Longitude de l'intersection a ajouter
     * @param latitude
     *            Latitude de l'intersection a ajouter
     * @throws ModeleException
     *             Envoie une exception si l'Intersection ne peut pas être
     *             ajoutée
     */
    public void creerIntersection(int id, int longitude, int latitude) throws ModeleException {
	Intersection nouvIntersection = new Intersection(id, longitude, latitude);
	if (longitude < 0 || latitude < 0) {
	    throw new ModeleException("Une intersection possède des coordonnées négatives");
	}
	if (!this.listeIntersections.containsKey(id)) {
	    this.listeIntersections.put(id, nouvIntersection);
	} else {
	    throw new ModeleException("Plusieurs intersections portent l'id " + id + ", seule l'intersection x("
		    + this.listeIntersections.get(id).getLongitude() + ") y("
		    + this.listeIntersections.get(id).getLongitude() + ") a été créée");
	}
	setChanged();
	notifyObservers();
    }

    /**
     * Cree et ajoute une livraison a la demande de livraison associee au Plan
     * 
     * @param adresse
     *            Identifiant de l'intersection correspondant a la livraison a
     *            effectuer
     * @param duree
     *            Duree de la livraison a effectuer
     * @throws ModeleException
     *             Renvoie une exception si la Livraison ne paut pas être créée
     */
    public void creerLivraisonDemande(int adresse, int duree) throws ModeleException {
	if (listeIntersections.get(adresse) == null)
	    throw new ModeleException(
		    "L'adresse " + adresse + " d'une livraison ne correspond pas à une intersection.");
	if (duree < 0)
	    throw new ModeleException("La durée de la livraison à l'adresse " + adresse + " est négative.");
	this.demandeDeLivraison.ajouterLivraison(duree, this.listeIntersections.get(adresse));
	setChanged();
	notifyObservers();
    }

    /**
     * Cree et ajoute une livraison possedant une plage horaire a la demande de
     * livraison associee au Plan
     * 
     * @param adresse
     *            Identifiant de l'intersection correspondant a la livraison a
     *            effectuer
     * @param duree
     *            Duree de la livraison a effectuer
     * @param debutPlage
     *            Debut de la plage horaire de la livraison a effectuer
     * @param finPlage
     *            Fin de la plage horaire de la livraison a effectuer
     * @throws ModeleException
     *             Renvoie une exception si la Livraison ne peut pas être créée
     */
    public void creerLivraisonDemande(int adresse, int duree, String debutPlage, String finPlage)
	    throws ModeleException {
	if (listeIntersections.get(adresse) == null)
	    throw new ModeleException(
		    "L'adresse " + adresse + " d'une livraison ne correspond pas à une intersection.");
	if (duree < 0)
	    throw new ModeleException("La durée de la livraison à l'adresse " + adresse + " est négative.");
	this.demandeDeLivraison.ajouterLivraison(duree, this.listeIntersections.get(adresse), debutPlage, finPlage);
	setChanged();
	notifyObservers();
    }

    /**
     * Cree et ajoute un troncon au Plan courant
     * 
     * @param nom
     *            Nom du troncon a ajouter
     * @param longueur
     *            Longueur (en decimetres) du troncon a ajouter
     * @param vitMoyenne
     *            Vitesse moyenne de circulation (en decimetres/seconde) du
     *            troncon a ajouter
     * @param origine
     *            Origine du troncon a ajouter
     * @param destination
     *            Destination du troncon a ajouter
     * @throws ModeleException
     *             Renvoie une exception si le Troncon ne peut pas être créé
     */
    public void creerTroncon(String nom, int longueur, int vitMoyenne, int origine, int destination)
	    throws ModeleException {
	if (listeIntersections.get(origine) == null || listeIntersections.get(destination) == null)
	    throw new ModeleException(
		    "L'origine ou la destination du tronçon " + nom + " ne correspond pas à une intersection.");
	if (vitMoyenne <= 0)
	    throw new ModeleException("La vitesse moyenne du tronçon " + nom + " est négative ou nulle.");
	if (longueur <= 0)
	    throw new ModeleException("La longueur du tronçon " + nom + " est négative ou nulle.");

	Troncon nouvTroncon = new Troncon(nom, this.listeIntersections.get(origine),
		this.listeIntersections.get(destination), longueur, vitMoyenne);
	// Si un troncon ayant la meme origine que le troncon à ajouter,
	// on les inserent dans la meme liste.
	if (this.listeTroncons.containsKey(origine)) {
	    this.listeTroncons.get(origine).add(nouvTroncon);
	} else {
	    // On cree une nouvelle liste pour le nouvel origine
	    List<Troncon> nouvListeTroncons = new ArrayList<Troncon>();
	    nouvListeTroncons.add(nouvTroncon);
	    this.listeTroncons.put(origine, nouvListeTroncons);
	}
	setChanged();
	notifyObservers();
    }

    /**
     * @return String formatée représentant la feuille de route à suivre pour la
     *         tournée
     */
    public String genererFeuilleRoute() {
	if (tournee != null) {
	    return tournee.genererFeuilleRoute();
	}
	return "";
    }

    /**
     * Retourne l'adresse de la livraison précédant la livraison donnée
     * 
     * @param adrLiv
     *            Adresse de la livraison dont on cherche la livraison
     *            précédente
     * @return Adresse de la livraison précédente ou -1 si l'adresse donnée n'a
     *         pas de livraison ou de livraison suivante
     */
    public int getAdresseLivraisonPrecedente(int adrLiv) {
	if (tournee != null) {
	    return tournee.getAdresseLivraisonPrecedente(adrLiv);
	}
	return -1;
    }

    /**
     * Retourne l'adresse de la livraison suivant la livraison donnée
     * 
     * @param adrLiv
     *            Adresse de la livraison dont on cherche la livraison suivante
     * @return Adresse de la livraison suivante ou -1 si l'adresse donnée n'a
     *         pas de livraison ou de livraison suivante
     */
    public int getAdresseLivraisonSuivante(int adrLiv) {
	if (tournee != null) {
	    return tournee.getAdresseLivraisonSuivante(adrLiv);
	}
	return -1;
    }

    /**
     * @return Retourne le boolean indiquant si un calcul de tournée est en
     *         cours
     */
    public boolean getCalculTourneeEnCours() {
	return this.calculTourneeEnCours;
    }

    /**
     * METHODE DE TEST
     */
    public Integer getDureeTournee() {
	if (tournee != null) {
	    return tournee.getDuree();
	} else {
	    return null;
	}

    }

    /**
     * @return Retourne l'intersection où est situé l'entrepôt
     */
    public Intersection getEntrepot() {
	if (demandeDeLivraison != null) {
	    return demandeDeLivraison.getEntrepot();
	} else {
	    return null;
	}
    }

    /**
     * @return HashMap des livraisons de la demande
     */
    public HashMap<Integer, Livraison> getHashMapLivraisonsDemande() {
	if (demandeDeLivraison != null) {
	    return demandeDeLivraison.getHashMapLivraisons();
	} else {
	    return null;
	}
    }

    /**
     * @param id
     *            Id de l'Intersection à retourner
     * @return Retourne l'Intersection correspondante à l'id donné ou null si
     *         aucune intersection n'existe
     */
    public Intersection getIntersection(int id) {
	return this.listeIntersections.get(id);
    }

    /**
     * @return Renvoie la liste des Itineraires de la Tournee. Si la Tournee est
     *         null, renvoie null
     */
    public List<Itineraire> getItineraires() {
	if (tournee != null) {
	    return tournee.getItineraires();
	} else {
	    return null;
	}
    }

    /**
     * METHODE DE TEST
     * 
     * @return
     */
    public HashMap<Integer, Intersection> getListeIntersections() {
	return this.listeIntersections;
    }

    /**
     * @return La liste de livraisons de la tournée si celle ci existe, sinon la
     *         liste de livraisons de la demande. Si la demande et la tournée
     *         sont null, renvoie null
     */
    public List<Livraison> getListeLivraisons() {
	if (tournee != null) {
	    return tournee.getListeLivraisons();
	} else {
	    if (demandeDeLivraison != null) {
		return new ArrayList<Livraison>(demandeDeLivraison.getHashMapLivraisons().values());
	    } else {
		return null;
	    }
	}
    }

    /**
     * @return Liste des Troncons du Plan
     */
    public List<Troncon> getListeTroncons() {
	List<Troncon> listeNonOrdonneeTroncons = new ArrayList<Troncon>();
	Set<Integer> cles = this.listeTroncons.keySet();
	Iterator<Integer> it = cles.iterator();
	while (it.hasNext()) {
	    Integer cle = it.next();
	    listeNonOrdonneeTroncons.addAll(this.listeTroncons.get(cle));
	}
	return listeNonOrdonneeTroncons;
    }

    /**
     * METHODE DE TEST
     * 
     * @return
     */
    public HashMap<Integer, List<Troncon>> getListeTronconsTriee() {
	return this.listeTroncons;
    }

    /**
     * Renvoie la livraison associée à l'intersection à l'adresse donnée en
     * cherchant en priorité dans la tournée si elle existe ou dans la demande
     * de livraison sinon
     * 
     * @param adresse
     *            Id de l'intersection adresse de la livraison
     * @return La livraison associée
     */
    public Livraison getLivraisonParAdresse(int adresse) {
	if (tournee != null) {
	    return tournee.getLivraison(adresse);
	}
	if (demandeDeLivraison != null) {
	    return demandeDeLivraison.getLivraison(adresse);
	}
	return null;
    }

    /**
     * Retourne le point contenant les coordonnées maximales du Plan
     * 
     * @return Le point de coordonnées maximales contenues dans le Plan
     */
    public Point getPointBasDroite() {
	int maxBas = Integer.MIN_VALUE;
	int maxDroite = Integer.MIN_VALUE;
	for (Intersection i : listeIntersections.values()) {
	    if (i.getLongitude() >= maxDroite) {
		maxDroite = i.getLongitude();
	    }
	    if (i.getLatitude() >= maxBas) {
		maxBas = i.getLatitude();
	    }
	}
	return new Point(maxDroite, maxBas);
    }

    /**
     * METHODE DE TEST
     * 
     * @param idIntersection
     * @return
     */
    public List<Troncon> getTronconsParIntersection(int idIntersection) {
	return this.listeTroncons.get(idIntersection);
    }

    /**
     * Insère la livraison entre les livraisons de la tournée aux adresses
     * données
     * 
     * @param adresse
     *            L'adresse associée à la livraison
     * @param duree
     *            Durée de la livraison
     * @param debutPlage
     *            Heure de début de la plage
     * @param finPlage
     *            Heure de fin de la plage
     * @param adrPrec
     *            Adresse précédente de la livraison à ajouter
     * @param adrSuiv
     *            Adresse suivante de la livraison à ajouter
     */
    public void insererLivraisonTournee(int adresse, int duree, String debutPlage, String finPlage, int adrPrec,
	    int adrSuiv) {
	Intersection interAdresse = this.getIntersection(adresse);
	try {
	    Livraison liv = new Livraison(duree, interAdresse, debutPlage, finPlage);
	    tournee.insererLivraison(liv, adrPrec, adrSuiv);
	} catch (Exception e) {
	    // TODO
	}
	setChanged();
	notifyObservers();
    }

    /**
     * Modifie la livraison de la tournée à l'adresse donnée avec les plages
     * horaires spécifiées
     * 
     * @param adrLivraison
     *            Adresse de la livraison à modifier
     * @param nvPlage
     *            True si la livraison doit avoir une plage, false sinon
     * @param nvDebut
     *            Nouvelle Heure de début de la plage
     * @param nvFin
     *            Nouvelle Heure de la fin de la plage
     */
    public void modifierPlageLivraison(int adrLivraison, boolean nvPlage, Heure nvDebut, Heure nvFin) {
	if (tournee != null) {
	    tournee.modifierPlageLivraison(adrLivraison, nvPlage, nvDebut, nvFin);
	}
	setChanged();
	notifyObservers();
    }

    @Override
    public void notifyObservers() {
	setChanged();
	super.notifyObservers();
    }

    /**
     * Recupere les durees des livraisons correspondant aux intersections
     * donnees en parametres
     * 
     * @param idSommets
     *            Liste des sommets dont il faut les durees
     * @return Tableau des durees ordonnees selon l'ordre des sommets en entree
     */
    private int[] recupererDurees(List<Integer> idSommets) {
	int[] durees = new int[idSommets.size()];
	durees[0] = 0; // temps a passer a l'entrepot
	for (int i = 1; i < idSommets.size(); i++) {
	    durees[i] = demandeDeLivraison.getLivraison(idSommets.get(i)).getDuree();
	}
	return durees;
    }

    /**
     * Retire la livraison indiquee de la tournee
     * 
     * @param adresse
     *            Identifiant de l'intersection correspondante a la livraison a
     *            retirer
     * @return Livraison retiree de la tournee
     */
    public Livraison retirerLivraisonTournee(int adresse) {
	// On recupere les ids du nouvel itineraire a creer
	Livraison livRetiree = this.tournee.supprimerLivraison(adresse);
	setChanged();
	notifyObservers();

	// On renvoie la livraison supprimee
	return livRetiree;
    }

    /**
     * Supprime la Tournee actuellement liée au Plan
     */
    public void supprimerTournee() {
	this.tournee = null;
	setChanged();
	notifyObservers();
    }

    /**
     * Vide le Plan, remet a zero les listes d'intersection, de troncon et
     * nullifie la demande de livraison et la tournee
     */
    public void viderPlan() {
	this.listeIntersections = new HashMap<Integer, Intersection>();
	this.listeTroncons = new HashMap<Integer, List<Troncon>>();
	this.demandeDeLivraison = null;
	this.tournee = null;
	setChanged();
	notifyObservers();
    }
}
