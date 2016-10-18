package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Tournee extends Observable{
    
    private DemandeDeLivraison demandeDeLivraison;
    private List<Itineraire> itineraires;
    
    public Tournee(DemandeDeLivraison uneDemande) {
	this.demandeDeLivraison = uneDemande;
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
    public void ajouterItineraire(Intersection depart, Intersection arrivee, List<Troncon> troncons) {
	itineraires.add(new Itineraire(depart, arrivee, troncons));
	setChanged();
	notifyObservers();
    }
    
    public List<Itineraire> getItineraires() {
	return itineraires;
    }
}
