package controleur;

import java.awt.Point;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import modele.Intersection;
import modele.Livraison;
import modele.ObjetGraphique;
import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatAjoutLivraison extends EtatDefaut {
		// Etat atteint lorsque l'utilisateur clique sur ajouter une nouvelle livraison
	
    	private int idIntersection;
	
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
	public void clicGaucheAjoutLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idPrec, int idSuiv) {
	    // TODO : recuperer l'id de la livraison/intersection au model en lui donnant un point.
	    // Si le point selectionner est une interseciton et qu'elle n'est pas deja une livraison

	    listeDeCdes.ajoute(new CdeAjoutLivraison(plan, idIntersection, idPrec, idSuiv));

	    fenetre.afficherMessage("Livraison ajoutée à la tournée");
	}
	
	@Override
	public void annulerAjout(Controleur controleur,Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes) {
	    controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
	}
	
	
	@Override
	public boolean possibleAjoutLivraison(Controleur controleur,Plan plan, Fenetre fenetre) {
	    return true;
	}
	
	public void setIdIntersection(int idIntersection){
	    this.idIntersection = idIntersection;
	}
}

