package modele;

public class PlageHoraire {

    private Heure heureDebut;
    private Heure heureFin;

    public PlageHoraire(Heure heureDebut, Heure heureFin) {
	this.heureDebut = heureDebut;
	this.heureFin = heureFin;
    }

    /**
     * @return Heure de début de la plage
     */
    protected Heure getHeureDebut() {
	return heureDebut;
    }

    /**
     * @return Heure de fin de la plage
     */
    protected Heure getHeureFin() {
	return heureFin;
    }
}
