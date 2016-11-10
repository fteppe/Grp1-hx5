package modele;

/**
 * Classe permettant la gestion des exceptions liees
 * a la mise en place des entites servant au calcul de tournee
 *
 */
public class ExceptionTournee extends Exception {
    public ExceptionTournee(String message) {
	super(message);
    }
}
