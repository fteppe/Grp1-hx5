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
    private HashMap<String, String> hmapUrlKeyword = new HashMap<String, String>();
    private HashMap<String, String> hmapUrlUri = new HashMap<String, String>();
    
    /**
     * @param requeteUtilisateur
     * @return
     * @throws Exception
     */
    public List<Cluster> execution(String requeteUtilisateur)throws Exception {	
	List<Page> listePages = new ArrayList<Page>();
	List<Cluster> listeClusters = new ArrayList<Cluster>();
	int nbPageSport = 0;
	int indexFirstPage = 1;
	// On centre la requete d'origine vers le sport
	requeteUtilisateur += " sport";
	
	// Gestion des espaces dans la requete
	requeteUtilisateur = requeteUtilisateur.replaceAll(" ", "%20");
	
	
	List<String> listeURL;
	
	// Pour chaque resultat on recupere les mots cles de la page,
	// On en trouve les URI Dbpedia, et on fait un model 
	// de l'union de tous ces URI, on a finalement le model de la page entiere.
	// ON ajoute a la liste uniquement les pages de sports, les autres nous interesse pas
	
	while(nbPageSport<10) {

	// On recupere les dix premiers resultats de Google
	    listeURL = googleCustomSearch(requeteUtilisateur, indexFirstPage);
	    for(String url : listeURL ) {
			Page page = new Page(url, nbPageSport);
			page.alchemyAPIKeywordPOO();  // Tres rapide mais peut etre pas tres bon
			//page.alchemyAPITextPOO(); // Plus long mais peut etre plus representatif
		    page.dbpediaSpotlightPOO();
		    if(page.isSportPage()) {
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
	//double[][] matriceJaccard = creationMatriceJaccardPOO(listePages);
	listeClusters = creationClustersPOOBis(listePages);
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
	String key="AIzaSyABrXjqXg28Np8AcFzf4_A1tALvf8pWVzs";
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

	System.out.println("4");
	String output;
	while ((output = br.readLine()) != null) {
	    if(output.contains("\"link\": \"")){                
		String link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
		System.out.println(link);       //Will print the google search links
		listeURL.add(link);
	    }     	
	}
	} catch (Exception e){
	    System.out.println(e);
	}
	conn.disconnect(); 
	return listeURL;
    }
	
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
		    
	for(int i=0;i<10;i++){
	    for(int j=0;j<i;j++){
		if(i!=j && listePages.get(j).getModel()!=null && listePages.get(i).getModel()!=null){
		    graphe1 = listePages.get(i).getModel().getGraph();
		    graphe2 = listePages.get(j).getModel().getGraph();
		    inter = new Intersection(graphe1, graphe2);
		    union = new Union(graphe1, graphe2);
		    tailleInter = inter.size();	
		    if(tailleInter>0){
			tailleUnion=union.size();
			indice = tailleInter/tailleUnion;
			//System.out.println("Indice[" + i+"]" + "["+j+"] : " + indice);
			matriceJaccard[i][j] = indice;
			matriceJaccard[j][i] = indice;
		    } else {
			matriceJaccard[i][j] = 0;
			matriceJaccard[j][i] = 0;
		    }
		} else {
		    matriceJaccard[i][j] = 1;
		    matriceJaccard[j][i] = 1;
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
	
	for(Page pageBuff : listeToutesPages){
	    List<Page> listePagesCluster = new ArrayList<Page>(); // 1 cluster par ligne
	   
	    if(!pageClusterise.contains(pageBuff)){
		listePagesCluster.add(pageBuff);
		pageClusterise.add(pageBuff);
	    }
		   
	    for(Page pageBuff2 : listeToutesPages){
		// Si 10% de deux pages sont similaires alors les pages sont dans le meme cluster..
		// On considere qu'une page ne peut etre que dans un seul et meme cluster
		if(matriceJaccard[pageBuff.getClassement()][pageBuff2.getClassement()]>0.1 && 
			pageBuff.getClassement()!=pageBuff2.getClassement() && !pageClusterise.contains(pageBuff2)){				   
		    listePagesCluster.add(pageBuff2);
		    pageClusterise.add(pageBuff2);
		}		   
	    }
			   
	    if(!listePagesCluster.isEmpty()){
		listeDesCluster.add(new Cluster(listePagesCluster));
		pageBuff.setCluster(new Cluster(listePagesCluster));
	    }
	}	
	return listeDesCluster;
    }
    
    /**
     * Recherche des Clusters, avec un indice de similarité basé sur une distance de Jaccard et prise en compte suivant un seuil fixe
     * @param listeToutesPages
     * @return
     * @throws Exception
     */
    public static List<Cluster> creationClustersPOOBis(List<Page> listeToutesPages)throws Exception {
	List<Cluster> listeDesCluster = new ArrayList<Cluster>();
	// Page deja dans un cluster
	List<Page> pageClusterise = new ArrayList<Page>();
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
