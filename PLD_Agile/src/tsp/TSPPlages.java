package tsp;

import java.util.ArrayList;
import java.util.Iterator;

public class TSPPlages {

	private Integer[] meilleureSolution;
	private int coutMeilleureSolution = 0;
	private Boolean tempsLimiteAtteint;

	private boolean lock = false;
	
	public void lock(){
		while(lock){}
		lock = true;
	}
	
	public void unlock(){
		lock = false;
	}
	
	
	/**
	 * @return true si chercheSolution() s'est terminee parce que la limite de temps avait ete atteinte, avant d'avoir pu explorer tout l'espace de recherche,
	 */
	public Boolean getTempsLimiteAtteint(){
		return tempsLimiteAtteint;
	}
	
	/**
	 * Cherche un circuit de duree minimale passant par chaque sommet (compris entre 0 et nbSommets-1)
	 * @param tpsLimite : limite (en millisecondes) sur le temps d'execution de chercheSolution
	 * @param nbSommets : nombre de sommets du graphe
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @param horaireDebut : horaireDebut[i] = temps en seconde a partir duquel i peut être visité, avec 0 <= i < nbSommets , horaireDebut[i] = 0 : i n'a pas de limite pour être visité
	 * @param horaireFin : horaireFin[i] = temps en seconde a partir duquel i doit avoir été visité, avec 0 <= i < nbSommets
	 */
	public void chercheSolution(int tpsLimite, int nbSommets, int[][] cout, int[] duree , int[] horaireDebut, int[] horaireFin){
		tempsLimiteAtteint = false;
		coutMeilleureSolution = Integer.MAX_VALUE;
		meilleureSolution = new Integer[nbSommets];
		ArrayList<Integer> nonVus = new ArrayList<Integer>();
		for (int i=1; i<nbSommets; i++) nonVus.add(i);
		ArrayList<Integer> vus = new ArrayList<Integer>(nbSommets);
		vus.add(0); // le premier sommet visite est 0
		branchAndBound(0, nonVus, vus, 0, cout, duree, System.currentTimeMillis(), tpsLimite,horaireDebut,horaireFin);
	}
	
	/**
	 * @param i
	 * @return le sommet visite en i-eme position dans la solution calculee par chercheSolution
	 */
	public Integer getMeilleureSolution(int i){
		if ((meilleureSolution == null) || (i<0) || (i>=meilleureSolution.length))
			return null;
		return meilleureSolution[i];
	}
	
	/** 
	 * @return la duree de la solution calculee par chercheSolution
	 */
	public int getCoutMeilleureSolution(){
		return coutMeilleureSolution;
	}
	

	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCourant
	 * @param nonVus : tableau des sommets restant a visiter
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @param coutActuel : cout pour atteindre sommetCourant depuis l'origine
	 * @param horaireFin : horaireFin[i] = temps en seconde a partir duquel i doit avoir été visité, avec 0 <= i < nbSommets
	 * @return une borne inferieure du cout des permutations commencant par sommetCourant, 
	 * contenant chaque sommet de nonVus exactement une fois et terminant par le sommet 0
	 */
	protected int bound(Integer sommetCourant, ArrayList<Integer> nonVus, int[][] cout, int[] duree, int coutActuel, int[] horaireFin) {
		int bound = Integer.MAX_VALUE;
		for(Integer i : nonVus){
			
			int coutVers = cout[sommetCourant][i];
			if(coutVers != Integer.MAX_VALUE){
				coutVers += duree[i]; 
				if(coutVers + coutActuel < horaireFin[i]){
					if(coutVers < bound){
						bound = coutVers;
					}
				}
				else{
					return Integer.MAX_VALUE;
				}
			}
		}
		return bound;
	}
	
	
	/**
	 * Methode devant etre redefinie par les sous-classes de TemplateTSP
	 * @param sommetCrt
	 * @param nonVus : tableau des sommets restant a visiter
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @param horaireDebut : horaireDebut[i] = temps en seconde a partir duquel i peut être visité, avec 0 <= i < nbSommets , horaireDebut[i] = 0 : i n'a pas de limite pour être visité
	 * @return un iterateur permettant d'iterer sur tous les sommets de nonVus
	 */
	protected Iterator<Integer> iterator(Integer sommetCrt, ArrayList<Integer> nonVus, int[][] cout, int[] duree, int[] heureDebut) {
		ArrayList<Integer> nonVusOrdonnes = new ArrayList<Integer>();
		nonVusOrdonnes.add(nonVus.get(0));
		for(int i = 1 ; i < nonVus.size() ; i++){
			boolean added = false;
			int j = 0;
			while(!added && j < nonVusOrdonnes.size()){
				if(heureDebut[nonVus.get(i)]<heureDebut[nonVusOrdonnes.get(j)]){
					nonVusOrdonnes.add(j,nonVus.get(i));
					added = true;
				}
				else{
					j++;
				}
			}
			if(!added){
				nonVusOrdonnes.add(i);
			}
		}
		return new IteratorSeq(nonVus, sommetCrt);
	}
	
