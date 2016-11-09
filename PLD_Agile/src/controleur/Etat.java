package controleur;

import vue.Fenetre;
import java.awt.Point;
import modele.Plan;

public interface Etat {

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Charger
     * demande livraison"
     * 
     * @param controleur
     * @param plan
     * @param fenetre
     * @param listeDeCdes
     */
    public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Calculer
     * Tournee"
     * 
     * @param controleur
     * @param plan
     * @param fenetre
     */
    public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre, int tempsLimite);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Quitter"
     */
    public void quitter();

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Generer
     * feuille de route"
     * 
     * @param plan
     * @param fenetre
     */
    // public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Charger un
     * plan"
     * 
     * @param controleur
     * @param plan
     * @param fenetre
     * @param listeDeCdes
     */
    public void chargerPlan(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic gauche sur un point de la
     * vue graphique
     * 
     * @param controleur
     * @param plan
     * @param fenetre
     * @param listeDeCdes
     * @param idPrec
     * @param idSuiv
     * @param duree
     */
    public void clicAjouterLivraisonPosition(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes,
	    int idPrec, int idSuiv, int duree);

    /**
     * Methode appelee par controleur apres un clic gauche sur un point de la
     * vue graphique
     * 
     * @param controleur
     * @param plan
     * @param fenetre
     * @param listeDeCdes
     * @param idPrec
     * @param idSuiv
     * @param duree
     * @param debutPlage
     * @param finPlage
     */
    public void clicAjouterLivraisonPosition(Controleur controleur, Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes,
	    int idPrec, int idSuiv, int duree, String debutPlage, String finPlage);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Undo"
     * 
     * @param listeDeCdes
     */
    public void undo(ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur apres un clic sur le bouton "Redo"
     * 
     * @param listeDeCdes
     */
    public void redo(ListeDeCdes listeDeCdes);

    /**
     * Methode appelee par controleur lorsque l'utilisateur clique sur le bouton
     * d'arret du calcul de tournee
     * 
     * @param controleur
     * @param plan
     * @param fenetre
     */
    public void arreterCalcul(Controleur controleur, Plan plan, Fenetre fenetre);

    /**
     * Methode appelee par controleur lorsque l'utilisateur fait un clique droit
     * sur un point du plan lorsque la tournee est deja calculee.
     * 
     * @param plan
     * @param fenetre
     * @param point
     */
    public boolean clicDroitZoneTextuellePossible();

    /**
     * Methode appelee par controleur lorsque l'utilisateur fait un clique droit
     * sur un point du plan lorsque la tournee est deja calculee.
     * 
     * @param plan
     * @param fenetre
     * @param listeDeCdes
     * @param idLivraison
     */
    public void supprimerLivraison(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int idLivraison);

    /**
     * Methode appelée lorsque l'utilisateur clique sur le bouton echanger
     * livraison
     * 
     * @param controleur
     * @param fenetre
     * @param idIntersection
     */
    public void clicAjouterLivraison(Controleur controleur, Fenetre fenetre, int idIntersection);

    /**
     * Methode appelee par controleur lorsque l'utilisateur fait un clic gauche
     * sur Echanger après avoir selectionner une 1ere livraison.
     * 
     * @param controleur
     * @param fenetre
     * @param idLivraison
     */
    public void clicEchangerLivraisons(Controleur controleur, Fenetre fenetre, int idLivraison);

    /**
     * Methode appelee par controleur lorsque l'utilisateur clique sur le bouton
     * "annuler" dans l'etat ajout de livraison.
     * 
     * @param controleur
     */
    public void annulerAjout(Controleur controleur);

    /**
     * Methode appelee par controleur lorsque l'utilisateur survole le plan avec
     * la souris, vérifie si le point est une intersection et si ce n'est pas
     * déjà une livraison.
     * 
     * @param plan
     * @param point
     * @param tolerance
     */
    public void survolPlan(Plan plan, Fenetre fenetre, Point point, int tolerance);

    /**
     * Methode appelee par controleur afin d'informer la fenêtre sur la
     * possibilité ou non d'ajouter une livraison dans l'etat courant.
     * 
     * @param controleur
     * @param plan
     * @param fenetre
     */
    public boolean possibleAjoutLivraison(Controleur controleur, Plan plan, Fenetre fenetre);

    /**
     * Methode appelee par controleur quand ...
     * 
     * @param plan
     * @param fenetre
     * @param idLivraison
     */
    public void clicDroitLivraison(Plan plan, Fenetre fenetre, int idLivraison);

    /**
     * Methode appelee par controleur quand ...
     * 
     * @param fenetre
     * @param idIntersection
     */
    public void clicDroitIntersection(Fenetre fenetre, int idIntersection);

    /**
     * Methode appelee par controleur quand un clic gauche est effectué sur une
     * livraison
     * 
     * @param fenetre
     * @param idLivraison
     */
    public void clicGaucheLivraison(Controleur controleur, Fenetre fenetre, Plan plan, int idLivraison);

    /**
     * Methode appelée pour sauvegarder la feuille de route de la tournée au
     * format txt
     */
    public void genererFeuilleDeRoute(Plan plan);

    /**
     * Méthode appelée pour modifier la plage horaire de la livraison à
     * l'adresse donnée
     * 
     * @param adrLiv
     *            Adresse de la livraison à modifier
     * @param possedePlage
     *            True si la modification comporte une plage horaire, false
     *            sinon
     * @param heureDebut
     *            Heure de début de la plage
     * @param heureFin
     *            Heure de fin de la plage
     */
    public void modifierLivraison(Plan plan, Fenetre fenetre, ListeDeCdes listeDeCdes, int adrLiv, boolean possedePlage, String heureDebut, String heureFin);

}
