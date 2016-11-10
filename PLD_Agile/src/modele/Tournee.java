package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Cette classe permet la creation et la gestion de la tournee calculee
 * sur commande de l'utilisateur.
 *
 */
public class Tournee extends Observable {

	private int duree;
	private int adrEntrepot;
	private Heure hDebut;
	private Heure hFin;
	private boolean valide;
	private List<Itineraire> itineraires; //Liste d'itineraire ordonnes
						//selon l'ordre de passage
	private HashMap<Integer, Livraison> livraisons; //Liste de livraisons ordonnes
							//selon l'identifiant de leur adresse

	/**
	 * Cree une tournee a partir de son Heure de depart de l'entrepot
	 * 
<<<<<<< HEAD
	 * @param duree
	 *                Duree de la tournee (en secondes)
	 * @param entrepot
	 *                Intersection de depart et d'arrivee de la tournee
=======
	 * @param heureDepart Heure de depart de l'entrepot
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 */
	public Tournee(Heure heureDepart) {
		this.valide = true;
		this.hDebut = heureDepart;
		this.hFin = heureDepart;
		duree = Integer.MAX_VALUE;
		itineraires = new ArrayList<Itineraire>();
		livraisons = new HashMap<Integer, Livraison>();
	}

	/**
	 * Cree la Tournee suivant la liste des livraisons, l'entrepot et les
	 * Itineraires associes
	 * 
	 * @param duree
	 *                Duree totale de la tournee
	 * @param livraisons
<<<<<<< HEAD
	 *                Liste ordonnee des intersections a visiter
	 * @param itineraires
	 *                Tableau des itineraires pour aller de la livraison i a
	 *                la livraison j
=======
	 *            Liste ordonnee des Intersections a visiter
	 * @param itineraires
	 *            Tableau des Itineraires pour aller de la Livraison i a la
	 *            Livraison j
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 */
	protected void mettreAJourTournee(int duree, int[] livraisons, Itineraire[][] itineraires,
			HashMap<Integer, Livraison> livDemande, List<Integer> idSommets) {
		this.viderTournee();
		for (int i = 0; i < livraisons.length - 1; i++) {
			Livraison prochLivr = livDemande.get(idSommets.get(livraisons[i + 1]));
			Itineraire nouvItineraire = itineraires[livraisons[i]][livraisons[i + 1]];
			this.ajouterItineraire(nouvItineraire, prochLivr);
		}
		this.ajouterItineraire(itineraires[livraisons[livraisons.length - 1]][livraisons[0]], null);
		this.duree = duree;
		this.mettreAJourTempsParcours(this.hDebut);
		// setChanged();
		// notifyObservers();
		System.out.println("Tournée mise à jour");
	}

	/**
	 * Ajoute un itineraire à la liste d'itineraires de la tournee courante
	 * 
	 * @param itineraire
<<<<<<< HEAD
	 *                Itineraire a ajouter à la tournee
=======
	 *            Itineraire a ajouter à la Tournee
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 */
	protected void ajouterItineraire(Itineraire itineraire, Livraison prochLivr) {
		if (itineraires.size() == 0)
			adrEntrepot = itineraire.getDepart().getId();
		itineraires.add(itineraire);
		if (prochLivr != null)
			livraisons.put(prochLivr.getAdresse().getId(), prochLivr);
		setChanged();
		notifyObservers();
	}

	/**
	 * Insère une nouvelle Livraison et calcule les Itinéraires entre
	 * l'Intersection associée et les Intersections précédentes et suivantes
	 * 
	 * @param liv
<<<<<<< HEAD
	 *                La livraison à insérer dans la tournée
	 * @param adrPrec
	 *                L'adresse de la livraison précédente
	 * @param adrSuiv
	 *                L'adresse de la livraison suivante
=======
	 *            La Livraison à insérer dans la Tournée
	 * @param adrPrec
	 *            L'adresse de la Livraison précédente
	 * @param adrSuiv
	 *            L'adresse de la Livraison suivante
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 */
	protected void insererLivraison(Livraison liv, int adrPrec, int adrSuiv) {
		// On ajoute la livraison dans la liste
		livraisons.put(liv.getAdresse().getId(), liv);

		ArrayList<Integer> nvItineraires = new ArrayList<Integer>();
		nvItineraires.add(adrPrec);
		nvItineraires.add(liv.getAdresse().getId());
		nvItineraires.add(adrSuiv);
		AlgoDijkstra algo = AlgoDijkstra.getInstance();
		Object[] resultAlgo = algo.calculerDijkstra(nvItineraires);
		Itineraire[][] nvItin = (Itineraire[][]) resultAlgo[1];
		for (Itineraire itin : itineraires) {
			if (itin.getDepart().getId() == nvItineraires.get(0)
					&& itin.getArrivee().getId() == nvItineraires.get(2)) {
				itineraires.remove(itin);
				break;
			}
		}
		this.insererItineraire(nvItin[0][1]);
		this.insererItineraire(nvItin[1][2]);
		this.mettreAJourTempsParcours(this.hDebut);
	}

