package modele;

import java.util.Observable;

public class Livraison extends Observable {
    
    private int duree;
    private Heure heureDepart;
    private Heure heureArrivee;
    private Intersection adresse;
    private PlageHoraire plage;
    

    /**
     * Cree une livraison possedant une plage horaire
     * a partir de sa duree et de son adresse
     * @param duree Duree (en secondes) de la livraison
     * @param adresse Intersection correspondant a la livraison
     * @param debutPlage Heure de debut de la plage horaire
     * @param finPlage Heure de fin de la plage horaire
     */
    public Livraison(int duree, Intersection adresse, String debutPlage,
	    String finPlage) {
	this.duree = duree;
	this.adresse = adresse;
	plage = new PlageHoraire(new Heure(debutPlage), new Heure(finPlage));
    }
    
    /**
     * Cree une livraison possedant une plage horaire
     * a partir de sa duree et de son adresse
     * @param duree Duree (en secondes) de la livraison
     * @param adresse Intersection correspondant a la livraison
     */
    public Livraison(int duree, Intersection adresse) {
	this.duree = duree;
	this.adresse = adresse;
    }
    
    public int getDuree() {
	return this.duree;
    }
    
    public Heure getHeureDepart() {
   	return this.heureDepart;
    }
    
    public Heure getHeureArrivee() {
	return this.heureArrivee;
    }
    
    public Heure getDebutPlage() {
	Heure heureDebutPlage = null;
	if(possedePlage())
	{
	    heureDebutPlage = this.plage.getHeureDebut();
	}
	return heureDebutPlage;
    }
    
    public boolean possedePlage() {
   	return this.plage != null;
    }
    
    public Heure getFinPlage() {
	Heure heureFinPlage = null;
	if(possedePlage())
	{
	    heureFinPlage = this.plage.getHeureFin();
	}
	return heureFinPlage;
    }
    
    public Intersection getAdresse() {
   	return this.adresse;
    }
    
    public void setHeures(Heure heureArrivee) {
	this.heureArrivee = heureArrivee;
	this.heureDepart = this.heureArrivee.ajouterSecondes(this.duree);
    }
    
    public void setDuree(int duree) {
	this.duree = duree;
    }
  
}
