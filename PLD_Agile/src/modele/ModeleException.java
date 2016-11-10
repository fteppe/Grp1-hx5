package modele;

/**
 * Cette classe permet la gestion des exceptions liees
 * a la mise en place des entites servant au calcul de tournee
 *
 */
public class ModeleException extends Exception {

    public ModeleException(String message) {
	super(message);
    }
}
