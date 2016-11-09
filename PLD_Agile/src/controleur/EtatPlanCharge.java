package controleur;

import java.awt.Point;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Intersection;
import modele.Livraison;
import modele.ObjetGraphique;
import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatPlanCharge extends EtatDefaut {
		// Etat apres que l'utilisateur ait charge un plan
	
	@Override
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes) {
	    try {
		listeDeCdes.reset();
		DeserialiseurXML.chargerLivraisons(plan);
		plan.setTournee(null);
		fenetre.afficherMessage("Demande de livraison chargée");
		fenetre.afficherDetailDemandeLivraison();
		controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
	    } catch (ParserConfigurationException 
			| SAXException | IOException 
			| ExceptionXML | NumberFormatException e) {
		fenetre.afficherMessage(e.getMessage());
	    }
	}
	
    @Override
    public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance) {
		int id =-1;
		ObjetGraphique objGraph = plan.cherche(point, tolerance);
		if (objGraph instanceof Intersection) {
		    id = ((Intersection) objGraph).getId();
		    fenetre.setIntersectionSurvol(id);
		}

		fenetre.setIntersectionSurvol(id);
    }
	
	@Override
	public void quitter() {
	    System.exit(0);
	}
	
}
