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

    @Before
    public void setUp() {
	updateAppele = false;
	observer = new Observer() {
	    public void update(Observable o, Object arg) {
		updateAppele = true;
	    }
	};
    }

    @Test
    public void testAjouterIntersection() {
	Plan plan = new Plan();
	plan.addObserver(observer);
	try {
	    plan.ajouterIntersection(4, 245, 241);
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	assert (updateAppele);
    }

    @Test
    public void testAjouterTroncon() {
	Plan plan = new Plan();
	plan.addObserver(observer);
	try {
	    plan.ajouterIntersection(1, 245, 241);
	    plan.ajouterIntersection(2, 245, 241);
	    plan.ajouterTroncon("Rue du Chata�gnier", 500, 50, 1, 2);
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	assert (updateAppele);
    }

    @Test
    public void testDemandeDeLivraison() {
	Plan plan = new Plan();
	plan.addObserver(observer);
	try {
	    plan.ajouterIntersection(4, 245, 241);
	    plan.ajouterIntersection(3, 142, 784);
	    plan.ajouterTroncon("Rue du Chataignier", 500, 50, 3, 4);
	    plan.ajouterTroncon("Rue des Paril", 500, 50, 4, 3);
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Heure heureDepart = new Heure("08:00:00");
	int[] adresses = { 4 };
	int[] durees = { 50 };
	try {
	    plan.creerDemandeDeLivraison(heureDepart, 3);
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	for (int i = 0; i < adresses.length; i++) {
	    try {
		plan.ajouterLivraisonDemande(adresses[i], durees[i]);
	    } catch (ModeleException e) {
		e.printStackTrace();
	    }
	}
	assert (updateAppele);
    }

    @Test
    /*
     * Graphe compose de 5 livraisons dont la tournee doit etre calculee
     * correctement
     */
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
	    p.ajouterLivraisonDemande(1, 20);
	    p.ajouterLivraisonDemande(2, 10);
	    p.ajouterLivraisonDemande(5, 8);
	    p.ajouterLivraisonDemande(6, 10);
	    p.ajouterLivraisonDemande(7, 14);
	} catch (ModeleException e1) {
	    e1.printStackTrace();
	}
	boolean tourneeTrouvee = false;
	try {
	    tourneeTrouvee = p.calculerTournee();
	} catch (ExceptionTournee e) {
	    // TODO Auto-generated catch block
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

    @Ignore("Ce test est ignoré car situationnel")
    @Test
    /*
     * On teste l'arrêt de la recherche d'un plan avant que la solution optimale
     * ait été trouvée. Pour cela, un fichier XML nécessitant un grand temps de
     * calcul doit être utilisé.
     */
    public void testCalculerTourneeValideArretImmediat() {
	Plan p = new Plan();
	try {
	    deserialiseur.chargerPlan(p);
	    deserialiseur.chargerLivraisons(p);
	} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
	    // TODO Auto-generated catch block
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
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }
	});

	executorCalculTournee.shutdown();
	try {
	    executorCalculTournee.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
	    tourneeTrouvee = futureCalculTournee.get();
	} catch (InterruptedException | ExecutionException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	assert (tourneeTrouvee == false || tourneeTrouvee == true);
    }

    @Test(expected = ExceptionTournee.class)
    /*
     * Calcul de tournée avec un plan vide de toute intersection, troncon ou
     * demande de livraison.
     */
    public void testCalculerTourneeVide() throws ExceptionTournee {
	Plan p = new Plan();
	initialisationPlan(p);
	boolean calculReussi = false;
	calculReussi = p.calculerTournee();
    }

    @Test
    /*
     * Graphe compose de 5 livraisons dont la tournee possede un cout de 0,
     * n'etant composee que de l'entrepot.
     */
    public void testCalculerTourneeEntrepotSlmt() {
	Plan p = new Plan();
	initialisationPlan(p);
	Heure heure = new Heure("21:05:00");
	try {
	    p.creerDemandeDeLivraison(heure, 4);
	} catch (ModeleException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	boolean calculReussi = false;
	try {
	    calculReussi = p.calculerTournee();
	} catch (ExceptionTournee e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	int dureeTotale = p.getDureeTournee();
	System.out.println(dureeTotale);
	assert (calculReussi == true);
	assert (dureeTotale == 0);
    }

    @Test
    /*
     * Graphe compose de 5 livraisons dont le calcul de tournee ne doit pas
     * s'operer correctement, les plages ne pouvant etre respectees
     */
    public void testCalculerTourneePlageNonValide() {
	Plan p = new Plan();
	initialisationPlan(p);
	Heure heure = new Heure("08:05:00");
	try {
	    p.creerDemandeDeLivraison(heure, 4);
	    p.ajouterLivraisonDemande(1, 20, "08:06:00", "08:07:00");
	    p.ajouterLivraisonDemande(2, 10, "08:06:00", "08:07:00");
	    p.ajouterLivraisonDemande(5, 8, "08:06:00", "08:07:00");
	    p.ajouterLivraisonDemande(6, 10, "08:06:00", "08:07:00");
	    p.ajouterLivraisonDemande(7, 14, "08:06:00", "08:07:00");
	    p.ajouterLivraisonDemande(3, 20, "08:06:00", "08:07:00");
	    p.ajouterLivraisonDemande(8, 8, "08:06:00", "08:06:30");
	} catch (ModeleException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	boolean tourneeTrouvee = false;

	try {
	    tourneeTrouvee = p.calculerTournee();
	} catch (ExceptionTournee e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	assert (tourneeTrouvee == false);
    }

    @Test(expected = ExceptionTournee.class)
    /*
     * Graphe compose de 5 livraisons dont le calcul de tournee ne doit pas
     * s'operer correctement, une livraison etant inatteignable
     */
    public void testCalculerTourneeLivraisonInatteignable() throws ExceptionTournee {
	Plan p = new Plan();
	try {
	    p.ajouterIntersection(1, 412, 574);
	    p.ajouterIntersection(2, 217, 574);
	    p.ajouterIntersection(3, 325, 574);
	    p.ajouterIntersection(4, 412, 544);
	    p.ajouterIntersection(5, 742, 574);
	    p.ajouterIntersection(6, 451, 174);
	    p.ajouterIntersection(7, 418, 974);
	    p.ajouterIntersection(8, 442, 484);
	    p.ajouterIntersection(9, 412, 574);
	    p.ajouterIntersection(10, 217, 574);
	    p.ajouterIntersection(11, 325, 574);
	    p.ajouterIntersection(12, 412, 544);
	    p.ajouterIntersection(13, 742, 574);
	    p.ajouterIntersection(14, 451, 174);
	    p.ajouterIntersection(15, 418, 974);
	    p.ajouterIntersection(16, 442, 484);
	    p.ajouterTroncon("h0", 75, 25, 1, 2);
	    p.ajouterTroncon("h1", 50, 25, 2, 3);
	    p.ajouterTroncon("h3", 100, 25, 4, 1);
	    p.ajouterTroncon("h4", 150, 25, 1, 5);
	    p.ajouterTroncon("h5", 25, 25, 5, 6);
	    p.ajouterTroncon("h6", 200, 25, 6, 7);
	    p.ajouterTroncon("h7", 25, 25, 6, 7);
	    p.ajouterTroncon("h8", 50, 25, 7, 2);
	    p.ajouterTroncon("h11", 50, 25, 2, 1);
	    p.ajouterTroncon("h10", 50, 25, 1, 4);
	    p.ajouterTroncon("h12", 50, 25, 3, 2);
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	Heure heure = new Heure("08:05:00");
	try {
	    p.creerDemandeDeLivraison(heure, 4);
	    p.ajouterLivraisonDemande(1, 20, "08:06:00", "08:08:00");
	    p.ajouterLivraisonDemande(2, 10, "08:06:00", "08:08:00");
	    p.ajouterLivraisonDemande(5, 8, "08:06:00", "08:08:00");
	    p.ajouterLivraisonDemande(6, 10, "08:06:00", "08:08:00");
	    p.ajouterLivraisonDemande(7, 14, "08:06:00", "08:08:00");
	    p.ajouterLivraisonDemande(3, 20, "08:06:00", "08:08:00");
	    p.ajouterLivraisonDemande(8, 8, "08:06:00", "08:06:30");
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	boolean tourneeTrouvee = false;

	tourneeTrouvee = p.calculerTournee();

	assert (tourneeTrouvee == false);
    }

    @Test
    /*
     * Graphe compose de 5 livraisons dont le calcul de tournee doit s'operer
     * correctement, avec la presence de plages horaires
     */
    public void testCalculerTourneeValideAvPlages() {
	Plan p = new Plan();
	initialisationPlan(p);
	Heure heure = new Heure("08:05:00");
	try {
	    p.creerDemandeDeLivraison(heure, 4);
	    p.ajouterLivraisonDemande(1, 20, "08:06:00", "08:08:15");
	    p.ajouterLivraisonDemande(2, 10, "08:06:00", "08:06:40");
	    p.ajouterLivraisonDemande(5, 8, "08:15:00", "08:16:00");
	    p.ajouterLivraisonDemande(6, 10, "08:30:00", "08:38:00");
	    p.ajouterLivraisonDemande(7, 14, "08:30:00", "08:30:15");
	    p.ajouterLivraisonDemande(3, 20, "09:06:00", "09:07:00");
	    p.ajouterLivraisonDemande(8, 8, "08:06:00", "08:06:15");
	} catch (ModeleException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	boolean tourneeTrouvee = false;

	try {
	    tourneeTrouvee = p.calculerTournee();
	} catch (ExceptionTournee e) {
	    // TODO Auto-generated catch block
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
		if (p.getLivraison(i.getArrivee().getId()).possedePlage()) {
		    assertTrue(p.getLivraison(i.getArrivee().getId()).getRespectePlage());
		}
	    }
	    position++;
	    System.out.println(i.getDepart().getId());
	}
	assertTrue(dureeTotale == 3686);
    }

    /**
     * Initialisation d'un plan par l'ajout d'intersections et de troncons
     * 
     * @param p
     *            Plan a initialiser
     */
    private void initialisationPlan(Plan p) {
	try {
	    p.ajouterIntersection(1, 412, 574);
	    p.ajouterIntersection(2, 217, 574);
	    p.ajouterIntersection(3, 325, 574);
	    p.ajouterIntersection(4, 412, 544);
	    p.ajouterIntersection(5, 742, 574);
	    p.ajouterIntersection(6, 451, 174);
	    p.ajouterIntersection(7, 418, 974);
	    p.ajouterIntersection(8, 442, 484);
	    p.ajouterIntersection(9, 412, 574);
	    p.ajouterIntersection(10, 217, 574);
	    p.ajouterIntersection(11, 325, 574);
	    p.ajouterIntersection(12, 412, 544);
	    p.ajouterIntersection(13, 742, 574);
	    p.ajouterIntersection(14, 451, 174);
	    p.ajouterIntersection(15, 418, 974);
	    p.ajouterIntersection(16, 442, 484);
	    p.ajouterTroncon("h0", 75, 25, 1, 2);
	    p.ajouterTroncon("h1", 50, 25, 2, 3);
	    p.ajouterTroncon("h12", 50, 25, 3, 2);
	    p.ajouterTroncon("h2", 25, 25, 8, 3);
	    p.ajouterTroncon("h9", 25, 25, 3, 8);
	    p.ajouterTroncon("h3", 100, 25, 4, 1);
	    p.ajouterTroncon("h4", 150, 25, 1, 5);
	    p.ajouterTroncon("h5", 25, 25, 5, 6);
	    p.ajouterTroncon("h6", 200, 25, 6, 7);
	    p.ajouterTroncon("h7", 25, 25, 6, 7);
	    p.ajouterTroncon("h8", 50, 25, 7, 2);
	    p.ajouterTroncon("h11", 50, 25, 2, 1);
	    p.ajouterTroncon("h10", 50, 25, 1, 4);
	} catch (ModeleException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}

/*
 * Intersection i1 = new Intersection(1, 412, 574); Intersection i2 = new
 * Intersection(2, 217, 574); Intersection i3 = new Intersection(3, 325, 574);
 * Intersection i4 = new Intersection(4, 412, 544); Intersection i5 = new
 * Intersection(5, 742, 574); Intersection i6 = new Intersection(6, 418, 974);
 * Intersection i10 = new Intersection(10, 418, 974); Troncon t_1_2 = new
 * Troncon("t_1_2", i1, i2, 5, 1); Troncon t_2_1 = new Troncon("t_2_1", i2, i1,
 * 5, 1); Troncon t_2_4 = new Troncon("t_2_4", i2, i4, 25, 1); Troncon t_4_2 =
 * new Troncon("t_4_2", i4, i2, 25, 1); Troncon t_4_5 = new Troncon("t_4_5", i4,
 * i5, 3, 1); Troncon t_5_4 = new Troncon("t_5_4", i5, i4, 3, 1); Troncon t_4_3
 * = new Troncon("t_4_3", i4, i3, 8, 1); Troncon t_3_4 = new Troncon("t_3_4",
 * i3, i4, 8, 1); Troncon t_3_5 = new Troncon("t_3_5", i3, i5, 1, 1); Troncon
 * t_5_3 = new Troncon("t_5_3", i5, i3, 1, 1); Troncon t_3_6 = new
 * Troncon("t_3_6", i3, i6, 6, 1); Troncon t_6_3 = new Troncon("t_6_3", i6, i3,
 * 6, 1); Troncon t_5_6 = new Troncon("t_5_6", i5, i6, 10, 1); Troncon t_6_5 =
 * new Troncon("t_6_5", i6, i5, 10, 1); Troncon t_6_10 = new Troncon("t_6_10",
 * i6, i10, 11, 1); Troncon t_10_6 = new Troncon("t_10_6", i10, i6, 11, 1);
 * Troncon t_10_1 = new Troncon("t_10_1", i10, i1, 6, 1); Troncon t_1_10 = new
 * Troncon("t_1_10", i1, i10, 6, 1); Troncon t_2_10 = new Troncon("t_2_10", i2,
 * i10, 1, 1); Troncon t_10_2 = new Troncon("t_10_2", i10, i2, 1, 1);
 * 
 * coutCompComplet = new int[4][4]; trajetsUnitCompComplet = new
 * Itineraire[4][4]; coutCompComplet[0][0] = 0; coutCompComplet[0][1] = 23;
 * coutCompComplet[0][2] = 6; coutCompComplet[0][3] = 17; coutCompComplet[1][0]
 * = 23; coutCompComplet[1][1] = 0; coutCompComplet[1][2] = 17;
 * coutCompComplet[1][3] = 6; coutCompComplet[2][0] = 6; coutCompComplet[2][1] =
 * 17; coutCompComplet[2][2] = 0; coutCompComplet[2][3] = 11;
 * coutCompComplet[3][0] = 17; coutCompComplet[3][1] = 6; coutCompComplet[3][2]
 * = 11; coutCompComplet[3][3] = 0;
 * 
 * List<Troncon> list1_1 = new ArrayList<>(); Itineraire iti1_1 = new
 * Itineraire(i1, i1, list1_1); List<Troncon> list1_3 = new ArrayList<>();
 * list1_3.add(t_1_10); list1_3.add(t_10_6); list1_3.add(t_6_3); Itineraire
 * iti1_3 = new Itineraire(i1, i3, list1_3); List<Troncon> list1_10 = new
 * ArrayList<>(); list1_10.add(t_1_10); Itineraire iti1_10 = new Itineraire(i1,
 * i10, list1_10); List<Troncon> list1_6 = new ArrayList<>();
 * list1_10.add(t_1_10); list1_10.add(t_10_6); Itineraire iti1_6 = new
 * Itineraire(i1, i6, list1_6); trajetsUnitCompComplet[0][0] = iti1_1;
 * trajetsUnitCompComplet[0][1] = iti1_3; trajetsUnitCompComplet[0][2] =
 * iti1_10; trajetsUnitCompComplet[0][3] = iti1_6;
 * 
 * List<Troncon> list3_1 = new ArrayList<>(); list3_1.add(t_3_6);
 * list3_1.add(t_6_10); list3_1.add(t_10_1); Itineraire iti3_1 = new
 * Itineraire(i3, i1, list3_1); trajetsUnitCompComplet[1][0] = iti3_1;
 * List<Troncon> list3_3 = new ArrayList<>(); Itineraire iti3_3 = new
 * Itineraire(i3, i3, list3_3); trajetsUnitCompComplet[1][1] = iti3_3;
 * List<Troncon> list3_10 = new ArrayList<>(); list3_10.add(t_3_6);
 * list3_10.add(t_6_10); Itineraire iti3_10 = new Itineraire(i3, i10, list3_10);
 * trajetsUnitCompComplet[1][2] = iti3_10; List<Troncon> list3_6 = new
 * ArrayList<>(); list3_6.add(t_3_6); Itineraire iti3_6 = new Itineraire(i3, i6,
 * list3_6); trajetsUnitCompComplet[1][3] = iti3_6;
 * 
 * List<Troncon> list10_1 = new ArrayList<>(); list10_1.add(t_10_1); Itineraire
 * iti10_1 = new Itineraire(i10, i1, list10_1); trajetsUnitCompComplet[2][0] =
 * iti10_1; List<Troncon> list10_3 = new ArrayList<>(); list10_3.add(t_10_6);
 * list10_3.add(t_6_3); Itineraire iti10_3 = new Itineraire(i10, i3, list10_3);
 * trajetsUnitCompComplet[2][1] = iti10_3; List<Troncon> list10_10 = new
 * ArrayList<>(); Itineraire iti10_10 = new Itineraire(i10, i10, list10_10);
 * trajetsUnitCompComplet[2][2] = iti10_10; List<Troncon> list10_6 = new
 * ArrayList<>(); list10_6.add(t_10_6); Itineraire iti10_6 = new Itineraire(i10,
 * i6, list10_6); trajetsUnitCompComplet[2][3] = iti10_6;
 * 
 * List<Troncon> list6_1 = new ArrayList<>(); list6_1.add(t_6_10);
 * list6_1.add(t_10_1); Itineraire iti6_1 = new Itineraire(i10, i3, list6_1);
 * trajetsUnitCompComplet[3][0] = iti10_3; List<Troncon> list6_3 = new
 * ArrayList<>(); list6_3.add(t_6_3); Itineraire iti6_3 = new Itineraire(i10,
 * i10, list10_10); trajetsUnitCompComplet[3][1] = iti6_3; List<Troncon>
 * list6_10 = new ArrayList<>(); list6_10.add(t_6_10); Itineraire iti6_10 = new
 * Itineraire(i10, i6, list6_10); trajetsUnitCompComplet[3][2] = iti10_6;
 * List<Troncon> list6_6 = new ArrayList<>(); Itineraire iti6_6 = new
 * Itineraire(i10, i6, list6_6); trajetsUnitCompComplet[3][3] = iti6_6;
 * 
 * duree = new int[4]; duree[0] = 0; duree[1] = 5; duree[2] = 10; duree[3] = 5;
 * 
 * horaireDebut = new int[4];
 * 
 * horaireDebut[0] = 0; Heure heureDebut = new Heure("8:25:00"); horaireDebut[1]
 * = heureDebut.toSeconds(); heureDebut = new Heure("9:25:00"); horaireDebut[2]
 * = heureDebut.toSeconds(); heureDebut = new Heure("8:25:00"); horaireDebut[3]
 * = heureDebut.toSeconds();
 * 
 * horaireFin = new int[4]; horaireFin[0] = Integer.MAX_VALUE; Heure heureFin =
 * new Heure("8:25:00"); horaireFin[1] = heureFin.toSeconds(); heureFin = new
 * Heure("10:20:00"); horaireFin[2] = heureFin.toSeconds(); heureFin = new
 * Heure("10:25:00"); horaireFin[3] = heureFin.toSeconds();
 * 
 * Heure heureDepartH = new Heure("07:25:00"); int heureDepart =
 * heureDepartH.toSeconds(); tsp.chercheSolution(4, coutCompComplet, duree,
 * horaireDebut, horaireFin, heureDepart); for(int i = 0; i < 4; i++) {
 * System.out.println(tsp.getMeilleureSolution(i)); }
 * assertEquals(Integer.MAX_VALUE, tsp.getCoutMeilleureSolution()); }
 */
