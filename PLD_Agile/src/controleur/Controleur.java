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
    protected final EtatChoisirLivraisonEchange ETAT_ECHANGER_LIVRAISON = new EtatChoisirLivraisonEchange();

    /**
     * Cree le controleur de l'application
     * 
     * @param plan
     *            le plan
     */
    public Controleur(Plan plan) {
	this.plan = plan;
	listeDeCdes = new ListeDeCdes();
	etatCourant = ETAT_INITIAL;
	String titre = "Optimod";
	tempsLimite = 10000;
	int longueur = 720;
	int largeur = 1024;
	fenetre = new Fenetre(titre, longueur, largeur, plan, this);
    }

    protected ListeDeCdes getListeCde() {
	return listeDeCdes;
    }

    /**
     * Change l'etat courant du controleur
     * 
     * @param etat
     *            le nouvel etat courant
     */
    protected void setEtatCourant(Etat etat) {
	etatCourant = etat;
    }

    /**
     * Retourne l'etat courant du controleur
     */
    protected Etat getEtatCourant() {
	return etatCourant;
    }

    protected Fenetre getFenetre() {
	return fenetre;
    }

    protected Plan getPlan() {
	return plan;
    }

    // Methodes correspondant aux evenements utilisateur
    /**
     * Methode appelee par fenetre apres un clic sur le bouton "Charger plan"
     */
    public void chargerPlan() {
	if (!plan.getCalculTourneeEnCours())
	    etatCourant.chargerPlan(this, plan, fenetre, listeDeCdes);
	else
	    fenetre.afficherMessage(
		    "Veuillez arrêter le calcul de la tournée avant de recharger le plan ou la demande de livraison.");
    }

    /**
     * Methode appelee par fenetre apres un clic sur le bouton "Charger demande
     * de livraison"
     */
    public void chargerDemandeLivraison() {
	if (!plan.getCalculTourneeEnCours())
	    etatCourant.chargerDemandeLivraison(this, plan, fenetre, listeDeCdes);
	else
	    fenetre.afficherMessage(
		    "Veuillez arrêter le calcul de la tournée avant de recharger le plan ou la demande de livraison.");
    }

    /**
     * Methode appelee par fenetre apres un clic sur le bouton "Quitter"
     */
    public void quitter() {
	etatCourant.quitter();
    }

    /**
     * Methode appelee par fenetre apres un clic sur le bouton "Generer feuille
     * de route"
     */
    // Pour seconde itération
    /*
     * public void genererFeuilleDeRoute() {
     * etatCourant.genererFeuilleDeRoute(plan, fenetre); }
     */

    /**
     * Methode appelee par fenetre apres un clic sur le bouton "Calcul Tournee"
     */
    public void calculTournee() {
	etatCourant.calculerTournee(this, plan, fenetre, tempsLimite);
    }

    /**
     * Methode appelee par fenetre apres un clic gauche sur un point de la vue
     * graphique
     */

    public void clicAjouterLivraisonPosition(int idPrec, int idSuiv, int duree) {
	etatCourant.clicAjouterLivraisonPosition(this, plan, fenetre, listeDeCdes, idPrec, idSuiv, duree);
    }

    /**
     * Methode appelee par fenetre apres un clic gauche sur un point de la vue
     * graphique
     */

    public void clicAjouterLivraisonPosition(int idPrec, int idSuiv, int duree, String debutPlage, String finPlage) {
	etatCourant.clicAjouterLivraisonPosition(this, plan, fenetre, listeDeCdes, idPrec, idSuiv, duree, debutPlage,
		finPlage);
    }

    /**
     * Methode appelee par la fenetre quand l'utilisateur clique sur le bouton
     * "Undo"
     */
    public void undo() {
	etatCourant.annulerAction(this);
	etatCourant.undo(listeDeCdes);
    }

    /**
     * Methode appelee par fenetre apres un clic sur le bouton "Redo"
     */
    public void redo() {
	etatCourant.redo(listeDeCdes);
    }

    /**
     * Methode appelee par fenetre apres un clic sur le bouton "Arreter" lors du
     * calcul de la tournee
     */
    public void arreterCalculTournee() {
	etatCourant.arreterCalcul(this, plan, fenetre);
    }

    public void clicGaucheLivraison(int idLivraison) {
	etatCourant.clicGaucheLivraison(this, fenetre, plan, idLivraison);
    }

    /**
     * Methode appelee par fenetre apres un clic droit sur un point du plan une
     * fois le calcul de la tournee termine.
     * 
     * @param point
     *            Le point clique par l'utilisateur
     */
    public void clicDroitLivraison(int idLivraison) {
	etatCourant.clicDroitLivraison(plan, fenetre, idLivraison);
    }

    public void clicDroitIntersection(int idIntersection) {
	etatCourant.clicDroitIntersection(fenetre, idIntersection);
    }

    /**
     * Methode appelee par fenetre afin de savoir si elle peut afficher le menu
     * de clique droit sur la zone textuelle.
     */
    public boolean clicDroitZoneTextuellePossible() {
	return etatCourant.clicDroitZoneTextuellePossible();
    }

    /**
     * Methode appelee par fenetre lorsque l'utilisateur clique sur supprimer
     * apres avoir fait un clic droit sur une livraison.
     * 
     * @param idLivraison
     */
    public void supprimerLivraison(int idLivraison) {
	etatCourant.supprimerLivraison(plan, fenetre, listeDeCdes, idLivraison);
    }

    /**
     * Methode appelee par fenetre lorsque l'utilisateur clique sur ajouter une
     * nouvelle livraison sur le plan.
     * 
     * @param idIntersection
     */
    public void clicAjouterLivraison(int idIntersection) {
	etatCourant.clicAjouterLivraison(this, fenetre, idIntersection);
    }

    /**
     * Methode appelee par fenetre lorsque l'utilisateur clique sur Echanger
     * apres avoir selectionner une 1ere livraison.
     * 
     * @param idIntersection
     */
    public void passerEtatEchangerLivraison(int idLivraison) {
	etatCourant.clicEchangerLivraisons(this, fenetre, idLivraison);
    }

    /**
     * Methode appelee par fenetre lorsque l'utilisateur clique sur "annuler"
     * dans le mode d'ajout de livraison.
     */
    public void annulerAjout() {
	etatCourant.annulerAction(this);
    }

    /**
     * Methode appelee par fenetre lorsque l'utilisateur survole le plan une
     * fois la tournée calculée.
     * 
     * @param point
     * @param tolerance
     */
    public void survolPlan(Point point, int tolerance) {
	etatCourant.survolPlan(plan, fenetre, point, tolerance);
    }

    /**
     * Methode appelee par fenetre afin qu'elle sache si il est possible
     * d'ajouter une livraison dans l'etat courant.
     */
    public boolean possibleAjoutLivraison() {
	return etatCourant.possibleAjoutLivraison(this, plan, fenetre);
    }

    /**
     * Methode appelée lorsque l'utilisateur clique sur le bouton echanger
     * livraison
     */
    public void clicEchangerLivraison(int idLivraison) {
	etatCourant.clicEchangerLivraisons(this, fenetre, idLivraison);
    }

    public void genererFeuilleDeRoute() {
	etatCourant.genererFeuilleDeRoute(plan);
    }

    public void modifierLivraison(int adrLiv, boolean possedePlage, String heureDebut, String heureFin) {
	etatCourant.modifierLivraison(plan, fenetre, listeDeCdes, adrLiv, possedePlage, heureDebut, heureFin);
    }
}
