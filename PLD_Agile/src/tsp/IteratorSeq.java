package tsp;

import java.util.Collection;
import java.util.Iterator;

/**
 * Cette classe permet de gerer et de consulter une liste de sommets
 *
 */
public class IteratorSeq implements Iterator<Integer> {

	private Integer[] candidats;
	private int nbCandidats;

	/**
	 * Cree un iterateur pour iterer sur l'ensemble des sommets de nonVus
	 * 
	 * @param nonVus Tableau contenant les sommets non visites
	 * @param sommetCrt Sommet actuellement visite
	 */
	public IteratorSeq(Collection<Integer> nonVus, int sommetCrt) {
		this.candidats = new Integer[nonVus.size()];
		nbCandidats = 0;
		for (Integer s : nonVus) {
			candidats[nbCandidats++] = s;
		}
	}
	
	/**
	 * Renvoit un booleen indiquant si la liste etudiee possede encore des membres
	 */
	@Override
	public boolean hasNext() {
		return nbCandidats > 0;
	}

	/**
	 * Renvoit le prochain candidat de la liste etudiee
	 */
	@Override
	public Integer next() {
		return candidats[--nbCandidats];
	}

}
