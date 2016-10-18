package modele;

import java.util.List;

public class Itineraire {
    
    private Intersection depart;
    private Intersection arrivee;
    private List<Troncon> troncons;
    
    public Itineraire(Intersection depart, Intersection arrivee, List<Troncon> troncons) {
	this.depart = depart;
	this.arrivee = arrivee;
	this.troncons = troncons;
    }

    public Intersection getDepart() {
	return this.depart;
    }
    
    public Intersection getArrivee() {
	return this.arrivee;
    }
    
    public List<Troncon> getTroncons() {
	return troncons;
    }
}
