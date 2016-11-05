package controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatDemandeLivraisonCharge extends EtatDefaut {
		// Etat après que l'utilisateur ait chargé une demande de livraison
	
	
	@Override
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre) {
	    try {
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
	public void quitter() {
	    System.exit(0);
	}
	
	@Override
	public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre, int tempsLimite) {
	    fenetre.afficherMessage("Lancement du calcul de la tournée");
	    controleur.setEtatCourant(controleur.ETAT_CALCUL_EN_COURS);
	    // Operations effectuees dans l'etat calcul en cours
	    if(plan.calculerTournee(tempsLimite)){
			fenetre.afficherMessage("Tournée calculée");
	    }
	    else {
		fenetre.afficherMessage("Temps limite atteint, si aucune tournée n'a été trouvée, veuillez selectionner un temps limite plus long");
	    }
	    controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
	    	
	}
}
