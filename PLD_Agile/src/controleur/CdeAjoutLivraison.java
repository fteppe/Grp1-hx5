package controleur;

import modele.Intersection;
import modele.Livraison;
import modele.Plan;

public class CdeAjoutLivraison implements Commande {

	private Plan plan;
	private int idIntersection;
	private Intersection intersection;
	private int idPrec;
	private int idSuiv;
	private String debutPlage;
	private String finPlage;
	private int duree;

	public CdeAjoutLivraison(Plan p, int idLivraison) {
		this.plan = p;
		this.idIntersection = idLivraison;
	}
	
	/**
	 * commande pour insérer une livraison sans plage horaire entre deux
	 * livraisons
	 * 
	 * @param p
	 *            le plan
	 * @param idIntersection
	 *            l'id de la livraison a ajouter
	 * @param idPrec
	 *            id de la livraison qui doit preceder l'intersection a ajouter
	 * @param idSuiv
	 *            id de la livraison qui doit suivre l'intersection a ajouter
	 * @param duree
	 *            duree que prend la livraison a ajouter
	 */
	public CdeAjoutLivraison(Plan p, int idIntersection, int idPrec, int idSuiv, int duree) {
		this.plan = p;
		this.idIntersection = idIntersection;
		this.idPrec = idPrec;
		this.idSuiv = idSuiv;
		this.duree = duree;
	}

	/**
	 * 
	 * @param p
	 *            le plan
	 * @param idIntersection
	 *            l'id de la livraison a ajouter
	 * @param idPrec
	 *            id de la livraison qui doit preceder l'intersection a ajouter
	 * @param idSuiv
	 *            id de la livraison qui doit suivre l'intersection a ajouter
	 * @param duree
	 *            duree que prend la livraison a ajouter
	 * @param debutPlage
	 *            debut de plage horaire permise de la livraison a ajouter
	 * @param finPlage
	 *            fin de plage horaire permise de la livraison a ajouter
	 */
	public CdeAjoutLivraison(Plan p, int idIntersection, int idPrec, int idSuiv, int duree, String debutPlage,
			String finPlage) {
		this.plan = p;
		this.idIntersection = idIntersection;
		this.debutPlage = debutPlage;
		this.finPlage = finPlage;
		this.idPrec = idPrec;
		this.idSuiv = idSuiv;
		this.duree = duree;
	}

	@Override
	public void doCde() {
		// Appel Ã  insererLivraisonTournee avec id des intersections
		// precedentes et suivantes
		plan.insererLivraisonTournee(idIntersection, duree, debutPlage, finPlage, idPrec, idSuiv);

	}

	@Override
	public void undoCde() {

		idPrec = plan.getAdresseLivraisonPrecedente(idIntersection);
		idSuiv = plan.getAdresseLivraisonSuivante(idIntersection);

		Livraison liv = plan.retirerLivraisonTournee(idIntersection);

		this.duree = liv.getDuree();
		if (liv.possedePlage()) {
			this.debutPlage = liv.getDebutPlage().toString();
			this.finPlage = liv.getFinPlage().toString();
		} else {
			this.debutPlage = null;
			this.finPlage = null;
		}

	}

}
