package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.compose.Intersection;
import org.apache.jena.graph.compose.Union;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;


import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DisambiguatedLinks;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentText;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentTitle;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entities;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Entity;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keywords;
import com.ibm.watson.developer_cloud.alchemy.v1.util.AlchemyEndPoints.AlchemyAPI;

//import static spark.Spark.*;
import main.Fenetre.Recherche;

public class Sportif {	
	
	
	// Deux hashmap utiliser dans la version non objet
	// A supprimer si on garde la version objet.
	private static HashMap<String, String> hmapUrlKeyword = new HashMap<String, String>();
	private static HashMap<String, String> hmapUrlUri = new HashMap<String, String>();
	
	
	/**
	 * @param requeteUtilisateur
	 * @return
	 * @throws Exception
	 */
	public List<Cluster> execution(String requeteUtilisateur)throws Exception {
		
		List<Page> listePages = new ArrayList<Page>();
		List<Cluster> listeClusters = new ArrayList<Cluster>();		
		
		// On centre la requete d'origine vers le sport
		//requeteUtilisateur += " sport";
		
		// Gestion des espaces dans la requete
		requeteUtilisateur = requeteUtilisateur.replaceAll(" ", "%20");
		
		// On recupere les dix premiers resultats de Google
		List<String> listeURL = googleCustomSearch(requeteUtilisateur);
		
		// Pour chaque resultat on recupere les mots cles de la page,
		// On en trouve les URI Dbpedia present a l'interieur, et on fait un model 
		// de l'union de tous ces URI, on a finalement le model de la page entiere.
		for(String url : listeURL )
		{
			Page page = new Page(url, listeURL.indexOf(url));
			//page.alchemyAPIKeywordPOO();
			page.alchemyAPITextPOO();
	        page.dbpediaSpotlightPOO();
	        listePages.add(page);
		}
		
		// On calcul la matrice de Jaccard qu'est l'indice de Jaccard entre chaque page.
		// On cherche ensuite les clusters et on essai de trouver un bon nom pour ce cluster.
		double[][] matriceJaccard = creationMatriceJaccardPOO(listePages);
		listeClusters = creationClustersPOO(matriceJaccard,listePages);
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
	public static List<String> googleCustomSearch(String requeteUtilisateur)throws Exception {

        String key="AIzaSyB4Vksrz6YsFHYXzUF4fYiIZuqqWksF2AI";
        List<String> listeURL = new ArrayList<String>();
        URL url = new URL(
        	"https://www.googleapis.com/customsearch/v1?key="+key+ "&cx=013036536707430787589:_pqjad5hr1a&hl=en&q="+ requeteUtilisateur + "&alt=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
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
        conn.disconnect(); 
        return listeURL;
	}
	
	    
	/*
    // Création du graphe
	public static void creationGraphe(Model model)throws Exception {

          Graph<RDFNode,Statement> g = new DirectedSparseGraph<RDFNode,Statement>();


          StmtIterator iter = model.listStatements();

            // print out the predicate, subject and object of each statement
            while (iter.hasNext()) {
                Statement stmt      = iter.nextStatement();         // get next statement
                RDFNode subject   = stmt.getSubject();   // get the subject
                RDFNode object    = stmt.getObject();    // get the object
                
                // On ajoute le sujet
                g.addVertex( subject);

               if(object instanceof Resource || !object.isLiteral())
               {           	
            	// On ajoute l'objet
                g.addVertex( object );     
                // On ajoute le predicat entre l'objet et le sujet
                g.addEdge(stmt, subject, object, EdgeType.DIRECTED); 
               }
            }

                Collection<RDFNode> collectionOfVertices = g.getVertices();

                System.out.println("number of vertices:" + collectionOfVertices.size());
                System.out.println("number of edges:" +g.getEdgeCount());
                
                
                JFrame jf = new JFrame("Graphe des triplets RDF");
            	//this is something we are adding from this class
            	jf.getContentPane().add(new jungBasicExample(g));
            	//set some size
            	jf.setSize(700, 500);
            	
            	//do something when click on x
            	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	//make sure everything fits...
            	jf.pack();
            	//make it show up...
            	jf.setVisible(true);
	}
	*/
	
	

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
		   
		   
		   for(int i=0;i<10;i++)
		   {
			   for(int j=0;j<10;j++)
			   {
				   if(i!=j && listePages.get(j).getModel()!=null && listePages.get(i).getModel()!=null)
				   {
					   graphe1 = listePages.get(i).getModel().getGraph();
					   graphe2 = listePages.get(j).getModel().getGraph();
					   inter = new Intersection(graphe1, graphe2);
					   union = new Union(graphe1, graphe2);
					   tailleInter = inter.size();
	
					   if(tailleInter>0)
					   {
						   tailleUnion=union.size();
						   indice = tailleInter/tailleUnion;
						   //System.out.println("Indice[" + i+"]" + "["+j+"] : " + indice);
						   matriceJaccard[i][j] = indice;
					   }
					   else {
						   matriceJaccard[i][j] = 0;
					   }
					   
				   }
				   else {
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
		
		   for(Page pageBuff : listeToutesPages)
		   {
			   List<Page> listePagesCluster = new ArrayList<Page>(); // 1 cluster par ligne
			   
			   if(!pageClusterise.contains(pageBuff)){
				   listePagesCluster.add(pageBuff);
				   pageClusterise.add(pageBuff);
				   }
			   
			   for(Page pageBuff2 : listeToutesPages)
			   {
				   // Si 10% de deux pages sont similaires alors les pages sont dans le meme cluster..
				   // On considere qu'une page ne peut etre que dans un seul et meme cluster
				   if(matriceJaccard[pageBuff.getClassement()][pageBuff2.getClassement()]>0.1 && 
						   pageBuff.getClassement()!=pageBuff2.getClassement() && !pageClusterise.contains(pageBuff2))
				   {
					   // Alors les deux pages sont similaires					   
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
	 * Trouve des mots décrivant chaque cluster spécifiés.
	 * @param listClusters
	 * @return
	 */
	public static List<Cluster> foundNameClustersPOO(List<Cluster> listClusters){

		for(Cluster cluster : listClusters)
		{
			cluster.foundNameClusterPOO();
		}
		
		return listClusters;
	}
	

// ****************************************************************
// ****************** VERSION NON OBJET ***************************
// **************************************************************** 

	/*
	public static void main(String[] args) throws Exception {
        
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fenetre window = new Fenetre();
					window.frmChooseyourroadV.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		*/
		/*
		String requeteUtilisateur = "Bordeaux";
		List<Page> listePages = new ArrayList<Page>();
		List<Cluster> listeClusters = new ArrayList<Cluster>();		
		
		// Gestion des espaces
		//requeteUtilisateur += " sport";
		requeteUtilisateur = requeteUtilisateur.replaceAll(" ", "%20");
		
		List<String> listeURL = googleCustomSearch(requeteUtilisateur);
		
		for(String url : listeURL )
		{
			Page page = new Page(url, listeURL.indexOf(url));
			page.alchemyAPIKeywordPOO();
	        page.dbpediaSpotlightPOO();
	        listePages.add(page);
		}
		
		double[][] matriceJaccard = creationMatriceJaccardPOO(listePages);
		listeClusters = creationClustersPOO(matriceJaccard,listePages);
		listeClusters = foundNameClustersPOO(listeClusters);
		
		for(Cluster clusterbugg : listeClusters)
		{
			System.out.println(clusterbugg.getNom());
		}
		*/
		
		/* VERSION NON OBJET
		 * 

		List<Model> listeModels = new ArrayList<Model>();
		Model modele;
		String texteExtrait = "";
		int number=0;
		List<List<Integer>> listeClusterNumber = new ArrayList<List<Integer>>();
		List<List<String>> listeClusterString = new ArrayList<List<String>>();
		HashMap<Integer, String> hmapNumeroURL = new HashMap<Integer, String>();
		HashMap<List<String>, String> hmapClusterNom = new HashMap<List<String>, String>();
		
		// Pour chaque page
		for(String url : listeURL )
		{
			hmapNumeroURL.put(number,url);
			number++;
			
			texteExtrait = alchemyAPIKeyword(url);
	        modele = dbpediaSpotlight(texteExtrait,url);
	        listeModels.add(modele);
	        System.out.println("une page de faite");
		}
		
		double[][] matriceJaccard = creationMatriceJaccard(listeModels);
		listeClusterNumber = creationCluster(matriceJaccard);
		listeClusterString = listNumberToString(listeClusterNumber,hmapNumeroURL);
		hmapClusterNom = foundNameClusters(listeClusterString);
		

		for (Entry<List<String>, String> e : hmapClusterNom.entrySet()){
		    System.out.println(e.getKey() + " : " + e.getValue());
		}
		
		System.out.println(listeClusterNumber);
		//System.out.println(listeClusterString);
		
		System.out.println("fin");
	
        //creationGraphe(intersection);
        */
         
	//}
	
    // Alchemy API 
	// On recupere les mots cles de l'URL
	public static String alchemyAPIKeyword(String url)throws Exception {

	    AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey("b377b5a0e4d914c3ec611f0dcba45e78f063d6a2");
	    
	    Map<String,Object> params = new HashMap<String, Object>();

	    
	    URL urlAlchemy = new URL(
	            url);
	    params.put(AlchemyLanguage.URL, urlAlchemy);
    
	    Keywords texteExtraitKeyWord = service.getKeywords(params).execute();
	    List<Keyword> texteExtraitKeywordList = texteExtraitKeyWord.getKeywords();
	   
	    String texteExtraitKeywordString = "";

	    // Le premier mot clé est celui avec la plus grande relevance.
    	hmapUrlKeyword.put(url,texteExtraitKeywordList.get(0).getText());
    	
	    for(Keyword motcle : texteExtraitKeywordList)
	    {
	    	texteExtraitKeywordString += motcle.getText()+" ";
	    }    
	    return texteExtraitKeywordString; 
	}
	
	
    // Alchemy API 
	// On recupere le texte de l'URL
	// Problème : Beaucoup trop long niveau temps d'execution
	public static String alchemyAPITexte(String url)throws Exception {

	    AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey("b377b5a0e4d914c3ec611f0dcba45e78f063d6a2");
	    
	    Map<String,Object> params = new HashMap<String, Object>();

	    
	    URL urlAlchemy = new URL(url);
	    params.put(AlchemyLanguage.URL, urlAlchemy);
    
	    DocumentText texteExtraitTexte = service.getText(params).execute();
	    String texte = texteExtraitTexte.getText();
	    return texte;
	}
	
	
    // Alchemy API 
	// On recupere le titre de la page de l'URL
	public static String alchemyAPITitrePage(String url)throws Exception {

	    AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey("b377b5a0e4d914c3ec611f0dcba45e78f063d6a2");
	    
	    Map<String,Object> params = new HashMap<String, Object>();
	    
	    URL urlAlchemy = new URL(url);
	    params.put(AlchemyLanguage.URL, urlAlchemy);
    
	    DocumentTitle texteExtraitTexte = service.getTitle(params).execute();
	    String titre = texteExtraitTexte.getTitle();
	    return titre;
	}
	
    // Dbpedia Spotlight + JENA
	public static Model dbpediaSpotlight(String texteExtrait, String url)throws Exception {

	    List<String> listeURI = new ArrayList<String>();
	    List<String> listeUrlRdf = new ArrayList<String>();
	    
	    
	    db c = new db ();  
	    c.configiration(0.0, 0, "non", "Default", "Default", "yes");  
	    c.evaluate(texteExtrait);
	    listeURI = c.getResuFullURI();
	    
	    hmapUrlUri.put(url,listeURI.get(0));
	    
	    // On enlève les doublons de la liste
	    Set<String> set = new HashSet<String>() ;
        set.addAll(listeURI) ;
        ArrayList<String> listeURIDistinct = new ArrayList<String>(set) ;
        
	    System.out.println("resource URI : "+listeURIDistinct);
	    //System.out.println(listeURIDistinct.size());
	    
	    // On modifie les URI afin d'obtenir le lien des fichiers RDF
	    for(String uri : listeURIDistinct)
	    {
	    	String uriData = uri.replace("resource","data");
	    	listeUrlRdf.add(uriData+".rdf");
	    }
	    
	    
	    // On charge le modèle avec le fichier RDF
	    FileManager fManager = FileManager.get();
	    fManager.addLocatorURL();
	    
	    Model model;
	    Model model1;
	    if(listeURIDistinct.size()>0)
	    {
	    	model =
		          fManager.loadModel(listeUrlRdf.get(0));
	    	

	    	
	    }
	    else
	    {
	    	model = null;
	    }
	    
	    if(listeURIDistinct.size()>1)
	    {
		    model1 =
			          fManager.loadModel(listeUrlRdf.get(1));
		    

		    // On fait l'union des modèles de chaque mots, afin d'obtenir le modele de la page.
		    for(String urlRDF : listeUrlRdf)
		    {
			    model1 =
			          fManager.loadModel(urlRDF);
			  
			    model = model.union(model1);
			    //model = ModelFactory.createUnion(model,model1);

		    }
	    }    
	    
	    // TODO : Ajouter des requêtes SPARQL afin de reduire le nombre de triplets.
	    /*
	    // On crée la requête SPARQL afin d'affiner le modèle
	    String sparqlQuery =
	    		"PREFIX db-owlr: <http://dbpedia.org/resource/>\n" +
	            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
	            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
	            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
	            "\n" +
	            "SELECT distinct ?a ?b ?c WHERE {\n" +
	            "  ?a ?b ?c" +
	            "}";
	
	    String resultat = new String();
	    
	    // Création de la requête
	    Query query = QueryFactory.create(sparqlQuery) ;
	    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
	    	
	      // Lancement de la requête
	      ResultSet results = qexec.execSelect() ;
	      
	      // Récupération des résultats
	      for ( ; results.hasNext() ; )
	      {
	        QuerySolution soln = results.nextSolution() ;
	        RDFNode a = soln.get("a") ;       
	        RDFNode b = soln.get("b") ;
	        RDFNode cB = soln.get("c") ;
	        String buffera = a.toString();
	        String bufferb = b.toString();
	        String bufferc = cB.toString();
	
	        resultat+= "a : " + buffera + " \n  b : " + bufferb + "\n c :" + bufferc + "\n\n";
	      }
	      System.out.println(resultat);
	    }
	    */
	    
	    
	    //model.write(System.out, "TTL") ;
	    return model;
	}
	
	// Calcul matrice de jaccard
public static double[][] creationMatriceJaccard(List<Model> listeModels)throws Exception {

	   double[][] matriceJaccard = new double[10][10];
	   double indice=0;
	   double tailleInter = 0;
	   double tailleUnion = 0;
	   Graph graphe1 = null;
	   Graph graphe2 = null;
	   Intersection inter;
	   Union union;
	   
	   
	   for(int i=0;i<10;i++)
	   {
		   for(int j=0;j<10;j++)
		   {
			   if(i!=j && listeModels.get(j)!=null && listeModels.get(i)!=null)
			   {
				   graphe1 = listeModels.get(i).getGraph();
				   graphe2 = listeModels.get(j).getGraph();
				   inter = new Intersection(graphe1, graphe2);
				   union = new Union(graphe1, graphe2);
				   tailleInter = inter.size();
				   //System.out.println("tailleInter : " + tailleInter);
				   if(tailleInter>0)
				   {
					   tailleUnion=union.size();
					   //System.out.println("tailleUnion : " + tailleUnion);
					   indice = tailleInter/tailleUnion;
					   System.out.println("Indice[" + i+"]" + "["+j+"] : " + indice);
					   matriceJaccard[i][j] = indice;
				   }
				   else {
					   matriceJaccard[i][j] = 0;
				   }
				   
			   }
			   else {
				   matriceJaccard[i][j] = 1;
			   }
			   
		   }
	   }
	   return matriceJaccard;	    
	}

	// Recherche des Clusters
	public static List<List<Integer>> creationCluster(double[][] matriceJaccard)throws Exception {
		
		List<List<Integer>> listeDesCluster = new ArrayList<List<Integer>>();
		List<Integer> numeroClusterise = new ArrayList<Integer>();
		
		   for(int i=0;i<10;i++)
		   {
			   List<Integer> cluster = new ArrayList<Integer>(); // 1 cluster par ligne
			   
			   if(!numeroClusterise.contains(i)){
				   cluster.add(i);
				   numeroClusterise.add(i);
				   }
			   
			   for(int j=0;j<10;j++)
			   {
				   if(matriceJaccard[i][j]>0.1 && i!=j && !numeroClusterise.contains(j))
				   {
					   // Alors les deux pages sont similaires					   
					   cluster.add(j);
					   numeroClusterise.add(j);
				   }
				   
			   }
			   
			   if(!cluster.isEmpty()){
			   listeDesCluster.add(cluster);
			   }
		   }		
		
		return listeDesCluster;
	}
	
	// Liste de numero à liste d'URL
	public static List<List<String>> listNumberToString(List<List<Integer>> listeClusterNumber, HashMap<Integer, String> hmapPages){
		
		List<List<String>> listeClusterString = new ArrayList<List<String>>();
		
		for(List<Integer> cluster : listeClusterNumber)
		{
			List<String> listeString = new ArrayList<String>();
			for(Integer numPage : cluster)
			{
				listeString.add(hmapPages.get(numPage));
			}
			listeClusterString.add(listeString);
		}
		
		return listeClusterString;
	}
	
	/**
	 * Trouve des mots décrivant le cluster spécifié
	 * @param cluster
	 * @return
	 */
	public static String foundNameCluster(List<String> cluster){
		
		List<String> listTitlePage = new ArrayList<String>();
		String nameCluster = "";
		String titlePage = "";
		int compteurOccurence = 0;
		
		
		for(String url : cluster)
		{
			try {
				titlePage = alchemyAPITitrePage(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			listTitlePage.add(titlePage);
		}
		
		for(String title : listTitlePage)
		{
			String mots[]  = title.split(" ");
			for(String mot : mots)
			{
				for(String title2 : listTitlePage)
				{
					if(title2.contains(mot))
					{
						compteurOccurence++;
					}
				}
				
				if(compteurOccurence==listTitlePage.size() && !nameCluster.contains(mot))
				{
					nameCluster+= mot + " ";
				}
				compteurOccurence=0;
			}
		}
		
		return nameCluster;
	}
	
	
	/**
	 * Trouve des mots décrivant chaque cluster spécifiés.
	 * @param listClusters
	 * @return
	 */
	public static HashMap<List<String>, String> foundNameClusters(List<List<String>> listClusters){
		
		HashMap<List<String>, String> hmapClusterNom = new HashMap<List<String>, String>();
		String nameCluster = "";
		
		for(List<String> cluster : listClusters)
		{
			nameCluster = foundNameCluster(cluster);
			hmapClusterNom.put(cluster, nameCluster);
		}
		
		return hmapClusterNom;
	}
	

}

