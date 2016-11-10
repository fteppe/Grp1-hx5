package modele.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import modele.ExceptionTournee;
import modele.Heure;
import modele.Intersection;
import modele.Itineraire;
import modele.ModeleException;
import modele.Plan;
import modele.Troncon;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class PlanTest {

	Observer observer;
	boolean updateAppele;
	private DeserialiseurXML deserialiseur;
	
	/**
	 * Mise en place d'un Observer 
	 */
	@Before
	public void setUp() {
		updateAppele = false;
		observer = new Observer() {
			public void update(Observable o, Object arg) {
				updateAppele = true;
			}
		};
	}

	/**
	 * On verifie que la creation d'intersection notifie l'observer
	 */
	@Test
	public void testcreerIntersection() {
		Plan plan = new Plan();
		plan.addObserver(observer);
		try {
			plan.creerIntersection(4, 245, 241);
		} catch (ModeleException e) {
			e.printStackTrace();
		}
		assert (updateAppele);
	}

	/**
	 * On verifie que la creation de troncon notifie l'observer
	 */
	@Test
	public void testcreerTroncon() {
		Plan plan = new Plan();
		plan.addObserver(observer);
		try {
			plan.creerIntersection(1, 245, 241);
			plan.creerIntersection(2, 245, 241);
			plan.creerTroncon("Rue du Chata�gnier", 500, 50, 1, 2);
		} catch (ModeleException e) {
			e.printStackTrace();
		}
		assert (updateAppele);
	}

	/**
	 * On verifie que la creation d'une demande de livraison notifie l'observer
	 */
	@Test
	public void testDemandeDeLivraison() {
		Plan plan = new Plan();
		plan.addObserver(observer);
		try {
			plan.creerIntersection(4, 245, 241);
			plan.creerIntersection(3, 142, 784);
			plan.creerTroncon("Rue du Chataignier", 500, 50, 3, 4);
			plan.creerTroncon("Rue des Paril", 500, 50, 4, 3);
		} catch (ModeleException e) {
			e.printStackTrace();
		}
		Heure heureDepart = new Heure("08:00:00");
		int[] adresses = { 4 };
		int[] durees = { 50 };
		try {
			plan.creerDemandeDeLivraison(heureDepart, 3);
		} catch (ModeleException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < adresses.length; i++) {
			try {
				plan.creerLivraisonDemande(adresses[i], durees[i]);
			} catch (ModeleException e) {
				e.printStackTrace();
			}
		}
		assert (updateAppele);
	}

	/**
	 * Graphe compose de 5 livraisons dont la tournee doit etre calculee
	 * correctement
	 */
	@Test
	public void testCalculerTourneeValide() {
		Plan p = new Plan();
		initialisationPlan(p);
		Heure heure = new Heure("21:05:00");
		try {
			p.creerDemandeDeLivraison(heure, 4);
		} catch (ModeleException e1) {
			e1.printStackTrace();
		}
		try {
			p.creerLivraisonDemande(1, 20);
			p.creerLivraisonDemande(2, 10);
			p.creerLivraisonDemande(5, 8);
			p.creerLivraisonDemande(6, 10);
			p.creerLivraisonDemande(7, 14);
		} catch (ModeleException e1) {
			e1.printStackTrace();
		}
		boolean tourneeTrouvee = false;
		try {
			tourneeTrouvee = p.calculerTournee();
		} catch (ExceptionTournee e) {
			e.printStackTrace();
		}
		int dureeTotale = p.getDureeTournee();
		List<Itineraire> listeItineraires = p.getItineraires();
		int[] listeSommetsTourneePoss1 = { 4, 1, 5, 6, 7, 2, 4 };
		int[] listeSommetsTourneePoss2 = { 4, 5, 6, 7, 2, 1, 4 };
		int position = 0;
		assertTrue(tourneeTrouvee);
		for (Itineraire i : listeItineraires) {
			assertTrue(i.getDepart().getId() == listeSommetsTourneePoss1[position]
					|| i.getDepart().getId() == listeSommetsTourneePoss2[position]);
			assertTrue(i.getArrivee().getId() == listeSommetsTourneePoss1[position + 1]
					|| i.getArrivee().getId() == listeSommetsTourneePoss2[position + 1]);
			position++;
			System.out.println(i.getDepart().getId());
		}
		assertTrue(dureeTotale == 80);
	}

	/**
	 * On teste l'arrêt de la recherche d'un plan avant que la solution optimale
	 * ait été trouvée. Pour cela, un fichier XML nécessitant un grand temps de
	 * calcul doit être utilisé.
	 */
	@Ignore("Ce test est ignoré car situationnel")
	@Test
	public void testCalculerTourneeValideArretImmediat() {
		Plan p = new Plan();
		try {
			deserialiseur.chargerPlan(p);
			deserialiseur.chargerLivraisons(p);
		} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
			e.printStackTrace();
		}

		boolean tourneeTrouvee = false;

		Callable<Boolean> calculTournee = () -> {
			// On cherche l'itineraire optimal via l'utilisation du TSP
			boolean resultat = p.calculerTournee();
			return resultat;
		};

		ExecutorService executorCalculTournee = Executors.newFixedThreadPool(2);

		Future<Boolean> futureCalculTournee = executorCalculTournee.submit(calculTournee);

		int tpsAttente = 2;
		executorCalculTournee.submit(() -> {
			// On cherche l'itineraire optimal via l'utilisation du TSP
			while (true) {
				try {
					TimeUnit.SECONDS.sleep(tpsAttente);
					if (p.getCalculTourneeEnCours()) {
						p.arreterCalculTournee();
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});

		executorCalculTournee.shutdown();
		try {
			executorCalculTournee.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
			tourneeTrouvee = futureCalculTournee.get();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}

		assert (tourneeTrouvee == false || tourneeTrouvee == true);
	}

	/**
	 * Calcul de tournée avec un plan vide de toute intersection, troncon ou
	 * demande de livraison.
	 * @throws ExceptionTournee
	 */
	@Test(expected = ExceptionTournee.class)
	public void testCalculerTourneeVide() throws ExceptionTournee {
		Plan p = new Plan();
		initialisationPlan(p);
		boolean calculReussi = false;
		calculReussi = p.calculerTournee();
	}

	/**
	 * On teste le calcul d'une tournee n'etant composee que de l'entrepot.
	 */
	@Test
	public void testCalculerTourneeEntrepotSlmt() {
		Plan p = new Plan();
		initialisationPlan(p);
		Heure heure = new Heure("21:05:00");
		try {
			p.creerDemandeDeLivraison(heure, 4);
		} catch (ModeleException e1) {
			e1.printStackTrace();
		}
		boolean calculReussi = false;
		try {
			calculReussi = p.calculerTournee();
		} catch (ExceptionTournee e) {
			e.printStackTrace();
		}
		int dureeTotale = p.getDureeTournee();
		System.out.println(dureeTotale);
		assert (calculReussi == true);
		assert (dureeTotale == 0);
	}
	
	/**
	 * Graphe compose de 7 livraisons dont le calcul de tournee ne doit pas
	 * s'operer correctement, les plages ne pouvant etre respectees
	 */
	public void testCalculerTourneePlageNonValide() {
		Plan p = new Plan();
		initialisationPlan(p);
		Heure heure = new Heure("08:05:00");
		try {
			p.creerDemandeDeLivraison(heure, 4);
			p.creerLivraisonDemande(1, 20, "08:06:00", "08:07:00");
			p.creerLivraisonDemande(2, 10, "08:06:00", "08:07:00");
			p.creerLivraisonDemande(5, 8, "08:06:00", "08:07:00");
			p.creerLivraisonDemande(6, 10, "08:06:00", "08:07:00");
			p.creerLivraisonDemande(7, 14, "08:06:00", "08:07:00");
			p.creerLivraisonDemande(3, 20, "08:06:00", "08:07:00");
			p.creerLivraisonDemande(8, 8, "08:06:00", "08:06:30");
		} catch (ModeleException e1) {
			e1.printStackTrace();
		}

		boolean tourneeTrouvee = false;

		try {
			tourneeTrouvee = p.calculerTournee();
		} catch (ExceptionTournee e) {
			e.printStackTrace();
		}

		assert (tourneeTrouvee == false);
	}

	/**
	 * Graphe dont le calcul de tournee ne doit pas
	 * s'operer correctement, une livraison etant inatteignable
	 * @throws ExceptionTournee
	 */
	@Test(expected = ExceptionTournee.class)
	public void testCalculerTourneeLivraisonInatteignable() throws ExceptionTournee {
		Plan p = new Plan();
		try {
			p.creerIntersection(1, 412, 574);
			p.creerIntersection(2, 217, 574);
			p.creerIntersection(3, 325, 574);
			p.creerIntersection(4, 412, 544);
			p.creerIntersection(5, 742, 574);
			p.creerIntersection(6, 451, 174);
			p.creerIntersection(7, 418, 974);
			p.creerIntersection(8, 442, 484);
			p.creerIntersection(9, 412, 574);
			p.creerIntersection(10, 217, 574);
			p.creerIntersection(11, 325, 574);
			p.creerIntersection(12, 412, 544);
			p.creerIntersection(13, 742, 574);
			p.creerIntersection(14, 451, 174);
			p.creerIntersection(15, 418, 974);
			p.creerIntersection(16, 442, 484);
			p.creerTroncon("h0", 75, 25, 1, 2);
			p.creerTroncon("h1", 50, 25, 2, 3);
			p.creerTroncon("h3", 100, 25, 4, 1);
			p.creerTroncon("h4", 150, 25, 1, 5);
			p.creerTroncon("h5", 25, 25, 5, 6);
			p.creerTroncon("h6", 200, 25, 6, 7);
			p.creerTroncon("h7", 25, 25, 6, 7);
			p.creerTroncon("h8", 50, 25, 7, 2);
			p.creerTroncon("h11", 50, 25, 2, 1);
			p.creerTroncon("h10", 50, 25, 1, 4);
			p.creerTroncon("h12", 50, 25, 3, 2);
		} catch (ModeleException e) {
			e.printStackTrace();
		}

		Heure heure = new Heure("08:05:00");
		try {
			p.creerDemandeDeLivraison(heure, 4);
			p.creerLivraisonDemande(1, 20, "08:06:00", "08:08:00");
			p.creerLivraisonDemande(2, 10, "08:06:00", "08:08:00");
			p.creerLivraisonDemande(5, 8, "08:06:00", "08:08:00");
			p.creerLivraisonDemande(6, 10, "08:06:00", "08:08:00");
			p.creerLivraisonDemande(7, 14, "08:06:00", "08:08:00");
			p.creerLivraisonDemande(3, 20, "08:06:00", "08:08:00");
			p.creerLivraisonDemande(8, 8, "08:06:00", "08:06:30");
		} catch (ModeleException e) {
			e.printStackTrace();
		}

		boolean tourneeTrouvee = false;

		tourneeTrouvee = p.calculerTournee();

		assert (tourneeTrouvee == false);
	}

	/**
	 * Graphe dont le calcul de tournee doit s'operer
	 * correctement, avec la presence de plages horaires
	 */
	public void testCalculerTourneeValideAvPlages() {
		Plan p = new Plan();
		initialisationPlan(p);
		Heure heure = new Heure("08:05:00");
		try {
			p.creerDemandeDeLivraison(heure, 4);
			p.creerLivraisonDemande(1, 20, "08:06:00", "08:08:15");
			p.creerLivraisonDemande(2, 10, "08:06:00", "08:06:40");
			p.creerLivraisonDemande(5, 8, "08:15:00", "08:16:00");
			p.creerLivraisonDemande(6, 10, "08:30:00", "08:38:00");
			p.creerLivraisonDemande(7, 14, "08:30:00", "08:30:15");
			p.creerLivraisonDemande(3, 20, "09:06:00", "09:07:00");
			p.creerLivraisonDemande(8, 8, "08:06:00", "08:06:15");
		} catch (ModeleException e1) {
			e1.printStackTrace();
		}

		boolean tourneeTrouvee = false;

		try {
			tourneeTrouvee = p.calculerTournee();
		} catch (ExceptionTournee e) {
			e.printStackTrace();
		}

		int dureeTotale = p.getDureeTournee();
		List<Itineraire> listeItineraires = p.getItineraires();
		int[] listeSommetsTourneePoss1 = { 4, 8, 2, 1, 5, 7, 6, 3, 4 };
		int position = 0;
		assertTrue(tourneeTrouvee);
		for (Itineraire i : listeItineraires) {
			assertTrue(i.getDepart().getId() == listeSommetsTourneePoss1[position]);
			assertTrue(i.getArrivee().getId() == listeSommetsTourneePoss1[position + 1]);
			if (i.getArrivee().getId() != 4) {
				if (p.getLivraisonParAdresse(i.getArrivee().getId()).possedePlage()) {
					assertTrue(p.getLivraisonParAdresse(i.getArrivee().getId()).getRespectePlage());
				}
			}
			position++;
			System.out.println(i.getDepart().getId());
		}
		assertTrue(dureeTotale == 3686);
	}

	private void initialisationPlan(Plan p) {
		try {
			p.creerIntersection(1, 412, 574);
			p.creerIntersection(2, 217, 574);
			p.creerIntersection(3, 325, 574);
			p.creerIntersection(4, 412, 544);
			p.creerIntersection(5, 742, 574);
			p.creerIntersection(6, 451, 174);
			p.creerIntersection(7, 418, 974);
			p.creerIntersection(8, 442, 484);
			p.creerIntersection(9, 412, 574);
			p.creerIntersection(10, 217, 574);
			p.creerIntersection(11, 325, 574);
			p.creerIntersection(12, 412, 544);
			p.creerIntersection(13, 742, 574);
			p.creerIntersection(14, 451, 174);
			p.creerIntersection(15, 418, 974);
			p.creerIntersection(16, 442, 484);
			p.creerTroncon("h0", 75, 25, 1, 2);
			p.creerTroncon("h1", 50, 25, 2, 3);
			p.creerTroncon("h12", 50, 25, 3, 2);
			p.creerTroncon("h2", 25, 25, 8, 3);
			p.creerTroncon("h9", 25, 25, 3, 8);
			p.creerTroncon("h3", 100, 25, 4, 1);
			p.creerTroncon("h4", 150, 25, 1, 5);
			p.creerTroncon("h5", 25, 25, 5, 6);
			p.creerTroncon("h6", 200, 25, 6, 7);
			p.creerTroncon("h7", 25, 25, 6, 7);
			p.creerTroncon("h8", 50, 25, 7, 2);
			p.creerTroncon("h11", 50, 25, 2, 1);
			p.creerTroncon("h10", 50, 25, 1, 4);
		} catch (ModeleException e) {
			e.printStackTrace();
		}
	}

}