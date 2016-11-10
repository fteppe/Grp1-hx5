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
     * Cree une Livraison possedant une Plage horaire a partir de sa duree et de
     * son adresse
     * 
     * @param duree
     *            Duree (en secondes) de la Livraison
     * @param adresse
     *            Intersection correspondant a la Livraison
     * @param debutPlage
     *            Heure de debut de la Plage horaire
     * @param finPlage
     *            Heure de fin de la Plage horaire
     */
    public Livraison(int duree, Intersection adresse, String debutPlage,
	    String finPlage) throws ModeleException {
	this.duree = duree;
	this.adresse = adresse;
	this.plage = null;
	this.tpsAttente = null;
	if ((debutPlage != null && finPlage != null)
		&& !(debutPlage.isEmpty() || finPlage.isEmpty()))
	    plage = new PlageHoraire(new Heure(debutPlage),
		    new Heure(finPlage));

	respectePlage = true;
    }

    /**
     * Cree une Livraison ne possedant pas de Plage horaire a partir 
     * de sa duree et de son adresse
     * 
     * @param duree
     *            Duree (en secondes) de la Livraison
     * @param adresse
     *            Intersection correspondant a la Livraison
     */
    public Livraison(int duree, Intersection adresse) {
	this.duree = duree;
	this.adresse = adresse;
	respectePlage = true;
    }

    /**
     * Indique si la Livraison courante possede le point indique,
     * avec une tolerance precisee.
     */
    @Override
    public boolean contient(Point p, int tolerance) {
	return adresse.contient(p, tolerance);
    }

    /**
     * @return La duree (en secondes) de la Livraison courante
     */
    public int getDuree() {
	return this.duree;
    }

    /**
     * @return Heure d'arrivee prevue du livreur 
     * sur la Livraison courante 
     */
    public Heure getHeureArrivee() {
	return this.heureArrivee;
    }
    
    /**
     * @return Heure de depart prevue du livreur
     * de la Livraison courante 
     */
    public Heure getHeureDepart() {
	return this.heureDepart;
    }

    /**
     * @return Heure de debut de la Plage horaire de la Livraison
     * 		courante si elle existe, null sinon
     */
    public Heure getDebutPlage() {
	Heure heureDebutPlage = null;
	if (possedePlage()) {
	    heureDebutPlage = this.plage.getHeureDebut();
	}
	return heureDebutPlage;
    }

    /**
     * @return True si la Livraison courante possede une plage horaire,
     * false sinon
     */
    public boolean possedePlage() {
	return this.plage != null;
    }

    /**
     * @return Heure de fin de la Plage horaire de la Livraison
     * courante si elle existe, null sinon
     */
    public Heure getFinPlage() {
	Heure heureFinPlage = null;
	if (possedePlage()) {
	    heureFinPlage = this.plage.getHeureFin();
	}
	return heureFinPlage;
    }

    /**
     * @return True si l'Heure de depart de la Livraison courante respecte sa Plage horaire,
     * false sinon
     */
    public boolean getRespectePlage() {
	return this.respectePlage;
    }

    /**
     * @return Heure correspondant au temps d'attente du livreur sur la Livraison
     * courante si elle existe, null sinon
     */
    public Heure getTpsAttente() {
	Heure tpsAttente = null;
	if (possedePlage()) {
	    tpsAttente = this.tpsAttente;
	}
	return tpsAttente;
    }

    /**
     * @return Intersection correspondant a la Livraison courante
     */
    public Intersection getAdresse() {
	return this.adresse;
    }

    /**
     * Met a jour l'Heure d'arrivee de la Livraison courante
     * suivant l'Heure de depart indiquee
     * @param heureArrivee Heure d'arrivee du livreur 
     * 				sur le point de Livraison
     * @return Heure de depart prevue du livreur 
     */
    public Heure setHeureArrivee(Heure heureArrivee) {
	this.heureArrivee = heureArrivee;
	if (this.plage != null) {
	    if (heureArrivee.toSeconds() < this.plage.getHeureDebut()
		    .toSeconds()) {
		this.heureDepart = new Heure(
			this.plage.getHeureDebut().toSeconds() + this.duree);
		tpsAttente = new Heure(this.plage.getHeureDebut().toSeconds()
			- heureArrivee.toSeconds());
	    } else {
		this.heureDepart = new Heure(
			heureArrivee.toSeconds() + this.duree);
		tpsAttente = new Heure(0);
	    }
	    if (this.heureDepart.toSeconds() > this.plage.getHeureFin()
		    .toSeconds()) {
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
     * Met a jour la duree de la Livraison courante
     * @param duree Duree a appliquer (en secondes)
     */
    public void setDuree(int duree) {
	this.duree = duree;
    }

    /**
     * Met à jour la Plage horaire de la Livraison courante
     * @param plage Plage horaire a appliquer
     */
    protected void setPlage(PlageHoraire plage) {
	this.plage = plage;
    }

    /**
     * Supprime la Plage horaire de la Livraison courante
     * si elle existe
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
	String affichage = "Arrivé en " + adresse.getId() + " à "
		+ heureArrivee.afficherHoraire() + ".";
	if (this.possedePlage()) {
	    if (tpsAttente != null && !tpsAttente.equals(new Heure(0))) {
		affichage += "Attendre " + tpsAttente.afficherHoraire()
			+ " pour effectuer la livraison à "
			+ plage.getHeureDebut().afficherHoraire() + ".";
	    }
	}
	affichage += "La livraison dure " + duree + ". Repartir à "
		+ getHeureDepart().afficherHoraire() + ".";
	return affichage;
    }
}
