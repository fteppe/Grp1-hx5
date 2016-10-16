package modele;

import java.sql.Time;
import java.util.HashMap;
import java.util.Observable;

public class DemandeDeLivraison extends Observable {
    
    private Time heureDepart;
    private Intersection entrepot;
    private HashMap<Integer, Livraison> livraisons; // Liste des livraisons classées selon l'identifiant de leur adresse
    
    /**
     * Cree une demande de livraison a partir de l'heure de depart 
     * et de son entrepot
     * @param heureDepart Heure de depart de l'entrepot
     * @param entrepot Intersection correspondant a l'entrepot 
     * 			de la demande de livraison
     */
    public DemandeDeLivraison(Time heureDepart, Intersection entrepot) {
	this.heureDepart = heureDepart;
	this.livraisons = new HashMap<Integer, Livraison>();
	this.entrepot = entrepot;
    }
    
    /**
     * Cree et ajoute une livraison a la demande de livraison courante
     * @param duree Duree de la livraison a ajouter
     * @param adresse Intersection correspondant a la livraison a ajouter
     */
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
    
    public int getNbrLivraisons(){
	return this.livraisons.size();
    }
    
    public Time getHeureDepart(){
	return this.heureDepart;
    }
}
