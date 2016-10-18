package modele.tests;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import modele.DemandeDeLivraison;
import modele.Heure;
import modele.Intersection;
import modele.Plan;

public class DemandeDeLivraisonTest {
    Observer observer;
    boolean updateAppele;
    Plan plan;
    	
    @Before
    public void setUp(){
    	updateAppele = false;
    	observer = new Observer(){
    	    public void update(Observable o, Object arg){updateAppele = true;}
    	    };
    }
    
    @Test
    public void testAjouterLivraison() {
	Intersection entrepot = new Intersection(5, 214, 584);
	Heure heureDepart = new Heure("00:05:00");
	DemandeDeLivraison demandeDeLivraison = 
		new DemandeDeLivraison (heureDepart, entrepot);
	Intersection intersection = new Intersection(7, 417, 976);
	demandeDeLivraison.addObserver(observer);
	demandeDeLivraison.ajouterLivraison(50, intersection);
	assert(updateAppele);
    }

}
