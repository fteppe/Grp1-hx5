package controleur;

import vue.Fenetre;
import modele.Plan;

public interface Etat {
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Charger demande livraison"
	 * @param controleur 
	 * @param fenetre
	 */
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Calculer Tournee"
	 * @param controleur 
	 * @param plan
	 * @param fenetre
	 */
	public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Quitter"
	 */
	public void quitter();
	
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Generer feuille de route"
	 * @param plan
	 * @param fenetre
	 */
	//public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre);
	
	/**
	 * Methode appelee par controleur apres un clic sur le bouton "Charger un plan"
	 * @param plan
	 * @param fenetre
	 */
	public void chargerPlan(Controleur controleur, Plan plan, Fenetre fenetre);
	
	
	/**
	 * Methode appelee par controleur apres un clic gauche sur un point de la vue graphique
	 * @param fenetre
	 * @param plan
	 */
	public void clicGauche(Plan plan, Fenetre fenetre);
}
