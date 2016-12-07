package clustering;

import java.util.ArrayList;
import java.util.List;

/**
 *  A implementer avec une structure en arbre.
 * @author florent
 *
 */
public class Cluster {
	
	private List<Cluster> subCluster;
	private String feuille;
	private boolean isLeaf;
	
	public Cluster(){
		subCluster = new ArrayList<Cluster>();
		isLeaf = false;
	}
	
	public Cluster(String feuille){
		this();
		isLeaf = true;
		this.feuille = feuille;
		
	}
	
	public Cluster(Cluster cluster1, Cluster cluster2){
		this();
		isLeaf = false;
		
		subCluster.add(cluster1);
		subCluster.add(cluster2);
	}
	
	public List<Cluster> getSubClusters(){
		return subCluster;
	}
	
	public void add(Cluster element){
		subCluster.add(element);
	}
	
	public ArrayList<String> getSujets(){
		
		ArrayList<String> sujets = new ArrayList<String>();
		if(isLeaf){
			sujets.add(feuille);
		}
		else
		{
			for(Cluster i : subCluster){
				sujets.addAll(i.getSujets());
			}
		}
		return sujets;
	}
	
	public int size(){
		int taille = 0;
		if(isLeaf){
			taille = 1;
		}
		else{
			for(Cluster c: subCluster){
				taille += c.size();
			}
		}
		return taille;
	}
	
	/**Renvoit le centre d'un cluster pour une certaine matrice de proximité
	 * 
	 * @param matrice la matrice de proximité utilisée pour déterminer le centre d'un cluster.
	 * @return
	 */
	public String getSujetCenter(Matrice matrice){
		return null;
	}
	
	/**
	 * 
	 * @param cluster le cluster dont on veut la distance.
	 * @param matrice la matri de proximité
	 * @return la distance entre deux clusters est le minimum de la distance.
	 */
	public double distance(Cluster cluster, Matrice matrice){
		Double distance = Double.MAX_VALUE;
		for(String i : getSujets()){
			for(String j : cluster.getSujets()){
				Double val = matrice.distance(i,j);
				if(val < distance && val != null){
					distance = val;
				}
			}
		}
		return distance;
	}
	
	public String toJSONstring(){
		String retour = "{\n";
		if(isLeaf){
			retour+="'sujet':'"+feuille+"'\n";
		}
		else{
			retour+="'subClusters': [\n";
			for(Cluster c : subCluster){
				retour+=c.toJSONstring()+",";
			}
			retour=retour.substring(0, retour.length()-1);
			retour+="]\n";
		}
		retour+="}\n";
		return retour;
	}
}
