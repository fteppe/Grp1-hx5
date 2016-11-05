package modele;

import java.awt.Point;
import java.util.Observable;

public abstract class ObjetGraphique extends Observable {
    
    /**
     * Determine si le point p est contenu dans this
     * 
     * @param p
     * @return true si p appartient a this, false sinon
     */
    public abstract boolean contient(Point p);

}
