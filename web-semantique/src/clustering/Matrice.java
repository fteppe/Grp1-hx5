package clustering;

import java.util.HashMap;
import java.util.Map;


public class Matrice {
	
	private Map<Pair, Float> distance;
	
	public Matrice(int n, int m){
		distance = new HashMap();
	}
	
	public float get(String a, String b){
		Pair pair = new Pair(a,b);
		return distance.get(pair);
		
	}
	
	public void set(String a, String b, Float valeur){
		Pair pair = new Pair(a,b);
		distance.put(pair, valeur);
		
	}
	
	/**Cette fonction permet de fusionner deux elements de la matrice et de mettreà jour les valeurs
	 * 
	 * @param i
	 * @param j
	 */
	public void fusionElem(int i, int j){
		
	}
}
