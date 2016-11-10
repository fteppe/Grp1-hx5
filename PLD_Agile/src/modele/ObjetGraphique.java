package modele;

import java.awt.Point;
import java.util.Observable;

public abstract class ObjetGraphique extends Observable {

    /**
     * Determine si le point p est contenu dans this
     * 
     * @param p
     *            Point aux coordonnées recherchées
     * @param tolerance
     *            Intervalle de tolérance dans lequel l'objet peut se trouver
     * @return True si p appartient a this, false sinon
     */
    protected abstract boolean contient(Point p, int tolerance);

}
