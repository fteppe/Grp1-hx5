package modele;

import java.util.HashMap;
import java.util.Observable;

public class DemandeDeLivraison extends Observable {

    private Heure heureDepart;
    private Intersection entrepot;
    private HashMap<Integer, Livraison> livraisons; // Liste des livraisons
						    // classees selon
						    // l'identifiant de leur
						    // adresse

    /**
     * Cree une demande de livraison a partir d'une heure de depart et d'un
     * entrepot
     * 
     * @param heureDepart
     *            Heure de depart de l'entrepot
     * @param entrepot
     *            Intersection correspondant a l'entrepot de la demande de
     *            livraison
     */
    public DemandeDeLivraison(Heure heureDepart, Intersection entrepot) {
	this.heureDepart = heureDepart;
	this.livraisons = new HashMap<Integer, Livraison>();
	this.entrepot = entrepot;
    }

    /**
     * Cree et ajoute une livraison possedant une plage horaire a la demande de
     * livraison courante
     * 
     * @param duree
     *            Duree de la livraison a ajouter
     * @param adresse
     *            Intersection correspondant a la livraison a ajouter
     * @param debutPlage
     *            Debut de la plage horaire de la livraison a ajouter
     * @param finPlage
     *            Fin de la plage horaire de la livraison a ajouter
     */
    public void ajouterLivraison(int duree, Intersection adresse, String debutPlage, String finPlage)
	    throws ModeleException {
	try {
	    Livraison nouvLivraison = new Livraison(duree, adresse, debutPlage, finPlage);
	    this.livraisons.put(adresse.getId(), nouvLivraison);
	} catch (ModeleException e) {
	    throw e;
	}
	// On indique au Controleur que la demande de livraison a ete
	// mise a jour
	setChanged();
	notifyObservers();
    }

    /**
     * Cree et ajoute une livraison a la demande de livraison courante
     * 
     * @param duree
     *            Duree de la livraison a ajouter
     * @param adresse
     *            Intersection correspondant a la livraison a ajouter
     */
    public void ajouterLivraison(int duree, Intersection adresse) {
	Livraison nouvLivraison = new Livraison(duree, adresse);
	this.livraisons.put(adresse.getId(), nouvLivraison);
	// On indique au Controleur que la demande de livraison a ete
	// mise a jour
	setChanged();
	notifyObservers(nouvLivraison);
    }

    public Livraison getLivraison(int adresse) {
	return this.livraisons.get(adresse);
    }

    public HashMap<Integer, Livraison> getListeLivraisons() {
	return this.livraisons;
    }

    public Intersection getEntrepot() {
	return this.entrepot;
    }

    public int getNbrLivraisons() {
	return this.livraisons.size();
    }

    public Heure getHeureDepart() {
	return this.heureDepart;
    }
}
