package modele.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import modele.Heure;
import modele.Intersection;
import modele.Itineraire;
import modele.Plan;
import modele.Troncon;
import tsp.TSPPlages;

public class TSPPlagesTest {
    Plan plan;
    TSPPlages tsp = new TSPPlages();
    int[][] coutCompComplet;
    Itineraire[][] trajetsUnitCompComplet;
    int[] duree;
    int[] horaireDebut;
    int[] horaireFin;
    
    @Before
    public void setUp(){
	
    }
    
    @Test
    public void testCalculerTourneeValideSsPlage() {
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
	
	coutCompComplet = new int[4][4];
	trajetsUnitCompComplet = new Itineraire[4][4];
	coutCompComplet[0][0] = 0;
	coutCompComplet[0][1] = 23;
	coutCompComplet[0][2] = 6;
	coutCompComplet[0][3] = 17;
	coutCompComplet[1][0] = 23;
	coutCompComplet[1][1] = 0;
	coutCompComplet[1][2] = 17;
	coutCompComplet[1][3] = 6;
	coutCompComplet[2][0] = 6;
	coutCompComplet[2][1] = 17;
	coutCompComplet[2][2] = 0;
	coutCompComplet[2][3] = 11;
	coutCompComplet[3][0] = 17;
	coutCompComplet[3][1] = 6;
	coutCompComplet[3][2] = 11;
	coutCompComplet[3][3] = 0;
	
	List<Troncon> list1_1 = new ArrayList<>();
	Itineraire iti1_1 = new Itineraire(i1, i1, list1_1);
	List<Troncon> list1_3 = new ArrayList<>();
	list1_3.add(t_1_10);
	list1_3.add(t_10_6);
	list1_3.add(t_6_3);
	Itineraire iti1_3 = new Itineraire(i1, i3, list1_3);
	List<Troncon> list1_10 = new ArrayList<>();
	list1_10.add(t_1_10);
	Itineraire iti1_10 = new Itineraire(i1, i10, list1_10);
	List<Troncon> list1_6 = new ArrayList<>();
	list1_10.add(t_1_10);
	list1_10.add(t_10_6);
	Itineraire iti1_6 = new Itineraire(i1, i6, list1_6);
	trajetsUnitCompComplet[0][0] = iti1_1;
	trajetsUnitCompComplet[0][1] = iti1_3;
	trajetsUnitCompComplet[0][2] = iti1_10;
	trajetsUnitCompComplet[0][3] = iti1_6;
	
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
	List<Troncon> list3_6 = new ArrayList<>();
	list3_6.add(t_3_6);
	Itineraire iti3_6 = new Itineraire(i3, i6, list3_6);
	trajetsUnitCompComplet[1][3] = iti3_6;
	
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
	List<Troncon> list10_6 = new ArrayList<>();
	list10_6.add(t_10_6);
	Itineraire iti10_6 = new Itineraire(i10, i6, list10_6);
	trajetsUnitCompComplet[2][3] = iti10_6;
	
	List<Troncon> list6_1 = new ArrayList<>();
	list6_1.add(t_6_10);
	list6_1.add(t_10_1);
	Itineraire iti6_1 = new Itineraire(i10, i3, list6_1);
	trajetsUnitCompComplet[3][0] = iti10_3;
	List<Troncon> list6_3 = new ArrayList<>();
	list6_3.add(t_6_3);
	Itineraire iti6_3 = new Itineraire(i10, i10, list10_10);
	trajetsUnitCompComplet[3][1] = iti6_3;
	List<Troncon> list6_10 = new ArrayList<>();
	list6_10.add(t_6_10);
	Itineraire iti6_10 = new Itineraire(i10, i6, list6_10);
	trajetsUnitCompComplet[3][2] = iti10_6;
	List<Troncon> list6_6 = new ArrayList<>();
	Itineraire iti6_6 = new Itineraire(i10, i6, list6_6);
	trajetsUnitCompComplet[3][3] = iti6_6;
	
	duree = new int[4];
	duree[0] = 0;
	duree[1] = 5;
	duree[2] = 10;
	duree[3] = 5;
	
	horaireDebut = new int[4];
	horaireDebut[0] = 0;
	horaireDebut[1] = 0;
	horaireDebut[2] = 0;
	horaireDebut[3] = 0;
	
	horaireFin = new int[4];
	horaireFin[0] = Integer.MAX_VALUE;
	horaireFin[1] = Integer.MAX_VALUE;
	horaireFin[2] = Integer.MAX_VALUE;
	horaireFin[3] = Integer.MAX_VALUE;

	Heure heureDepartH = new Heure("07:25:00");
	int heureDepart = heureDepartH.toSeconds();
	tsp.chercheSolution(4, coutCompComplet, duree, horaireDebut, horaireFin, heureDepart);
	Integer[] meilleureSolution = {1, 3, 6, 10};
	Integer[] meilleureSolution2 = {1, 6, 10, 3};
	Integer[] meilleureSolution3 = {1, 10, 6, 3};
	Integer[] meilleureSolution4 = {1, 10, 3, 6};
	Integer[] meilleureSolution5 = {1, 6, 3, 10};
	Integer[] listeSommets = {1, 3, 10, 6};
	for(int i=0; i < 4; i++) {
	    assertTrue(listeSommets[tsp.getMeilleureSolution(i)] == meilleureSolution[i]
		    || meilleureSolution2[i] == listeSommets[tsp.getMeilleureSolution(i)]
			    ||listeSommets[tsp.getMeilleureSolution(i)] == meilleureSolution3[i]
				    || meilleureSolution4[i] == listeSommets[tsp.getMeilleureSolution(i)]
					    || meilleureSolution5[i] == listeSommets[tsp.getMeilleureSolution(i)]);
	    assertEquals(66, tsp.getCoutMeilleureSolution());
	}
    }
    
