package modele;

import java.util.List;
import java.util.Observable;

/**
 * Cette classe permet la creation et la gestion des itineraires composant une
 * tournee.
 *
 */
public class Itineraire extends Observable {

    private Intersection depart;
    private Intersection arrivee;
    private List<Troncon> troncons; // Liste de troncons ordonnes
				    // selon l'ordre de passage
    private int tpsParcours;

    /**
     * Cree une intersection a partir d'un depart, d'une arrivee et de la liste
     * des troncons les reliant
     * 
     * @param depart
     *            Intersection de depart de l'itineraire
     * @param arrivee
     *            Intersection d'arrivee de l'itineraire
     * @param troncons
     *            Chemin reliant le depart et l'arrivee
     */
    public Itineraire(Intersection depart, Intersection arrivee, List<Troncon> troncons) {
	this.depart = depart;
	this.arrivee = arrivee;
	this.troncons = troncons;
	this.calculTpsParcours();
    }

    /**
     * @return Intersection de départ de l'Itineraire
     */
    public Intersection getDepart() {
	return this.depart;
    }

    /**
     * @return Intersection d'arrivée de l'Itineraire
     */
    public Intersection getArrivee() {
	return this.arrivee;
    }

    /**
     * @return Liste des Troncons qui composent l'Itineraire
     */
    public List<Troncon> getTroncons() {
	return this.troncons;
    }

    /**
     * @return Temps de parcours de l'Itineraire
     */
    protected int getTpsParcours() {
	return this.tpsParcours;
    }

    /**
     * Calcule le temps de parcours de l'itineraire, et met a jour l'attribut
     * correspondant
     */
    private void calculTpsParcours() {
	int tpsParcours = 0;
	if (this.troncons != null) {
	    for (Troncon t : this.troncons) {
		if (t.getTpsParcours() == Integer.MAX_VALUE) {
		    tpsParcours = Integer.MAX_VALUE;
		    break;
		} else {
		    tpsParcours += t.getTpsParcours();
		}
	    }
	}
	this.tpsParcours = tpsParcours;
    }

    /**
     * Compare deux itineraires. Renvoit true si les objets manipules sont
     * composes par les memes troncon dans le meme ordre, et false sinon.
     */
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

    /**
     * @return Retourne la string formatée pour l'affichage sur la feuille de
     *         route
     */
    protected String afficherFeuilleRoute() {
	String itineraire = "";
	String current = troncons.get(0).getNom();
	int depart = troncons.get(0).getOrigine().getId();
	int arrive = troncons.get(0).getDestination().getId();
	int duree = troncons.get(0).getTpsParcours();
	for (int i = 0; i < troncons.size(); i++) {
	    Troncon t = troncons.get(i);
	    if (t.getNom().equals(current)) {
		arrive = t.getDestination().getId();
		duree += t.getTpsParcours();
	    } else {
		int sec = duree % 60;
		duree = duree / 60;
		if (sec > 30)
		    duree++;
		itineraire += "\r\n\tSuivre la route " + current + " entre les intersections " + depart + " et "
			+ arrive + " pendant " + (duree > 1 ? (duree + " minutes") : ("1 minute"));

		current = t.getNom();
		depart = t.getOrigine().getId();
		arrive = t.getDestination().getId();
		duree = t.getTpsParcours();
	    }
	}
	duree = duree / 60;
	if (duree < 1)
	    duree = 1;
	itineraire += "\r\n\tSuivre la route " + current + " entre les intersections " + depart + " et " + arrive
		+ " pendant " + (duree > 1 ? (duree + " minutes") : ("1 minute"));

	return itineraire;
    }

}
