 package controleur;

import vue.Fenetre;

import java.awt.Point;

import modele.Plan;
 
public class Controleur {

	private Plan plan;
	private Fenetre fenetre;
	private Etat etatCourant;
	private int tempsLimite;
	private ListeDeCdes listeDeCdes;
	
	// Instances associees a chaque etat possible du controleur
	protected final EtatInitial ETAT_INITIAL = new EtatInitial();
	protected final EtatPlanCharge ETAT_PLAN_CHARGE = new EtatPlanCharge();
	protected final EtatDemandeLivraisonCharge ETAT_DEMANDE_LIVRAISON_CHARGE = new EtatDemandeLivraisonCharge();
	protected final EtatCalculEnCours ETAT_CALCUL_EN_COURS = new EtatCalculEnCours();
	protected final EtatTourneeCalculee ETAT_TOURNEE_CALCULEE = new EtatTourneeCalculee();
	protected final EtatAjoutLivraison ETAT_AJOUT_LIVRAISON = new EtatAjoutLivraison();
	

	/**
	 * Cree le controleur de l'application
	 * @param plan le plan
	 */
	public Controleur(Plan plan) {
		this.plan = plan;
		listeDeCdes = new ListeDeCdes();
		etatCourant = ETAT_INITIAL;
		String titre = "Optimod";
		tempsLimite = 10000;
		int longueur = 720;
		int largeur = 1024;
		fenetre = new Fenetre(titre,longueur,largeur, plan, this);
	}
	
	/**
	 * Change l'etat courant du controleur
	 * @param etat le nouvel etat courant
	 */
	protected void setEtatCourant(Etat etat){
		etatCourant = etat;
	}
	
	/**
	 * Retourne l'etat courant du controleur
	 */
	protected Etat getEtatCourant(){
		return etatCourant;
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
		etatCourant.calculerTournee(this, plan, fenetre, tempsLimite);
	}
	
 
	/**
	 * Methode appelee par fenetre apres un clic gauche sur un point de la vue graphique
	 */
	
	public void clicAjouterLivraisonPosition(int idPrec, int idSuiv) {
		etatCourant.clicGaucheAjoutLivraison(this, plan, fenetre, listeDeCdes, idPrec, idSuiv);
	}
	
	
	/**
	 * Methode appelee par la fenetre quand l'utilisateur clique sur le bouton "Undo"
	 */
	public void undo(){
		etatCourant.undo(listeDeCdes);
	}

	/**
	 * Methode appelee par fenetre apres un clic sur le bouton "Redo"
	 */
	public void redo(){
		etatCourant.redo(listeDeCdes);
	}
	
	/**
	 * Methode appelee par fenetre apres un clic sur le bouton "Arreter" lors du calcul de la tournee
	 */
	public void arreterCalculTournee(){
	    	etatCourant.arreterCalcul(this, plan, fenetre);
	}
	
	/**
	 * Methode appelee par fenetre apres un clic droit sur un point du plan 
	 * une fois le calcul de la tournee termine.
	 * @param point Le point clique par l'utilisateur
	 */
	public void clicDroitPlan(Point point){
	    	etatCourant.clicDroitPlan(plan, fenetre, listeDeCdes, point);
	}
	
	/**
	 * Methode appelee par fenetre afin de savoir si elle peut afficher 
	 * le menu de clique droit sur la zone textuelle.
	 */
	public boolean clicDroitZoneTextuellePossible(){
	    return etatCourant.clicDroitZoneTextuellePossible(this);
	}
	
	/**
	 * Methode appelee par fenetre lorsque l'utilisateur clique
	 * sur supprimer apres avoir fait un clic droit sur une livraison.
	 * @param idLivraison
	 */
	public void supprimerLivraison(int idLivraison){
	    	etatCourant.supprimerLivraison(plan, fenetre, listeDeCdes, idLivraison);
	}
	
	/**
	 * Methode appelee par fenetre lorsque l'utilisateur clique
	 * sur ajouter une nouvelle livraison sur le plan.
	 * @param idIntersection
	 */
	public void passerEtatAjouterLivraison(int idIntersection){
	    	etatCourant.passerEtatAjouterLivraison(this, fenetre, idIntersection);
	}
	
	
	/**
	 * Methode appelee par fenetre lorsque l'utilisateur clique
	 * sur "annuler" dans le mode d'ajout de livraison.
	 */
	public void annulerAjout() {
	    etatCourant.annulerAjout(this, plan, fenetre, listeDeCdes);
	}
	
	/**
	 * Methode appelee par fenetre lorsque l'utilisateur survole
	 * le plan une fois la tournée calculée.
	 * @param point
	 * @param tolerance
	 */
	public void survolPlan(Point point, int tolerance) {
	    etatCourant.survolPlan(plan, fenetre, point, tolerance);
	}
	
	/**
	 * Methode appelee par fenetre lorsque l'utilisateur survole
	 * le plan une fois la tournée calculée.
	 * @param point
	 * @param tolerance
	 */
	public boolean possibleAjoutLivraison(Point point, int tolerance) {
	    return etatCourant.possibleAjoutLivraison(this, plan, fenetre);
	}

}
