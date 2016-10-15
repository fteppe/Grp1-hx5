package modele;

import java.sql.Time;
import java.util.HashMap;
import java.util.Observable;

public class DemandeDeLivraison extends Observable {
    
    private Time heureDepart;
    private Intersection entrepot;
    private HashMap<Integer, Livraison> livraisons;
    
    public DemandeDeLivraison(Time heureDepart, Intersection entrepot) {
	this.heureDepart = heureDepart;
	this.livraisons = new HashMap<Integer, Livraison>();
	this.entrepot = entrepot;
    }
    
    public void ajouterLivraison (int duree, Intersection adresse){
	Livraison nouvLivraison = new Livraison(duree, adresse);
	this.livraisons.put(adresse.getId(), nouvLivraison);
	setChanged();
	notifyObservers(nouvLivraison);
    }
    
    public Livraison getLivraison(int id){
	return this.livraisons.get(id);
    }
    
    public Intersection getEntrepot(){
	return this.entrepot;
    }
    
    public Time getHeureDepart(){
	return this.heureDepart;
    }
}
