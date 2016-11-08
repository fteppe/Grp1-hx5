package modele;

import java.awt.Point;

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
    public Livraison(int duree, Intersection adresse, String debutPlage, String finPlage) {
	this.duree = duree;
	this.adresse = adresse;
	this.plage = null;
	if ((debutPlage != null && finPlage != null) && !(debutPlage.isEmpty() || finPlage.isEmpty()))
	    plage = new PlageHoraire(new Heure(debutPlage), new Heure(finPlage));
	respectePlage = true;
    }

    /**
     * Cree une livraison possedant une plage horaire a partir de sa duree et de
     * son adresse
     * 
     * @param duree
     *            Duree (en secondes) de la livraison
     * @param adresse
     *            Intersection correspondant a la livraison
     */
    public Livraison(int duree, Intersection adresse) {
	this.duree = duree;
	this.adresse = adresse;
    }

    @Override
    public boolean contient(Point p, int tolerance) {
	return adresse.contient(p, tolerance);
    }

    public int getDuree() {
	return this.duree;
    }

    public Heure getHeureDepart() {
	return this.heureDepart;
    }

    public Heure getHeureArrivee() {
	return this.heureArrivee;
    }

    public Heure getDebutPlage() {
	Heure heureDebutPlage = null;
	if (possedePlage()) {
	    heureDebutPlage = this.plage.getHeureDebut();
	}
	return heureDebutPlage;
    }

    public boolean possedePlage() {
	return this.plage != null;
    }

    public Heure getFinPlage() {
	Heure heureFinPlage = null;
	if (possedePlage()) {
	    heureFinPlage = this.plage.getHeureFin();
	}
	return heureFinPlage;
    }

    public boolean getRespectePlage() {
	return this.respectePlage;
    }

    public Heure getTpsAttente() {
	Heure tpsAttente = null;
	if (possedePlage()) {
	    tpsAttente = this.tpsAttente;
	}
	return tpsAttente;
    }

    public Intersection getAdresse() {
	return this.adresse;
    }

    public Heure setHeureArrivee(Heure heureArrivee) {
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

    public void setDuree(int duree) {
	this.duree = duree;
    }

}
