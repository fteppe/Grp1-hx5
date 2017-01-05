package controleur;

import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.awt.Point;
import modele.Plan;

public abstract class EtatDefaut {
	// Definition des comportements par defaut des methodes

	protected Controleur controleur;
	protected Plan plan;
	protected Fenetre fenetre;

	/**
	 * cette fonction permet de revenir à l'état précédent dans certaines
	 * situations
	 */
	public void annulerAction() {
	}

	/**
	 * Methode appelee par controleur lorsque l'utilisateur clique sur le
	 * bouton d'arret du calcul de tournee
	 */
	public void arreterCalcul() {
	}

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Calculer
	 * Tournee"
	 * @param tempsLimite
	 */
	public void calculerTournee(int tempsLimite) {
	}

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Charger
	 * demande livraison"
	 */
	public void chargerDemandeLivraison() {
	}

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Charger
	 * un plan"
	 */
	public void chargerPlan() {
		fenetre.afficherMessage("Chargement du plan en cours");
		try {
			controleur.getListeCde().reset();
			String rapport = DeserialiseurXML.chargerPlan(plan);
			if (rapport.isEmpty())
				fenetre.afficherMessage("Plan chargé avec succés");
			else
				fenetre.afficherMessage("Plan créé avec des erreurs :\n" + rapport);
			controleur.setEtatCourant(controleur.ETAT_PLAN_CHARGE);
		} catch (ParserConfigurationException | SAXException | IOException | ExceptionXML
				| NumberFormatException e) {
			fenetre.afficherMessage(e.getMessage());
		}
	}

	/**
	 * Methode appelée lorsque l'utilisateur clique sur le bouton echanger
	 * livraison
	 * 
	 * @param idIntersection
	 */
	public void clicAjouterLivraison(int idIntersection) {
	}

	/**
	 * Methode appelee par controleur apres un clic gauche sur un point de
	 * la vue graphique
	 * 
	 * @param idPrec
	 * @param idSuiv
	 * @param duree
	 */
	public void clicAjouterLivraisonPosition(int idPrec, int idSuiv, int duree) {
	}

	/**
	 * Methode appelee par controleur apres un clic gauche sur un point de
	 * la vue graphique
	 * 
	 * @param idPrec
	 * @param idSuiv
	 * @param duree
	 * @param debutPlage
	 * @param finPlage
	 */
	public void clicAjouterLivraisonPosition(int idPrec, int idSuiv, int duree, String debutPlage,
			String finPlage) {
	}

	/**
	 * Methode appelee par controleur quand ...
	 * 
	 * @param idIntersection
	 */
	public void clicDroitIntersection(int idIntersection) {
	}

	// public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre){}

	/**
	 * Methode appelee par controleur quand ...
	 * 
	 * @param idLivraison
	 */
	public void clicDroitLivraison(int idLivraison) {
	}

	/**
	 * Methode appelee par controleur lorsque l'utilisateur fait un clic
	 * gauche sur Echanger après avoir selectionner une 1ere livraison.
	 * 
	 * @param idLivraison
	 */
	public void clicEchangerLivraisons(int idLivraison) {
	}

	/**
	 * Methode appelee par controleur quand un clic gauche est effectué sur
	 * une livraison
	 * 
	 * @param idLivraison
	 */
	public void clicGaucheLivraison(int idLivraison) {
	}

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Generer
	 * feuille de route"
	 * 
	 * @param plan
	 * @param fenetre
	 */
	public void genererFeuilleDeRoute() {
	}

	/**
	 * Initialise l'état pour qu'il puisse utiliser les différentes classe
	 * nécessaires pour le MVC
	 * 
	 * @param controleur
	 *                : Le controleur du MVC
	 * @param plan
	 *                : Le plan utilisé par le modéle
	 * @param fenetre
	 *                : La fenétre gérant la vue
	 */
	public void init(Controleur c, Plan p, Fenetre f) {
		this.controleur = c;
		this.plan = p;
		this.fenetre = f;
	}

	/**
	 * Méthode appelée pour modifier la plage horaire de la livraison à
	 * l'adresse donnée
	 * 
	 * @param adrLiv
	 *                Adresse de la livraison à modifier
	 * @param possedePlage
	 *                True si la modification comporte une plage horaire,
	 *                false sinon
	 * @param heureDebut
	 *                Heure de début de la plage
	 * @param heureFin
	 *                Heure de fin de la plage
	 */
	public void modifierLivraison(int adrLiv, boolean possedePlage, String heureDebut, String heureFin) {
	}

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Quitter"
	 */
	public void quitter() {
	}

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Redo"
	 */
	public void redo() {
	}

	/**
	 * Methode appelee par controleur lorsque l'utilisateur fait un clique
	 * droit sur un point du plan lorsque la tournee est deja calculee.
	 * 
	 * @param idLivraison
	 */
	public void supprimerLivraison(int idLivraison) {
	}

	/**
	 * Methode appelee par controleur lorsque l'utilisateur survole le plan
	 * avec la souris, vérifie si le point est une intersection et si ce
	 * n'est pas déjà une livraison.
	 * 
	 * @param point
	 * @param tolerance
	 */
	public void survolPlan(Point point, int tolerance) {
	}

	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Undo"
	 */
	public void undo() {
	}

}
