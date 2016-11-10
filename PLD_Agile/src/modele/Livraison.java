package modele;

import java.awt.Point;

/**
 * Cette classe permet de creer et de gerer les livraisons a effectuer.
 *
 */
public class Livraison extends ObjetGraphique {

    private int duree;
    private Heure heureDepart;
    private Heure heureArrivee;
    private Heure tpsAttente;
    private Intersection adresse;
    private PlageHoraire plage;
    private boolean respectePlage;

    /**
     * Cree une livraison possedant une plage horaire a partir de sa duree et de
     * son adresse
     * 
     * @param duree
     *            Duree (en secondes) de la livraison
     * @param adresse
     *            Intersection correspondant a la livraison
     * @param debutPlage
     *            Heure de debut de la plage horaire
     * @param finPlage
     *            Heure de fin de la plage horaire
     */
    public Livraison(int duree, Intersection adresse, String debutPlage, String finPlage) throws ModeleException {
	this.duree = duree;
	this.adresse = adresse;
	this.plage = null;
	this.tpsAttente = null;
	if ((debutPlage != null && finPlage != null) && !(debutPlage.isEmpty() || finPlage.isEmpty()))
	    plage = new PlageHoraire(new Heure(debutPlage), new Heure(finPlage));

	respectePlage = true;
    }

    /**
     * Cree une livraison ne possedant pas de plage horaire a partir de sa duree
     * et de son adresse
     * 
     * @param duree
     *            Duree (en secondes) de la livraison
     * @param adresse
     *            Intersection correspondant a la livraison
     */
    public Livraison(int duree, Intersection adresse) {
	this.duree = duree;
	this.adresse = adresse;
	respectePlage = true;
    }

    /**
     * Indique si la livraison courante possede le point indique, avec une
     * tolerance precisee.
     */
    @Override
    protected boolean contient(Point p, int tolerance) {
	return adresse.contient(p, tolerance);
    }

    /**
     * @return Duree de la Livraison
     */
    public int getDuree() {
	return this.duree;
    }

    /**
     * @return Heure de départ une une fois que la livraison est réalisée
     */
    public Heure getHeureDepart() {
	return this.heureDepart;
    }

    /**
     * @return Heure d'arrivée sur le point de Livraison
     */
    public Heure getHeureArrivee() {
	return this.heureArrivee;
    }

    /**
     * @return Heure de début de la plage horaire de Livraison
     */
    public Heure getDebutPlage() {
	Heure heureDebutPlage = null;
	if (possedePlage()) {
	    heureDebutPlage = this.plage.getHeureDebut();
	}
	return heureDebutPlage;
    }

    /**
     * @return True si la Livraison possède une plage, false sinon
     */
    public boolean possedePlage() {
	return this.plage != null;
    }

    /**
     * @return Heure de fin de la plage horaire de Livraison
     */
    public Heure getFinPlage() {
	Heure heureFinPlage = null;
	if (possedePlage()) {
	    heureFinPlage = this.plage.getHeureFin();
	}
	return heureFinPlage;
    }

    /**
     * @return True si la plage horaire de Livraison est respectée, false sinon
     */
    public boolean getRespectePlage() {
	return this.respectePlage;
    }

    /**
     * @return Temps d'attente au point de Livraison avant ouverture de la plage
     *         horaire
     */
    public Heure getTpsAttente() {
	Heure tpsAttente = null;
	if (possedePlage()) {
	    tpsAttente = this.tpsAttente;
	}
	return tpsAttente;
    }

    /**
     * @return Intersection adresse de la Livraison
     */
    public Intersection getAdresse() {
	return this.adresse;
    }

    /**
     * Met a jour l'heure d'arrivee de la livraison courante suivant l'heure de
     * depart indiquee
     * 
     * @param heureArrivee
     *            Heure d'arrivee du livreur sur le point de livraison
     * @return Heure de depart prevue du livreur
     */
    protected Heure setHeureArrivee(Heure heureArrivee) {
	this.heureArrivee = heureArrivee;
	if (this.plage != null) {
	    if (heureArrivee.toSeconds() < this.plage.getHeureDebut().toSeconds()) {
		this.heureDepart = new Heure(this.plage.getHeureDebut().toSeconds() + this.duree);
		tpsAttente = new Heure(this.plage.getHeureDebut().toSeconds() - heureArrivee.toSeconds());
	    } else {
		this.heureDepart = new Heure(heureArrivee.toSeconds() + this.duree);
		tpsAttente = new Heure(0);
	    }
	    if (this.heureDepart.toSeconds() > this.plage.getHeureFin().toSeconds()) {
		this.respectePlage = false;
	    } else {
		this.respectePlage = true;
	    }
	} else {
	    this.heureDepart = new Heure(heureArrivee.toSeconds() + this.duree);
	    tpsAttente = new Heure(0);
	}
	return this.heureDepart;
    }

    /**
     * Modifie la plage horaire de la Livraison
     * 
     * @param plage
     *            Nouvelle plage horaire
     */
    protected void setPlage(PlageHoraire plage) {
	this.plage = plage;
    }

    /**
     * Supprime la plage horaire existante
     */
    protected void supprimerPlage() {
	this.plage = null;
	this.respectePlage = true;
    }

    /**
     * @return Retourne la string formatée pour l'affichage sur la feuille de
     *         route.
     */
    protected String afficherFeuilleRoute() {
	String affichage = "Arrivé en " + adresse.getId() + " à " + heureArrivee.afficherHoraire() + ".";
	if (this.possedePlage()) {
	    if (tpsAttente != null && !tpsAttente.equals(new Heure(0))) {
		affichage += "Attendre " + tpsAttente.afficherHoraire() + " pour effectuer la livraison à "
			+ plage.getHeureDebut().afficherHoraire() + ".";
	    }
	}
	affichage += "La livraison dure " + duree + ". Repartir à " + getHeureDepart().afficherHoraire() + ".";
	return affichage;
    }
}
