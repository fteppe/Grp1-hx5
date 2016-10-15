package modele;

import java.util.Observable;

public class Troncon extends Observable {
    
    private String nom;
    private Intersection origine;
    private Intersection destination;
    private int longueur;
    private int vitesseMoy;
    private double tpsParcours;
    
    /**
     * Cree un troncon a partir de son nom, sa longueur, 
     * la vitesse moyenne observee ainsi que les intersections reliees 
     * @param nom Nom du troncon
     * @param origine Origine du troncon
     * @param destination Destination du troncon
     * @param longueur Longueur (en decimetres) du troncon
     * @param vitesseMoy Vitesse moyenne de circulation 
     * 				(en decimetres/seconde) du troncon
     */
    public Troncon(String nom, Intersection origine, Intersection destination, 
	    int longueur, int vitesseMoy){
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
	this.tpsParcours = this.longueur/this.vitesseMoy;
    }
    
    public String getNom(){
	return this.nom;
    }
    
    public double getTpsParcours(){
	return this.tpsParcours;
    }
    
    public Intersection getOrigine(){
	return this.origine;
    }
    
    public Intersection getDestination(){
	return this.destination;
    }

}
