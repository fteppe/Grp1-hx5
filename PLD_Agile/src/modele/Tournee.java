package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class Tournee extends Observable {

    private int duree;
    private int adrEntrepot;
    private Heure hDebut;
    private Heure hFin;
    private boolean valide;

    private List<Itineraire> itineraires;
    private HashMap<Integer, Livraison> livraisons;
    
    /**
     * Cree une tournee à partir de sa duree
     * 
     * @param duree
     *            Duree de la tournee (en secondes)
     * @param entrepot
     *            Intersection de depart et d'arrivee de la tournee
     */
    public Tournee(Heure heureDepart) {
	this.valide = true;
	this.hDebut = heureDepart;
	this.hFin = heureDepart;
	duree = Integer.MAX_VALUE;
	itineraires = new ArrayList<Itineraire>();
	livraisons = new HashMap<Integer, Livraison>();
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
    protected void mettreAJourTournee(int duree, int[] livraisons, Itineraire[][] itineraires,
	    HashMap<Integer, Livraison> livDemande, List<Integer> idSommets) {
	this.viderTournee();
	for (int i = 0; i < livraisons.length - 1; i++) {
	    Livraison prochLivr = livDemande.get(idSommets.get(livraisons[i + 1]));
	    Itineraire nouvItineraire = itineraires[livraisons[i]][livraisons[i + 1]];
	    this.ajouterItineraire(nouvItineraire, prochLivr);
	}
	this.ajouterItineraire(itineraires[livraisons[livraisons.length - 1]][livraisons[0]], null);
	this.setDuree(duree);
	this.mettreAJourTempsParcours(this.hDebut);
	//setChanged();
	//notifyObservers();
	System.out.println("Tournée mise à jour");
    }

    /**
     * Ajoute un itineraire à la liste d'itineraires de la tournee courante
     * 
     * @param itineraire
     *            Itineraire a ajouter à la tournee
     */
    protected void ajouterItineraire(Itineraire itineraire, Livraison prochLivr) {
	if (itineraires.size() == 0)
	    adrEntrepot = itineraire.getDepart().getId();
	itineraires.add(itineraire);
	if (prochLivr != null)
	    livraisons.put(prochLivr.getAdresse().getId(), prochLivr);
	setChanged();
	notifyObservers();
    }

    /**
     * Insère une nouvelle livraison et calcule les itinéraires entre
     * l'intersection associée et les intersections précédentes et suivantes
     * 
     * @param liv
     *            La livraison à insérer dans la tournée
     * @param adrPrec
     *            L'adresse de la livraison précédente
     * @param adrSuiv
     *            L'adresse de la livraison suivante
     */
    protected void insererLivraison(Livraison liv, int adrPrec, int adrSuiv) {
	// On ajoute la livraison dans la liste
	livraisons.put(liv.getAdresse().getId(), liv);

	ArrayList<Integer> nvItineraires = new ArrayList<Integer>();
	nvItineraires.add(adrPrec);
	nvItineraires.add(liv.getAdresse().getId());
	nvItineraires.add(adrSuiv);
	AlgoDijkstra algo = AlgoDijkstra.getInstance();
	Object[] resultAlgo = algo.calculerDijkstra(nvItineraires);
	Itineraire[][] nvItin = (Itineraire[][]) resultAlgo[1];
	for (Itineraire itin : itineraires) {
	    if (itin.getDepart().getId() == nvItineraires.get(0) && itin.getArrivee().getId() == nvItineraires.get(2)) {
		itineraires.remove(itin);
		break;
	    }
	}
	this.insererItineraire(nvItin[0][1]);
	this.insererItineraire(nvItin[1][2]);
	this.mettreAJourTempsParcours(this.hDebut);
    }

    /**
     * Supprime l'itineraire concernant la livraison a l'adresse specifiee
     * 
     * @param adresse
     *            Identifiant de l'intersection correspondante a la livraison
     * @return Tableau compose des identifiants de depart et d'arrivee du nouvel
     *         Itineraire a construire
     */
    protected Livraison supprimerLivraison(int adresse) {
	// On parcourt la liste d'itineraires pour supprimer
	// les itineraires concernant la livraison
	ArrayList<Integer> nvItineraire = new ArrayList<Integer>();
	Iterator<Itineraire> iter = itineraires.iterator();
	while (iter.hasNext()) {
	    Itineraire itin = iter.next();
	    if (itin.getArrivee().getId() == adresse) {
		nvItineraire.add(itin.getDepart().getId());
		iter.remove();
	    } else if (itin.getDepart().getId() == adresse) {
		nvItineraire.add(itin.getArrivee().getId());
		iter.remove();
		break;
	    }
	}
	AlgoDijkstra algo = AlgoDijkstra.getInstance();
	Object[] resultAlgo = algo.calculerDijkstra(nvItineraire);
	Itineraire[][] nvItin = (Itineraire[][]) resultAlgo[1];
	this.insererItineraire(nvItin[0][1]);
	Livraison liv = livraisons.remove(adresse);
	this.mettreAJourTempsParcours(this.hDebut);

	return liv;
    }

    /**
     * Insère l'itinéraire dans la liste des itinéraires de la tournée selon les
     * intersections de départ et d'arrivée associées
     * 
     * @param nvItineraire
     *            Itineraire à insérer
     */
    protected void insererItineraire(Itineraire nvItineraire) {
	int i = 0;
	if (nvItineraire.getDepart().getId() == adrEntrepot) {
	    itineraires.add(0, nvItineraire);
	} else {
	    for (; i < itineraires.size(); i++) {
		Itineraire itin = itineraires.get(i);
		if (itin.getArrivee() == nvItineraire.getDepart()) {
		    itineraires.add(++i, nvItineraire);
		    break;
		}
		// TODO test sur l'intersection d'arrivée
	    }
	}
    }

    /**
     * Met à jour les horaires de la tournée et des livraisons qui lui sont
     * associée
     * 
     * @param heureDepartTournee
     *            Horaire de départ du calcul
     */
    private void mettreAJourTempsParcours(Heure heureDepartTournee) {
	Heure cur = heureDepartTournee;
	for (int i = 0; i < itineraires.size() - 1; i++) {
	    Itineraire itin = itineraires.get(i);
	    System.out.println(itin);
	    cur = new Heure(cur.toSeconds() + itin.getTpsParcours());
	    int adrLiv = itin.getArrivee().getId();
	    Livraison liv = livraisons.get(adrLiv);
	    cur = liv.setHeureArrivee(cur);
	}
	Itineraire itin = itineraires.get(itineraires.size() - 1);
	cur = new Heure(cur.toSeconds() + itin.getTpsParcours());
	hFin = cur;
	valide = true;
	for (Livraison liv : livraisons.values()) {
	    if (!liv.getRespectePlage())
		valide = false;
	}
	//setChanged();
	//notifyObservers();
    }

    /**
     * Réinitialise la liste des itinéraires de la tournée
     */
    protected void viderTournee() {
	this.itineraires.clear();
    }

    protected List<Livraison> getListeLivraisons() {
	ArrayList<Livraison> listLivraisons = new ArrayList<Livraison>();
	for (Itineraire itin : itineraires) {
	    Livraison livraison = livraisons.get(itin.getDepart().getId());
	    if (livraison != null)
		listLivraisons.add(livraison);
	}
	return listLivraisons;
    }

    protected Livraison getLivraison(int adresse) {
	return livraisons.get(adresse);
    }

    protected List<Itineraire> getItineraires() {
	return itineraires;
    }

    protected Heure gethDebut() {
	return hDebut;
    }

    protected Heure gethFin() {
	return (hFin != null ? hFin : new Heure("00:00:00"));
    }

    protected boolean getValidite() {
	return valide;
    }

    protected int getDuree() {
	return duree;
    }

    protected void setDuree(int duree) {
	this.duree = duree;
    }

    protected List<Itineraire> getItineraireTroncon(Troncon tronc) {
	List<Itineraire> lstIitin = new ArrayList<Itineraire>();
	for (Itineraire itin : itineraires) {
	    if (itin.comprendTroncon(tronc))
		lstIitin.add(itin);
	}
	return lstIitin;
    }

    /**
     * Retourne l'adresse de la livraison suivant la livraison donnée
     * 
     * @param adrLiv
     *            Adresse de la livraison dont on cherche la livraison suivante
     * @return Adresse de la livraison suivante ou -1 si l'adresse donnée n'a
     *         pas de livraison ou de livraison suivante
     */
    protected int getAdresseLivraisonSuivante(int adrLiv) {
	for (Itineraire itin : itineraires) {
	    if (itin.getDepart().getId() == adrLiv)
		return itin.getArrivee().getId();
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
    protected int getAdresseLivraisonPrecedente(int adrLiv) {
	for (Itineraire itin : itineraires) {
	    if (itin.getArrivee().getId() == adrLiv)
		return itin.getDepart().getId();
	}
	return -1;
    }

    /**
     * Modifie la livraison à l'adresse donnée avec les plages horaires
     * spécifiées
     * 
     * @param adrLivraison
     *            Adresse de la livraison à modifier
     * @param nvPlage
     *            True si la livraison doit avoir une plage, false sinon
     * @param nvDebut
     *            Nouvelle heure de début de la plage
     * @param nvFin
     *            Nouvelle heure de la fin de la plage
     */
    public void modifierPlageLivraison(int adrLivraison, boolean nvPlage, Heure nvDebut, Heure nvFin) {
	Livraison liv = livraisons.remove(adrLivraison);
	if (nvPlage)
	    liv.setPlage(new PlageHoraire(nvDebut, nvFin));
	else
	    liv.supprimerPlage();
	livraisons.put(adrLivraison, liv);
	this.mettreAJourTempsParcours(hDebut);
    }

    /**
     * @return Retourne la string formatée pour l'affichage sur la feuille de
     *         route
     */
    public String genererFeuilleRoute() {
	String route;
	route = "Départ de l'intersection " + adrEntrepot + " à " + hDebut.afficherHoraire();
	route += "\r\n"+itineraires.get(0).afficherFeuilleRoute()+"\r\n";
	for (int i = 1; i < itineraires.size(); i++) {
	    Itineraire itin = itineraires.get(i);
	    route += "\r\n"+livraisons.get(itin.getDepart().getId()).afficherFeuilleRoute();
	    route += "\r\n"+itin.afficherFeuilleRoute()+"\r\n";
	}
	route += "\r\n"+"Arrivée à l'entrepôt à " + adrEntrepot + " à " + hFin.afficherHoraire();
	return route;
    }
}
