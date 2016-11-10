package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class AlgoDijkstra {

	private static AlgoDijkstra instance = null;
	private HashMap<Integer, Sommet> sommets;
	private HashMap<Integer, List<Troncon>> troncons;
	private HashMap<Integer, Intersection> intersections;

	private AlgoDijkstra() {
		sommets = new HashMap<Integer, Sommet>();
		troncons = new HashMap<Integer, List<Troncon>>();
	}

	public static AlgoDijkstra getInstance() {
		if (instance == null)
			instance = new AlgoDijkstra();
		return instance;
	}

	public void chargerAlgo(HashMap<Integer, Intersection> intersections, HashMap<Integer, List<Troncon>> troncons) {
		sommets.clear();
		int position = 0;
		// On initialise l'ensemble des sommets a parcourir par l'algorithme,
		// correspondant a la liste des intersections du plan
		for (int id : intersections.keySet()) {
			Sommet nouveauSommet = new Sommet(id, position, Integer.MAX_VALUE, Etat.BLANC);
			sommets.put(id, nouveauSommet);
			position++;
		}
		// On initialise la liste de troncons
		this.intersections = intersections;
		this.troncons = troncons;
	}

	/**
	 * Calcule les plus courts chemins entre les sommets indiques selon
	 * l'algorithme de Dijkstra
	 * 
	 * @param idSommets
	 *            Identifiants des intersections depuis lesquels il faut
	 *            calculer les plus courts chemins
	 * @return Deux tableaux contenant l'ensemble des couts et des itineraires
	 *         optimaux resultant des calculs de plus court chemin effectues
	 */
	public Object[] calculerDijkstra(ArrayList<Integer> idSommets) {
		int nbrSommets = idSommets.size();
		int[][] couts = new int[nbrSommets][nbrSommets];
		@SuppressWarnings("unchecked")
		Itineraire[][] trajets = new Itineraire[nbrSommets][nbrSommets];
		int position = 0;
		// On calcule les plus courts chemins en utilisant l'algorithme de
		// Dijkstra avec chacune des intersections passees
		// en parametre comme point de depart
		for (Integer i : idSommets) {
			Object[] resultDijkstra = calculerDijkstra(i, idSommets);
			int[] cout = (int[]) resultDijkstra[0];
			Itineraire[] trajetsUnit = (Itineraire[]) resultDijkstra[1];
			// On complete les tableaux de cout et d'itineraires permettant de
			// de calculer la tournee
			couts[position] = cout;
			trajets[position] = trajetsUnit;
			position++;
		}
		return new Object[] { couts, trajets };
	}

	/**
	 * Calcule le plus court chemin a partir d'un sommet defini selon
	 * l'algorithme de Dijkstra
	 * 
	 * @param sourceId
	 *            Identifiant du sommet de depart
	 * @param idSommets
	 *            Identifiants des intersections vers lesquelles il faut trouver
	 *            les plus courts chemins
	 * @return Deux tableaux contenant l'ensemble des couts et des itineraires
	 *         optimaux resultant des calculs de plus court chemin partant d'un
	 *         unique sommet
	 */
	public Object[] calculerDijkstra(int sourceId, ArrayList<Integer> idSommets) {
		// On réinitialise les etats, antecedents et couts des sommets pour
		// recalculer
		reinitialiserSommets();

		int coutsSommets[] = new int[idSommets.size()];
		NavigableSet<Sommet> sommetsGris = new TreeSet<>();

		// On initialise la liste des sommets gris en y mettant le sommet source
		Sommet som = this.sommets.get(sourceId);
		som.etat = Etat.GRIS;
		som.cout = 0;
		sommetsGris.add(som);
		while (!sommetsGris.isEmpty()) {
			Sommet premierSommet = sommetsGris.first();
			if (this.troncons.get(premierSommet.getId()) != null) {
				for (Troncon t : this.troncons.get(premierSommet.getId())) {
					Sommet destination = sommets.get(t.getDestination().getId());
					Etat etat = destination.getEtat();
					if (etat != Etat.NOIR) {
						// On enleve du tas binaire le sommet pret a etre
						// relache
						// avant sa modification afin de ne pas dissocier
						// l'objet recupere
						// et celui contenu dans la liste des sommets gris
						sommetsGris.remove(destination);
						relacher(premierSommet, destination, t);
						if (etat == Etat.BLANC) {
							destination.setEtat(Etat.GRIS);
						}
						sommetsGris.add(destination);
					}
				}
			}
			sommetsGris.remove(premierSommet);
			premierSommet.setEtat(Etat.NOIR);
		}

		// On recupere seulement les couts des sommets devant etre
		// presents dans la tournee
		int position = 0;
		for (int id : idSommets) {
			coutsSommets[position] = sommets.get(id).getCout();
			position++;
		}

		// On transforme la liste des sommets avec leur antecedent en un tableau
		// d'itineraires
		Itineraire[] tableauPiTrie = convertirTableauItineraires(idSommets, sommets, sourceId);
		return new Object[] { coutsSommets, tableauPiTrie };
	}

	/**
	 * Relachement de l'arc (origine, destination)
	 * 
	 * @param origine
	 *            Sommet d'origine de l'arc
	 * @param destination
	 *            Sommet de destination de l'arc
	 * @param antecedent
	 *            Arc relache
	 */
	private void relacher(Sommet origine, Sommet destination, Troncon antecedent) {
		int nouveauCout = origine.getCout() + antecedent.getTpsParcours();
		if (destination.getCout() > nouveauCout) {
			destination.setCout(nouveauCout);
			destination.setAntecedent(antecedent);
		}
	}

	/**
	 * Mise en place de la liste des itineraires correspondant aux plus courts
	 * chemins calcules selon l'algorithme de Dijkstra a partir d'un unique
	 * sommet defini
	 * 
	 * @param idSommets
	 *            Identifiants des intersections constituant la tournee finale
	 * @param listeSommets
	 *            Liste des sommets parcourus par l'algorithme de Dijkstra
	 * @param sourceId
	 *            Identifiant du sommet de depart
	 * @return Tableau d'itineraires correpondant aux plus courts chemins entre
	 *         le sommet de depart et les sommets constituant la tournee finale
	 */
	private Itineraire[] convertirTableauItineraires(ArrayList<Integer> idSommets,
			HashMap<Integer, Sommet> listeSommets, int sourceId) {
		@SuppressWarnings("unchecked")
		int nbrSommets = idSommets.size();
		Itineraire[] trajetsUnit = new Itineraire[nbrSommets];
		int position = 0;
		// On cree les itineraires correspondant aux plus courts chemins
		// calcules a partir d'un unique sommet de depart, et possedant pour
		// arrivee un sommet composant la tournee finale
		int idSommetCourant;
		for (Integer id : idSommets) {
			List<Troncon> trajet = new ArrayList<Troncon>();
			Troncon antecedent = listeSommets.get(id).antecedent;
			idSommetCourant = id;
			while (antecedent != null) {
				trajet.add(0, antecedent);
				idSommetCourant = antecedent.getOrigine().getId();
				antecedent = listeSommets.get(antecedent.getOrigine().getId()).getAntecedent();
			}
			// Si il n'y a pas de chemin entre le sommet de depart et le sommet
			// courant,
			// on considere que l'itineraire correspondant est vide
			if (idSommetCourant != sourceId) {
				trajet.clear();
			}
			Itineraire iti = new Itineraire(this.intersections.get(sourceId), this.intersections.get(id), trajet);
			trajetsUnit[position] = iti;
			position++;
		}
		return trajetsUnit;
	}

	private void reinitialiserSommets() {
		for (Sommet som : sommets.values()) {
			som.cout = Integer.MAX_VALUE;
			som.antecedent = null;
			som.etat = Etat.BLANC;
		}
	}

	/**
	 * Enumeration servant au calcul de plus court chemin selon l'algorithme de
	 * Dijkstra
	 * 
	 * @author utilisateur
	 *
	 */
	private enum Etat {
		GRIS, NOIR, BLANC
	}

	private class Sommet implements Comparable<Sommet> {

		private int id;
		private int position;
		private int cout;
		private Etat etat;
		private Troncon antecedent;

		/**
		 * Cree un sommet a partir des informations de l'intersection
		 * correspondante
		 * 
		 * @param id
		 *            Id du sommet
		 * @param position
		 *            Position dans les tableaux de cout et d'itineraires
		 *            servant au calcul de la tournee finale
		 * @param cout
		 *            Cout intial du sommet
		 * @param etat
		 *            Etat initial du sommet (Gris, Noir ou Blanc)
		 */
		public Sommet(int id, int position, int cout, Etat etat) {
			this.id = id;
			this.position = position;
			this.cout = cout;
			this.etat = etat;
		}

		@Override
		public int compareTo(Sommet autre) {
			// TODO Auto-generated method stub
			int coutCompare = this.cout - autre.cout;
			if (coutCompare == 0) {
				coutCompare = this.id - autre.id;
			}
			return coutCompare;
		}

		public int getId() {
			return this.id;
		}

		public int getPosition() {
			return this.position;
		}

		public int getCout() {
			return this.cout;
		}

		public Etat getEtat() {
			return this.etat;
		}

		public Troncon getAntecedent() {
			return this.antecedent;
		}

		public void setEtat(Etat nouvelEtat) {
			this.etat = nouvelEtat;
		}

		public void setCout(int nouveauCout) {
			this.cout = nouveauCout;
		}

		public void setAntecedent(Troncon nouvelAntecedent) {
			this.antecedent = nouvelAntecedent;
		}

	}
}
