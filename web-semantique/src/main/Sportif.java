package main;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

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
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentText;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keywords;
import com.ibm.watson.developer_cloud.alchemy.v1.util.AlchemyEndPoints.AlchemyAPI;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import static spark.Spark.*;

public class Sportif {
		
	private static Renderer mRenderer;
	private static Layout mVisualizer;
	private static VisualizationViewer mVizViewer;
	
	// TODO : mémoire cache, enregistrez une requete avec ses resultats dans un fichier, (hashmap)
	public static void main(String[] args) throws Exception {
        
		String requeteUtilisateur = "Laura";
		List<String> listeTxtExtrait = new ArrayList<String>();
		List<Model> listeModels = new ArrayList<Model>();
		
		// Gestion des espaces
		requeteUtilisateur += " sport";
		requeteUtilisateur = requeteUtilisateur.replaceAll(" ", "%20");
		
		List<String> listeURL = googleCustomSearch(requeteUtilisateur);
		
		for(String url : listeURL )
		{
			String texteExtrait = alchemyAPI(url);
			listeTxtExtrait.add(texteExtrait);
		}
        
		for(String texteExtrait : listeTxtExtrait)
		{
	        Model model = dbpediaSpotlight(texteExtrait);
	        listeModels.add(model);
	        System.out.println("une page de faite");
		}
		
		//Model intersection = listeModels.get(0).intersection(listeModels.get(1));
		
        creationGraphe(listeModels.get(0));
	}
	
	
	// Google custom Search
	public static List<String> googleCustomSearch(String requeteUtilisateur)throws Exception {

        String key="AIzaSyB4Vksrz6YsFHYXzUF4fYiIZuqqWksF2AI";
        List<String> listeURL = new ArrayList<String>();
        URL url = new URL(
        	"https://www.googleapis.com/customsearch/v1?key="+key+ "&cx=013036536707430787589:_pqjad5hr1a&q="+ requeteUtilisateur + "&alt=json");
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
	
	
    // Alchemy API
	public static String alchemyAPI(String url)throws Exception {

	    AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey("b377b5a0e4d914c3ec611f0dcba45e78f063d6a2");
	    
	    Map<String,Object> params = new HashMap<String, Object>();
	    
	    URL urlAlchemy = new URL(
	            url);
	    params.put(AlchemyLanguage.URL, urlAlchemy);
	    DocumentText texteExtraitWeb = service.getText(params).execute();
	    String texteExtraitWebString= texteExtraitWeb.getText().toString();
	   
	    
	    return texteExtraitWebString;
	    
	}

	
    // Dbpedia Spotlight
	public static Model dbpediaSpotlight(String texteExtrait)throws Exception {

	    List<String> listeURI = new ArrayList<String>();
	    List<String> listeUrlRdf = new ArrayList<String>();
	    
	    
	    db c = new db ();  
	    c.configiration(0.0, 0, "non", "Default", "Default", "yes");  
	    c.evaluate(texteExtrait);
	    listeURI = c.getResuFullURI();
	    
	    System.out.println("resource URI : "+listeURI);
	    
	    System.out.println(listeURI.size());
	    // On modifie les URI afin d'obtenir le lien des fichiers RDF
	    for(String uri : listeURI)
	    {
	    	String uriData = uri.replace("resource","data");
	    	listeUrlRdf.add(uriData+".rdf");
	    }
	    
	    // On charge le modèle avec le fichier RDF
	    FileManager fManager = FileManager.get();
	    fManager.addLocatorURL();
	    
	    
	    Model model =
		          fManager.loadModel(listeUrlRdf.get(0));
	    
	    if(listeURI.size()>1)
	    {
		    Model model1 =
			          fManager.loadModel(listeUrlRdf.get(1));
		    
		    
		    // On fait l'union des modèles de chaque mots, afin d'obtenir le modele de la page.
		    for(String urlRDF : listeUrlRdf)
		    {
			    model1 =
			          fManager.loadModel(urlRDF);
			  
			    //model2 = model1.union(model);
			    // Pas sûr que le truc d'aux dessus supporte model =..(model)
			    //model = model2;
			    model = ModelFactory.createUnion(model,model1);
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
}
