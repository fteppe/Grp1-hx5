package controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatCalculEnCours extends EtatDefaut {
    // Etat lorsque le calcul de la tournee est en cours

    @Override
    public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes) {
	try {
	    listeDeCdes.reset();
	    String rapport = DeserialiseurXML.chargerLivraisons(plan);
	    plan.setTournee(null);
	    if (rapport.isEmpty())
		fenetre.afficherMessage("Demande de livraison chargée");
	    else
		fenetre.afficherMessage("Demande de livraison chargée avec des erreurs :\n" + rapport);
	    fenetre.afficherDetailDemandeLivraison();
	    controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
	} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML | NumberFormatException e) {
	    fenetre.afficherMessage(e.getMessage());
	}
    }

    @Override
    public void quitter() {
	System.exit(0);
    }

    @Override
    public void arreterCalcul(Controleur controleur, Plan plan, Fenetre fenetre) {
	plan.arreterCalculTournee();
	fenetre.afficherMessage("Calcul de tournée arrêté");
	controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
    }

}