	/**
	 * Supprime l'Itineraire concernant la Livraison a l'adresse specifiee
	 * 
	 * @param adresse
<<<<<<< HEAD
	 *                Identifiant de l'intersection correspondante a la
	 *                livraison
	 * @return Tableau compose des identifiants de depart et d'arrivee du
	 *         nouvel Itineraire a construire
=======
	 *            Identifiant de l'Intersection correspondante a la Livraison
	 * @return Tableau compose des identifiants de depart et d'arrivee du nouvel
	 *         Itineraire a construire
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 */
	protected Livraison supprimerLivraison(int adresse) {
		// On parcourt la liste d'itineraires pour supprimer
		// les itineraires concernant la livraison
		ArrayList<Integer> nvItineraire = new ArrayList<Integer>();
		Iterator<Itineraire> iter = itineraires.iterator();
		while (iter.hasNext()) {
			Itineraire itin = iter.next();
			if (itin.getArrivee().getId() == adresse) {
				nvItineraire.add(itin.getDepart().getId());
				iter.remove();
			} else if (itin.getDepart().getId() == adresse) {
				nvItineraire.add(itin.getArrivee().getId());
				iter.remove();
				break;
			}
		}
		AlgoDijkstra algo = AlgoDijkstra.getInstance();
		Object[] resultAlgo = algo.calculerDijkstra(nvItineraire);
		Itineraire[][] nvItin = (Itineraire[][]) resultAlgo[1];
		this.insererItineraire(nvItin[0][1]);
		Livraison liv = livraisons.remove(adresse);
		this.mettreAJourTempsParcours(this.hDebut);

		return liv;
	}

	/**
<<<<<<< HEAD
	 * Insère l'itinéraire dans la liste des itinéraires de la tournée selon
	 * les intersections de départ et d'arrivée associées
=======
	 * Insère l'Itinéraire dans la liste des Itinéraires de la Tournée selon les
	 * Intersections de départ et d'arrivée associées
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 * 
	 * @param nvItineraire
	 *                Itineraire à insérer
	 */
	protected void insererItineraire(Itineraire nvItineraire) {
		int i = 0;
		if (nvItineraire.getDepart().getId() == adrEntrepot) {
			itineraires.add(0, nvItineraire);
		} else {
			for (; i < itineraires.size(); i++) {
				Itineraire itin = itineraires.get(i);
				if (itin.getArrivee() == nvItineraire.getDepart()) {
					itineraires.add(++i, nvItineraire);
					break;
				}
				// TODO test sur l'intersection d'arrivée
			}
		}
	}

	/**
	 * Met à jour les horaires de la Tournée et des Livraisons qui lui sont
	 * associée
	 * 
	 * @param heureDepartTournee
	 *                Horaire de départ du calcul
	 */
	private void mettreAJourTempsParcours(Heure heureDepartTournee) {
		Heure cur = heureDepartTournee;
		for (int i = 0; i < itineraires.size() - 1; i++) {
			Itineraire itin = itineraires.get(i);
			System.out.println(itin);
			cur = new Heure(cur.toSeconds() + itin.getTpsParcours());
			int adrLiv = itin.getArrivee().getId();
			Livraison liv = livraisons.get(adrLiv);
			cur = liv.setHeureArrivee(cur);
		}
		Itineraire itin = itineraires.get(itineraires.size() - 1);
		cur = new Heure(cur.toSeconds() + itin.getTpsParcours());
		hFin = cur;
		valide = true;
		for (Livraison liv : livraisons.values()) {
			if (!liv.getRespectePlage())
				valide = false;
		}
	}

	/**
	 * Réinitialise la liste des Itinéraires de la tournée
	 */
	protected void viderTournee() {
		this.itineraires.clear();
	}

	/**
	 * @return Retourne la liste ordonnée des Livraisons de la Tournee
	 */
	protected List<Livraison> getListeLivraisons() {
		ArrayList<Livraison> listLivraisons = new ArrayList<Livraison>();
		for (Itineraire itin : itineraires) {
			Livraison livraison = livraisons.get(itin.getDepart().getId());
			if (livraison != null)
				listLivraisons.add(livraison);
		}
		return listLivraisons;
	}

