package tsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class TSPPlages {

	private Integer[] meilleureSolution;
	private int coutMeilleureSolution = 0;
	private Boolean calculEnCours = true;
	private static boolean locked = false;

	/**
	 * Verrouille l'acces aux tableaux presentant la meilleure tournee trouvee
	 */
	public void lock() {
		while (locked) {
		}
		locked = true;
	}

	/**
	 * Deverrouille l'acces aux tableaux presentant la meilleure tournee trouvee
	 */
	public void unlock() {
		locked = false;
	}

	/**
	 * Cherche un circuit de duree minimale passant par chaque sommet (compris
	 * entre 0 et nbSommets-1)
	 * 
	 * @param nbSommets
	 *            Nombre de sommets du graphe
	 * @param cout
	 *            cout[i][j] = Duree en secondes pour aller de i a j, avec 0 <= i <
	 *            nbSommets et 0 <= j < nbSommets
	 * @param duree
	 *            duree[i] = Duree en secondes pour visiter le sommet i, * avec
	 *            0 <= i < nbSommets
	 * @param horaireDebut
	 *            horaireDebut[i] = temps en seconde a partir duquel i peut etre
	 *            visité, avec 0 <= i < nbSommets , horaireDebut[i] = 0 : i
	 *            peut etre visite a partir de n'importe quelle heure
	 * @param horaireFin
	 *            horaireFin[i] = temps en seconde a partir duquel i doit avoir
	 *            ete visite, avec 0 <= i < nbSommets
	 * @param heureDepart
	 *            Heure de depart de l'entrepot en secondes
	 */
	public void chercheSolution(int nbSommets, int[][] cout, int[] duree, int[] horaireDebut, int[] horaireFin,
			int heureDepart) {
		coutMeilleureSolution = Integer.MAX_VALUE;
		meilleureSolution = new Integer[nbSommets];
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i = 1; i < nbSommets; i++)
			nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // Le premier sommet visite est l'entrepôt
		branchAndBound(0, nonVus, vus, 0, cout, duree, horaireDebut, horaireFin, heureDepart);
	}

	/**
	 * Renvoit l'indice appartenant a la meilleure tournee trouvee et present
	 * a la position indiquee
	 * 
	 * @param i Position du sommet recherche dans la tournée optimale
	 * @return Le sommet visite en i-eme position dans la solution calculee par
	 *         chercheSolution
	 */
	public Integer getMeilleureSolution(int i) {
		if ((meilleureSolution == null) || (i < 0) || (i >= meilleureSolution.length)) {
			return null;
		}
		return meilleureSolution[i];
	}

	/**
	 * Renvoit le cout de la meilleure solution trouvee jusqu'a present
	 * 
	 * @return La duree de la solution calculee par chercheSolution (en secondes)
	 */
	public int getCoutMeilleureSolution() {
		return coutMeilleureSolution;
	}

	/**
	 * Estime la cout d'une branche prete a etre exploree
	 * 
	 * @param sommetCourant
	 *            Sommet sur lequel le bound est effectue
	 * @param nonVus
	 *            Tableau des sommets restants a visiter
	 * @param cout
	 *            cout[i][j] = Duree en secondes pour aller de i a j, avec 0 <= i <
	 *            nbSommets et 0 <= j < nbSommets
	 * @param duree
	 *            duree[i] = Duree en secondes pour visiter le sommet i, avec 0
	 *            <= i < nbSommets
	 * @param coutActuel
	 *            Cout pour atteindre sommetCourant depuis l'origine
	 * @param horaireDebut
	 *            horaireDebut[i] = Heure en secondes a partir de laquelle i
	 *            peut etre livre, avec 0 <= i < nbSommets
	 * @param horaireFin
	 *            horaireFin[i] = Heure en secondes avant laquelle i doit être
	 *            livré, avec 0 <= i < nbSommets
	 * @param heureDepart
	 *            Heure de depart de l'entrepot en secondes
	 * @return Une borne inferieure du cout des permutations commencant par
	 *         sommetCourant, contenant chaque sommet de nonVus exactement une
	 *         fois et terminant par le sommet 0
	 */
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree, int coutActuel,
			int[] horaireDebut, int[] horaireFin, int heureDepart) {
		int bound = Integer.MAX_VALUE;
		for (Integer i : nonVus) {
			int coutVers = cout[sommetCourant][i] + duree[i];
			int attente = Math.max(0, horaireDebut[i] - (heureDepart + coutActuel) - cout[sommetCourant][i]);
			if (coutVers != Integer.MAX_VALUE) {
				// On prend en compte seulement les sommets pour lesquels
				// on peut respecter les plages horaires associees
				if ((coutActuel + heureDepart + coutVers + attente) < horaireFin[i]) {
					if (coutVers + attente < bound) {
						bound = coutVers + attente;
					}
				}
			}
		}
		return bound;
	}

	/**
	 * Classe les voisins du sommet explore de telle sorte a ce qu'une solution
	 * puisse etre trouvee rapidement
	 * 
	 * @param sommetCrt
	 *            Sommet sur lequel le branch va etre effectue
	 * @param nonVus
	 *            Tableau des sommets restants a visiter
	 * @param cout
	 *            cout[i][j] = Duree en secondes pour aller de i a j, avec 0 <= i <
	 *            nbSommets et 0 <= j < nbSommets
	 * @param duree
	 *            duree[i] = Duree en secondes pour visiter le sommet i, avec 0
	 *            <= i < nbSommets
	 * @param horaireDebut
	 *            horaireDebut[i] = Heure en secondes a partir de laquelle i
	 *            peut etre livre, avec 0 <= i < nbSommets
	 * @param horaireFin
	 *            horaireFin[i] = Heure en secondes jusqu'a laquelle i peut etre
	 *            livre, avec 0 <= i < nbSommets
	 * @return Un iterateur permettant d'iterer sur tous les sommets de nonVus
	 *         atteignables en respectant les plages horaires et triés par couts
	 *         decroissant
	 */
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree,
			int[] heureDebut, int[] heureFin, int coutVus, int heureDepart) {
		ArrayList<Integer[]> nonVusTri = new ArrayList<Integer[]>();
		ArrayList<Integer> nonVusOrdonnes = new ArrayList<Integer>();
		// On prend en compte seulement les sommets pour lesquels
		// on peut respecter les plages horaires associees
		for (int i = 0; i < nonVus.size(); i++) {
			Integer coutSommet = cout[sommetCrt][nonVus.get(i)] + duree[nonVus.get(i)];
			int attente = Math.max(0,
					heureDebut[nonVus.get(i)] - (heureDepart + coutVus) - cout[sommetCrt][nonVus.get(i)]);
			if (heureFin[nonVus.get(i)] >= coutVus + coutSommet + heureDepart + attente
					&& cout[sommetCrt][nonVus.get(i)] != Integer.MAX_VALUE) {
				Integer[] pair = new Integer[2];
				pair[0] = nonVus.get(i);
				pair[1] = coutSommet + attente;
				nonVusTri.add(pair);
			}
		}
		// On classe les sommets atteignables par cout decroissant
		nonVusTri.sort((Integer[] num1, Integer[] num2) -> {
			return num2[1].compareTo(num1[1]);
		});

		for (int i = 0; i < nonVusTri.size(); i++) {
			nonVusOrdonnes.add(nonVusTri.get(i)[0]);
		}
		return new IteratorSeq(nonVusOrdonnes, sommetCrt);
	}

	/**
	 * Methode definissant le patron (template) d'une resolution par separation
	 * et evaluation (branch and bound) du TSP
	 * 
	 * @param sommetCrt
	 *            Le dernier sommet visite
	 * @param nonVus
	 *            la liste des sommets qui n'ont pas encore ete visites
	 * @param vus
	 *            la liste des sommets visites (y compris sommetCrt)
	 * @param coutVus
	 *            La somme des couts des arcs du chemin passant par tous les
	 *            sommets de vus + la somme des duree des sommets de vus
	 * @param cout
	 *            cout[i][j] = duree pour aller de i a j, avec 0 <= i <
	 *            nbSommets et 0 <= j < nbSommets
	 * @param duree
	 *            duree[i] = duree pour visiter le sommet i, avec 0 <= i <
	 *            nbSommets
	 * @param tpsLimite
	 *            Limite de temps (en millisecondes) accorde pour la resolution
	 * @param horaireDebut
	 *            horaireDebut[i] = Heure en secondes a partir de laquelle i
	 *            peut être livre, avec 0 <= i < nbSommets
	 * @param horaireFin
	 *            horaireFin[i] = Heure en secondes avant laquelle i doit avoir
	 *            été livre, avec 0 <= i < nbSommets
	 * @param heureDepart
	 *            Heure de depart de l'entrepot en secondes
	 */
	void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int coutVus, int[][] cout,
			int[] duree, int[] horaireDebut, int[] horaireFin, int heureDepart) {
		if (!this.calculEnCours) {
			return;
		}
		if (coutVus >= coutMeilleureSolution) {
			return;
		}
		if (nonVus.size() == 0) { 
			coutVus += cout[sommetCrt][0];
			if (coutVus < coutMeilleureSolution) { 
				// On protege les tableaux d'un acces concurrent
				lock();
				vus.toArray(meilleureSolution);
				coutMeilleureSolution = coutVus;
				unlock();
			}
		} else {
			int nouveauCout = coutVus;
			int bound = bound(sommetCrt, nonVus, cout, duree, coutVus, horaireDebut, horaireFin, heureDepart);
			if (bound == Integer.MAX_VALUE) {
				nouveauCout = bound;
			} else {
				nouveauCout += bound;
			}
			if (nouveauCout < coutMeilleureSolution) {
				Iterator<Integer> it = iterator(sommetCrt, nonVus, cout, duree, horaireDebut, horaireFin, coutVus,
						heureDepart);
				while (it.hasNext()) {
					Integer prochainSommet = it.next();

					vus.add(prochainSommet);
					nonVus.remove(prochainSommet);

					int heurePassageProchain = heureDepart + cout[sommetCrt][prochainSommet] + coutVus;
					int coutProchain = coutVus + cout[sommetCrt][prochainSommet] + duree[prochainSommet]
							+ Math.max(0, horaireDebut[prochainSommet] - heurePassageProchain);
					branchAndBound(prochainSommet, nonVus, vus, coutProchain, cout, duree, horaireDebut, horaireFin,
							heureDepart);

					vus.remove(prochainSommet);
					nonVus.add(prochainSommet);
				}
			}
		}
	}

	/**
	 * Indique si le calcul doit s'arreter
	 * @param calculEnCours Booleen indiquant si le calcul doit s'arreter
	 */
	public void setCalculEnCours(boolean calculEnCours) {
		this.calculEnCours = calculEnCours;
	}
}
