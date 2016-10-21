package modele.tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import modele.Heure;
import modele.Itineraire;
import modele.Plan;
import modele.Troncon;

public class PlanTest {

    Observer observer;
    boolean updateAppele;
    	
    @Before
    public void setUp(){
    	updateAppele = false;
    	observer = new Observer(){
    	    public void update(Observable o, Object arg){updateAppele = true;}
    	    };
    }
    	
    @Test
    public void testAjouterIntersection() {
	Plan plan = new Plan();
    	plan.addObserver(observer);
	plan.ajouterIntersection(4, 245, 241);
    	assert(updateAppele);
    }

    @Test
    public void testAjouterTroncon() {
	Plan plan = new Plan();
    	plan.addObserver(observer);
	plan.ajouterTroncon("Rue du Chataîgnier", 500, 50, 1, 2);
    	assert(updateAppele);
    }
    
    @Test
    public void testDemandeDeLivraison() {
	Plan plan = new Plan();
    	plan.addObserver(observer);
	plan.ajouterIntersection(4, 245, 241);
	plan.ajouterIntersection(3, 142, 784);
	plan.ajouterTroncon("Rue du Chataîgnier", 500, 50, 3, 4);
	plan.ajouterTroncon("Rue des Paril", 500, 50, 4, 3);
	Heure heureDepart = new Heure("08:00:00");
	int[] adresses = {4};
	int[] durees = {50};
	plan.creerDemandeDeLivraison(heureDepart, 3);
	for(int i = 0; i < adresses.length; i++){
	    plan.ajouterLivraison(adresses[i], durees[i]);
	}
    	assert(updateAppele);
    }

    @Test
	/*
	 * Graphe compose de 5 livraisons dont la tournee doit etre calculee correctement
	 */
	public void testCalculerTournee() {
	    Plan p  = new Plan();
	    p.ajouterIntersection(1, 412, 574);
	    p.ajouterIntersection(2, 217, 574);
	    p.ajouterIntersection(3, 325, 574);
	    p.ajouterIntersection(4, 412, 544);
	    p.ajouterIntersection(5, 742, 574);
	    p.ajouterIntersection(6, 451, 174);
	    p.ajouterIntersection(7, 418, 974);
	    p.ajouterIntersection(8, 442, 484);
	    p.ajouterTroncon("h0", 75, 25, 1, 2);
	    p.ajouterTroncon("h1", 50, 25, 2, 3);
	    p.ajouterTroncon("h2", 25, 25, 3, 8);
	    p.ajouterTroncon("h3", 100, 25, 4, 1);
	    p.ajouterTroncon("h4", 150, 25, 1, 5);
	    p.ajouterTroncon("h5", 25, 25, 5, 6);
	    p.ajouterTroncon("h6", 200, 25, 6, 7);
	    p.ajouterTroncon("h7", 25, 25, 6, 7);
	    p.ajouterTroncon("h8", 50, 25, 7, 2);
	    p.ajouterTroncon("h0", 50, 25, 2, 1);
	    p.ajouterTroncon("h3", 50, 25, 1, 4);
	    Heure heure = new Heure("21:05:00");
	    p.creerDemandeDeLivraison(heure, 4);
	    p.ajouterLivraison(1, 20);
	    p.ajouterLivraison(2, 10);
	    p.ajouterLivraison(5, 8);
	    p.ajouterLivraison(6, 10);
	    p.ajouterLivraison(7, 14);
	    boolean calculReussi = p.calculerTournee(60000);
	    int dureeTotale = p.getDureeTournee();
	    List<Itineraire> listeItineraires= p.getItineraires();
	    int[] listeSommetsTourneePoss1 = {4,1,5,6,7,2,4};
	    int[] listeSommetsTourneePoss2 = {4,5,6,7,2,1,4};
	    int position = 0;
	    assertTrue(calculReussi);
	    for (Itineraire i : listeItineraires) {
		assertTrue(i.getDepart().getId() 
			== listeSommetsTourneePoss1[position] 
			|| i.getDepart().getId()
			== listeSommetsTourneePoss2[position]);
		assertTrue(i.getArrivee().getId() 
			== listeSommetsTourneePoss1[position+1] 
			|| i.getArrivee().getId()
			== listeSommetsTourneePoss2[position+1]);
		position ++;
		System.out.println(i.getDepart().getId());
	    }
	    assertTrue(dureeTotale == 80);
	}
    