	/**
	 * Retourne la Livraison à l'adresse donnée
	 * 
	 * @param adresse
	 *                Id de l'intersection adresse
	 * @return Livraison si elle existe à cette adresse, null sinon
	 */
	protected Livraison getLivraison(int adresse) {
		return livraisons.get(adresse);
	}

	/**
	 * @return Liste des Itineraires de la Tournee
	 */
	protected List<Itineraire> getItineraires() {
		return itineraires;
	}

	/**
	 * @return Heure de début de la Tournee
	 */
	protected Heure getHeureDebut() {
		return hDebut;
	}

	/**
	 * @return Heure de fin de la Tournee
	 */
	protected Heure getHeureFin() {
		return (hFin != null ? hFin : new Heure());
	}

	/**
	 * @return True si la Tournee est possible, false sinon
	 */
	protected boolean getValidite() {
		return valide;
	}

	/**
	 * @return Duree de la Tournee
	 */
	protected int getDuree() {
		return duree;
	}

	/**
	 * Retourne l'adresse de la Livraison suivant la Livraison donnée
	 * 
	 * @param adrLiv
<<<<<<< HEAD
	 *                Adresse de la livraison dont on cherche la livraison
	 *                suivante
	 * @return Adresse de la livraison suivante ou -1 si l'adresse donnée
	 *         n'a pas de livraison ou de livraison suivante
=======
	 *            Adresse de la Livraison dont on cherche la Livraison suivante
	 * @return Adresse de la Livraison suivante ou -1 si l'adresse donnée n'a
	 *         pas de Livraison ou de Livraison suivante
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 */
	protected int getAdresseLivraisonSuivante(int adrLiv) {
		for (Itineraire itin : itineraires) {
			if (itin.getDepart().getId() == adrLiv)
				return itin.getArrivee().getId();
		}
		return -1;
	}

	/**
	 * Retourne l'adresse de la Livraison précédant la Livraison donnée
	 * 
	 * @param adrLiv
<<<<<<< HEAD
	 *                Adresse de la livraison dont on cherche la livraison
	 *                précédente
	 * @return Adresse de la livraison précédente ou -1 si l'adresse donnée
	 *         n'a pas de livraison ou de livraison suivante
=======
	 *            Adresse de la Livraison dont on cherche la Livraison
	 *            précédente
	 * @return Adresse de la Livraison précédente ou -1 si l'adresse donnée n'a
	 *         pas de Livraison ou de Livraison suivante
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 */
	protected int getAdresseLivraisonPrecedente(int adrLiv) {
		for (Itineraire itin : itineraires) {
			if (itin.getArrivee().getId() == adrLiv)
				return itin.getDepart().getId();
		}
		return -1;
	}

	/**
	 * Modifie la Livraison à l'adresse donnée avec les Plages horaires
	 * spécifiées
	 * 
	 * @param adrLivraison
<<<<<<< HEAD
	 *                Adresse de la livraison à modifier
	 * @param nvPlage
	 *                True si la livraison doit avoir une plage, false sinon
	 * @param nvDebut
	 *                Nouvelle heure de début de la plage
	 * @param nvFin
	 *                Nouvelle heure de la fin de la plage
=======
	 *            Adresse de la Livraison à modifier
	 * @param nvPlage
	 *            True si la Livraison doit avoir une plage, false sinon
	 * @param nvDebut
	 *            Nouvelle Heure de début de la plage
	 * @param nvFin
	 *            Nouvelle Heure de la fin de la plage
>>>>>>> 3b4f7db69ff5d6645cec21bb96e903b694d4d83d
	 */
	protected void modifierPlageLivraison(int adrLivraison, boolean nvPlage, Heure nvDebut, Heure nvFin) {
		Livraison liv = livraisons.remove(adrLivraison);
		if (nvPlage)
			liv.setPlage(new PlageHoraire(nvDebut, nvFin));
		else
			liv.supprimerPlage();
		livraisons.put(adrLivraison, liv);
		this.mettreAJourTempsParcours(hDebut);
	}

	/**
	 * @return Retourne la string formatée pour l'affichage sur la feuille
	 *         de route
	 */
	protected String genererFeuilleRoute() {
		String route;
		route = "Départ de l'intersection " + adrEntrepot + " à " + hDebut.afficherHoraire();
		route += "\r\n" + itineraires.get(0).afficherFeuilleRoute() + "\r\n";
		for (int i = 1; i < itineraires.size(); i++) {
			Itineraire itin = itineraires.get(i);
			route += "\r\n" + livraisons.get(itin.getDepart().getId()).afficherFeuilleRoute();
			route += "\r\n" + itin.afficherFeuilleRoute() + "\r\n";
		}
		route += "\r\n" + "Arrivée à l'entrepôt à " + adrEntrepot + " à " + hFin.afficherHoraire();
		return route;
	}
}
