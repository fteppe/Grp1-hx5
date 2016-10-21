package modele;

import java.util.List;

public class Itineraire {
    
    private Intersection depart;
    private Intersection arrivee;
    private List<Troncon> troncons;
    private int tpsParcours;
    
    /**
     * Cree une intersection a partir d'un depart, d'une arrivee et de la liste
     * des troncons les reliant
     * @param depart Intersection de depart de l'itineraire
     * @param arrivee Intersection d'arrivee de l'itineraire
     * @param troncons Plus court chemin reliant le depart et l'arrivee
     */
    public Itineraire(Intersection depart, Intersection arrivee, List<Troncon> troncons) {
	this.depart = depart;
	this.arrivee = arrivee;
	this.troncons = troncons;
	this.calculTpsParcours();
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
    
    /**
     * Calcule le temps de parcours de l'itineraire, et met 
     * a jour l'attribut correspondant
     */
    private void calculTpsParcours() {
	int tpsParcours = 0;
	if (this.troncons != null) {
	    for(Troncon t : this.troncons) {
		tpsParcours += t.getTpsParcours();    
	    }
	}
	this.tpsParcours = tpsParcours;
    }
    
    /**
     * Affiche textuellement l'itineraire courant
     */
    public String toString() {
	return "Itinéraire entre la livraison en " + depart.getId()
		+ " et " + arrivee.getId() + " :";
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
