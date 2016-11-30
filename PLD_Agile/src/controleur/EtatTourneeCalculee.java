package controleur;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import modele.Intersection;
import modele.Livraison;
import modele.ObjetGraphique;
import modele.Plan;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatTourneeCalculee extends EtatDefaut {
	// Etat atteint apres que le plan et la livraison aient ete charge
	// et que la tournee soit calculee

	@Override
	public void chargerDemandeLivraison() {
		try {
			controleur.getListeCde().reset();
			String rapport = DeserialiseurXML.chargerLivraisons(plan);
			plan.supprimerTournee();
			if (rapport.isEmpty())
				fenetre.afficherMessage("Demande de livraison chargée");
			else
				fenetre.afficherMessage("Demande de livraison chargée avec des erreurs :\n" + rapport);
			controleur.setEtatCourant(controleur.ETAT_DEMANDE_LIVRAISON_CHARGE);
		} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML
				| NumberFormatException e) {
			fenetre.afficherMessage(e.getMessage());
		}
	}

	@Override
	public void quitter() {
		System.exit(0);
	}


	@Override
	public void undo() {
		controleur.getListeCde().undo();
	}

	@Override
	public void redo() {
		controleur.getListeCde().redo();
	}

	@Override
	public void supprimerLivraison(int idLivraison) {
		controleur.getListeCde().ajoute(new CdeInverse(new CdeAjoutLivraison(plan, idLivraison)));
		fenetre.afficherMessage("Livraison supprimée de la tournée");
	}

	@Override
	public void clicAjouterLivraison(int idIntersection) {
		controleur.ETAT_AJOUT_LIVRAISON.setIdIntersection(idIntersection);
		controleur.setEtatCourant(controleur.ETAT_AJOUT_LIVRAISON);
		fenetre.afficherMessage("Choisissez où placer la nouvelle livraison:");

		Plan plan = controleur.getPlan();
		if (plan.getListeLivraisons().isEmpty()) {
			controleur.ETAT_AJOUT_LIVRAISON.setIdIntersection(idIntersection);
			controleur.setEtatCourant(controleur.ETAT_AJOUT_LIVRAISON);
			fenetre.ouvrirMenuCreationLivraison(plan.getEntrepot().getId(), false);
		} else {
			controleur.ETAT_AJOUT_LIVRAISON.setIdIntersection(idIntersection);
			controleur.setEtatCourant(controleur.ETAT_AJOUT_LIVRAISON);
			fenetre.afficherMessage(
					"Choisissez une livraison avant ou après laquelle placer votre nouvelle livraison");
		}
	}

	/*
	 * QUand on clique sur ajouter livraison on passe dans l'etat
	 * AjoutLivraison dans lequel on choisit où insérer la livraison.
	 * 
	 * Si il n'y a plus de livraisons, alors cela insésère au niveau de
	 * l'entrepot
	 */
	@Override
	public void clicEchangerLivraisons(int idLivraison) {
		controleur.ETAT_ECHANGER_LIVRAISON.setIdLivraison(idLivraison);
		controleur.setEtatCourant(controleur.ETAT_ECHANGER_LIVRAISON);
		fenetre.afficherMessage("Choisissez une seconde livraison:");
	}

	@Override
	public void survolPlan(Point point, int tolerance) {
		int id = -1;
		ObjetGraphique objGraph = plan.cherche(point, tolerance);
		if (objGraph instanceof Livraison) {
			id = ((Livraison) objGraph).getAdresse().getId();
			fenetre.setLivraisonSurvol(id);
		} else if (objGraph instanceof Intersection) {
			id = ((Intersection) objGraph).getId();
			fenetre.setIntersectionSurvol(id);
		}

		fenetre.setIntersectionSurvol(id);
		fenetre.setLivraisonSurvol(id);
	}

	@Override
	public void clicDroitLivraison(int idLivraison) {
		fenetre.ouvrirPopMenuLivraison(idLivraison);
	}

	@Override
	public void clicDroitIntersection(int idIntersection) {
		fenetre.ouvrirPopMenuIntersection(idIntersection);
	}

	@Override
	public void modifierLivraison(int adrLiv, boolean possedePlage, String heureDebut, String heureFin) {
		controleur.getListeCde()
				.ajoute(new CdeModifierLivraison(plan, adrLiv, possedePlage, heureDebut, heureFin));
		fenetre.afficherMessage("Livraison supprimée de la tournée");
	}

	@Override
	public void genererFeuilleDeRoute() {
		JFileChooser chooser = new JFileChooser();
		String path = lireFichier("./pathFolder.txt");
		if (path != null) {
			chooser.setCurrentDirectory(new File(path));
		}
		int retrival = chooser.showSaveDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			try (FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".txt")) {
				fw.write(plan.genererFeuilleRoute());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private String lireFichier(String fichier) {
		String ligne = "";
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			ligne = br.readLine();
			br.close();
			return ligne;
		} catch (Exception e) {
			return null;
		}

	}
}
