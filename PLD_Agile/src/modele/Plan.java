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
     * Cree et ajoute une intersection au plan courant
     * 
     * @param id
     *            Identifiant de l'intersection a ajouter
     * @param longitude
     *            Longitude de l'intersection a ajouter
     * @param latitude
     *            Latitude de l'intersection a ajouter
     */
    public void ajouterIntersection(int id, int longitude, int latitude) {
	Intersection nouvIntersection = new Intersection(id, longitude, latitude);
	this.listeIntersections.put(id, nouvIntersection);
	setChanged();
	notifyObservers();
	// Gestion d'une exception si deux intersections ont le meme numero de
	// sommet ?
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
     */
    public void ajouterTroncon(String nom, int longueur, int vitMoyenne, int origine, int destination) {
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
     * Cree et ajoute une demande de livraison au plan
     * 
     * @param heureDepart
     *            Heure de depart de l'entrepot
     * @param entrepot
     *            Identifiant de l'intersection correspondant a l'entrepot
     */
    public void creerDemandeDeLivraison(Heure heureDepart, int entrepot) {
	this.demandeDeLivraison = new DemandeDeLivraison(heureDepart, this.listeIntersections.get(entrepot));
	setChanged();
	notifyObservers(demandeDeLivraison);
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
     */
    public void ajouterLivraisonDemande(int adresse, int duree, String debutPlage, String finPlage) {
	this.demandeDeLivraison.ajouterLivraison(duree, this.listeIntersections.get(adresse), debutPlage, finPlage);
	setChanged();
	notifyObservers();
    }

    /**
     * Cree et ajoute une livraison a la demande de livraison associee au Plan
     * 
     * @param adresses
     *            Identifiant de l'intersection correspondant a la livraison a
     *            effectuer
     * @param durees
     *            Duree de la livraison a effectuer
     */
    public void ajouterLivraisonDemande(int adresse, int duree) {
	this.demandeDeLivraison.ajouterLivraison(duree, this.listeIntersections.get(adresse));
	setChanged();
	notifyObservers();
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
     * Insere la livraison entre les livraisons de la tournee aux adresses
     * donnees
     * 
     * @param liv
     *            La livraison a inserer
     * @param adrPrec
     *            L'adresse de la livraison precedente
     * @param adrSuiv
     *            L'adresse de la livraison suivante
     */
    /**
     * Insère la livraison entre les livraisons de la tournée aux adresses
     * données
     * 
     * @param adresse
     *            L'adresse associée à la livraison
     * @param duree
     *            Durée de la livraison
     * @param debutPlage
     * @param finPlage
     * @param adrPrec
     * @param adrSuiv
     */
    public void insererLivraisonTournee(int adresse, int duree, String debutPlage, String finPlage, int adrPrec,
	    int adrSuiv) {
	Intersection interAdresse = this.getIntersection(adresse);
	Livraison liv = new Livraison(duree, interAdresse, debutPlage, finPlage);
	tournee.insererLivraison(liv, adrPrec, adrSuiv);
	setChanged();
	notifyObservers();
    }

    /**
     * Calcule la tournee (algo Dijkstra et TSP) si possible et la cree
     * 
     * @param tpsLimite
     *            Temps maximum en millisecondes pour le calcul du parcours
     *            optimal
     * @return true Si le calcul s'est bien deroule false Si le calcul a ete
     *         stoppe par la limite de temps
     * @throws ExceptionTournee
     */
    public boolean calculerTournee(int tpsLimite) throws ExceptionTournee {
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

	    if (this.getHashMapLivraisons().get(idSommets.get(i)).possedePlage()) {
		plageDepart[i] = this.getHashMapLivraisons().get(idSommets.get(i)).getDebutPlage().toSeconds();
		plageFin[i] = this.getHashMapLivraisons().get(idSommets.get(i)).getFinPlage().toSeconds();
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

	tournee = new Tournee(demandeDeLivraison.getHeureDepart());
	this.calculTourneeEnCours = true;

	// On lance le calcul de la tournee dans un nouveau thread
	Callable<Boolean> calculTournee = () -> {
	    // On cherche l'itineraire optimal via l'utilisation du TSP
	    tsp.chercheSolution(tpsLimite, idSommets.size(), couts, durees, plageDepart, plageFin,
		    this.demandeDeLivraison.getHeureDepart().toSeconds());
	    return tsp.getTempsLimiteAtteint();
	};

	ExecutorService executorCalculTournee = Executors.newFixedThreadPool(2);

	Future<Boolean> futureCalculTournee = executorCalculTournee.submit(calculTournee);

	// On recupere la meilleure tournee calculee a intervalle de temps
	// regulier dans un autre thread
	Callable<Boolean> recuperationMeilleurResultat = () -> {
	    boolean tpsLimiteAtteint = false;
	    while (this.calculTourneeEnCours == true) {
		try {
		    TimeUnit.SECONDS.sleep(tpsAttente);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		boolean calculTermine = futureCalculTournee.isDone();
		tsp.lock();
		int dureeTournee = tsp.getCoutMeilleureSolution();
		if (this.tournee.getDuree() != dureeTournee) {
		    System.out.println("Meilleur résultat");
		    System.out.println(dureeTournee);
		    int[] ordreTournee = new int[idSommets.size()];
		    for (int i = 0; i < idSommets.size(); i++) {
			ordreTournee[i] = tsp.getMeilleureSolution(i);
		    }
		    tsp.unlock();
		    Itineraire[][] trajets = (Itineraire[][]) resultDijkstra[1];
		    mettreAJourTournee(dureeTournee, ordreTournee, trajets);
		} else {
		    tsp.unlock();
		}
		// Si le calcul est termine, on arrête de chercher une
		// nouvelle tournee
		if (calculTermine) {
		    System.out.println("Calcul terminé");
		    this.calculTourneeEnCours = false;
		    try {
			tpsLimiteAtteint = futureCalculTournee.get();
		    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    System.out.println(tpsLimiteAtteint);
		}
	    }
	    futureCalculTournee.cancel(true);
	    return tpsLimiteAtteint;
	};

	Future<Boolean> futureRecuperationMeilleurResultat = executorCalculTournee.submit(recuperationMeilleurResultat);

	executorCalculTournee.shutdown();
	boolean tpsLimiteAtteint = false;
	// On attend la fin de l'execution des deux thread lances
	try {
	    executorCalculTournee.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
	    tpsLimiteAtteint = futureRecuperationMeilleurResultat.get();
	    if (this.tournee.getDuree() == Integer.MAX_VALUE && tpsLimiteAtteint == false) {
		throw new ExceptionTournee(
			"Aucune tournée n'a été trouvée. " + "Veuillez recommencer avec de nouvelles plages horaires");
	    }
	} catch (InterruptedException | ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return !tpsLimiteAtteint;

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
     * Cree la Tournee suivant la liste des livraisons, l'entrepot et les
     * itineraires associes
     * 
     * @param duree
     *            Duree totale de la tournee
     * @param livraisons
     *            Liste ordonnee des intersections a visiter
     * @param itineraires
     *            Tableau des itineraires pour aller de la livraison i a la
     *            livraison j
     */
    private void mettreAJourTournee(int duree, int[] livraisons, Itineraire[][] itineraires) {
	Heure heureActuelle = this.demandeDeLivraison.getHeureDepart();
	tournee.viderTournee();
	for (int i = 0; i < livraisons.length - 1; i++) {
	    Livraison prochLivr = demandeDeLivraison.getLivraison(idSommets.get(livraisons[i + 1]));
	    Itineraire nouvItineraire = itineraires[livraisons[i]][livraisons[i + 1]];
	    heureActuelle = new Heure(heureActuelle.toSeconds() + nouvItineraire.getTpsParcours());
	    heureActuelle = prochLivr.setHeureArrivee(heureActuelle);
	    /*
	     * if(prochLivr.possedePlage()) { heureActuelle = new
	     * Heure(Math.max(heureActuelle.toSeconds(),
	     * prochLivr.getDebutPlage().toSeconds()) + prochLivr.getDuree()); }
	     * else { heureActuelle = new Heure(heureActuelle.toSeconds() +
	     * prochLivr.getDuree()); }
	     */
	    tournee.ajouterItineraire(nouvItineraire, prochLivr);
	}
	tournee.ajouterItineraire(itineraires[livraisons[livraisons.length - 1]][livraisons[0]], null);
	tournee.setDuree(duree);
	setChanged();
	notifyObservers();
	System.out.println("Modifié");
    }

    /**
     * Renvoie l'ObjetGraphique positionné au coordonnées du point précisé
     * 
     * @param p
     *            Position de l'ObjetGraphique à rechercher
     * @return ObjetGraphique aux coordonnées p si il existe, null sinon
     */
    public ObjetGraphique cherche(Point p) {
	ObjetGraphique objGraph = null;
	// On teste le clic sur la liste d'intersections
	for (Intersection inter : listeIntersections.values()) {
	    if (inter.contient(p)) {
		objGraph = inter;
		Livraison livAssociee = this.getLivraisonAdresse(inter.getId());
		if (livAssociee != null)
		    objGraph = livAssociee;
	    }
	}

	return objGraph;
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
    private Livraison getLivraisonAdresse(int adresse) {
	if (tournee != null) {
	    return tournee.getLivraison(adresse);
	}
	if (demandeDeLivraison != null) {
	    return demandeDeLivraison.getLivraison(adresse);
	}
	return null;
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

    public Intersection getIntersection(int id) {
	return this.listeIntersections.get(id);
    }

    public List<Troncon> getTronconsParIntersection(int idIntersection) {
	return this.listeTroncons.get(idIntersection);
    }

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
    
    public HashMap<Integer, List<Troncon>> getListeTronconsTriee() {
	return this.listeTroncons;
    }

    public HashMap<Integer, Intersection> getListeIntersections() {
	return this.listeIntersections;
    }

    public HashMap<Integer, Livraison> getHashMapLivraisons() {
	if (demandeDeLivraison != null) {
	    return demandeDeLivraison.getListeLivraisons();
	} else {
	    return null;
	}

    }

    public List<Livraison> getListeLivraisons() {
	if (tournee != null) {
	    return tournee.getListeLivraisons();
	} else {
	    if (demandeDeLivraison != null) {
		return new ArrayList<Livraison>(demandeDeLivraison.getListeLivraisons().values());
	    } else {
		return null;
	    }
	}
    }

    /**
     * Arrête le calcul de la tournée en cours si il existe
     */
    public void arreterCalculTournee() {
	this.calculTourneeEnCours = false;
    }

    public Intersection getEntrepot() {
	if (demandeDeLivraison != null) {
	    return demandeDeLivraison.getEntrepot();
	} else {
	    return null;
	}
    }

    public List<Itineraire> getItineraires() {
	if (tournee != null) {
	    return tournee.getItineraires();
	} else {
	    return null;
	}
    }

    public void setTournee(Tournee tournee) {
	this.tournee = tournee;
    }

    public Integer getDureeTournee() {
	if (tournee != null) {
	    return tournee.getDuree();
	} else {
	    return null;
	}

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
	Set<Integer> cles = this.getHashMapLivraisons().keySet();
	Iterator<Integer> it = cles.iterator();
	while (it.hasNext()) {
	    Integer cle = it.next();
	    sommets.add(cle);
	}
	return sommets;
    }

    public String getHeureDepart() {
	if (tournee != null)
	    return tournee.gethDebut().toString();
	if (demandeDeLivraison != null)
	    return demandeDeLivraison.getHeureDepart().toString();
	return "";
    }
}
