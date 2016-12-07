package clustering;

import java.util.ArrayList;
import java.util.List;

public class Analyse {
	private Matrice matriceProximite;
	private int nbClusterFinal;
	private List<Cluster> listeCluster;
	
	public Analyse(int nbClusterFinal){
		matriceProximite = new Matrice();
		listeCluster = new ArrayList<Cluster>();
	}
	
	public void ajouterDistance(String a, String b, double distance){
		matriceProximite.add(a, b, distance);
	}
	
	public void addCluster(Cluster cluster){
		listeCluster.add(cluster);
	}
	
	public void setMatrice(Matrice mat){
		matriceProximite = mat;
	}
	
	public Cluster popCluster(Cluster cluster){
		for(Cluster i : listeCluster){
			if(i.equals(cluster)){
				listeCluster.remove(i);
				return cluster;
			}
		}
		return null;
	}
	
	
	
	public void fusionClusterProche(){
		double distanceMin = Double.MAX_VALUE;
		double distanceCourante;
		Cluster cluster1 = null;
		Cluster cluster2 = null;
		for(Cluster c1 : listeCluster){
			for(Cluster c2 : listeCluster){
				distanceCourante = c1.distance(c2, matriceProximite);
				if( distanceCourante < distanceMin && distanceCourante > 0)
				{
					distanceMin = distanceCourante;
					cluster1 = c1;
					cluster2 = c2;
				}
			}
		}
		popCluster(cluster1);
		popCluster(cluster2);
		addCluster(new Cluster(cluster1, cluster2));
	}
	
	public void remplissageMatriceProx(){
		matriceProximite.add("a", "b", (double) 1);
		matriceProximite.add("b", "c", (double) 1);
		matriceProximite.add("a", "c", (double) 2);
		matriceProximite.add("b", "lol", (double) 24);
		matriceProximite.add("c", "lol", (double) 23);
		matriceProximite.add("a", "lol", (double) 25);
	}
	
	/**la distance entre deux clusters est la distance minimale entre deux clusters
	 * 
	 * @param cluster1
	 * @param cluster2
	 * @return la distance entre deux clusters
	 */
	protected Double distanceCluster(Cluster cluster1, Cluster cluster2){
		
		Double min = Double.MAX_VALUE;
		for(String i : cluster1.getSujets()){
			for(String j : cluster2.getSujets()){
				Double val = matriceProximite.distance(i,j);
				if(val < min && val != null){
					min = val;
				}
			}
		}
		return min;
	}
}