    @Test
    public void testCalculerTourneeValideAvPlage() {
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
	
	coutCompComplet = new int[4][4];
	trajetsUnitCompComplet = new Itineraire[4][4];
	coutCompComplet[0][0] = 0;
	coutCompComplet[0][1] = 23;
	coutCompComplet[0][2] = 6;
	coutCompComplet[0][3] = 17;
	coutCompComplet[1][0] = 23;
	coutCompComplet[1][1] = 0;
	coutCompComplet[1][2] = 17;
	coutCompComplet[1][3] = 6;
	coutCompComplet[2][0] = 6;
	coutCompComplet[2][1] = 17;
	coutCompComplet[2][2] = 0;
	coutCompComplet[2][3] = 11;
	coutCompComplet[3][0] = 17;
	coutCompComplet[3][1] = 6;
	coutCompComplet[3][2] = 11;
	coutCompComplet[3][3] = 0;
	
	List<Troncon> list1_1 = new ArrayList<>();
	Itineraire iti1_1 = new Itineraire(i1, i1, list1_1);
	List<Troncon> list1_3 = new ArrayList<>();
	list1_3.add(t_1_10);
	list1_3.add(t_10_6);
	list1_3.add(t_6_3);
	Itineraire iti1_3 = new Itineraire(i1, i3, list1_3);
	List<Troncon> list1_10 = new ArrayList<>();
	list1_10.add(t_1_10);
	Itineraire iti1_10 = new Itineraire(i1, i10, list1_10);
	List<Troncon> list1_6 = new ArrayList<>();
	list1_10.add(t_1_10);
	list1_10.add(t_10_6);
	Itineraire iti1_6 = new Itineraire(i1, i6, list1_6);
	trajetsUnitCompComplet[0][0] = iti1_1;
	trajetsUnitCompComplet[0][1] = iti1_3;
	trajetsUnitCompComplet[0][2] = iti1_10;
	trajetsUnitCompComplet[0][3] = iti1_6;
	
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
	List<Troncon> list3_6 = new ArrayList<>();
	list3_6.add(t_3_6);
	Itineraire iti3_6 = new Itineraire(i3, i6, list3_6);
	trajetsUnitCompComplet[1][3] = iti3_6;
	
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
	List<Troncon> list10_6 = new ArrayList<>();
	list10_6.add(t_10_6);
	Itineraire iti10_6 = new Itineraire(i10, i6, list10_6);
	trajetsUnitCompComplet[2][3] = iti10_6;
	
	List<Troncon> list6_1 = new ArrayList<>();
	list6_1.add(t_6_10);
	list6_1.add(t_10_1);
	Itineraire iti6_1 = new Itineraire(i10, i3, list6_1);
	trajetsUnitCompComplet[3][0] = iti10_3;
	List<Troncon> list6_3 = new ArrayList<>();
	list6_3.add(t_6_3);
	Itineraire iti6_3 = new Itineraire(i10, i10, list10_10);
	trajetsUnitCompComplet[3][1] = iti6_3;
	List<Troncon> list6_10 = new ArrayList<>();
	list6_10.add(t_6_10);
	Itineraire iti6_10 = new Itineraire(i10, i6, list6_10);
	trajetsUnitCompComplet[3][2] = iti10_6;
	List<Troncon> list6_6 = new ArrayList<>();
	Itineraire iti6_6 = new Itineraire(i10, i6, list6_6);
	trajetsUnitCompComplet[3][3] = iti6_6;
	
	duree = new int[4];
	duree[0] = 0;
	duree[1] = 5;
	duree[2] = 10;
	duree[3] = 5;
	
	horaireDebut = new int[4];

	horaireDebut[0] = 0;
	Heure heureDebut = new Heure("8:25:00");
	horaireDebut[1] = heureDebut.toSeconds();
	heureDebut = new Heure("9:25:00");
	horaireDebut[2] = heureDebut.toSeconds();
	heureDebut = new Heure("8:25:00");
	horaireDebut[3] = heureDebut.toSeconds();
	
	horaireFin = new int[4];
	horaireFin[0] = Integer.MAX_VALUE;
	Heure heureFin = new Heure("8:35:00");
	horaireFin[1] = heureFin.toSeconds();
	heureFin = new Heure("10:20:00");
	horaireFin[2] = heureFin.toSeconds();
	heureFin = new Heure("10:25:00");
	horaireFin[3] = heureFin.toSeconds();

	Heure heureDepartH = new Heure("07:25:00");
	int heureDepart = heureDepartH.toSeconds();
	tsp.chercheSolution(4, coutCompComplet, duree, horaireDebut, horaireFin, heureDepart);
	Integer[] meilleureSolution = {1, 3, 6, 10};
	Integer[] listeSommets = {1, 3, 10, 6};
	for(int i = 0; i < 4; i++) {
	    assertTrue(listeSommets[tsp.getMeilleureSolution(i)] == meilleureSolution[i]);
	    assertEquals(120*60+16, tsp.getCoutMeilleureSolution());
	}
    }

}
