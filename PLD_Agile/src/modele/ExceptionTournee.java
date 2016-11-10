package modele;

/**
 * Classe permettant la gestion des exceptions liees au lancement du calcul de
 * la tournee
 *
 */
public class ExceptionTournee extends Exception {
	public ExceptionTournee(String message) {
		super(message);
	}
}
