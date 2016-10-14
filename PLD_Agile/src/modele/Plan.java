package modele;

import java.util.HashMap;
import java.util.Observable;

public class Plan extends Observable {
   private HashMap<Integer, Intersection> listeIntersections;
   private HashMap<Integer, Troncon> listeTroncons;
   
   public Plan(){
   }
   
   public void ajouterIntersection(int id, int longitude, int latitude) {
       Intersection nouvIntersection = new Intersection (id, longitude, latitude);
       this.listeIntersections.put(id, nouvIntersection);
   }
   
   public void ajouterTroncon(String nom, int longueur, int vitMoyenne, int origine, int destination) {
       Troncon nouvTroncon = new Troncon (nom, this.listeIntersections.get(origine), this.listeIntersections.get(destination), longueur, vitMoyenne);
       this.listeTroncons.put(origine, nouvTroncon);
   }

}
