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
   private HashMap<Integer, Intersection> listeIntersections; //Liste des intersections du plan classees selon leur identifiant
   private HashMap<Integer, List<Troncon>> listeTroncons; //Liste des troncons du plan classes selon l'identifiant de leur origine
   private DemandeDeLivraison demandeDeLivraison;
   private Tournee tournee;
   
   /**
    * Cree un Plan ne possedant aucune intersection et aucun troncon
    */
   public Plan() {
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
       //Gestion d'une exception si deux intersections ont le m�me num�ro de sommet ?
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
       if(this.listeTroncons.containsKey(origine)) {
	   this.listeTroncons.get(origine).add(nouvTroncon);
       } else {
	   List<Troncon> nouvListeTroncons = new ArrayList<Troncon>();
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
   public void creerDemandeDeLivraison(Heure heureDepart, int entrepot) {
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
   public void ajouterLivraison(int adresse, int duree) {
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
       //On recupere la liste des identifiants des sommets devant constituer le
       //graphe complet analyse par le TSP
       ArrayList<Integer> idSommets = completionTableauLivraison();
       //On cherche a constituer un graphe complet grace � l'algorithme
       //de Dijkstra
       Object[] resultDijkstra = calculerDijkstra(idSommets);
       TSP1 tsp = new TSP1();
       int[] durees = recupererDurees(idSommets);
       int[][] couts = (int[][]) resultDijkstra[0];
       //On cherche l'itineraire optimal via l'utilisation du TSP
       tsp.chercheSolution(tpsLimite, idSommets.size(), couts, durees);
       if(!tsp.getTempsLimiteAtteint()) {
	   //On recupere la liste ordonnee des itineraires calculee par le TSP 
	   //ainsi que le cout correspondant
	   int dureeTournee = tsp.getCoutMeilleureSolution();
	   if(dureeTournee < 0) //dureeTournee est negatif donc pas de chemin possible
	       return false;
	   else {
	       int[] ordreTournee = new int[idSommets.size()];
	       for(int i=0; i<idSommets.size(); i++) {
		   ordreTournee[i] = tsp.getMeilleureSolution(i);
	       }
	       Itineraire[][] trajets = (Itineraire[][]) resultDijkstra[1];
	       creerTournee(dureeTournee, ordreTournee, trajets);
	       return true;
	   }
       } else {
	   return false;
       }
   }
   
   /**
    * Calcul de tout les plus courts chemins selon l'algorithme de Dijkstra
    * entre les sommets indiques
    * @param idSommets Identifiants des sommets constituant la tournee finale
    * @return Deux tableaux contenant l'ensemble des couts et des itineraires optimaux
    * resultant des calculs de plus court chemin effectues
    */
   private Object [] calculerDijkstra(ArrayList<Integer> idSommets){
       int nbrSommets = idSommets.size();
       int[][] couts = new int[nbrSommets][nbrSommets];
       @SuppressWarnings("unchecked")
       Itineraire[][] trajets = new Itineraire[nbrSommets][nbrSommets];
       int position = 0;
       //On calcule les plus courts chemins en utilisant l'algorithme de
       //Dijkstra avec chacun des sommets en parametre comme point de depart
       for(Integer i : idSommets) {
	   Object[] resultDijkstra = calculerDijkstra(i, idSommets);
	   int[] cout = (int[]) resultDijkstra[0];
	   Itineraire[] trajetsUnit = (Itineraire[]) resultDijkstra[1];
	   //On complete les tableaux de cout et d'itineraires permettant de
	   //de calculer la tournee 
	   couts[position] = cout;
	   trajets[position] = trajetsUnit;
	   position ++;
       }
       return new Object[]{couts, trajets};
   }
   
   /**
    * Creation d'un tableau faisant correspondre l'identifiant de chaque sommet 
    * avec sa place dans le tableau des couts et des itineraires resultant des
    * calculs de plus court chemin
    * @return Identifiants des sommets constituant la tournee finale
    */
   private ArrayList<Integer> completionTableauLivraison() {
       ArrayList<Integer> sommets = new ArrayList<>();
       sommets.add(demandeDeLivraison.getEntrepot().getId());
       Set<Integer> cles = this.getListeLivraisons().keySet();
       Iterator<Integer> it = cles.iterator();
       while (it.hasNext()) {
          Integer cle = it.next();
	  sommets.add(cle);
       }
       return sommets;
   }
   
   /**
    * Calcul du plus court chemin selon Dijkstra a partir d'un sommet defini
    * @param sourceId Identifiant du sommet de depart 
    * @param idSommets Identifiants des sommets constituant la tournee finale
    * @return Deux tableaux contenant l'ensemble des couts et des itineraires optimaux
    * resultant des calculs de plus court chemin effectues a partir d'un unique sommet
    */
   private Object[] calculerDijkstra(int sourceId, ArrayList<Integer> idSommets) {
       int coutsSommets[] = new int[idSommets.size()];
       HashMap<Integer, Sommet> listeSommets = new HashMap<>();
       NavigableSet<Sommet> sommetsGris = new TreeSet<>();
       int position = 0;
       //On initialise l'ensemble des sommets a parcourir par l'algorithme,
       //correspondant a la liste des intersections du plan
       for(int id : this.listeIntersections.keySet()) {
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
       while(!sommetsGris.isEmpty()) {
	   Sommet premierSommet = sommetsGris.first();
	   if(this.listeTroncons.get(premierSommet.getId()) != null){
	       for(Troncon t : this.listeTroncons.get(premierSommet.getId())){
		   Sommet destination = listeSommets.get(t.getDestination().getId());
		   Etat etat = destination.getEtat();
		   if(etat != Etat.NOIR) {
		       //On enleve du tas binaire le sommet pret a etre relache
		       //avant sa modification afin de ne pas dissocier l'objet recupere
		       //et celui contenu dans la liste des sommets gris
		       sommetsGris.remove(destination);
		       relacher(premierSommet, destination, t);
	    	       if(etat == Etat.BLANC) {
	    		   destination.setEtat(Etat.GRIS);
	    	       }
	    	       sommetsGris.add(destination);
	    	   }      
    	       }	  
	   }
	   sommetsGris.remove(premierSommet);
	   premierSommet.setEtat(Etat.NOIR);
       }
       position = 0;
       //On recupere seulement les couts des sommets devant etre
       //presents dans la tournee 
       for(int id : idSommets) {
	   int cout = listeSommets.get(id).getCout();
	   coutsSommets[position] = listeSommets.get(id).getCout();
	   position ++;
       }
       //On transforme la liste des sommets avec leur antecedent en un tableau
       //d'itineraires
       Itineraire[] tableauPiTrie = triTableauPi(idSommets, listeSommets, sourceId);
       return new Object[]{coutsSommets, tableauPiTrie};
   }

   /**
    * Relachement de l'arc (origine, destination)
    * @param origine Sommet d'origine de l'arc
    * @param destination Sommet de destination de l'arc
    * @param antecedent Arc relache
    */
   private void relacher(Sommet origine, Sommet destination, Troncon antecedent) {
       int nouveauCout = origine.getCout() + antecedent.getTpsParcours();
       if(destination.getCout() > nouveauCout) {
	   destination.setCout(nouveauCout);
	   destination.setAntecedent(antecedent);
       }
   }
   
   /**
    * Mise en place de la liste des troncons correspondant aux plus courts 
    * chemins calcules selon l'algorithme de Dijkstra a partir d'un 
    * sommet defini
    * @param idSommets Identifiants des sommets constituant la tournee finale
    * @param listeSommets Liste des sommets parcourus par l'algorithme 
    * de Dijkstra
    * @param sourceId Identifiant du sommet de depart 
    * @return Tableau d'itineraires correpondant aux plus courts chemins
    * entre le sommet de depart et les sommets constituant la tournee finale
    */
   private Itineraire[] triTableauPi(ArrayList<Integer> idSommets, HashMap<Integer, Sommet> listeSommets, int sourceId) {
       @SuppressWarnings("unchecked")
       int nbrSommets = idSommets.size();
       Itineraire[] trajetsUnit = new Itineraire[nbrSommets];
       int position = 0;
       //On cree les itineraires correspondant aux plus courts chemins 
       //calcules a partir d'un unique sommet de depart
       for(Integer id : idSommets) {
	   List<Troncon> trajet = new ArrayList<Troncon>();
	   Troncon antecedent = listeSommets.get(id).antecedent;
	   int idSommetCourant = id;
	   while(antecedent != null) {
	      trajet.add(0, antecedent);
	      idSommetCourant = antecedent.getOrigine().getId();
	      antecedent = listeSommets.get(antecedent.getOrigine().getId()).getAntecedent();
	   }
	   //Si il n'y a pas de chemin entre le sommet de depart et le sommet courant,
	   //on considere que l'itineraire correspondant est vide
	   if(idSommetCourant != sourceId) {
	       trajet.clear();
	   }
	   Itineraire iti = new Itineraire(this.listeIntersections.get(sourceId), this.listeIntersections.get(id), trajet);
	   trajetsUnit[position]=iti;
	   position ++;
       }
       return trajetsUnit;
   }
   
   /**
    * Recupere les durees des sommets donnes
    * @param idSommets Liste des sommets dont il faut les durees
    * @return Tableau des durees ordonnees selon l'ordre
    * des sommets en entree
    */
   private int[] recupererDurees(List<Integer> idSommets) {
       int[] durees = new int[idSommets.size()];
       durees[0] = 0; //temps a passer a l'entrepot
       for(int i=1; i<idSommets.size(); i++) {
	   durees[i] = demandeDeLivraison.getLivraison(idSommets.get(i)).getDuree();
       }
       return durees;
   }
   
   /**
    * Cree la Tournee suivant la liste des livraisons et les itineraires associes
    * @param duree Duree totale de la tournee
    * @param livraisons Liste ordonnee des livraisons a effectuer
    * @param itineraires Tableau des itineraires pour aller de la livraison i � la livraison j
    */
   private void creerTournee(int duree, int[] livraisons, Itineraire[][] itineraires) {
       tournee = new Tournee(duree);
       for(int i = 0; i < livraisons.length-1; i++) {
	   tournee.ajouterItineraire(itineraires[livraisons[i]][livraisons[i+1]]);
       }
       tournee.ajouterItineraire(itineraires[livraisons[livraisons.length-1]][livraisons[0]]);
       setChanged();
       notifyObservers(tournee);
   }
   
   /*public ArrayList<Integer> methodeTest() {
       return completionTableauLivraison();
   }*/
   
   /*public Object[] methodeTest2(ArrayList<Integer> idSommets) {
       return calculerDijkstra(idSommets);
   }*/
   
   /*public Object[] methodeTest3(int tpsMax) {
       boolean fini = calculerTournee(tpsMax);
       if(fini){
       return new Object[]{this.tournee.getDuree(), this.tournee.getItineraires()};
       }
       else {
	   System.out.println("Pas fini.");
	   return null;
       }
   }*/
   
   /**
    * Vide le Plan, remet a zero les listes d'intersection,
    * de troncon et nullifie la demande de livraison et la tournee
    */
   public void viderPlan() {
       this.listeIntersections = new HashMap<Integer, Intersection>();
       this.listeTroncons = new HashMap<Integer, List<Troncon>>();
       this.demandeDeLivraison = null;
       this.tournee = null;
       setChanged();
       notifyObservers();
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
       while (it.hasNext()) {
          Integer cle = it.next();
	  listeNonOrdonneeTroncons.addAll(this.listeTroncons.get(cle));
       }
       return listeNonOrdonneeTroncons;
   }
   
   public HashMap<Integer, Intersection> getListeIntersections() {
       return this.listeIntersections;
   }
   
   public HashMap<Integer, Livraison> getListeLivraisons() {
	   if(demandeDeLivraison != null) {
		   return demandeDeLivraison.getListeLivraisons();
	   } else {
		   return null;
	   }
       
   }
   
   public Intersection getEntrepot() {
	   if(demandeDeLivraison != null) {
	       return demandeDeLivraison.getEntrepot();
	   } else {
		   return null;
	   }
   }
   
   public List<Itineraire> getItineraires() {
	   if(tournee != null) {
		   return tournee.getItineraires();
	   } else {
		   return null;
	   }
   }
   
   public void setTournee(Tournee tournee) {
	this.tournee = tournee;
}

public Integer getDureeTournee() {
	   if(tournee != null) {
		   return tournee.getDuree();
	   } else {
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
       
       public Sommet(int id, int position, int cout, Etat etat) {
   	this.id = id;
   	this.position = position;
   	this.cout = cout;
   	this.etat = etat;
       }
       
       @Override
       public int compareTo(Sommet autre) {
   	// TODO Auto-generated method stub
	int coutCompare = this.cout - autre.cout;
	if(coutCompare == 0) {
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
