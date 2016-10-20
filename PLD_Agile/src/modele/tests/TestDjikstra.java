package modele.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import modele.Heure;
import modele.Intersection;
import modele.Itineraire;
import modele.Livraison;
import modele.Plan;
import modele.Troncon;
import xml.DeserialiseurXML;

public class TestDjikstra {
	
	private static Plan p;

	/*@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		p = new Plan();
		try{
			DeserialiseurXML.chargerPlan(p);
			DeserialiseurXML.chargerLivraisons(p);
		} catch (Exception e){
			e.printStackTrace();
		}
	}*/

	/*@Test
	public void testCalculerDijkstra() {
		Livraison livraison_addresse_2 = p.getListeLivraisons().get(2);
		Livraison livraison_addresse_3 = p.getListeLivraisons().get(3);
		
		List<Troncon> troncons_e_2 = new ArrayList<Troncon>();
		troncons_e_2.add(p.getListeTroncons().get(0).get(0));
		troncons_e_2.add(p.getListeTroncons().get(1).get(1));
		Itineraire i_e_vers_2 = new Itineraire(p.getEntrepot(),livraison_addresse_2.getAdresse(),troncons_e_2);
		
		List<Troncon> troncons_2_3 = new ArrayList<Troncon>();
		troncons_2_3.add(p.getListeTroncons().get(2).get(1));
		troncons_2_3.add(p.getListeTroncons().get(5).get(2));
		Itineraire i_2_vers_3 = new Itineraire(livraison_addresse_2.getAdresse(),livraison_addresse_3.getAdresse(),troncons_2_3);

		List<Troncon> troncons_3_e = new ArrayList<Troncon>();
		troncons_3_e.add(p.getListeTroncons().get(3).get(0));
		troncons_3_e.add(p.getListeTroncons().get(5).get(0));
		Itineraire i_3_vers_e = new Itineraire(livraison_addresse_3.getAdresse(),p.getEntrepot(),troncons_3_e);
		
		boolean calculReussi = p.calculerTournee(60000);
		assert(calculReussi);
		assertTrue(p.getItineraires().get(0).equals(i_e_vers_2));
		assertTrue(p.getItineraires().get(1).equals(i_2_vers_3));
		assertTrue(p.getItineraires().get(2).equals(i_3_vers_e));
		
		fail("Not yet implemented");
	}*/
	
	@Test
	/*
	 * Graphe composé de 5 livraisons dont la tournée doit être calculée correctement
	 */
	public void testCalculerDijkstra() {
	    p  = new Plan();
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
	    }
	    assertTrue(dureeTotale == 80);
	}
}
