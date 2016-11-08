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
	
	public void chargerPlan(Controleur controleur, Plan plan, Fenetre fenetre) {
		try {
			DeserialiseurXML.chargerPlan(plan);
			fenetre.afficherMessage("Plan chargé avec succés");
			controleur.setEtatCourant(controleur.ETAT_PLAN_CHARGE);
		} catch (ParserConfigurationException 
				| SAXException | IOException 
				| ExceptionXML | NumberFormatException e) {
		    fenetre.afficherMessage(e.getMessage());
		}
	}
	
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre){}

	public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre, int tempsLimite){}

	public void quitter(){}
	
	public void undo(ListeDeCdes listeDeCdes){}
	
	public void redo(ListeDeCdes listeDeCdes) {}
	
	public void arreterCalcul(Controleur controleur, Plan plan, Fenetre fenetre) {}

	public void clicDroitPlan(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, Point point) {}
	
	public void supprimerLivraison(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idLivraison) {}

	//public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre){}
	
	public void clicGauche(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, Point p) {}
	
	public boolean clicDroitZoneTextuellePossible(Controleur controleur){
		return false;
	}
	
	public void passerEtatAjouterLivraison(Controleur controleur, Fenetre fenetre) {}
	
	public void annulerAjout(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes) {}
	
	public int survolPlan(Plan plan, Point point, int tolerance) {
	    return -1;
	}
}
