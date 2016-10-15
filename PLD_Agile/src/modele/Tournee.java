package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Tournee extends Observable{
    
    private DemandeDeLivraison demandeDeLivraison;
    private List<Itineraire> itineraires;
    
    public Tournee(DemandeDeLivraison uneDemande)
    {
	this.demandeDeLivraison = uneDemande;
	itineraires = new ArrayList<Itineraire>();
    }
    
    protected void ajouterItineraire(Livraison depart, Livraison arrivee, List<Troncon> troncons)
    {
	itineraires.add(new Itineraire(depart, arrivee, troncons));
	setChanged();
	notifyObservers();
    }
    
    public List<Itineraire> getItineraires()
    {
	return itineraires;
    }
}
