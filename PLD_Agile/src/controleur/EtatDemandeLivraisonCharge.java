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
	    } catch (ParserConfigurationException 
			| SAXException | IOException 
			| ExceptionXML | NumberFormatException e) {
		fenetre.afficherMessage(e.getMessage());
	    }
	    fenetre.afficherMessage("Demande de livraison chargée");
	    controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
	}
	
	@Override
	public void quitter() {
	    System.exit(0);
	}
	
	@Override
	public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre, int tempsLimite) {
	    fenetre.afficherMessage("Lancement du calcul de la tournee");
	    if(plan.calculerTournee(tempsLimite))
		fenetre.afficherMessage("Tournée calculée");
	    else
		fenetre.afficherMessage("Erreur : aucune tournée possible trouvée");
	    controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
	}

}
