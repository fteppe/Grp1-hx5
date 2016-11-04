package controleur;

import java.awt.Point;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatAjoutLivraison extends EtatDefaut {
		// Etat atteint lorsque l'utilisateur clique sur ajouter une nouvelle livraison
	
	
	@Override
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre) {
	    try {
		DeserialiseurXML.chargerLivraisons(plan);
		plan.setTournee(null);
		fenetre.afficherMessage("Demande de livraison charg�e");
		fenetre.afficherDetailDemandeLivraison();
		controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
	    } catch (ParserConfigurationException 
			| SAXException | IOException 
			| ExceptionXML | NumberFormatException e) {
		fenetre.afficherMessage(e.getMessage());
	    }
	}
	
	@Override
	public void quitter() {
	    System.exit(0);
	}
	
	@Override
	public void clicGauche(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, Point p) {
	    // 
	    //listeDeCdes.ajoute(new CdeAjoutLivraison(plan, idLivraison));
	    fenetre.afficherMessage("Livraison ajoutée à la tournée");
	}
	
	@Override
	public void clicDroitPlan(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, Point p) {
	    
	}
	
}

