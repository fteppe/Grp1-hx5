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
	
	public void chargerPlan(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes) {
		try {
		    	listeDeCdes.reset();
			DeserialiseurXML.chargerPlan(plan);
			fenetre.afficherMessage("Plan chargé avec succés");
			controleur.setEtatCourant(controleur.ETAT_PLAN_CHARGE);
		} catch (ParserConfigurationException 
				| SAXException | IOException 
				| ExceptionXML | NumberFormatException e) {
		    fenetre.afficherMessage(e.getMessage());
		}
	}
	
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes){}

	public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre, int tempsLimite){}

	public void quitter(){}
	
	public void undo(ListeDeCdes listeDeCdes){}
	
	public void redo(ListeDeCdes listeDeCdes) {}
	
	public void arreterCalcul(Controleur controleur, Plan plan, Fenetre fenetre) {}

	
	public void supprimerLivraison(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idLivraison) {}

	//public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre){}
	
	public void clicAjouterLivraisonPosition(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, 
		int idPrec, int idSuiv, int duree) {}
	
	public void clicAjouterLivraisonPosition(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes,
		int idPrec, int idSuiv, int duree, String debutPlage, String finPlage) {}
	
	public boolean clicDroitZoneTextuellePossible(){
		return false;
	}
	
	public void clicEchangerLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idLivraison2) {}
	
	public void passerEtatAjouterLivraison(Controleur controleur, Fenetre fenetre, int idIntersection) {}
	
	public void passerEtatEchangerLivraison(Controleur controleur, Fenetre fenetre, int idLivraison) {}
	
	public void annulerAjout(Controleur controleur) {}
	
	public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance) {}
	
	public boolean possibleAjoutLivraison(Controleur controleur,Plan plan, Fenetre fenetre) {
	    return false;
	}
	
	public void clicDroitLivraison(Plan plan, Fenetre fenetre, int idLivraison){}
	
	public void clicDroitIntersection(Fenetre fenetre, int idIntersection){}
	
	public void clicGaucheLivraison(Controleur controleur,Fenetre fenetre,Plan plan, int idLivraison){}
}
