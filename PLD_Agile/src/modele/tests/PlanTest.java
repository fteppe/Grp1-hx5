package modele.tests;

import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import modele.Heure;
import modele.Plan;

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

}