    @Test
	/*
	 * Graphe compose de 5 livraisons dont le calcul de tournee ne doit pas
	 * s'operer correctement, le temps limite etant de 0 secondes
	 */
	public void testCalculerTournee2() {
	    Plan p  = new Plan();
	    p.ajouterIntersection(1, 412, 574);
	    p.ajouterIntersection(2, 217, 574);
	    p.ajouterIntersection(3, 325, 574);
	    p.ajouterIntersection(4, 412, 544);
	    p.ajouterIntersection(5, 742, 574);
	    p.ajouterIntersection(6, 451, 174);
	    p.ajouterIntersection(7, 418, 974);
	    p.ajouterIntersection(8, 442, 484);
	    p.ajouterTroncon("h0", 75, 25, 1, 2);
	    p.ajouterTroncon("h1", 50, 25, 2, 3);
	    p.ajouterTroncon("h2", 25, 25, 3, 8);
	    p.ajouterTroncon("h3", 100, 25, 4, 1);
	    p.ajouterTroncon("h4", 150, 25, 1, 5);
	    p.ajouterTroncon("h5", 25, 25, 5, 6);
	    p.ajouterTroncon("h6", 200, 25, 6, 7);
	    p.ajouterTroncon("h7", 25, 25, 6, 7);
	    p.ajouterTroncon("h8", 50, 25, 7, 2);
	    p.ajouterTroncon("h0", 50, 25, 2, 1);
	    p.ajouterTroncon("h3", 50, 25, 1, 4);
	    Heure heure = new Heure("21:05:00");
	    p.creerDemandeDeLivraison(heure, 4);
	    p.ajouterLivraison(1, 20);
	    p.ajouterLivraison(2, 10);
	    p.ajouterLivraison(5, 8);
	    p.ajouterLivraison(6, 10);
	    p.ajouterLivraison(7, 14);
	    boolean calculReussi = p.calculerTournee(0);
	    assert(calculReussi == false);
    }
    
    
    @Test
	/*
	 * Graphe compose de 5 livraisons dont la tournee possede un cout de 
	 * 0, n'etant composee que de l'entrepot.
	 */
	public void testCalculerTournee3() {
	    Plan p  = new Plan();
	    p.ajouterIntersection(1, 412, 574);
	    p.ajouterIntersection(2, 217, 574);
	    p.ajouterIntersection(3, 325, 574);
	    p.ajouterIntersection(4, 412, 544);
	    p.ajouterIntersection(5, 742, 574);
	    p.ajouterIntersection(6, 451, 174);
	    p.ajouterIntersection(7, 418, 974);
	    p.ajouterIntersection(8, 442, 484);
	    p.ajouterTroncon("h0", 75, 25, 1, 2);
	    p.ajouterTroncon("h1", 50, 25, 2, 3);
	    p.ajouterTroncon("h2", 25, 25, 3, 8);
	    p.ajouterTroncon("h3", 100, 25, 4, 1);
	    p.ajouterTroncon("h4", 150, 25, 1, 5);
	    p.ajouterTroncon("h5", 25, 25, 5, 6);
	    p.ajouterTroncon("h6", 200, 25, 6, 7);
	    p.ajouterTroncon("h7", 25, 25, 6, 7);
	    p.ajouterTroncon("h8", 50, 25, 7, 2);
	    p.ajouterTroncon("h0", 50, 25, 2, 1);
	    p.ajouterTroncon("h3", 50, 25, 1, 4);
	    Heure heure = new Heure("21:05:00");
	    p.creerDemandeDeLivraison(heure, 4);
	    boolean calculReussi = p.calculerTournee(2000);
	    int dureeTotale = p.getDureeTournee();
	    System.out.println(dureeTotale);
	    assert(calculReussi == true);
	    assert(dureeTotale == 0);
    }

}

