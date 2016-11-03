package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Tournee extends Observable {
    
    private int duree;

    private List<Itineraire> itineraires;
    
    /**
     * Cree une tournee à partir de sa duree
     * @param duree Duree de la tournee (en secondes)
     */
    public Tournee() {
	duree = Integer.MAX_VALUE;
	itineraires = new ArrayList<Itineraire>();
    }
    
    /**
     * Ajoute un itineraire à la liste d'itineraires de la tournee courante
     * @param itineraire Itineraire a ajouter à la tournee
     */
    public void ajouterItineraire(Itineraire itineraire) {
	itineraires.add(itineraire);
	setChanged();
	notifyObservers();
    }
    
    public List<Itineraire> getItineraires() {
	return itineraires;
    }    

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
}
