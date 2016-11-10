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
import modele.ModeleException;
import modele.Plan;
import modele.Troncon;
import xml.DeserialiseurXML;

public class AlgoDijkstraTest {

    @Before

    @Test
    public void testCalculerDijkstraValide() {
	Plan plan = new Plan();
	AlgoDijkstra algoDijkstra = AlgoDijkstra.getInstance();
	Intersection i1 = new Intersection(1, 412, 574);
	Intersection i2 = new Intersection(2, 217, 574);
	Intersection i3 = new Intersection(3, 325, 574);
	Intersection i4 = new Intersection(4, 412, 544);
	Intersection i5 = new Intersection(5, 742, 574);
	Intersection i6 = new Intersection(6, 418, 974);
	Intersection i10 = new Intersection(10, 418, 974);
	Troncon t_1_2 = new Troncon("t_1_2", i1, i2, 5, 1);
	Troncon t_2_1 = new Troncon("t_2_1", i2, i1, 5, 1);
	Troncon t_2_4 = new Troncon("t_2_4", i2, i4, 25, 1);
	Troncon t_4_2 = new Troncon("t_4_2", i4, i2, 25, 1);
	Troncon t_4_5 = new Troncon("t_4_5", i4, i5, 3, 1);
	Troncon t_5_4 = new Troncon("t_5_4", i5, i4, 3, 1);
	Troncon t_4_3 = new Troncon("t_4_3", i4, i3, 8, 1);
	Troncon t_3_4 = new Troncon("t_3_4", i3, i4, 8, 1);
	Troncon t_3_5 = new Troncon("t_3_5", i3, i5, 1, 1);
	Troncon t_5_3 = new Troncon("t_5_3", i5, i3, 1, 1);
	Troncon t_3_6 = new Troncon("t_3_6", i3, i6, 6, 1);
	Troncon t_6_3 = new Troncon("t_6_3", i6, i3, 6, 1);
	Troncon t_5_6 = new Troncon("t_5_6", i5, i6, 10, 1);
	Troncon t_6_5 = new Troncon("t_6_5", i6, i5, 10, 1);
	Troncon t_6_10 = new Troncon("t_6_10", i6, i10, 11, 1);
	Troncon t_10_6 = new Troncon("t_10_6", i10, i6, 11, 1);
	Troncon t_10_1 = new Troncon("t_10_1", i10, i1, 6, 1);
	Troncon t_1_10 = new Troncon("t_1_10", i1, i10, 6, 1);
	Troncon t_2_10 = new Troncon("t_2_10", i2, i10, 1, 1);
	Troncon t_10_2 = new Troncon("t_10_2", i10, i2, 1, 1);
	try {
	    plan.ajouterIntersection(1, 412, 574);
	    plan.ajouterIntersection(2, 217, 574);
	    plan.ajouterIntersection(3, 325, 574);
	    plan.ajouterIntersection(4, 412, 544);
	    plan.ajouterIntersection(5, 742, 574);
	    plan.ajouterIntersection(6, 451, 174);
	    plan.ajouterIntersection(10, 418, 974);
	    plan.ajouterTroncon("t_1_2", 5, 1, 1, 2);
	    plan.ajouterTroncon("t_2_1", 5, 1, 2, 1);
	    plan.ajouterTroncon("t_2_4", 25, 1, 2, 4);
	    plan.ajouterTroncon("t_4_2", 25, 1, 4, 2);
	    plan.ajouterTroncon("t_4_5", 3, 1, 4, 5);
	    plan.ajouterTroncon("t_5_4", 3, 1, 5, 4);
	    plan.ajouterTroncon("t_4_3", 8, 1, 4, 3);
	    plan.ajouterTroncon("t_3_4", 8, 1, 3, 4);
	    plan.ajouterTroncon("t_3_5", 1, 1, 3, 5);
	    plan.ajouterTroncon("t_5_3", 1, 1, 5, 3);
	    plan.ajouterTroncon("t_3_6", 6, 1, 3, 6);
	    plan.ajouterTroncon("t_6_3", 6, 1, 6, 3);
	    plan.ajouterTroncon("t_5_6", 10, 1, 5, 6);
	    plan.ajouterTroncon("t_6_5", 10, 1, 6, 5);
	    plan.ajouterTroncon("t_6_10", 11, 1, 6, 10);
	    plan.ajouterTroncon("t_10_6", 11, 1, 10, 6);
	    plan.ajouterTroncon("t_10_1", 6, 1, 10, 1);
	    plan.ajouterTroncon("t_1_10", 6, 1, 1, 10);
	    plan.ajouterTroncon("t_2_10", 1, 1, 2, 10);
	    plan.ajouterTroncon("t_10_2", 1, 1, 10, 2);
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

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
	List<Troncon> list1_1 = new ArrayList<>();
	Itineraire iti1_1 = new Itineraire(i1, i1, list1_1);
	trajetsUnitComp[0] = iti1_1;
	List<Troncon> list1_2 = new ArrayList<>();
	list1_2.add(t_1_2);
	Itineraire iti1_2 = new Itineraire(i1, i2, list1_2);
	trajetsUnitComp[1] = iti1_2;
	List<Troncon> list1_3 = new ArrayList<>();
	list1_3.add(t_1_10);
	list1_3.add(t_10_6);
	list1_3.add(t_6_3);
	Itineraire iti1_3 = new Itineraire(i1, i3, list1_3);
	trajetsUnitComp[2] = iti1_3;
	List<Troncon> list1_4 = new ArrayList<>();
	list1_4.add(t_1_10);
	list1_4.add(t_10_6);
	list1_4.add(t_6_3);
	list1_4.add(t_3_5);
	list1_4.add(t_5_4);
	Itineraire iti1_4 = new Itineraire(i1, i4, list1_4);
	trajetsUnitComp[3] = iti1_4;
	List<Troncon> list1_10 = new ArrayList<>();
	list1_10.add(t_1_10);
	Itineraire iti1_10 = new Itineraire(i1, i10, list1_10);
	trajetsUnitComp[4] = iti1_10;

	for (int i = 0; i < 5; i++) {
	    assertEquals(cout[i], coutComp[i]);
	    assertEquals(trajetsUnit[i].getDepart().getId(), trajetsUnitComp[i].getDepart().getId());
	    assertEquals(trajetsUnit[i].getArrivee().getId(), trajetsUnitComp[i].getArrivee().getId());
	}

	ArrayList<Integer> nouvListeSommets = new ArrayList<>();
	nouvListeSommets.add(1);
	nouvListeSommets.add(3);
	nouvListeSommets.add(10);

	Object[] resultDijkstraComplet = algoDijkstra.calculerDijkstra(nouvListeSommets);
	int[][] coutComplet = (int[][]) resultDijkstraComplet[0];
	Itineraire[][] trajetsUnitComplet = (Itineraire[][]) resultDijkstraComplet[1];
	int[][] coutCompComplet = new int[3][3];
	Itineraire[][] trajetsUnitCompComplet = new Itineraire[3][3];
	coutCompComplet[0][0] = 0;
	coutCompComplet[0][1] = 23;
	coutCompComplet[0][2] = 6;
	coutCompComplet[1][0] = 23;
	coutCompComplet[1][1] = 0;
	coutCompComplet[1][2] = 17;
	coutCompComplet[2][0] = 6;
	coutCompComplet[2][1] = 17;
	coutCompComplet[2][2] = 0;

	trajetsUnitCompComplet[0][0] = iti1_1;
	trajetsUnitCompComplet[0][1] = iti1_3;
	trajetsUnitCompComplet[0][2] = iti1_10;
	List<Troncon> list3_1 = new ArrayList<>();
	list3_1.add(t_3_6);
	list3_1.add(t_6_10);
	list3_1.add(t_10_1);
	Itineraire iti3_1 = new Itineraire(i3, i1, list3_1);
	trajetsUnitCompComplet[1][0] = iti3_1;
	List<Troncon> list3_3 = new ArrayList<>();
	Itineraire iti3_3 = new Itineraire(i3, i3, list3_3);
	trajetsUnitCompComplet[1][1] = iti3_3;
	List<Troncon> list3_10 = new ArrayList<>();
	list3_10.add(t_3_6);
	list3_10.add(t_6_10);
	Itineraire iti3_10 = new Itineraire(i3, i10, list3_10);
	trajetsUnitCompComplet[1][2] = iti3_10;
	List<Troncon> list10_1 = new ArrayList<>();
	list10_1.add(t_10_1);
	Itineraire iti10_1 = new Itineraire(i10, i1, list10_1);
	trajetsUnitCompComplet[2][0] = iti10_1;
	List<Troncon> list10_3 = new ArrayList<>();
	list10_3.add(t_10_6);
	list10_3.add(t_6_3);
	Itineraire iti10_3 = new Itineraire(i10, i3, list10_3);
	trajetsUnitCompComplet[2][1] = iti10_3;
	List<Troncon> list10_10 = new ArrayList<>();
	Itineraire iti10_10 = new Itineraire(i10, i10, list10_10);
	trajetsUnitCompComplet[2][2] = iti10_10;

	for (int i = 0; i < 3; i++) {
	    for (int j = 0; j < 3; j++) {
		assertEquals(coutComplet[i][j], coutCompComplet[i][j]);
		assertEquals(trajetsUnitComplet[i][j].getDepart().getId(),
			trajetsUnitCompComplet[i][j].getDepart().getId());
		assertEquals(trajetsUnitComplet[i][j].getArrivee().getId(),
			trajetsUnitCompComplet[i][j].getArrivee().getId());
	    }
	}
	/*
	 * Object[] objectResult = plan.methodeTest2(listeSommets); int[][]
	 * couts = (int[][]) objectResult[0]; Itineraire[][] itineraires =
	 * (Itineraire[][]) objectResult[1]; for (int i = 0; i <
	 * couts[0].length; i++) { for (int j = 0; j < couts[0].length; j++) {
	 * System.out.println(listeSommets.get(i) + " - " + listeSommets.get(j)
	 * + " : " + couts[i][j]); } } for (int i = 0; i <
	 * itineraires[0].length; i++) { for (int j = 0; j <
	 * itineraires[0].length; j++) { System.out.println(
	 * "----------------------------------------------------");
	 * System.out.println(itineraires[i][j].getDepart().getId() + "-" +
	 * itineraires[i][j].getArrivee().getId()); for (Troncon t :
	 * itineraires[i][j].getTroncons()) {
	 * System.out.println(t.getOrigine().getId() + " - " +
	 * t.getDestination().getId()); } } }
	 * 
	 * Object[] resultTournee = plan.methodeTest3(100);
	 * System.out.println("Cout : " + (int) resultTournee[0]); for
	 * (Itineraire itineraire : (ArrayList<Itineraire>) resultTournee[1]) {
	 * System.out.println(itineraire.getDepart().getId() + "-" +
	 * itineraire.getArrivee().getId()); for (Troncon t :
	 * itineraire.getTroncons()) { System.out.println(t.getOrigine().getId()
	 * + " - " + t.getDestination().getId()); } }
	 */
    }

    @Test
    public void testCalculerDijkstraLivraisonNonAtteignable() {
	// Plan avec l'intersection 10 qui n'est pas atteignable à partir des
	// autres interserctions
	Plan plan = new Plan();
	// Creation et Ajout des intersections au plan
	AlgoDijkstra algoDijkstra = AlgoDijkstra.getInstance();
	Intersection i1 = new Intersection(1, 412, 574);
	Intersection i2 = new Intersection(2, 217, 574);
	Intersection i3 = new Intersection(3, 325, 574);
	Intersection i4 = new Intersection(4, 412, 544);
	Intersection i5 = new Intersection(5, 742, 574);
	Intersection i6 = new Intersection(6, 418, 974);
	Intersection i10 = new Intersection(10, 418, 974);

	// Creation et Ajout des Troncons au plan
	Troncon t_1_2 = new Troncon("t_1_2", i1, i2, 5, 1);
	Troncon t_2_1 = new Troncon("t_2_1", i2, i1, 5, 1);
	Troncon t_2_4 = new Troncon("t_2_4", i2, i4, 25, 1);
	Troncon t_4_2 = new Troncon("t_4_2", i4, i2, 25, 1);
	Troncon t_4_5 = new Troncon("t_4_5", i4, i5, 3, 1);
	Troncon t_5_4 = new Troncon("t_5_4", i5, i4, 3, 1);
	Troncon t_4_3 = new Troncon("t_4_3", i4, i3, 8, 1);
	Troncon t_3_4 = new Troncon("t_3_4", i3, i4, 8, 1);
	Troncon t_3_5 = new Troncon("t_3_5", i3, i5, 1, 1);
	Troncon t_5_3 = new Troncon("t_5_3", i5, i3, 1, 1);
	Troncon t_3_6 = new Troncon("t_3_6", i3, i6, 6, 1);
	Troncon t_6_3 = new Troncon("t_6_3", i6, i3, 6, 1);
	Troncon t_5_6 = new Troncon("t_5_6", i5, i6, 10, 1);
	Troncon t_6_5 = new Troncon("t_6_5", i6, i5, 10, 1);

	try {
	    plan.ajouterIntersection(1, 412, 574);
	    plan.ajouterIntersection(2, 217, 574);
	    plan.ajouterIntersection(3, 325, 574);
	    plan.ajouterIntersection(4, 412, 544);
	    plan.ajouterIntersection(5, 742, 574);
	    plan.ajouterIntersection(6, 451, 174);
	    plan.ajouterIntersection(10, 418, 974);
	    plan.ajouterTroncon("t_1_2", 5, 1, 1, 2);
	    plan.ajouterTroncon("t_2_1", 5, 1, 2, 1);
	    plan.ajouterTroncon("t_2_4", 25, 1, 2, 4);
	    plan.ajouterTroncon("t_4_2", 25, 1, 4, 2);
	    plan.ajouterTroncon("t_4_5", 3, 1, 4, 5);
	    plan.ajouterTroncon("t_5_4", 3, 1, 5, 4);
	    plan.ajouterTroncon("t_4_3", 8, 1, 4, 3);
	    plan.ajouterTroncon("t_3_4", 8, 1, 3, 4);
	    plan.ajouterTroncon("t_3_5", 1, 1, 3, 5);
	    plan.ajouterTroncon("t_5_3", 1, 1, 5, 3);
	    plan.ajouterTroncon("t_3_6", 6, 1, 3, 6);
	    plan.ajouterTroncon("t_6_3", 6, 1, 6, 3);
	    plan.ajouterTroncon("t_5_6", 10, 1, 5, 6);
	    plan.ajouterTroncon("t_6_5", 10, 1, 6, 5);
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// chargement des informations dans algoDijkstra
	algoDijkstra.chargerAlgo(plan.getListeIntersections(), plan.getListeTronconsTriee());
	ArrayList<Integer> listeSommets = new ArrayList<>();
	listeSommets.add(1);
	listeSommets.add(2);
	listeSommets.add(3);
	listeSommets.add(4);
	listeSommets.add(10);
	// Dijkstra à partir de i1 vers tous les autres intersections
	// verifier que le cout vers i10 est infini et qu'il n'y a pas
	// d'itinéraire vers i10
	Object[] resultDijkstra = algoDijkstra.calculerDijkstra(1, listeSommets);
	int[] cout = (int[]) resultDijkstra[0];
	assertTrue(cout[4] == Integer.MAX_VALUE);
	Itineraire[] trajetsUnit = (Itineraire[]) resultDijkstra[1];
	assertTrue(trajetsUnit[0].getTroncons().isEmpty());
	assertFalse(trajetsUnit[1].getTroncons().isEmpty());
	assertFalse(trajetsUnit[2].getTroncons().isEmpty());
	assertFalse(trajetsUnit[3].getTroncons().isEmpty());
	assertTrue(trajetsUnit[4].getTroncons().isEmpty());
	// Dijkstra à partir de i10 vers tous les autres intersections
	// verifier que tous les couts à partir de i10 sont infinis et qu'il n'y
	// a pas d'itinéraire possible

	resultDijkstra = algoDijkstra.calculerDijkstra(10, listeSommets);
	cout = (int[]) resultDijkstra[0];
	assertTrue(cout[0] == Integer.MAX_VALUE && cout[1] == Integer.MAX_VALUE && cout[2] == Integer.MAX_VALUE
		&& cout[3] == Integer.MAX_VALUE && cout[4] != Integer.MAX_VALUE);
	trajetsUnit = (Itineraire[]) resultDijkstra[1];
	assertTrue(trajetsUnit[0].getTroncons().isEmpty());
	assertTrue(trajetsUnit[1].getTroncons().isEmpty());
	assertTrue(trajetsUnit[2].getTroncons().isEmpty());
	assertTrue(trajetsUnit[3].getTroncons().isEmpty());
	assertTrue(trajetsUnit[4].getTroncons().isEmpty());

    }

    @Test
    public void testCalculerDijkstraPlanVide() {
	Plan plan = new Plan();
	// Creation et Ajout des intersections au plan
	AlgoDijkstra algoDijkstra = AlgoDijkstra.getInstance();
	ArrayList<Integer> listeSommets = new ArrayList<>();
	Object[] resultDijkstra = algoDijkstra.calculerDijkstra(listeSommets);
	int[][] coutComplet = (int[][]) resultDijkstra[0];
	Itineraire[][] trajetsUnitComplet = (Itineraire[][]) resultDijkstra[1];
	assertEquals(coutComplet.length, 0);
	assertEquals(trajetsUnitComplet.length, 0);
    }

    @Test
    public void testCalculerDijkstraLivraisonInexistante() {
	Plan plan = new Plan();
	// Creation et Ajout des intersections au plan
	AlgoDijkstra algoDijkstra = AlgoDijkstra.getInstance();
	ArrayList<Integer> listeSommets = new ArrayList<>();
	Object[] resultDijkstra = algoDijkstra.calculerDijkstra(1, listeSommets);
	int[] cout = (int[]) resultDijkstra[0];
	Itineraire[] trajetsUnit = (Itineraire[]) resultDijkstra[1];
	assertEquals(cout.length, 0);
	assertEquals(trajetsUnit.length, 0);
    }

    /*
     * Livraison livraison_addresse_2 = p.getListeLivraisons().get(2); Livraison
     * livraison_addresse_3 = p.getListeLivraisons().get(3); List<Troncon>
     * troncons_e_2 = new ArrayList<Troncon>();
     * troncons_e_2.add(p.getListeTroncons().get(0).get(0));
     * troncons_e_2.add(p.getListeTroncons().get(1).get(1)); Itineraire
     * i_e_vers_2 = new
     * Itineraire(p.getEntrepot(),livraison_addresse_2.getAdresse(),troncons_e_2
     * ); List<Troncon> troncons_2_3 = new ArrayList<Troncon>();
     * troncons_2_3.add(p.getListeTroncons().get(2).get(1));
     * troncons_2_3.add(p.getListeTroncons().get(5).get(2)); Itineraire
     * i_2_vers_3 = new
     * Itineraire(livraison_addresse_2.getAdresse(),livraison_addresse_3.
     * getAdresse(),troncons_2_3); List<Troncon> troncons_3_e = new
     * ArrayList<Troncon>();
     * troncons_3_e.add(p.getListeTroncons().get(3).get(0));
     * troncons_3_e.add(p.getListeTroncons().get(5).get(0)); Itineraire
     * i_3_vers_e = new
     * Itineraire(livraison_addresse_3.getAdresse(),p.getEntrepot(),troncons_3_e
     * ); boolean calculReussi = p.calculerTournee(60000); assert(calculReussi);
     * assertTrue(p.getItineraires().get(0).equals(i_e_vers_2));
     * assertTrue(p.getItineraires().get(1).equals(i_2_vers_3));
     * assertTrue(p.getItineraires().get(2).equals(i_3_vers_e));
     * fail("Not yet implemented");
     */
}
