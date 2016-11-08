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

public class EtatChoisirLivraisonEchange extends EtatDefaut {
	// Etat lorsque l'utilisateur clique sur une livraison puis sur "Echanger"
	
    private int idLivraison;
    
    @Override
    public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes) {
    try {
    	listeDeCdes.reset();
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
    public void clicEchangerLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idLivraison2) {
	    listeDeCdes.ajoute(new CdeEchangeLivraisons(plan, idLivraison, idLivraison2));
	    fenetre.afficherMessage("Livraisons échangées");
	    controleur.setEtatCourant(controleur.ETAT_TOURNEE_CALCULEE);
    }
    
    
    @Override
    public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance) {
	int id =-1;
	ObjetGraphique objGraph = plan.cherche(point, tolerance);
	if (objGraph instanceof Livraison) {
	    id = ((Livraison) objGraph).getAdresse().getId();
	}
	fenetre.setLivraisonSurvole(id);
    }
    
    
    public void setIdLivraison(int idLivraison){
	    this.idLivraison = idLivraison;
	}
    
    
    
}