package controleur;

import java.awt.Point;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import modele.Intersection;
import modele.Itineraire;
import modele.Livraison;
import modele.ObjetGraphique;
import modele.Plan;
import modele.Troncon;
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
	    DeserialiseurXML.chargerLivraisons(plan);
	    plan.setTournee(null);
	    fenetre.afficherMessage("Demande de livraison chargée");
	    controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
	} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML | NumberFormatException e) {
	    // fenetre.afficherMessage(e.getMessage());
	    // On n'affiche pas le message pour ne pas effacer la feuille de
	    // route
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
    public void clicDroitPlan(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, Point point) {
	System.out.println("Clic droit sur le plan coord: x(" + point.getX() + ") - y(" + point.getY() + ")");
	// TODO ajout tolérance
	ObjetGraphique objGraph = plan.cherche(point, 15);
	if (objGraph instanceof Livraison) {
	    System.out.println("L'Objet est une livraison");
	    Livraison livCliquee = (Livraison) objGraph;
	    fenetre.ouvrirMenuSupprimer(livCliquee.getAdresse().getId());
	}
	if (objGraph instanceof Intersection) {
	    System.out.println("L'Objet est l'intersection id=" + ((Intersection) objGraph).getId());
	}
    }

    @Override
    public void supprimerLivraison(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idLivraison) {
	 listeDeCdes.ajoute(new CdeInverse(new CdeAjoutLivraison(plan, idLivraison)));
	 fenetre.afficherMessage("Livraison supprimée de la tournée");
    }

    @Override
    public boolean clicDroitZoneTextuellePossible(Controleur controleur) {
	return true;
    }

    @Override
    public void passerEtatAjouterLivraison(Controleur controleur, Fenetre fenetre, int idIntersection) {
	controleur.ETAT_AJOUT_LIVRAISON.setIdIntersection(idIntersection);
	controleur.setEtatCourant(controleur.ETAT_AJOUT_LIVRAISON);
    }
    
    @Override
    public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance) {
	int id =-1;
	ObjetGraphique objGraph = plan.cherche(point, tolerance);
	if (objGraph instanceof Intersection) {
	    id = ((Intersection) objGraph).getId();
	}
	fenetre.setIntersectionSelectionne(id);
    }

}
