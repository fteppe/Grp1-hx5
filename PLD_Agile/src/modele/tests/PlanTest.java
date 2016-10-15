package modele.tests;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

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
	Time heureDepart = new Time(15622523);
	int[] adresses = {4};
	int[] durees = {50};
	plan.ajouterDemandeDeLivraison(heureDepart, 3, adresses, durees);
    	assert(updateAppele);
    }

}
