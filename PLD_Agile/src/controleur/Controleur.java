 package controleur;

import vue.Fenetre;
import modele.Plan;
 
public class Controleur {

	private Plan plan;
	private Fenetre fenetre;
	private Etat etatCourant;
	
	// Instances associees a chaque etat possible du controleur
	protected final EtatInitial etatInitial = new EtatInitial();
	protected final EtatPlanCharge EtatPlanCharge = new EtatPlanCharge();
	protected final EtatDemandeLivraisonCharge EtatDemandeLivraisonCharge = new EtatDemandeLivraisonCharge();
	protected final EtatTourneeCalculee EtatTourneeCalculee = new EtatTourneeCalculee();

	/**
	 * Cree le controleur de l'application
	 * @param plan le plan
	 */
	public Controleur(Plan plan) {
		this.plan = plan;
		etatCourant = etatInitial;
		//fenetre = new Fenetre(plan, this);
	}
	
	/**
	 * Change l'etat courant du controleur
	 * @param etat le nouvel etat courant
	 */
	protected void setEtatCourant(Etat etat){
		etatCourant = etat;
	}

	// Methodes correspondant aux evenements utilisateur
	/**
	 * Methode appelee par fenetre apres un clic sur le bouton "Charger plan"
	 */
	public void chargerPlan() {
		etatCourant.chargerPlan(this, plan, fenetre);
	}

	/**
	 * Methode appelee par fenetre apres un clic sur le bouton "Charger demande de livraison"
	 */
	public void chargerDemandeLivraison() {
		etatCourant.chargerDemandeLivraison(this, plan, fenetre);
	}

	/**
	 * Methode appelee par fenetre apres un clic sur le bouton "Quitter"
	 */
	public void quitter() {
		etatCourant.quitter();
	}

	/**
	 * Methode appelee par fenetre apres un clic sur le bouton "Generer feuille de route"
	 */
	// Pour seconde itération
	/* 
	public void genererFeuilleDeRoute() {
		etatCourant.genererFeuilleDeRoute(plan, fenetre);
	}
	*/
	
	/**
	 * Methode appelee par fenetre apres un clic sur le bouton "Calcul Tournee"
	 */
	public void calculTournee() {
		etatCourant.calculerTournee(this, plan, fenetre);
	}
	

	/**
	 * Methode appelee par fenetre apres un clic gauche sur un point de la vue graphique
	 */
	public void clicGauche() {
		etatCourant.clicGauche(plan, fenetre);
	}


}
