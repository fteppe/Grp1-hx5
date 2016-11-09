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

public class EtatTourneeCalculee extends EtatDefaut {
    // Etat atteint apres que le plan et la livraison aient ete charge
    // et que la tournee soit calculee

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

    /*
     * @Override public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre) {
     * 
     * }
     */

    @Override
    public void undo(ListeDeCdes listeDeCdes) {
	listeDeCdes.undo();
    }

    @Override
    public void redo(ListeDeCdes listeDeCdes) {
	listeDeCdes.redo();
    }


    @Override
    public void supprimerLivraison(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idLivraison) {
	 listeDeCdes.ajoute(new CdeInverse(new CdeAjoutLivraison(plan, idLivraison)));
	 fenetre.afficherMessage("Livraison supprimée de la tournée");
    }

    @Override
    public boolean clicDroitZoneTextuellePossible() {
	return true;
    }

    @Override
    public void clicAjouterLivraison(Controleur controleur, Fenetre fenetre, int idIntersection) {
		controleur.ETAT_AJOUT_LIVRAISON.setIdIntersection(idIntersection);
		controleur.setEtatCourant(controleur.ETAT_AJOUT_LIVRAISON);
		fenetre.afficherMessage("Choisissez où placer la nouvelle livraison:");
		
		Plan plan = controleur.getPlan();
		if(plan.getListeLivraisons().isEmpty()){
			controleur.ETAT_AJOUT_LIVRAISON.setIdIntersection(idIntersection);
			controleur.setEtatCourant(controleur.ETAT_AJOUT_LIVRAISON);
			fenetre.ouvrirMenuCreationLivraison(plan.getEntrepot().getId(), false);
		}
		else{
			controleur.ETAT_AJOUT_LIVRAISON.setIdIntersection(idIntersection);
			controleur.setEtatCourant(controleur.ETAT_AJOUT_LIVRAISON);
			fenetre.afficherMessage("Choisissez une livraison avant ou après laquelle placer votre nouvelle livraison");
		}
    }
    
    /*QUand on clique sur ajouter livraison on passe dans l'etat AjoutLivraison dans lequel on choisit
     * où insérer la livraison.
     * 
     * Si il n'y a plus de livraisons, alors cela insésère au niveau de l'entrepot
     */
    @Override
    public void clicEchangerLivraisons(Controleur controleur, Fenetre fenetre, int idLivraison) {
		controleur.ETAT_ECHANGER_LIVRAISON.setIdLivraison(idLivraison);
		controleur.setEtatCourant(controleur.ETAT_ECHANGER_LIVRAISON);
		fenetre.afficherMessage("Choisissez une seconde livraison:");
    }
    
    @Override
    public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance) {
		int id =-1;
		ObjetGraphique objGraph = plan.cherche(point, tolerance);
		if(objGraph instanceof Livraison) {
		    id = ((Livraison) objGraph).getAdresse().getId();
		    fenetre.setLivraisonSurvol(id);
		}
		else if (objGraph instanceof Intersection) {
		    id = ((Intersection) objGraph).getId();
		    fenetre.setIntersectionSurvol(id);
		}

		fenetre.setIntersectionSurvol(id);
		fenetre.setLivraisonSurvol(id);
    }
    
    @Override
    public void clicDroitLivraison(Plan plan, Fenetre fenetre, int idLivraison){
    	fenetre.ouvrirPopMenuLivraison(idLivraison);
    }
    
    @Override
    public void clicDroitIntersection(Fenetre fenetre, int idIntersection){
    	fenetre.ouvrirPopMenuIntersection(idIntersection);
    }   

}
