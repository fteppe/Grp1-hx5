package controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatTourneeCalculee extends EtatDefaut {
	// Etat atteint aprï¿½s que le plan et la livraison aient ï¿½tï¿½ chargï¿½ 
    	// et que la tournï¿½e soit calculï¿½e

	@Override
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre) {
	    try {
		DeserialiseurXML.chargerLivraisons(plan);
		plan.setTournee(null);
		fenetre.afficherMessage("Demande de livraison chargée");
		controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
	    } catch (ParserConfigurationException 
			| SAXException | IOException 
			| ExceptionXML | NumberFormatException e) {
		// fenetre.afficherMessage(e.getMessage());
		// On n'affiche pas le message pour ne pas effacer la feuille de route
	    }
	}
	
	@Override
	public void quitter() {
	    System.exit(0);
	}
	
	/*
	@Override
	public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre) {
		
	}
	*/
}
