package controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatDemandeLivraisonCharge extends EtatDefaut {
		// Etat aprï¿½s que l'utilisateur ait chargï¿½ une demande de livraison
	
	
	@Override
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre) {
	    try {
		DeserialiseurXML.chargerLivraisons(plan);
		fenetre.afficherMessage("Demande de livraison chargée");
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
	public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre, int tempsLimite) {
	    fenetre.afficherMessage("Lancement du calcul de la tournée");
	    if(plan.calculerTournee(tempsLimite)){
			fenetre.afficherMessage("Tournée calculée");
		    fenetre.afficherFeuilleDeRoute();
	    }
	    else
		fenetre.afficherMessage("Erreur : aucune tournée possible trouvée");
	    controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
	}

}
