package controleur;

import java.awt.Point;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.ExceptionTournee;
import modele.Intersection;
import modele.Livraison;
import modele.ObjetGraphique;
import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatDemandeLivraisonCharge extends EtatDefaut {
    // Etat après que l'utilisateur ait chargé une demande de livraison

    @Override
    public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes) {
	try {
	    listeDeCdes.reset();
	    plan.setTournee(null);
	    String rapport = DeserialiseurXML.chargerLivraisons(plan);
	    if (rapport.isEmpty())
		fenetre.afficherMessage("Demande de livraison chargée");
	    else
		fenetre.afficherMessage("Demande de livraison chargée avec des erreurs :\n" + rapport);
	    controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
	} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML | NumberFormatException e) {
	    fenetre.afficherMessage(e.getMessage());
	}
    }

    @Override
    public void quitter() {
	System.exit(0);
    }

    @Override
    public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance) {

	int id = -1;
	ObjetGraphique objGraph = plan.cherche(point, tolerance);
	if (objGraph instanceof Livraison) {
	    id = ((Livraison) objGraph).getAdresse().getId();
	    fenetre.setLivraisonSurvol(id);
	} else if (objGraph instanceof Intersection) {
	    id = ((Intersection) objGraph).getId();
	    fenetre.setIntersectionSurvol(id);
	}

	fenetre.setIntersectionSurvol(id);
	fenetre.setLivraisonSurvol(id);
    }

    @Override
    public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre, int tempsLimite) {
	fenetre.afficherMessage("Lancement du calcul de la tournée");
	controleur.setEtatCourant(controleur.ETAT_CALCUL_EN_COURS);
	// Operations effectuees dans l'etat calcul en cours
	try {
	    if (plan.calculerTournee()) {
		fenetre.afficherMessage("Tournée calculée");
	    } else {
		fenetre.afficherMessage(
			"Temps limite atteint, si aucune tournée n'a été trouvée, veuillez selectionner un temps limite plus long");
	    }
	} catch (ExceptionTournee e) {
	    e.printStackTrace();
	}
	controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
    }
}
