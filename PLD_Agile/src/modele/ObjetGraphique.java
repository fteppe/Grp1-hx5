package modele;

import java.awt.Point;
import java.util.Observable;

/**
 * Cette classe permet une geston commune des differents
 * elements a afficher sur l'interface graphique.
 *
 */
public abstract class ObjetGraphique extends Observable {

    /**
     * Determine si le point p est contenu dans this
     * 
     * @param p
     * @return true si p appartient a this, false sinon
     */
    protected abstract boolean contient(Point p, int tolerance);

}
