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
    public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre) {
	try {
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
	ObjetGraphique OG = plan.cherche(point);
	if (OG != null) {
	    System.out.println("Objet trouvé");
	    if (OG instanceof Livraison) {
		System.out.println("L'Objet est une livraison");
		Livraison livCliquee = (Livraison) OG;
		fenetre.ouvrirMenuSupprimer(livCliquee.getAdresse().getId());
	    }
	    if (OG instanceof Intersection) {
		System.out.println("L'Objet est l'intersection id="+((Intersection) OG).getId());
	    }
	}
    }

    @Override
    public void supprimerLivraison(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idLivraison) {
	// TODO
	// listeDeCdes.ajoute(new CdeInverse(new CdeAjoutLivraison(plan,
	// idLivraison)));
	// fenetre.afficherMessage("Livraison supprimée de la tournée");
	plan.retirerLivraisonTournee(idLivraison);
    }

    @Override
    public boolean clicDroitZoneTextuellePossible(Controleur controleur) {
	return true;
    }

    @Override
    public void ajouterLivraison(Controleur controleur, Plan plan, Fenetre fenetre, int idLivraison, int duree) {
	// On passe dans l'état d'ajout d'une livraison
	// TODO : fenetre.passerEtatAjoutLivraison
	fenetre.afficherMessage("Veuillez selectionner un point de livraison");
	controleur.setEtatCourant(controleur.ETAT_AJOUT_LIVRAISON);
    }

}
