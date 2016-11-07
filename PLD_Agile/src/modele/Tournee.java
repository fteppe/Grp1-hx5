package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class Tournee extends Observable {
    
    private int duree;
    private Heure hDebut;
    private Heure hFin;
    private boolean valide;

    private List<Itineraire> itineraires;
    private HashMap<Integer, Livraison> livraisons;
    
    /**
     * Cree une tournee à partir de sa duree
     * @param duree Duree de la tournee (en secondes)
     * @param entrepot Intersection de depart et d'arrivee
     * 		de la tournee
     */
    public Tournee(Heure heureDepart) {
	this.valide = true;
	this.hDebut = heureDepart;
	this.hFin = null;
	duree = Integer.MAX_VALUE;
	itineraires = new ArrayList<Itineraire>();
	livraisons = new HashMap<Integer, Livraison>();
    }
    
    /**
     * Ajoute un itineraire à la liste d'itineraires de la tournee courante
     * @param itineraire Itineraire a ajouter à la tournee
     */
    public void ajouterItineraire(Itineraire itineraire, Livraison prochLivr) {
	itineraires.add(itineraire);
	if(prochLivr != null) livraisons.put(prochLivr.getAdresse().getId(), prochLivr);
	setChanged();
	notifyObservers();
    }
    
    /**
     * Insère une nouvelle livraison et calcule les itinéraires entre
     * l'intersection associée et les intersections précédentes et suivantes
     * 
     * @param liv La livraison à insérer dans la tournée
     * @param adrPrec L'adresse de la livraison précédente
     * @param adrSuiv L'adresse de la livraison suivante
     */
    public void insererLivraison(Livraison liv, int adrPrec, int adrSuiv) {
	// On ajoute la livraison dans la liste
	livraisons.put(liv.getAdresse().getId(), liv);
	
	ArrayList<Integer> nvItineraires = new ArrayList<Integer>();
	nvItineraires.add(adrPrec);
	nvItineraires.add(liv.getAdresse().getId());
	nvItineraires.add(adrSuiv);
	AlgoDijkstra algo = AlgoDijkstra.getInstance();
	Object[] resultAlgo = algo.calculerDijkstra(nvItineraires);
	Itineraire[][] nvItin = (Itineraire [][]) resultAlgo[1];
	this.insererItineraire(nvItin[0][1]);
	this.insererItineraire(nvItin[1][2]);
	this.mettreAJourTempsParcours(this.hDebut);
    }
    
    /**
     * Supprime l'itineraire concernant la livraison a l'adresse specifiee
     * @param adresse Identifiant de l'intersection correspondante a la livraison
     * @return Tableau compose des identifiants de depart et d'arrivee
     * 		du nouvel Itineraire a construire
     */
    public Livraison supprimerLivraison(int adresse) {
	// On parcourt la liste d'itineraires pour supprimer
	// les itineraires concernant la livraison
	ArrayList<Integer> nvItineraire = new ArrayList<Integer>();
	Iterator<Itineraire> iter = itineraires.iterator();
	while(iter.hasNext()) {
	    Itineraire itin = iter.next();
	    if(itin.getArrivee().getId() == adresse) {
		nvItineraire.add(itin.getDepart().getId());
		iter.remove();
	    }
	    else if(itin.getDepart().getId() == adresse) {
		nvItineraire.add(itin.getArrivee().getId());
		iter.remove();
		break;
	    }
	}
	AlgoDijkstra algo = AlgoDijkstra.getInstance();
	Object[] resultAlgo = algo.calculerDijkstra(nvItineraire);
	Itineraire[][] nvItin = (Itineraire [][]) resultAlgo[1];
	this.insererItineraire(nvItin[0][1]);
	this.mettreAJourTempsParcours(this.hDebut);
	
	return livraisons.remove(adresse);
    }
    
    /**
     * Insère l'itinéraire dans la liste des itinéraires de la tournée
     * selon les intersections de départ et d'arrivée associées
     * 
     * @param nvItineraire Itineraire à insérer
     */
    public void insererItineraire(Itineraire nvItineraire) {
	int i=0;
	for( ; i < itineraires.size(); i++) {
	    Itineraire itin = itineraires.get(i);
	    if(itin.getArrivee() == nvItineraire.getDepart()) {
		itineraires.add(++i, nvItineraire);
		break;
	    }
	    //TODO test sur l'intersection d'arrivée
	}
    }
    
    /**
     * Met à jour les  horaires de la tournée et des livraisons qui lui
     * sont associée
     * 
     * @param heureDepartTournee Horaire de départ du calcul
     */
    private void mettreAJourTempsParcours(Heure heureDepartTournee) {
	Heure cur = heureDepartTournee;
	for(int i=0; i < itineraires.size()-1; i++) {
	    Itineraire itin = itineraires.get(i);
	    cur = new Heure(cur.toSeconds() + itin.getTpsParcours());
	    int adrLiv = itin.getArrivee().getId();
	    Livraison liv = livraisons.get(adrLiv);
	    cur = liv.setHeureArrivee(cur);
	}
	Itineraire itin = itineraires.get(itineraires.size() - 1);
	cur = cur.ajouterSecondes(itin.getTpsParcours());
	hFin = cur;
	valide = true;
	for(Livraison liv : livraisons.values()) {
	    if(!liv.getRespectePlage()) valide = false;
	}
    }
    
    /**
     * Réinitialise la liste des itinéraires de la tournée
     */
    public void viderTournee(){
	this.itineraires.clear();
    }
    
    public List<Livraison> getListeLivraisons() {
	ArrayList<Livraison> listLivraisons = new ArrayList<Livraison>();
	for(Itineraire itin : itineraires) {
	    Livraison livraison = livraisons.get(itin.getDepart().getId());
	    if(livraison != null) listLivraisons.add(livraison);
	}
	return listLivraisons;
    }
    
    public Livraison getLivraison(int adresse) {
	return livraisons.get(adresse);
    }
    
    public List<Itineraire> getItineraires() {
	return itineraires;
    }    
    
    public Heure gethDebut() {
	return hDebut;
    }
    
    public Heure gethFin() {
	return hFin;
    }
    
    public boolean getValidite() {
	return valide;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public List<Itineraire> getItineraireTroncon(Troncon tronc) {
	List<Itineraire> lstIitin = new ArrayList<Itineraire>();
	for (Itineraire itin : itineraires) {
	    if(itin.comprendTroncon(tronc)) lstIitin.add(itin);
	}
	return lstIitin;
    }
}
