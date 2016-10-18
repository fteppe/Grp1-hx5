package modele;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import tsp.TSP1;

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
       //Gestion d'une exception si deux intersections ont le même numéro de sommet ?
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
   public void creerDemandeDeLivraison(Heure heureDepart, int entrepot)
   {
       this.demandeDeLivraison = new DemandeDeLivraison(heureDepart,
	       this.listeIntersections.get(entrepot));
       setChanged();
       notifyObservers(demandeDeLivraison);
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
   
   public boolean calculerTournee(int tpsLimite) {
       ArrayList<Integer> idSommets = completionTableauLivraison();
       Object[] resultDijkstra = calculerDijkstra(idSommets);
       // TODO
       TSP1 tsp = new TSP1();
       int[] durees = recupererDurees(idSommets);
       int[][] couts = (int[][]) resultDijkstra[0];
       tsp.chercheSolution(tpsLimite, idSommets.size(), couts, durees);
       
       if(!tsp.getTempsLimiteAtteint()) {
	   int dureeTournee = tsp.getCoutMeilleureSolution();
	   int[] ordreTournee = new int[idSommets.size()+1]; //idSommets.size()+1 car retour a l'entrepot ?
	   for(int i=0; i<idSommets.size()+1; i++)
	   {
	       ordreTournee[i] = tsp.getMeilleureSolution(i);
	   }
	   
	   Itineraire[][] trajets = (Itineraire[][]) resultDijkstra[1];
	   creerTournee(dureeTournee, ordreTournee, trajets);
	   
	   return true;
       }
       else {
	   return false;
       }
   }
   
   /**
    * Calcul du plus court chemin selon Dijkstra a partir d'une liste de sommets définis
    * @param idSommets
    * @return
    */
   private Object [] calculerDijkstra(ArrayList<Integer> idSommets){
       int nbrSommets = idSommets.size();
       double[][] couts = new double[nbrSommets][nbrSommets];
       @SuppressWarnings("unchecked")
       Itineraire[][] trajets = new Itineraire[nbrSommets][nbrSommets];
       for(Integer i : idSommets){
	   Object[] resultDijkstra = calculerDijkstra(i, idSommets);
	   double[] cout = (double[]) resultDijkstra[0];
	   Troncon[] pi = (Troncon[]) resultDijkstra[1];
	   couts[i] = cout;
	   Itineraire[] trajetsUnit = triTableauPi(pi, idSommets);
	   trajets[i] = trajetsUnit;
       }
       return new Object[]{couts, trajets};
   }
   
   /**
    * Calcul du plus court chemin selon Dijkstra a partir d'un sommet défini
    * @param id
    * @param nbrSommets
    * @return
    */
   private Object[] calculerDijkstra(int sourceId, ArrayList<Integer> idSommets){
       double couts[] = new double[idSommets.size()];
       Troncon[] pi = new Troncon[idSommets.size()];
       HashMap<Integer, Sommet> listeSommets = new HashMap<>();
       NavigableSet<Sommet> sommetsGris = new TreeSet<>();
       
       for(Integer id : idSommets){
	   int position = idSommets.indexOf(id);
	   Sommet nouveauSommet;
	   if(id != sourceId){
	       nouveauSommet = new Sommet(id, position, Double.POSITIVE_INFINITY, Etat.BLANC);
	   } else {
	       nouveauSommet = new Sommet(id, position, 0, Etat.GRIS);
	       sommetsGris.add(nouveauSommet);
	   }
	   listeSommets.put(id, nouveauSommet);
       }
       
       while(!sommetsGris.isEmpty()){
	   Sommet premierSommet = sommetsGris.first();
	   for(Troncon t : this.listeTroncons.get(premierSommet.getId())){
	       Sommet destination = listeSommets.get(t.getDestination().getId());
	       Etat etat = destination.getEtat();
	       if(etat != Etat.NOIR){
		   relacher(premierSommet, destination, t, pi, couts);
		   if(etat == Etat.BLANC){
		       destination.setEtat(Etat.GRIS);
		       sommetsGris.add(destination);
		   }
	       }
	   }
	   premierSommet.setEtat(Etat.NOIR);
	   sommetsGris.remove(premierSommet);
       }
       return new Object[]{couts, pi};
   }
   
   private void relacher(Sommet origine, Sommet destination, Troncon antecedent, Troncon[] pi, double[] couts){
       double nouveauCout = origine.getCout() + antecedent.getTpsParcours();
       if(destination.getCout() > nouveauCout){
	   destination.setCout(nouveauCout);
	   pi[destination.getPosition()] = antecedent;
	   couts[destination.getPosition()] = nouveauCout;
       }
   }
   
   /**
    * Mise en place de la liste des troncons correspondant aux plus courts 
    * chemins calcules selon l'algorithme de Dijkstra a partir d'un 
    * sommet defini
    * @param pi
    * @return
    */
   private Itineraire[] triTableauPi(Troncon[] pi, ArrayList<Integer> idSommets){
       @SuppressWarnings("unchecked")
       Itineraire[] trajetsUnit = new Itineraire[pi.length];
       for(int i = 0; i < pi.length; i++)
       {
	   List<Troncon> trajet = new ArrayList<Troncon>();
	   int j=i;
	   while(pi[j]!=null){
	      trajet.add(0, pi[j]);;
	      j=idSommets.indexOf(pi[j].getOrigine().getId());
	   }
	   Itineraire iti = new Itineraire(listeIntersections.get(idSommets.get(j)), listeIntersections.get(idSommets.get(i)), trajet);
	   trajetsUnit[i]=iti;
       }
       return trajetsUnit;
   }
   
   /**
    * Creation d'un tableau faisant correspondre l'identifiant de chaque sommet 
    * avec sa place dans le tableau des couts des plus courts chemins
    * @param nbrLivraisons
    * @return
    */
   private ArrayList<Integer> completionTableauLivraison() {
       ArrayList<Integer> sommets = new ArrayList<>();
       sommets.add(demandeDeLivraison.getEntrepot().getId());
       Set<Integer> cles = this.getListeLivraisons().keySet();
       Iterator<Integer> it = cles.iterator();
       while (it.hasNext()){
          Integer cle = it.next();
	  sommets.add(cle);
       }
       return sommets;
   }
   
   private int[] recupererDurees(List<Integer> idSommets)
   {
       int[] durees = new int[idSommets.size()];
       durees[0] = 0; //temps à passer à l'entrepot
       for(int i=1; i<idSommets.size(); i++) {
	   durees[i] = demandeDeLivraison.getLivraison(idSommets.get(i)).getDuree();
       }
       return durees;
   }
   
   private void creerTournee(int duree, int[] livraisons, Itineraire[][] itineraires) {
       tournee = new Tournee(duree);
       for(int i = 0; i < livraisons.length-1; i++)
       {
	   tournee.ajouterItineraire(itineraires[i][i+1]);
       }
       
       setChanged();
       notifyObservers(tournee);
   }
   
   public Intersection getIntersection(int id) {
       return this.listeIntersections.get(id);
   }
   
   public List<Troncon> getTronconsParIntersection(int idIntersection) {
       return this.listeTroncons.get(idIntersection);
   }

   public HashMap<Integer, List<Troncon>> getListeTroncons() {
       return this.listeTroncons;
   }
   
   public HashMap<Integer, Intersection> getListeIntersections() {
       return this.listeIntersections;
   }
   
   public HashMap<Integer, Livraison> getListeLivraisons() {
       return demandeDeLivraison.getListeLivraisons();
   }
   
   public Intersection getEntrepot() {
       return demandeDeLivraison.getEntrepot();
   }
   
   public List<Itineraire> getItineraires() {
       return tournee.getItineraires();
   }
   
   private enum Etat {
       GRIS,
       NOIR,
       BLANC
   }
   
   private class Sommet implements Comparable<Sommet> {

       private int id;
       private int position;
       private double cout;
       private Etat etat;
       
       public Sommet(int id, int position, double cout, Etat etat){
   	this.id = id;
   	this.position = position;
   	this.cout = cout;
   	this.etat = etat;
       }
       
       @Override
       public int compareTo(Sommet autre) {
   	// TODO Auto-generated method stub
   	return Double.compare(this.cout, autre.cout);
       }

       public int getId() {
           return id;
       }

       public int getPosition() {
           return position;
       }

       public double getCout() {
           return cout;
       }
       
       public Etat getEtat() {
           return etat;
       }
       
       public void setEtat(Etat nouvelEtat) {
   	this.etat = nouvelEtat;
       }
       
       public void setCout(double nouveauCout) {
   	this.cout = nouveauCout;
       }
       

   }
}
