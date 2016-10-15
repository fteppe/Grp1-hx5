package modele.tests;

import static org.junit.Assert.*;

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
    	observer = new Observer(){public void update(Observable o, Object arg){updateAppele = true;}};
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

}
