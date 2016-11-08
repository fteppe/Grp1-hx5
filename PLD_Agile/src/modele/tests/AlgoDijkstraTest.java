package modele.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import modele.AlgoDijkstra;
import modele.Heure;
import modele.Intersection;
import modele.Itineraire;
import modele.Livraison;
import modele.Plan;
import modele.Troncon;
import xml.DeserialiseurXML;

public class AlgoDijkstraTest {

    @Test
    public void testCalculerDijkstra() {
	Plan plan = new Plan();
	AlgoDijkstra algoDijkstra = AlgoDijkstra.getInstance();
	plan.ajouterIntersection(1, 412, 574);
	Intersection i1 = new Intersection(1, 412, 574);
	plan.ajouterIntersection(2, 217, 574);
	Intersection i2 = new Intersection(2, 217, 574);
	plan.ajouterIntersection(3, 325, 574);
	Intersection i3 = new Intersection(3, 325, 574);
	plan.ajouterIntersection(4, 412, 544);
	Intersection i4 = new Intersection(4, 412, 544);
	plan.ajouterIntersection(5, 742, 574);
	Intersection i5 = new Intersection(5, 742, 574);
	plan.ajouterIntersection(6, 451, 174);
	Intersection i6 = new Intersection(6, 418, 974);
	plan.ajouterIntersection(10, 418, 974);
	Intersection i10 = new Intersection(10, 418, 974);
	plan.ajouterTroncon("t_1-2", 5, 1, 1, 2);
	Troncon a = new Troncon("t_1-2", i1, i2, 5, 1);
	plan.ajouterTroncon("t_2-1", 5, 1, 2, 1);
	Troncon b = new Troncon("t_2-1", i2, i1, 5, 1);
	plan.ajouterTroncon("t_2-4", 25, 1, 2, 4);
	Troncon c = new Troncon("t_2-4", i2, i4, 25, 1);
	plan.ajouterTroncon("t_4-2", 25, 1, 4, 2);
	Troncon d = new Troncon("t_4-2", i4, i2, 25, 1);
	plan.ajouterTroncon("t_4-5", 3, 1, 4, 5);
	Troncon e = new Troncon("t_4-5", i4, i5, 3, 1);
	plan.ajouterTroncon("t_5-4", 3, 1, 5, 4);
	Troncon f = new Troncon("t_5-4", i5, i4, 3, 1);
	plan.ajouterTroncon("t_4-3", 8, 1, 4, 3);
	Troncon g = new Troncon("t_4-3", i4, i3, 8, 1);
	plan.ajouterTroncon("t_3-4", 8, 1, 3, 4);
	Troncon h = new Troncon("t_3-4", i3, i4, 8, 1);
	plan.ajouterTroncon("t_3-5", 1, 1, 3, 5);
	Troncon i = new Troncon("t_3-5", i3, i5, 1, 1);
	plan.ajouterTroncon("t_5-3", 1, 1, 5, 3);
	Troncon j = new Troncon("t_5-3", i5, i3, 1, 1);
	plan.ajouterTroncon("t_3-6", 6, 1, 3, 6);
	Troncon k = new Troncon("t_3-6", i3, i6, 6, 1);
	plan.ajouterTroncon("t_6-3", 6, 1, 6, 3);
	Troncon l = new Troncon("t_6-3", i6, i3, 6, 1);
	plan.ajouterTroncon("t_5-6", 10, 1, 5, 6);
	Troncon m = new Troncon("t_5-6", i5, i6, 10, 1);
	plan.ajouterTroncon("t_6-5", 10, 1, 6, 5);
	Troncon n = new Troncon("t_6-5", i6, i5, 10, 1);
	plan.ajouterTroncon("t_6-10", 11, 1, 6, 10);
	Troncon o = new Troncon("t_6-10", i6, i10, 11, 1);
	plan.ajouterTroncon("t_10-6", 11, 1, 10, 6);
	Troncon p = new Troncon("t_10-6", i10, i6, 11, 1);
	plan.ajouterTroncon("t_10-1", 6, 1, 10, 1);
	Troncon q = new Troncon("t_10-1", i10, i1, 6, 1);
	plan.ajouterTroncon("t_1-10", 6, 1, 1, 10);
	Troncon r = new Troncon("t_1-10", i1, i10, 6, 1);
	plan.ajouterTroncon("t_2-10", 1, 1, 2, 10);
	Troncon s = new Troncon("t_2-10", i2, i10, 1, 1);
	plan.ajouterTroncon("t_10-2", 1, 1, 10, 2);
	Troncon t = new Troncon("t_10-2", i10, i2, 1, 1);
	
	algoDijkstra.chargerAlgo(plan.getListeIntersections(), plan.getListeTronconsTriee());
	ArrayList<Integer> listeSommets = new ArrayList<>();
	listeSommets.add(1);
	listeSommets.add(2);
	listeSommets.add(3);
	listeSommets.add(4);
	listeSommets.add(10);
	Object[] resultDijkstra = algoDijkstra.calculerDijkstra(1, listeSommets);
	int[] cout = (int[]) resultDijkstra[0];
	Itineraire[] trajetsUnit = (Itineraire[]) resultDijkstra[1];
	int[] coutComp = new int[5];
	coutComp[0] = 0;
	coutComp[1] = 5;
	coutComp[2] = 23;
	coutComp[3] = 27;
	coutComp[4] = 6;
	Itineraire[] trajetsUnitComp = new Itineraire[5];
	List<Troncon> list1 = new ArrayList<>();
	Itineraire iti1 = new Itineraire(i1, i1, list1);
	trajetsUnitComp[0] = iti1;
	List<Troncon> list2 = new ArrayList<>();
	list2.add(a);
	Itineraire iti2 = new Itineraire(i1, i2, list2);
	trajetsUnitComp[1] = iti2;
	List<Troncon> list3 = new ArrayList<>();
	list3.add(r);
	list3.add(p);
	list3.add(l);
	Itineraire iti3 = new Itineraire(i1, i3, list3);
	trajetsUnitComp[2] = iti3;
	List<Troncon> list4 = new ArrayList<>();
	list4.add(r);
	list4.add(p);
	list4.add(l);
	list4.add(i);
	list4.add(f);
	Itineraire iti4 = new Itineraire(i1, i4, list4);
	trajetsUnitComp[3] = iti4;
	List<Troncon> list5 = new ArrayList<>();
	list5.add(r);
	Itineraire iti5 = new Itineraire(i1, i10, list5);
	trajetsUnitComp[4] = iti5;
	
	plan.creerDemandeDeLivraison(heure, 4);
	plan.ajouterLivraison(1, 20);
	plan.ajouterLivraison(2, 10);
	plan.ajouterLivraison(5, 8);
	plan.ajouterLivraison(6, 10);
	plan.ajouterLivraison(7, 14);
	ArrayList<Integer> listeSommets = plan.methodeTest();
	for (Integer i : listeSommets) {
	    System.out.println(i);
	}
	Object[] objectResult = plan.methodeTest2(listeSommets);
	int[][] couts = (int[][]) objectResult[0];
	Itineraire[][] itineraires = (Itineraire[][]) objectResult[1];
	for (int i = 0; i < couts[0].length; i++) {
	    for (int j = 0; j < couts[0].length; j++) {
		System.out.println(listeSommets.get(i) + " - "
			+ listeSommets.get(j) + " : " + couts[i][j]);
	    }
	}
	for (int i = 0; i < itineraires[0].length; i++) {
	    for (int j = 0; j < itineraires[0].length; j++) {
		System.out.println(
			"----------------------------------------------------");
		System.out.println(itineraires[i][j].getDepart().getId() + "-"
			+ itineraires[i][j].getArrivee().getId());
		for (Troncon t : itineraires[i][j].getTroncons()) {
		    System.out.println(t.getOrigine().getId() + " - "
			    + t.getDestination().getId());
		}
	    }
	}

	Object[] resultTournee = plan.methodeTest3(100);
	System.out.println("Cout : " + (int) resultTournee[0]);
	for (Itineraire itineraire : (ArrayList<Itineraire>) resultTournee[1]) {
	    System.out.println(itineraire.getDepart().getId() + "-"
		    + itineraire.getArrivee().getId());
	    for (Troncon t : itineraire.getTroncons()) {
		System.out.println(t.getOrigine().getId() + " - "
			+ t.getDestination().getId());
	    }
	}
   }
    
