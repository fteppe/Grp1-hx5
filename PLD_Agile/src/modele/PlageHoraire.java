package modele;

/**
 * Cette classe permet de creer et de gerer les plages horaires
 * associees aux differents livraisons.
 *
 */
public class PlageHoraire {

	private Heure heureDebut;
	private Heure heureFin;

        /**
         * Construit un objet PlageHoraire a partir de son heure de debut
         * et de son heure de fin
         * @param heureDebut Heure de debut de la plage horaire
         * @param heureFin Heure de fin de la plage horaire
         */
	public PlageHoraire(Heure heureDebut, Heure heureFin) {
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
	}
	
	/**
	 * @return Heure de debut de la Plage horaire courante
	 */
	public Heure getHeureDebut() {
		return heureDebut;
	}

	/**
	 * @return Heure de fin de la Plage horaire courante
	 */
	public Heure getHeureFin() {
		return heureFin;
	}
}
