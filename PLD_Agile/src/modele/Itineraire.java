package modele;

import java.util.List;

public class Itineraire {
    
    private Intersection depart;
    private Intersection arrivee;
    private List<Troncon> troncons;
    private int tpsParcours;
    
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Itineraire other = (Itineraire) obj;
		if (arrivee == null) {
			if (other.arrivee != null)
				return false;
		} else if (!arrivee.equals(other.arrivee))
			return false;
		if (depart == null) {
			if (other.depart != null)
				return false;
		} else if (!depart.equals(other.depart))
			return false;
		if (troncons == null) {
			if (other.troncons != null)
				return false;
		} else if (!troncons.equals(other.troncons))
			return false;
		return true;
	}
   
    
}
