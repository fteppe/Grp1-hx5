package modele.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import modele.Itineraire;
import modele.Livraison;
import modele.Plan;
import modele.Troncon;
import xml.DeserialiseurXML;

public class TestDjikstra {
	
	private static Plan p;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		p = new Plan();
		try{
			DeserialiseurXML.chargerPlan(p);
			DeserialiseurXML.chargerLivraisons(p);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
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
		
		p.calculerTournee();

		assertTrue(p.getItineraires().get(0).equals(i_e_vers_2));
		assertTrue(p.getItineraires().get(1).equals(i_2_vers_3));
		assertTrue(p.getItineraires().get(2).equals(i_3_vers_e));
		
		fail("Not yet implemented");
	}

}
