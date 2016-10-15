package modele;

import java.sql.Time;
import java.util.Observable;

public class Livraison extends Observable {
    
    private int duree;
    private Time heureDepart;
    private Time heureArrivee;
    private Intersection adresse;
    
    public Livraison(int duree, Intersection adresse) {
	this.duree = duree;
	this.adresse = adresse;
    }
    
    public int getDuree(){
	return this.duree;
    }
    
    public Time getheureDepart(){
   	return this.heureDepart;
    }
    
    public Time getheureArrivee(){
   	return this.heureArrivee;
    }
    
    public Intersection getAdresse(){
   	return this.adresse;
    }
    
    public void setHeureArrivee(Time heureArrivee)
    {
	this.heureArrivee = heureArrivee;
    }
    
    public void setHeureDepart(Time heureDepart)
    {
	this.heureDepart = heureDepart;
    }
}
