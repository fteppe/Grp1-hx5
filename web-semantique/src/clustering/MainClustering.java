package clustering;

public class MainClustering {

	private static Cluster cluster1;
	private static Cluster cluster2;
	private static Cluster cluster3;
	private static Matrice matriceProximite;
	private static Analyse analyse;
	
	public static void main(String[] args){
		analyse = new Analyse(1);
		clusterTest();
		DistanceTest();
		
	}
	
	private static void clusterTest(){	
		cluster1 = new Cluster("a");
		cluster2 = new Cluster("b");
		cluster3 = new Cluster("lol");
		analyse.addCluster(cluster1);
		analyse.addCluster(cluster2);
		analyse.addCluster(cluster3);
		
		matriceTest();
		
		analyse.setMatrice(matriceProximite);
		analyse.fusionClusterProche();
		for(Cluster c : analyse.getListCluster()){
			System.out.println("cluster de:"+c);
			System.out.println(c.toJSONstring());
		}
		
	}
	
	private static void matriceTest(){
		matriceProximite = new Matrice();
		matriceProximite.add("a", "b", (double) 1);
		matriceProximite.add("b", "c", (double) 1);
		matriceProximite.add("a", "c", (double) 2);
		matriceProximite.add("b", "lol", (double) 24);
		matriceProximite.add("c", "lol", (double) 23);
		matriceProximite.add("a", "lol", (double) 25);
		
		System.out.println(cluster3.toJSONstring());
		System.out.println("distance cl1 cl2 " + cluster3.distance(cluster2, matriceProximite));
		System.out.println(matriceProximite.distance("lol", "a"));
		
	}
	
	private static void DistanceTest(){
		analyse = new Analyse(2);
		analyse.setMatrice(matriceProximite);
		analyse.addCluster(cluster1);
		analyse.addCluster(cluster2);
		System.out.println(analyse.distanceCluster(cluster1, cluster2));
	}
}
