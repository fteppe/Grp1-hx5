package controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatDemandeLivraisonCharge extends EtatDefaut {
		// Etat apr�s que l'utilisateur ait charg� une demande de livraison
	
	
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
	public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre, int tempsLimite) {
	    fenetre.afficherMessage("Lancement du calcul de la tourn�e");
	    if(plan.calculerTournee(tempsLimite)){
			//fenetre.afficherMessage("Tourn�e calcul�e");
		    fenetre.afficherFeuilleDeRoute();
	    }
	    else
		fenetre.afficherMessage("Erreur : aucune tourn�e possible trouv�e");
	    controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
	}
	
	@Override
	public void undo(ListeDeCdes listeDeCdes){
		listeDeCdes.undo();
	}

	@Override
	public void redo(ListeDeCdes listeDeCdes){
		listeDeCdes.redo();
	}

}
