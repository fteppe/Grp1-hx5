package controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatTourneeCalculee extends EtatDefaut {
	// Etat atteint apr�s que le plan et la livraison aient �t� charg� 
    	// et que la tourn�e soit calcul�e

	@Override
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre) {
	    try {
		DeserialiseurXML.chargerLivraisons(plan);
	    } catch (ParserConfigurationException 
			| SAXException | IOException 
			| ExceptionXML | NumberFormatException e) {
		fenetre.afficherMessage(e.getMessage());
	    }
	    controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
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
