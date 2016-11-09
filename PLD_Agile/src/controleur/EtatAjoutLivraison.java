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

public class EtatAjoutLivraison extends EtatDefaut {
    // Etat atteint lorsque l'utilisateur clique sur ajouter une nouvelle
    // livraison

    private int idIntersection;

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
	    fenetre.afficherDetailDemandeLivraison();
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
    public void clicAjouterLivraisonPosition(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes,
	    int idPrec, int idSuiv, int duree) {
	listeDeCdes.ajoute(new CdeAjoutLivraison(plan, idIntersection, idPrec, idSuiv, duree));
	fenetre.afficherMessage("Livraison ajoutée à la tournée");
	controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
    }

    @Override
    public void clicAjouterLivraisonPosition(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes,
	    int idPrec, int idSuiv, int duree, String debutPlage, String finPlage) {
	listeDeCdes.ajoute(new CdeAjoutLivraison(plan, idIntersection, idPrec, idSuiv, duree, debutPlage, finPlage));
	fenetre.afficherMessage("Livraison ajoutée à la tournée");
	controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
    }

    @Override
    public void annulerAjout(Controleur controleur) {
	controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
    }

    @Override
    public boolean possibleAjoutLivraison(Controleur controleur, Plan plan, Fenetre fenetre) {
	return true;
    }

    @Override
    public void clicDroitLivraison(Plan plan, Fenetre fenetre, int idLivraison) {
	fenetre.ouvrirPopMenuLivraisonInsertion(idLivraison);
    }

    public void setIdIntersection(int idIntersection) {
	this.idIntersection = idIntersection;
    }

    @Override
    public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance) {
	int id = -1;
	ObjetGraphique objGraph = plan.cherche(point, tolerance);
	if (objGraph instanceof Livraison) {
	    id = ((Livraison) objGraph).getAdresse().getId();
	    fenetre.setLivraisonSurvol(id);
	}
	fenetre.setLivraisonSurvol(id);
    }
}
