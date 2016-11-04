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
   private int[][] couts;
   private Itineraire[][] trajets;
   private ArrayList<Integer> idSommets;
   private AlgoDijkstra algo;
   
   /**
    * Cree un plan ne possedant aucune intersection et aucun troncon
    */
   public Plan() {
       this.listeIntersections = new HashMap<Integer, Intersection>();
       this.listeTroncons = new HashMap<Integer, List<Troncon>>();
   }
   
   /**
    * Cree et ajoute une intersection au plan courant
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
       //Gestion d'une exception si deux intersections ont le meme numero de sommet ?
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
       //Si un troncon ayant la meme origine que le troncon à ajouter,
       //on les inserent dans la meme liste.
       if(this.listeTroncons.containsKey(origine)) {
	   this.listeTroncons.get(origine).add(nouvTroncon);
       } else {
	   //On cree une nouvelle liste pour le nouvel origine
	   List<Troncon> nouvListeTroncons = new ArrayList<Troncon>();
	   nouvListeTroncons.add(nouvTroncon);
	   this.listeTroncons.put(origine, nouvListeTroncons);
       }
       setChanged();
       notifyObservers();
   }
   
   /**
    * Cree et ajoute une demande de livraison au plan
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
    * Cree et ajoute une livraison a la demande de livraison associee au Plan
    * @param adresses Identifiant de l'intersection correspondant 
    * 				a la livraison a effectuer
    * @param durees Duree de la livraison a effectuer
    */
   public void ajouterLivraison(int adresse, int duree) {
       this.demandeDeLivraison.ajouterLivraison(duree,
		   this.listeIntersections.get(adresse));
       setChanged();
       notifyObservers();
   }
   
   /**
    * Retire la livraison indiquee de la tournee
    * @param adresse Identifiant de l'intersection correspondante
    * 			a la livraison a retirer 
    * @return Livraison retiree de la tournee
    */
   public Livraison retirerLivraisonTournee(int adresse) {
       // On recupere les ids du nouvel itineraire a creer
       int[] nvItin = this.tournee.supprimerLivraison(adresse);
       int indiceDepart = idSommets.indexOf(nvItin[0]);
       int indiceArrivee = idSommets.indexOf(nvItin[1]);
       tournee.insererItineraire(trajets[indiceDepart][indiceArrivee]);
       setChanged();
       notifyObservers();
       
       // On recupere la livraison supprimee
       return tournee.getLivraison(adresse);
   }
   
   public void insererLivraisonTournee(Livraison liv, int idPrec, int idSuiv) {
       int indiceDepart = idSommets.indexOf(idPrec);
       int indiceLivr = idSommets.indexOf(liv.getAdresse().getId());
       int indiceArrivee = idSommets.indexOf(idSuiv);
       tournee.insererItineraire(trajets[indiceDepart][indiceLivr]);
       tournee.insererItineraire(trajets[indiceLivr][indiceArrivee]);
       setChanged();
       notifyObservers();
   }
   
   /**
    * Calcule la tournee (algo Dijkstra et TSP) si possible
    * et la cree
    * @param tpsLimite Temps maximum en millisecondes pour le calcul du parcours optimal
    * @return true Si le calcul s'est bien deroule
    * false Si le calcul a ete stoppe par la limite de temps
    */
   public boolean calculerTournee(int tpsLimite) {
       // On initialise l'algo de Dijkstra
       algo = AlgoDijkstra.getInstance();
       algo.chargerAlgo(listeIntersections, listeTroncons);
       //On recupere la liste des identifiants des sommets devant constituer le
       //graphe complet analyse par le TSP
       idSommets = completionTableauLivraison();
       //On constitue un graphe complet grace a l'algorithme
       //de Dijkstra
       Object[] resultDijkstra = algo.calculerDijkstra(idSommets);
       TSP1 tsp = new TSP1();
       int[] durees = recupererDurees(idSommets);
       this.couts = (int[][]) resultDijkstra[0];
       //On cherche l'itineraire optimal via l'utilisation du TSP
       tsp.chercheSolution(tpsLimite, idSommets.size(), this.couts, durees);
       if(!tsp.getTempsLimiteAtteint()) {
	   int dureeTournee = tsp.getCoutMeilleureSolution();
	   if(dureeTournee < 0) //dureeTournee est negatif donc pas de chemin possible
	       return false;
	   else {
	       //On recupere la liste ordonnee des livraisons calculee par le TSP 
	       //et on lui associe une liste d'itineraires
	       int[] ordreTournee = new int[idSommets.size()];
	       for(int i=0; i<idSommets.size(); i++) {
		   ordreTournee[i] = tsp.getMeilleureSolution(i);
	       }
	       this.trajets = (Itineraire[][]) resultDijkstra[1];
	       creerTournee(dureeTournee, ordreTournee, this.trajets);
	       return true;
	   }
       } else {
	   return false;
       }
   }
   
   /**
    * Recupere les durees des livraisons correspondant aux intersections 
    * donnees en parametres
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
    * Cree la Tournee suivant la liste des livraisons, 
    * l'entrepot et les itineraires associes
    * @param duree Duree totale de la tournee
    * @param livraisons Liste ordonnee des intersections a visiter
    * @param itineraires Tableau des itineraires pour aller de la livraison i a la livraison j
    */
   private void creerTournee(int duree, int[] livraisons, Itineraire[][] itineraires) {
       tournee = new Tournee(duree);
       for(int i = 0; i < livraisons.length-1; i++) {
	   Livraison prochLivr = demandeDeLivraison.getLivraison(livraisons[i+1]);
	   tournee.ajouterItineraire(itineraires[livraisons[i]][livraisons[i+1]], prochLivr);
       }
       tournee.ajouterItineraire(itineraires[livraisons[livraisons.length-1]][livraisons[0]], null);
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
   
   /**
    * Creation d'un tableau faisant correspondre l'identifiant de chaque
    * intersection avec sa place dans le tableau des couts et des itineraires
    * resultant des calculs de plus court chemin
    * 
    * @return Identifiants des intersections constituant la tournee finale
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
}
