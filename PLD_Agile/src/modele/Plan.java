package modele;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class Plan extends Observable {
   private HashMap<Integer, Intersection> listeIntersections;
   private HashMap<Integer, List<Troncon>> listeTroncons;
   private DemandeDeLivraison demandeDeLivraison;
   
   public Plan(){
       this.listeIntersections = new HashMap<Integer, Intersection>();
       this.listeTroncons = new HashMap<Integer, List<Troncon>>();
   }
   
   public void ajouterIntersection(int id, int longitude, int latitude) {
       Intersection nouvIntersection = 
	       new Intersection (id, longitude, latitude);
       this.listeIntersections.put(id, nouvIntersection);
       setChanged();
       notifyObservers(nouvIntersection);
       //Gestion d'une exception si deux intersections ont le m�me num�ero de sommet ?
   }
   
   public void ajouterTroncon(String nom, int longueur, int vitMoyenne,
	   int origine, int destination) {
       Troncon nouvTroncon = new Troncon (nom,
	       this.listeIntersections.get(origine),
	       this.listeIntersections.get(destination), longueur, vitMoyenne);
       if(this.listeTroncons.containsKey(origine)){
	   this.listeTroncons.get(origine).add(nouvTroncon);
       } else {
	   List<Troncon> nouvListeTroncons = new ArrayList<Troncon> ();
	   nouvListeTroncons.add(nouvTroncon);
	   this.listeTroncons.put(origine, nouvListeTroncons);
       }
       setChanged();
       notifyObservers(nouvTroncon);
   }
   
   public void ajouterDemandeDeLivraison(Time heureDepart, int entrepot,
	   int[] adresses, int[] durees) {
       this.demandeDeLivraison = new DemandeDeLivraison(heureDepart,
	       this.listeIntersections.get(entrepot));
       setChanged();
       notifyObservers(this.demandeDeLivraison);
       for(int i = 0; i < adresses.length; i++){
	   this.demandeDeLivraison.ajouterLivraison(durees[i],
		   this.listeIntersections.get(adresses[i]));
       }
   }
   
   public Intersection getIntersection (int id) {
       return this.listeIntersections.get(id);
   }
   
   public List<Troncon> getTroncons (int id) {
       return this.listeTroncons.get(id);
   }

}
