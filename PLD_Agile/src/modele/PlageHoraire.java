package modele;

public class PlageHoraire {

	private Heure heureDebut;
	private Heure heureFin;

	public PlageHoraire(Heure heureDebut, Heure heureFin) {
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
	}

	public Heure getHeureDebut() {
		return heureDebut;
	}

	public Heure getHeureFin() {
		return heureFin;
	}
}
