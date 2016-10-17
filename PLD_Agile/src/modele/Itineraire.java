package modele;

import java.util.List;

public class Itineraire {
    
    private Livraison depart;
    private Livraison arrivee;
    private List<Troncon> troncons;
    
    public Itineraire(Livraison depart, Livraison arrivee, List<Troncon> troncons)
    {
	this.depart = depart;
	this.arrivee = arrivee;
	this.troncons = troncons;
    }

    public Livraison getDepart()
    {
	return this.depart;
    }
    
    public Livraison getArrivee()
    {
	return this.arrivee;
    }
    
    public List<Troncon> getTroncons()
    {
	return troncons;
    }
}
