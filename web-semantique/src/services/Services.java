package services;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.compose.Intersection;
import org.apache.jena.graph.compose.Union;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import org.json.JSONObject;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentText;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentTitle;

import main.Cluster;
import main.Page;
import main.db;

/**
 * Classe renfermant l'ensemble des méthodes permettant de traiter les requêtes externes.
 * @author utilisateur
 *
 */
public class Services {
    private HashMap<String, String> hmapUrlKeyword = new HashMap<String, String>();
    private HashMap<String, String> hmapUrlUri = new HashMap<String, String>();
    
    /**
     * Retourne une liste de thèmes à partir d'une liste de sites web
     * @param data Données reçues à partir d'une recherche google custom search
     * @return Liste de clusters obtenus
     * @throws Exception
     */
    public List<Cluster> retrieveThemes(JSONObject data) throws Exception {
	List<Page> listePages = new ArrayList<Page>();
	List<Cluster> listeClusters = new ArrayList<Cluster>();	
	List<String> listeURL = new ArrayList<String>();
	for(int i=0; i<data.getJSONArray("items").length(); i++){
	    String url = data.getJSONArray("items").getJSONObject(i).getString("link");
	    System.out.println(url);
	    listeURL.add(url);
	    Page page = new Page(url, listeURL.indexOf(url)); //On construit une page par site reçu
	    page.alchemyAPIKeywordPOO(); //On analyse cette page
	    page.dbpediaSpotlightPOO();
	    listePages.add(page);
	}
	double[][] matriceJaccard = creationMatriceJaccardPOO(listePages); //On crée des clusters à partir de la liste de pages obtenue
	listeClusters = creationClustersPOO(matriceJaccard,listePages);
	listeClusters = foundNameClustersPOO(listeClusters);
	return listeClusters;
    }
    
    /**
     * Calcul matrice de jaccard
     * @param listePages
     * @return
     * @throws Exception
     */
    public static double[][] creationMatriceJaccardPOO(List<Page> listePages)throws Exception {
	double[][] matriceJaccard = new double[10][10];
	double indice=0;
	double tailleInter = 0;
	double tailleUnion = 0;
	Graph graphe1 = null;
	Graph graphe2 = null;
	Intersection inter;
	Union union; 
	for(int i=0; i < 10; i++) {
	    for(int j=0; j < 10; j++) {
		if(i!=j && listePages.get(j).getModel() != null && listePages.get(i).getModel() != null) {
		    graphe1 = listePages.get(i).getModel().getGraph();
		    graphe2 = listePages.get(j).getModel().getGraph();
		    inter = new Intersection(graphe1, graphe2);
		    union = new Union(graphe1, graphe2);
		    tailleInter = inter.size();
		    if(tailleInter > 0) {
			tailleUnion=union.size();
			indice = tailleInter/tailleUnion;
			//System.out.println("Indice[" + i+"]" + "["+j+"] : " + indice);
			matriceJaccard[i][j] = indice;
		    } else {
			matriceJaccard[i][j] = 0;
		    }		   
		} else {
		    matriceJaccard[i][j] = 1;
		}	   
	    }
	}
	return matriceJaccard;	    	
    }	

    /**
     * TODO : Ameliorer la recherche de cluster, pour l'instant on se base uniquement sur l'indice de Jaccard,
     * et la valeur seuil est fixe, et choisi un peu a la louche par le programmeur.
     * De meme on considere pour l'instant qu'une page ne peut etre que dans un seul cluster, je sais pas si c'est top.
     * 
     * Recherche des Clusters
     * 
     * @param matriceJaccard
     * @param listeToutesPages
     * @return
     * @throws Exception
     */	
    public static List<Cluster> creationClustersPOO(double[][] matriceJaccard, List<Page> listeToutesPages)throws Exception {
	List<Cluster> listeDesCluster = new ArrayList<Cluster>();
	// Page deja dans un cluster
	List<Page> pageClusterise = new ArrayList<Page>();
	for(Page pageBuff : listeToutesPages) {
	    List<Page> listePagesCluster = new ArrayList<Page>(); // 1 cluster par ligne   
	    if(!pageClusterise.contains(pageBuff)){
		listePagesCluster.add(pageBuff);
		pageClusterise.add(pageBuff);
	    }	   
	    for(Page pageBuff2 : listeToutesPages) {
		// Si 10% de deux pages sont similaires alors les pages sont dans le meme cluster..
		// On considere qu'une page ne peut etre que dans un seul et meme cluster
		if(matriceJaccard[pageBuff.getClassement()][pageBuff2.getClassement()]>0.1 && 
			pageBuff.getClassement()!=pageBuff2.getClassement() && !pageClusterise.contains(pageBuff2)) {
		    // Alors les deux pages sont similaires					   
		    listePagesCluster.add(pageBuff2);
		    pageClusterise.add(pageBuff2);
		}	   
	    }   
	    if(!listePagesCluster.isEmpty()) {
		listeDesCluster.add(new Cluster(listePagesCluster));
		pageBuff.setCluster(new Cluster(listePagesCluster));
	    }
	}	
	return listeDesCluster;
    }
    
    /**
     * Trouve des mots décrivant chaque cluster spécifiés.
     * @param listClusters
     * @return
     */
    public static List<Cluster> foundNameClustersPOO(List<Cluster> listClusters) {
	for(Cluster cluster : listClusters) {
	    cluster.foundNameClusterPOO();
	}	
	return listClusters;
    }
}
