package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import javax.print.attribute.standard.Destination;

import tsp.TSP1;

public class Plan extends Observable {
   private HashMap<Integer, Intersection> listeIntersections; //Liste des intersections du plan classï¿½es selon leur identifiant
   private HashMap<Integer, List<Troncon>> listeTroncons; //Liste des troncons du plan classï¿½s selon l'identifiant de leur origine
   private DemandeDeLivraison demandeDeLivraison;
   private Tournee tournee;
   
   /**
    * Cree un Plan ne possedant aucune intersection et aucun tronï¿½on
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
       //Gestion d'une exception si deux intersections ont le mï¿½me numï¿½ro de sommet ?
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
   
   /**
    * Calcule la tournee (algo Dijkstra et TSP) si possible
    * et la cree
    * @param tpsLimite Temps maximum en millisecondes pour le calcul du parcours optimal
    * @return true si le calcul s'est bien deroule
    * false si le calcul a ete stoppe par la limite de temps
    */
   public boolean calculerTournee(int tpsLimite) {
       ArrayList<Integer> idSommets = completionTableauLivraison();
       Object[] resultDijkstra = calculerDijkstra(idSommets);
       
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
    * Calcul du plus court chemin selon Dijkstra a partir d'une liste de sommets dï¿½finis
    * @param idSommets
    * @return
    */
   private Object [] calculerDijkstra(ArrayList<Integer> idSommets){
       int nbrSommets = idSommets.size();
       int[][] couts = new int[nbrSommets][nbrSommets];
       @SuppressWarnings("unchecked")
       Itineraire[][] trajets = new Itineraire[nbrSommets][nbrSommets];
       int position = 0;
       for(Integer i : idSommets){
	   Object[] resultDijkstra = calculerDijkstra(i, idSommets);
	   int[] cout = (int[]) resultDijkstra[0];
	   Itineraire[] trajetsUnit = (Itineraire[]) resultDijkstra[1];
	   couts[position] = cout;
	   trajets[position] = trajetsUnit;
	   position ++;
       }
       return new Object[]{couts, trajets};
   }
   
   /**
    * Calcul du plus court chemin selon Dijkstra a partir d'un sommet dï¿½fini
    * @param id
    * @param nbrSommets
    * @return
    */
   private Object[] calculerDijkstra(int sourceId, ArrayList<Integer> idSommets){
       int coutsSommets[] = new int[idSommets.size()];
       HashMap<Integer, Sommet> listeSommets = new HashMap<>();
       NavigableSet<Sommet> sommetsGris = new TreeSet<>();
       int position = 0;
       for(int id : this.listeIntersections.keySet()){
	   Sommet nouveauSommet;
	   if(id != sourceId){
	       nouveauSommet = new Sommet(id, position, Integer.MAX_VALUE, Etat.BLANC);
	   } else {
	       nouveauSommet = new Sommet(id, position, 0, Etat.GRIS);
	       sommetsGris.add(nouveauSommet);
	   }
	   listeSommets.put(id, nouveauSommet);
	   position ++;
       }
       while(!sommetsGris.isEmpty()){
	   Sommet premierSommet = sommetsGris.first();
	   if(this.listeTroncons.get(premierSommet.getId()) != null){
	       for(Troncon t : this.listeTroncons.get(premierSommet.getId())){
		   Sommet destination = listeSommets.get(t.getDestination().getId());
		   Etat etat = destination.getEtat();
		   if(etat != Etat.NOIR){
		       relacher(premierSommet, destination, t);
	    	       if(etat == Etat.BLANC){
	    		   destination.setEtat(Etat.GRIS);
	    		   sommetsGris.add(destination);
	    		   /*System.out.println("++++++" + destination.getId());
	    		   for(Sommet s : sommetsGris){
	    		       System.out.println(s.getId());
	    		   }*/
	    	       }
	    	   }      
    	       }	  
	   }
	   premierSommet.setEtat(Etat.NOIR);
	   sommetsGris.remove(premierSommet);
	   if(idSommets.contains(premierSommet.getId())){
	       
	   }
       }
       position = 0;
       for(int id : idSommets){
	   coutsSommets[position] = listeSommets.get(id).getCout();
	   position ++;
       }
       Itineraire[] tableauPiTrie = triTableauPi(idSommets, listeSommets, sourceId);
       return new Object[]{coutsSommets, tableauPiTrie};
   }

   private void relacher(Sommet origine, Sommet destination, Troncon antecedent){
       int nouveauCout = origine.getCout() + antecedent.getTpsParcours();
       if(destination.getCout() > nouveauCout){
	   destination.setCout(nouveauCout);
	   destination.setAntecedent(antecedent);
       }
   }
   
   /**
    * Mise en place de la liste des troncons correspondant aux plus courts 
    * chemins calcules selon l'algorithme de Dijkstra a partir d'un 
    * sommet defini
    * @param pi
    * @return
    */
   private Itineraire[] triTableauPi(ArrayList<Integer> idSommets, HashMap<Integer, Sommet> listeSommets, int sourceId){
       @SuppressWarnings("unchecked")
       int nbrSommets = idSommets.size();
       Itineraire[] trajetsUnit = new Itineraire[nbrSommets];
       int position = 0;
       for(Integer id : idSommets)
       {
	   List<Troncon> trajet = new ArrayList<Troncon>();
	   Troncon antecedent = listeSommets.get(id).antecedent;
	   int idSommetCourant = id;
	   while(antecedent != null){
	      trajet.add(0, antecedent);
	      idSommetCourant = antecedent.getOrigine().getId();
	      antecedent = listeSommets.get(antecedent.getOrigine().getId()).getAntecedent();
	   }
	   if(idSommetCourant != sourceId){
	       trajet.clear();
	   }
	   Itineraire iti = new Itineraire(this.listeIntersections.get(sourceId), this.listeIntersections.get(id), trajet);
	   trajetsUnit[position]=iti;
	   position ++;
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
   
   /**
    * Recupere les durees des sommets donnes
    * @param idSommets Liste des sommets dont il faut les durees
    * @return Tableau des durees ordonnees selon l'ordre
    * des sommets en entree
    */
   private int[] recupererDurees(List<Integer> idSommets)
   {
       int[] durees = new int[idSommets.size()];
       durees[0] = 0; //temps a passer a l'entrepot
       for(int i=1; i<idSommets.size(); i++) {
	   durees[i] = demandeDeLivraison.getLivraison(idSommets.get(i)).getDuree();
       }
       return durees;
   }
   
   public ArrayList<Integer> methodeTest() {
       return completionTableauLivraison();
   }
   
   public Object[] methodeTest2(ArrayList<Integer> idSommets) {
       return calculerDijkstra(idSommets);
   }
   
   /**
    * Cree la Tournee suivant la liste des livraisons et les itineraires associes
    * @param duree Duree totale de la tournee
    * @param livraisons Liste ordonnee des livraisons a effectuer
    * @param itineraires Tableau des itineraires pour aller de la livraison i à la livraison j
    */
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

   public List<Troncon> getListeTroncons() {
       List<Troncon> listeNonOrdonneeTroncons = new ArrayList<Troncon>();
       Set<Integer> cles = this.listeTroncons.keySet();
       Iterator<Integer> it = cles.iterator();
       while (it.hasNext()){
          Integer cle = it.next();
	  listeNonOrdonneeTroncons.addAll(this.listeTroncons.get(cle));
       }
       return listeNonOrdonneeTroncons;
   }
   
   public HashMap<Integer, Intersection> getListeIntersections() {
       return this.listeIntersections;
   }
   
   public HashMap<Integer, Livraison> getListeLivraisons() {
	   if(demandeDeLivraison != null){
		   return demandeDeLivraison.getListeLivraisons();
	   }
	   else{
		   return null;
	   }
       
   }
   
   public Intersection getEntrepot() {
	   if(demandeDeLivraison != null){
	       return demandeDeLivraison.getEntrepot();
	   }
	   else{
		   return null;
	   }
   }
   
   public List<Itineraire> getItineraires() {
	   if(tournee != null)
	   {
		   return tournee.getItineraires();
	   }
	   else{
		   return null;
	   }
       
   }
   
   private enum Etat {
       GRIS,
       NOIR,
       BLANC
   }
   
   private class Sommet implements Comparable<Sommet> {

       private int id;
       private int position;
       private int cout;
       private Etat etat;
       private Troncon antecedent;
       
       public Sommet(int id, int position, int cout, Etat etat){
   	this.id = id;
   	this.position = position;
   	this.cout = cout;
   	this.etat = etat;
       }
       
       @Override
       public int compareTo(Sommet autre) {
   	// TODO Auto-generated method stub
	int coutCompare = this.cout - autre.cout;
	if(coutCompare == 0){
	    coutCompare = this.id - autre.id;
	}
   	return coutCompare;
       }

       public int getId() {
           return this.id;
       }

       public int getPosition() {
           return this.position;
       }

       public int getCout() {
           return this.cout;
       }
       
       public Etat getEtat() {
           return this.etat;
       }
       
       public Troncon getAntecedent() {
	   return this.antecedent;
       }
       
       public void setEtat(Etat nouvelEtat) {
   	this.etat = nouvelEtat;
       }
       
       public void setCout(int nouveauCout) {
   	this.cout = nouveauCout;
       }
       
       public void setAntecedent(Troncon nouvelAntecedent) {
	  this.antecedent = nouvelAntecedent;
       }
       

   }
}
