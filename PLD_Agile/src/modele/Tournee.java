package modele;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

public class Tournee extends Observable {
    
    private int duree;

    private List<Itineraire> itineraires;
    private List<Livraison> livraisons;
    
    /**
     * Cree une tournee à partir de sa duree
     * @param duree Duree de la tournee (en secondes)
     * @param entrepot Intersection de depart et d'arrivee
     * 		de la tournee
     */
    public Tournee(int duree) {
	this.duree = duree;
	itineraires = new ArrayList<Itineraire>();
	livraisons = new ArrayList<Livraison>();
    }
    
    /**
     * Ajoute un itineraire à la liste d'itineraires de la tournee courante
     * @param itineraire Itineraire a ajouter à la tournee
     */
    public void ajouterItineraire(Itineraire itineraire, Livraison prochLivr) {
	itineraires.add(itineraire);
	if(prochLivr != null) livraisons.add(prochLivr);
	setChanged();
	notifyObservers();
    }
    
    /**
     * Supprime l'itineraire concernant la livraison a l'adresse specifiee
     * @param adresse Identifiant de l'intersection correspondante a la livraison
     * @return Tableau compose des identifiants de depart et d'arrivee
     * 		du nouvel Itineraire a construire
     */
    public int[] supprimerLivraison(int adresse) {
	// On parcourt la liste d'itineraires pour supprimer
	// les itineraires concernant la livraison
	int[] nvItineraire = new int[2];
	Iterator<Itineraire> iter = itineraires.iterator();
	while(iter.hasNext()) {
	    Itineraire itin = iter.next();
	    if(itin.getArrivee().getId() == adresse) {
		nvItineraire[0] = itin.getDepart().getId();
		itineraires.remove(itin);
	    }
	    else if(itin.getDepart().getId() == adresse) {
		nvItineraire[1] = itin.getArrivee().getId();
		itineraires.remove(itin);
		break; // CECI EST UN TEST
	    }
	}
	return nvItineraire;
    }
    
    public void insererItineraire(Itineraire nvItineraire) {
	int i=0;
	for( ; i < itineraires.size(); i++) {
	    Itineraire itin = itineraires.get(i);
	    if(itin.getArrivee() == nvItineraire.getDepart()) {
		itineraires.add(++i, nvItineraire);
		break;
	    }
	}
	for( ; i < itineraires.size(); i++) {
	    // TODO Modification des horaires
	}
	setChanged();
	notifyObservers();
    }
    
    public Livraison getLivraison(int adresse) {
	for (Livraison liv : livraisons) {
	    if (liv.getAdresse().getId() == adresse) return liv;
	}
	return null;
    }
    
    public List<Itineraire> getItineraires() {
	return itineraires;
    }    

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
}
