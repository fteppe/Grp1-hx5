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

	/**
	 * Calcul d'une tournee valide sans plage horaire
	 */
	@Test
	public void testCalculerTourneeValideSsPlage() {
		Integer[] listeSommets = { 1, 3, 10, 6 };
		coutCompComplet = new int[4][4];
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
		Integer[] meilleureSolution = { 1, 3, 6, 10 };
		Integer[] meilleureSolution2 = { 1, 6, 10, 3 };
		Integer[] meilleureSolution3 = { 1, 10, 6, 3 };
		Integer[] meilleureSolution4 = { 1, 10, 3, 6 };
		Integer[] meilleureSolution5 = { 1, 6, 3, 10 };
		for (int i = 0; i < 4; i++) {
			assertTrue(listeSommets[tsp.getMeilleureSolution(i)] == meilleureSolution[i]
					|| meilleureSolution2[i] == listeSommets[tsp.getMeilleureSolution(i)]
					|| listeSommets[tsp.getMeilleureSolution(i)] == meilleureSolution3[i]
					|| meilleureSolution4[i] == listeSommets[tsp.getMeilleureSolution(i)]
					|| meilleureSolution5[i] == listeSommets[tsp.getMeilleureSolution(i)]);
		}
		assertEquals(66, tsp.getCoutMeilleureSolution());
	}


	/**
	 * Calcul d'une tournee valide avec plage horaire
	 */
	@Test
	public void testCalculerTourneeValideAvPlage() {
		Integer[] listeSommets = { 1, 3, 10, 6 };
		coutCompComplet = new int[4][4];
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
		Integer[] meilleureSolution = { 1, 6, 3, 10 };
		Integer[] meilleureSolution2 = { 1, 3, 6, 10 };
		for (int i = 0; i < 4; i++) {
			int sommetsMeilleureSolution = listeSommets[tsp.getMeilleureSolution(i)];
			assertTrue(sommetsMeilleureSolution == meilleureSolution[i]
					|| sommetsMeilleureSolution == meilleureSolution2[i]);

		}
		assertEquals(120 * 60 + 16, tsp.getCoutMeilleureSolution());
	}

	/**
	 * Calcul d'une tournee si l'entrepot est isole, dont le cout doit donc etre de 0
	 */
	@Test
	public void testTourneeEntrepotIsole() {
		coutCompComplet = new int[4][4];
		coutCompComplet[0][0] = 0;
		coutCompComplet[0][1] = Integer.MAX_VALUE;
		coutCompComplet[0][2] = Integer.MAX_VALUE;
		coutCompComplet[0][3] = Integer.MAX_VALUE;
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
		assertEquals(Integer.MAX_VALUE, tsp.getCoutMeilleureSolution());
	}

	/**
	 * Calcul d'une tournee si une livraison est inatteignable, ne devant donc renvoyer aucune
	 * solution
	 */
	@Test
	public void testTourneeLivraisonInatteignable() {
		coutCompComplet = new int[4][4];
		coutCompComplet[0][0] = 0;
		coutCompComplet[0][1] = Integer.MAX_VALUE;
		coutCompComplet[0][2] = 6;
		coutCompComplet[0][3] = 17;
		coutCompComplet[1][0] = Integer.MAX_VALUE;
		coutCompComplet[1][1] = Integer.MAX_VALUE;
		coutCompComplet[1][2] = Integer.MAX_VALUE;
		coutCompComplet[1][3] = Integer.MAX_VALUE;
		coutCompComplet[2][0] = 6;
		coutCompComplet[2][1] = Integer.MAX_VALUE;
		coutCompComplet[2][2] = 0;
		coutCompComplet[2][3] = 11;
		coutCompComplet[3][0] = 17;
		coutCompComplet[3][1] = Integer.MAX_VALUE;
		coutCompComplet[3][2] = 11;
		coutCompComplet[3][3] = 0;

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
		assertEquals(Integer.MAX_VALUE, tsp.getCoutMeilleureSolution());
	}

	/**
	 * Calcul d'une tournee ne devant pas s'effectuer correctement, 
	 * des plages horaires ne pouvant etre respectees (aucune solution n'est
	 * renvoyee)
	 */
	@Test
	public void testTourneePlageIncoherente() {
		coutCompComplet = new int[4][4];
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
		Heure heureFin = new Heure("8:25:00");
		horaireFin[1] = heureFin.toSeconds();
		heureFin = new Heure("10:20:00");
		horaireFin[2] = heureFin.toSeconds();
		heureFin = new Heure("10:25:00");
		horaireFin[3] = heureFin.toSeconds();

		Heure heureDepartH = new Heure("07:25:00");
		int heureDepart = heureDepartH.toSeconds();
		tsp.chercheSolution(4, coutCompComplet, duree, horaireDebut, horaireFin, heureDepart);
		assertEquals(Integer.MAX_VALUE, tsp.getCoutMeilleureSolution());
	}

	/**
	 * Calcul d'une tournee en cas de plan vide, ne devant donc pas 
	 * s'effectuer correctement (aucune solution n'est renvoyee)
	 */
	@Test
	public void testCalculerTourneePlanVide() {
		coutCompComplet = new int[4][4];
		duree = new int[4];
		horaireDebut = new int[4];
		horaireFin = new int[4];

		tsp.chercheSolution(0, coutCompComplet, duree, horaireDebut, horaireFin, 0);
		assertEquals(0, tsp.getCoutMeilleureSolution());
		assertNull(tsp.getMeilleureSolution(0));
	}

}
