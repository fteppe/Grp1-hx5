package modele;

import java.util.Observable;

public class Troncon extends Observable {

    private String nom;
    private Intersection origine;
    private Intersection destination;
    private int longueur;
    private int vitesseMoy;
    private int tpsParcours;

    /**
     * Cree un troncon a partir de son nom, sa longueur, la vitesse moyenne
     * observee ainsi que les intersections reliees
     * 
     * @param nom
     *            Nom du troncon
     * @param origine
     *            Origine du troncon
     * @param destination
     *            Destination du troncon
     * @param longueur
     *            Longueur (en decimetres) du troncon
     * @param vitesseMoy
     *            Vitesse moyenne de circulation (en decimetres/seconde) du
     *            troncon
     */
    public Troncon(String nom, Intersection origine, Intersection destination,
	    int longueur, int vitesseMoy) {
	this.nom = nom;
	this.longueur = longueur;
	this.vitesseMoy = vitesseMoy;
	this.setTpsParcours();
	this.origine = origine;
	this.destination = destination;
    }

    /**
     * Calcule et modifie le temps de parcours moyen du troncon courant
     */
    private void setTpsParcours() {
	this.tpsParcours = Math.round(this.longueur / this.vitesseMoy);
    }

    public String getNom() {
	return this.nom;
    }

    public int getTpsParcours() {
	return this.tpsParcours;
    }

    public Intersection getOrigine() {
	return this.origine;
    }

    public Intersection getDestination() {
	return this.destination;
    }

    /**
     * Affiche textuellement le troncon courant
     */
    public String toString() {
	return " - Troncon " + nom + " de l'intersection " + origine.getId()
		+ " à l'intersection " + destination.getId();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Troncon other = (Troncon) obj;
	if (destination == null) {
	    if (other.destination != null)
		return false;
	} else if (!destination.equals(other.destination))
	    return false;
	if (longueur != other.longueur)
	    return false;
	if (nom == null) {
	    if (other.nom != null)
		return false;
	} else if (!nom.equals(other.nom))
	    return false;
	if (origine == null) {
	    if (other.origine != null)
		return false;
	} else if (!origine.equals(other.origine))
	    return false;
	if (Double.doubleToLongBits(tpsParcours) != Double
		.doubleToLongBits(other.tpsParcours))
	    return false;
	if (vitesseMoy != other.vitesseMoy)
	    return false;
	return true;
    }

    /**
     * @return Retourne la string formatée pour l'affichage sur la feuille de
     *         route
     */
    protected String afficherFeuilleRoute() {
	return "suivre " + nom + " de " + origine.getId() + " à "
		+ destination.getId() + " pendant " + tpsParcours + " secondes";
    }
}
