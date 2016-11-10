/*package modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class AlgoDijkstra {

	
	private static AlgoDijkstra instance = null;
	public static AlgoDijkstra getInstance() {
		if (instance == null)
			instance = new AlgoDijkstra();
		return instance;
	}

	private HashMap<Integer, Sommet> sommets;

	private HashMap<Integer, List<Troncon>> troncons;

	private HashMap<Integer, Intersection> intersections;

	private AlgoDijkstra() {
		sommets = new HashMap<Integer, Sommet>();
		troncons = new HashMap<Integer, List<Troncon>>();
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

	

	private void reinitialiserSommets() {
		for (Sommet som : sommets.values()) {
			som.cout = Integer.MAX_VALUE;
			som.antecedent = null;
			som.etat = Etat.BLANC;
		}
	}

	
}*/