	/**
	 * Methode definissant le patron (template) d'une resolution par separation et evaluation (branch and bound) du TSP
	 * @param sommetCrt le dernier sommet visite
	 * @param nonVus la liste des sommets qui n'ont pas encore ete visites
	 * @param vus la liste des sommets visites (y compris sommetCrt)
	 * @param coutVus la somme des couts des arcs du chemin passant par tous les sommets de vus + la somme des duree des sommets de vus
	 * @param cout : cout[i][j] = duree pour aller de i a j, avec 0 <= i < nbSommets et 0 <= j < nbSommets
	 * @param duree : duree[i] = duree pour visiter le sommet i, avec 0 <= i < nbSommets
	 * @param tpsDebut : moment ou la resolution a commence
	 * @param tpsLimite : limite de temps pour la resolution
	 * @param horaireDebut : horaireDebut[i] = temps en seconde a partir duquel i peut être visité, avec 0 <= i < nbSommets , horaireDebut[i] = 0 : i n'a pas de limite pour être visité
	 * @param horaireFin : horaireFin[i] = temps en seconde a partir duquel i doit avoir été visité, avec 0 <= i < nbSommets
	 */	
	 void branchAndBound(int sommetCrt, ArrayList<Integer> nonVus, ArrayList<Integer> vus, int coutVus, int[][] cout, int[] duree, long tpsDebut, int tpsLimite, int[] horaireDebut, int[] horaireFin){
		 if (System.currentTimeMillis() - tpsDebut > tpsLimite){
			 tempsLimiteAtteint = true;
			 return;
		 }
	    if (nonVus.size() == 0){ // tous les sommets ont ete visites
	    	coutVus += cout[sommetCrt][0];
	    	if (coutVus < coutMeilleureSolution){ // on a trouve une solution meilleure que meilleureSolution
	    		lock();
	    		vus.toArray(meilleureSolution);
	    		coutMeilleureSolution = coutVus;
	    		unlock();
	    	}
	    } else {
	    	int nouveauCout = coutVus;
	    	int bound = bound(sommetCrt, nonVus, cout, duree, coutVus, horaireFin);
	    	
	    	if(bound == Integer.MAX_VALUE)
	    		nouveauCout = bound;
	    	else
	    		nouveauCout += bound;
	    	
	    	if ( nouveauCout < coutMeilleureSolution){
	    		Iterator<Integer> it = iterator(sommetCrt, nonVus, cout, duree, horaireDebut);
	    		while (it.hasNext()){
	    			Integer prochainSommet = it.next();
	    			vus.add(prochainSommet);
	    			nonVus.remove(prochainSommet);
	    			branchAndBound(prochainSommet, nonVus, vus, coutVus + cout[sommetCrt][prochainSommet] + duree[prochainSommet], cout, duree, tpsDebut, tpsLimite, horaireDebut, horaireFin);
	    			vus.remove(prochainSommet);
	    			nonVus.add(prochainSommet);
	    		}	    
	    	}
	    }
	}
}
