package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Tournee extends Observable {
    
    private int duree;

    private List<Itineraire> itineraires;
    
    public Tournee(int duree) {
	this.duree = duree;
	itineraires = new ArrayList<Itineraire>();
    }
    
    /**
     * Cree un nouvel Itineraire et l'ajoute a la liste d'Itineraires
     * de la Tournee
     * @param depart Livraison de depart de l'Itineraire
     * @param arrivee Livraison d'arrivee
     * @param troncons Ensemble des troncons a parcourir entre
     * le depart et l'arrivee
     */
    public void ajouterItineraire(Itineraire itineraire) {
	itineraires.add(itineraire);
	setChanged();
	notifyObservers();
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
