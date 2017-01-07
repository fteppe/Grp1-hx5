package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

    /**
     * Execute la requete d'un utilisateur
     * @param requeteUtilisateur
     * @return
     * @throws Exception
     */
    public List<Cluster> execution(String requeteUtilisateur)throws Exception {	
	List<Page> listePages = new ArrayList<Page>();
	List<Cluster> listeClusters = new ArrayList<Cluster>();
	List<String> listeURL;
	int nbPageSport = 0;
	int indexFirstPage = 1;
	
	// On centre la requete d'origine vers le sport et on gere les espaces
	requeteUtilisateur += " sport";
	requeteUtilisateur = requeteUtilisateur.replaceAll(" ", "%20");

	// Pour chaque resultat on recupere les mots cles de la page,
	// On en trouve les URI Dbpedia, et on fait un model 
	// de l'union de tous ces URI, on a finalement le model de la page entiere.
	// ON ajoute a la liste uniquement les pages de sports, les autres nous interesse pas
	
	while(nbPageSport<10) {

	// On recupere les dix premiers resultats de Google
	    listeURL = googleCustomSearch(requeteUtilisateur, indexFirstPage);
	    for(String url : listeURL ) {
			Page page = new Page(url, nbPageSport);
			page.alchemyAPIKeywordPOO();  // Plus rapide mais peut etre pas tres bon
		    page.dbpediaSpotlightPOO();
		    if(page.isSportPage() && !page.getMotscles().isEmpty()) {
		         nbPageSport++;
		         listePages.add(page);
		    }  
	        if(nbPageSport==10){break;}
	    }
	    indexFirstPage += 10;
	    listeURL.clear();
	}
	// On calcul la matrice de Jaccard qu'est l'indice de Jaccard entre chaque page.
	// On cherche ensuite les clusters et on essai de trouver un bon nom pour ce cluster.
	listeClusters = creationClustersPOO(listePages);
	listeClusters = foundNameClustersPOO(listeClusters);
	
	// On renvoi le resultat a la fenetre.
	return listeClusters;
    }

    /**
     * Google custom Search : Renvoi la liste des dix premiers resultat de la requete
     * @param requeteUtilisateur
     * @return
     * @throws Exception
     */
    public static List<String> googleCustomSearch(String requeteUtilisateur, int start)throws Exception {
	String key="AIzaSyB4Vksrz6YsFHYXzUF4fYiIZuqqWksF2AI";
	List<String> listeURL = new ArrayList<String>();
	String startString = Integer.toString(start);
	URL url = new URL(
		"https://www.googleapis.com/customsearch/v1?key="+key+ "&start="+ startString + "&cx=013632266919387871672:pgb1ce2nwuq&q="+ requeteUtilisateur + "&alt=json");
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Accept", "application/json");;
	try{
	BufferedReader br = new BufferedReader(new InputStreamReader(
		(conn.getInputStream())));

	String output;
	while ((output = br.readLine()) != null) {
	    if(output.contains("\"link\": \"")){                
		String link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
		System.out.println(link);       //Will print the google search links
		listeURL.add(link);
	    }     	
	}
	} catch (Exception e){
	    
	}
	conn.disconnect(); 
	return listeURL;
    }
    
    /**
     * Recherche des Clusters, avec un indice de similarité basé sur une distance de Jaccard et prise en compte suivant un seuil fixe
     * @param listeToutesPages
     * @return
     * @throws Exception
     */
    public static List<Cluster> creationClustersPOO(List<Page> listeToutesPages)throws Exception {
	List<Cluster> listeDesCluster = new ArrayList<Cluster>();
	
	for(Page pageBuff : listeToutesPages){
	    List<Page> listePagesCluster = new ArrayList<Page>();
	    listePagesCluster.add(pageBuff);
	    Cluster newCluster = new Cluster(listePagesCluster);
	    listeDesCluster.add(newCluster);
	    pageBuff.setCluster(newCluster);
	}
	double similMax=1;
	    
	while(similMax >0.20){
	    similMax= 0;
	    Cluster maxCluster = null;
	    Cluster secMaxCluster = null;

	    for(int i = 0; i <listeDesCluster.size(); i++){
		for(int j = 0; j < i; j++){
		    Cluster firstClus = listeDesCluster.get(i);
		    Cluster secClus = listeDesCluster.get(j);
    		    if(firstClus.getModel()!=null && secClus.getModel()!=null){
    			Model union = firstClus.getModel().union(secClus.getModel());
    			Model inters = firstClus.getModel().intersection(secClus.getModel());
    			double indice = (double)(inters.size())/(union.size());
    			if(indice > similMax) {
    			    maxCluster = firstClus;
    			    secMaxCluster = secClus;
    			    similMax = indice;
    			}
		    }
		}
	    }
	    if(maxCluster != null && secMaxCluster != null && similMax >0.20){
        	    listeDesCluster.remove(maxCluster);
        	    listeDesCluster.remove(secMaxCluster);
        	    List<Page> newListPages = maxCluster.getPages();
        	    newListPages.addAll(secMaxCluster.getPages());
        	    Cluster newCluster = new Cluster(newListPages);
        	    listeDesCluster.add(newCluster);
	    }
	    System.out.println("Sim Max = " + similMax);
	}    
	    
	return listeDesCluster;
    }
    
    /**
     * Trouve des mots décrivant chaque cluster spécifiés.
     * @param listClusters
     * @return
     */
    public static List<Cluster> foundNameClustersPOO(List<Cluster> listClusters) {
	for(Cluster cluster : listClusters){
		cluster.foundNameClusterPOOV2();
	}
	return listClusters;
    }
}
