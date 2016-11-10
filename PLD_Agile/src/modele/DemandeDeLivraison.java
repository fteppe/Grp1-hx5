package modele;

import java.util.HashMap;
import java.util.Observable;

/**
 * Classe gérant l'entrepot et les livraisons liees a meme demande de 
 * livraison
 *
 */
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
     * Cree et ajoute une livraison sans plage horaire
     * a la demande de livraison courante
     * 
     * @param duree
     *            Duree de la livraison a ajouter (en secondes)
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

    /**
     * Cree et ajoute une livraison possedant une plage horaire a la demande de
     * livraison courante
     * 
     * @param duree
     *            Duree de la livraison a ajouter (en secondes)
     * @param adresse
     *            Intersection correspondant a la livraison a ajouter
     * @param debutPlage
     *            Debut de la plage horaire de la livraison a ajouter
     * @param finPlage
     *            Fin de la plage horaire de la livraison a ajouter
     * @throws ModeleException
     */
    protected void ajouterLivraison(int duree, Intersection adresse,
	    String debutPlage, String finPlage) throws ModeleException {
	try {
	    Livraison nouvLivraison = new Livraison(duree, adresse, debutPlage,
		    finPlage);
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
     * @return Intersection adresse de l'entrepôt
     */
    protected Intersection getEntrepot() {
	return this.entrepot;
    }

    protected HashMap<Integer, Livraison> getHashMapLivraisons() {
	return this.livraisons;
    }

    /**
     * @return Heure de départ demandée
     */
    public Heure getHeureDepart() {
	return this.heureDepart;
    }
    
    /**
     * Retourne la Livraison de la Demande associée à l'adresse donnée
     * @param adresse Adresse de la livraison
     * @return La Livraison si elle existe, null sinon
     */
    protected Livraison getLivraison(int adresse) {
	return this.livraisons.get(adresse);
    }
}
