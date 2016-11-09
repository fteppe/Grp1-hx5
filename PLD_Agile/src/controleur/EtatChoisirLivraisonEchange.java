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

public class EtatChoisirLivraisonEchange extends EtatDefaut {
    // Etat lorsque l'utilisateur clique sur une livraison puis sur "Echanger"

    private int idLivraison;

    @Override
    public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes) {
	try {
	    listeDeCdes.reset();
	    String rapport = DeserialiseurXML.chargerLivraisons(plan);
	    plan.setTournee(null);
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

    private void echangerLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes,
	    int idLivraison2) {
	if (idLivraison != idLivraison2) {
	    listeDeCdes.ajoute(new CdeEchangeLivraisons(plan, idLivraison, idLivraison2));
	    fenetre.afficherMessage("Livraisons échangées");
	} else {
	    fenetre.afficherMessage("Veuillez choisir deux livraisons différentes pour les intervertir.");
	}
	controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
    }
    
    @Override
    public void annulerAction(Controleur controleur) {
    	controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
    	controleur.getFenetre().afficherMessage("action annulée");
    }
    
    @Override
    public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance) {
	int id = -1;
	ObjetGraphique objGraph = plan.cherche(point, tolerance);
	if (objGraph instanceof Livraison) {
	    id = ((Livraison) objGraph).getAdresse().getId();
	}
	fenetre.setLivraisonSurvol(id);
    }

    public void setIdLivraison(int idLivraison) {
	this.idLivraison = idLivraison;
    }

    @Override
    public void clicGaucheLivraison(Controleur controleur, Fenetre fenetre, Plan plan, int idLivraison2) {
	echangerLivraison(controleur, plan, fenetre, controleur.getListeCde(), idLivraison2);
    }

}