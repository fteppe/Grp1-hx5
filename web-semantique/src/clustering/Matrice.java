package clustering;

import java.util.HashMap;
import java.util.Map;

/**Map de double qui prendpour clé une pair de String qui sont des points dont ont connait la distance qui les sépare.
 * 
 * @author florent
 *
 */
public class Matrice {
	
	private Map<Pair, Double> distance;
	
	
	public Matrice(){
		distance = new HashMap<Pair, Double>();
	}
	

	public Double distance(String a, String b){
		Pair pair = new Pair(a,b);
		return distance.get(pair);
		
		
		
	}
	
	public void add(String a, String b, Double valeur){
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
