package controleur;

import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.awt.Point;
import modele.Plan;

public abstract class EtatDefaut implements Etat {
    // Definition des comportements par defaut des methodes

	protected Controleur controleur;
	protected Plan plan;
	protected Fenetre fenetre;
	
	public void init(Controleur c, Plan p, Fenetre f){
		this.controleur = c;
		this.plan = p;
		this.fenetre = f;
	}
	
    public void chargerPlan() {
	fenetre.afficherMessage("Chargement du plan en cours");
	try {
	    controleur.getListeCde().reset();
	    String rapport = DeserialiseurXML.chargerPlan(plan);
	    if (rapport.isEmpty())
		fenetre.afficherMessage("Plan chargé avec succés");
	    else
		fenetre.afficherMessage("Plan créé avec des erreurs :\n" + rapport);
	    controleur.setEtatCourant(controleur.ETAT_PLAN_CHARGE);
	} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML | NumberFormatException e) {
	    fenetre.afficherMessage(e.getMessage());
	}
    }

    public void chargerDemandeLivraison() {
    }

    public void calculerTournee(int tempsLimite) {
    }

    public void quitter() {
    }

    public void undo() {
    }

    public void redo() {
    }

    public void arreterCalcul() {
    }

    public void supprimerLivraison(int idLivraison) {
    }

    // public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre){}

    public void clicAjouterLivraisonPosition(int idPrec, int idSuiv, int duree) {
    }

    public void clicAjouterLivraisonPosition(int idPrec, int idSuiv, int duree, String debutPlage,
	    String finPlage) {
    }

    public boolean clicDroitZoneTextuellePossible() {
	return false;
    }

    public void clicAjouterLivraison(int idIntersection) {
    }

    public void clicEchangerLivraisons(int idLivraison) {
    }

    public void annulerAction() {
    }

    public void survolPlan(Point point, int tolerance) {
    }

    public boolean possibleAjoutLivraison() {
	return false;
    }

    public void clicDroitLivraison(int idLivraison) {
    }

    public void clicDroitIntersection(int idIntersection) {
    }

    public void clicGaucheLivraison(int idLivraison) {
    }

    public void genererFeuilleDeRoute() {
    }

    public void modifierLivraison(int adrLiv, boolean possedePlage, String heureDebut, String heureFin) {
    }

}
