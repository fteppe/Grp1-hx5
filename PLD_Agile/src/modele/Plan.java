package modele;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.Vector;

public class Plan extends Observable {
   private HashMap<Integer, Intersection> listeIntersections; //Liste des intersections du plan classées selon leur identifiant
   private HashMap<Integer, List<Troncon>> listeTroncons; //Liste des troncons du plan classés selon l'identifiant de leur origine
   private DemandeDeLivraison demandeDeLivraison;
   private Tournee tournee;
   
   /**
    * Cree un Plan ne possedant aucune intersection et aucun tronçon
    */
   public Plan(){
       this.listeIntersections = new HashMap<Integer, Intersection>();
       this.listeTroncons = new HashMap<Integer, List<Troncon>>();
   }
   
   /**
    * Cree et ajoute une intersection au Plan courant
    * @param id Identifiant de l'intersection a ajouter
    * @param longitude Longitude de l'intersection a ajouter
    * @param latitude Latitude de l'intersection a ajouter
    */
   public void ajouterIntersection(int id, int longitude, int latitude) {
       Intersection nouvIntersection = 
	       new Intersection (id, longitude, latitude);
       this.listeIntersections.put(id, nouvIntersection);
       setChanged();
       notifyObservers();
       //Gestion d'une exception si deux intersections ont le même numéero de sommet ?
   }
   
   /**
    * Cree et ajoute un troncon au Plan courant
    * @param nom Nom du troncon a ajouter
    * @param longueur Longueur (en decimetres) du troncon a ajouter
    * @param vitMoyenne Vitesse moyenne de circulation
    * 				(en decimetres/seconde) du troncon a ajouter
    * @param origine Origine du troncon a ajouter
    * @param destination Destination du troncon a ajouter
    */
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
       notifyObservers();
   }
   
   
   /**
    * Cree et ajoute une DemandeDeLivraison au Plan
    * @param heureDepart Heure de depart de l'entrepot
    * @param entrepot Identifiant de l'intersection
    * 			correspondant a l'entrepot
    */
   public void creerDemandeDeLivraison(Time heureDepart, int entrepot)
   {
       this.demandeDeLivraison = new DemandeDeLivraison(heureDepart,
	       this.listeIntersections.get(entrepot));
       setChanged();
       notifyObservers();
   }
   
   /**
    * Cree et ajoute une livraison a la demande associee au Plan
    * @param adresses Identifiant de l'intersection correspondant 
    * 				a la livraisons a effectuer
    * @param durees Duree de la livraison a effectuer
    */
   public void ajouterLivraison(int adresse, int duree)
   {
       this.demandeDeLivraison.ajouterLivraison(duree,
		   this.listeIntersections.get(adresse));
       setChanged();
       notifyObservers();
   }
   
   public void calculerTournee() {
       int nbrLivraisons = demandeDeLivraison.getNbrLivraisons();
       int[] listeSommets = completionTableauLivraison(nbrLivraisons);
       calculerDijkstra(this.demandeDeLivraison, this.listeTroncons, listeSommets);
   }
   
   private Object [] calculerDijkstra(DemandeDeLivraison demandeDeLivraison,
	   		HashMap<Integer, List<Troncon>> listeTroncons, int[]listeSommets){

       int[][] couts = new int[listeSommets.length+2][listeSommets.length+2];
       @SuppressWarnings("unchecked")
       List<Troncon>[][] trajets = (ArrayList<Troncon>[][])new ArrayList[listeSommets.length+2][listeSommets.length+2];
       for(int i = 0; i < listeSommets.length; i++){
	   Object[] resultDijkstra = calculerDijkstraUnit(listeSommets[i], demandeDeLivraison, listeTroncons);
	   Troncon[] pi = (Troncon[]) resultDijkstra[0];
	   int[] cout = (int[]) resultDijkstra[1];
	   couts[i]=cout;
	   ArrayList<Troncon>[] trajetsUnit = triTableauPi(pi);
	   trajets[i]=trajetsUnit;
       }
       return new Object[]{couts, trajets};
   }
   
   private Object[] calculerDijkstraUnit(int id, DemandeDeLivraison demandeDeLivraison, HashMap<Integer, List<Troncon>> listeTroncons){
       return null;
   }
   
   private ArrayList<Troncon>[] triTableauPi(Troncon[] pi){
       @SuppressWarnings("unchecked")
       ArrayList<Troncon>[] trajetsUnit = (ArrayList<Troncon>[]) new ArrayList[pi.length];
       for(int i = 0; i < pi.length; i++)
       {
	   ArrayList<Troncon> trajet = new ArrayList<Troncon>();
	   int j=i;
	   while(pi[j]!=null){
	      trajet.add(0, pi[j]);;
	      j=pi[j].getOrigine().getId();
	   }
	   trajetsUnit[i]=trajet;
       }
       return trajetsUnit;
   }
   
   private int[] completionTableauLivraison(int nbrLivraisons){
       int i = 0;
       int[] sommets = new int[nbrLivraisons+2];
       sommets[0] = demandeDeLivraison.getEntrepot().getId();
       sommets[nbrLivraisons+1] = demandeDeLivraison.getEntrepot().getId();
       Set<Integer> cles = this.listeIntersections.keySet();
       Iterator<Integer> it = cles.iterator();
       while (it.hasNext()){
          Integer cle = it.next();
	  sommets[i] = cle;
	  i++;
       }
       return sommets;
   }
   
   public Intersection getIntersection(int id) {
       return this.listeIntersections.get(id);
   }
   
   public List<Troncon> getTroncons(int id) {
       return this.listeTroncons.get(id);
   }

}
