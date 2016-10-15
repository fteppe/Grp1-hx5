package modele;

import java.util.Observable;

public class Troncon extends Observable {
    
    private String nom;
    private Intersection origine;
    private Intersection destination;
    private int longueur;
    private int vitesseMoy;
    private double tpsParcours;
    
    public Troncon(String nom, Intersection origine, Intersection destination, int longueur, int vitesseMoy){
	this.nom = nom;
	this.longueur = longueur;
	this.vitesseMoy = vitesseMoy;
	this.setTpsParcours();
	this.origine = origine;
	this.destination = destination;
    }
    
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