    @Test
    public void testCalculerDijkstraLivraisonNonAtteignable() {
    //Plan avec l'intersection 10 qui n'est pas atteignable à partir des autres interserctions
	Plan plan = new Plan();
	//Creation et Ajout des intersections au plan
	AlgoDijkstra algoDijkstra = AlgoDijkstra.getInstance();
	plan.ajouterIntersection(1, 412, 574);
	Intersection i1 = new Intersection(1, 412, 574);
	plan.ajouterIntersection(2, 217, 574);
	Intersection i2 = new Intersection(2, 217, 574);
	plan.ajouterIntersection(3, 325, 574);
	Intersection i3 = new Intersection(3, 325, 574);
	plan.ajouterIntersection(4, 412, 544);
	Intersection i4 = new Intersection(4, 412, 544);
	plan.ajouterIntersection(5, 742, 574);
	Intersection i5 = new Intersection(5, 742, 574);
	plan.ajouterIntersection(6, 451, 174);
	Intersection i6 = new Intersection(6, 418, 974);
	plan.ajouterIntersection(10, 418, 974);
	Intersection i10 = new Intersection(10, 418, 974);
	
	//Creation et Ajout des Troncons au plan
	plan.ajouterTroncon("t_1-2", 5, 1, 1, 2);
	Troncon a = new Troncon("t_1-2", i1, i2, 5, 1);
	plan.ajouterTroncon("t_2-1", 5, 1, 2, 1);
	Troncon b = new Troncon("t_2-1", i2, i1, 5, 1);
	plan.ajouterTroncon("t_2-4", 25, 1, 2, 4);
	Troncon c = new Troncon("t_2-4", i2, i4, 25, 1);
	plan.ajouterTroncon("t_4-2", 25, 1, 4, 2);
	Troncon d = new Troncon("t_4-2", i4, i2, 25, 1);
	plan.ajouterTroncon("t_4-5", 3, 1, 4, 5);
	Troncon e = new Troncon("t_4-5", i4, i5, 3, 1);
	plan.ajouterTroncon("t_5-4", 3, 1, 5, 4);
	Troncon f = new Troncon("t_5-4", i5, i4, 3, 1);
	plan.ajouterTroncon("t_4-3", 8, 1, 4, 3);
	Troncon g = new Troncon("t_4-3", i4, i3, 8, 1);
	plan.ajouterTroncon("t_3-4", 8, 1, 3, 4);
	Troncon h = new Troncon("t_3-4", i3, i4, 8, 1);
	plan.ajouterTroncon("t_3-5", 1, 1, 3, 5);
	Troncon i = new Troncon("t_3-5", i3, i5, 1, 1);
	plan.ajouterTroncon("t_5-3", 1, 1, 5, 3);
	Troncon j = new Troncon("t_5-3", i5, i3, 1, 1);
	plan.ajouterTroncon("t_5-6", 10, 1, 5, 6);
	Troncon m = new Troncon("t_5-6", i5, i6, 10, 1);
	plan.ajouterTroncon("t_6-5", 10, 1, 6, 5);
	Troncon n = new Troncon("t_6-5", i6, i5, 10, 1);

	//chargement des informations dans algoDijkstra
	algoDijkstra.chargerAlgo(plan.getListeIntersections(), plan.getListeTronconsTriee());
	ArrayList<Integer> listeSommets = new ArrayList<>();
	listeSommets.add(1);
	listeSommets.add(2);
	listeSommets.add(3);
	listeSommets.add(4);
	listeSommets.add(10);
	//Dijkstra à partir de i1 vers tous les autres intersections
	//verifier que le cout vers i10 est infini et qu'il n'y a pas d'itinéraire vers i10
	Object[] resultDijkstra = algoDijkstra.calculerDijkstra(1, listeSommets);
	int[] cout = (int[]) resultDijkstra[0];
	assertTrue(cout[4]==Integer.MAX_VALUE);
	Itineraire[] trajetsUnit = (Itineraire[]) resultDijkstra[1];
	assertTrue(trajetsUnit[0].getTroncons().isEmpty());
	assertFalse(trajetsUnit[1].getTroncons().isEmpty());
	assertFalse(trajetsUnit[2].getTroncons().isEmpty());
	assertFalse(trajetsUnit[3].getTroncons().isEmpty());
	assertTrue(trajetsUnit[4].getTroncons().isEmpty());
	//Dijkstra à partir de i10 vers tous les autres intersections
	//verifier que tous les couts à partir de i10 sont infinis et qu'il n'y a pas d'itinéraire possible

	resultDijkstra = algoDijkstra.calculerDijkstra(10,listeSommets);
	cout=(int[]) resultDijkstra[0];
	assertTrue(cout[0]==Integer.MAX_VALUE &&
			cout[1]==Integer.MAX_VALUE &&
			cout[2]==Integer.MAX_VALUE &&
			cout[3]==Integer.MAX_VALUE &&
			cout[4]!=Integer.MAX_VALUE
			);
	trajetsUnit = (Itineraire[]) resultDijkstra[1];
	assertTrue(trajetsUnit[0].getTroncons().isEmpty());
	assertTrue(trajetsUnit[1].getTroncons().isEmpty());
	assertTrue(trajetsUnit[2].getTroncons().isEmpty());
	assertTrue(trajetsUnit[3].getTroncons().isEmpty());
	assertTrue(trajetsUnit[4].getTroncons().isEmpty());

   }
		
	/*Livraison livraison_addresse_2 = p.getListeLivraisons().get(2);
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
	fail("Not yet implemented");*/
}
