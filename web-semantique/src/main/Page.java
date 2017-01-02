package main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentText;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentTitle;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keywords;
import com.ibm.watson.developer_cloud.alchemy.v1.model.LanguageSelection;

public class Page {
	
	private final String url;
	private final int classement;
	private String texteExtrait;
	private List<String> motscles;
	private Cluster cluster;
	private Model model;


	public Page(String url, int classement){
		this.url = url;
		this.classement = classement;
	}
	
	

	/**
	 * Trouve les mots cles et le texte extrait de la page.
	 */
	public void alchemyAPIKeywordPOO() {
		AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey("b377b5a0e4d914c3ec611f0dcba45e78f063d6a2");
	    
	    Map<String,Object> params = new HashMap<String, Object>();
	    String url = this.getUrl();
	    List<String> listKeywords = new ArrayList<String>();
	    
		try {
			URL urlAlchemy = new URL(url);
		    params.put(AlchemyLanguage.URL, urlAlchemy);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	    Keywords texteExtraitKeyWord = service.getKeywords(params).execute();
	    List<Keyword> texteExtraitKeywordList = texteExtraitKeyWord.getKeywords();
	   
	    String texteExtrait = "";
    	
	    // On fait un string de la concatenation de tous les mot cles
	    for(Keyword motcle : texteExtraitKeywordList)
	    {
	    	listKeywords.add(motcle.getText());
	    	texteExtrait += motcle.getText()+" ";
	    }
	    
	    this.setMotscles(listKeywords);
	    this.setTexteExtrait(texteExtrait);
	}
	
	/**
	 * Trouve le texte extrait de la page.
	 */
	public void alchemyAPITextPOO() {
		AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey("b377b5a0e4d914c3ec611f0dcba45e78f063d6a2");
	    
	    Map<String,Object> params = new HashMap<String, Object>();
	    String url = this.getUrl();
	    List<String> listKeywords = new ArrayList<String>();
	    
		try {
			URL urlAlchemy = new URL(url);
			System.out.println("URL :" + url);;
		    params.put(AlchemyLanguage.URL, urlAlchemy);
		    DocumentText texteExtraitKeyWord = service.getText(params).execute();
			   
		    String texteExtrait = texteExtraitKeyWord.getText();
		    listKeywords.add(texteExtrait);
		    this.setMotscles(listKeywords);
		    this.setTexteExtrait(texteExtrait);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}
	
	
	/**
	 * Trouve le titre de la page.
	 * @return
	 * @throws Exception
	 */
	public String alchemyAPITitrePage()throws Exception {

	    AlchemyLanguage service = new AlchemyLanguage();
	    service.setApiKey("b377b5a0e4d914c3ec611f0dcba45e78f063d6a2");
	    
	    Map<String,Object> params = new HashMap<String, Object>();
	    
	    URL urlAlchemy = new URL(url);
	    params.put(AlchemyLanguage.URL, urlAlchemy);
	    String titre;
	    try {
		DocumentTitle texteExtraitTexte = service.getTitle(params).execute();
	    	titre = texteExtraitTexte.getTitle();
	    } catch(Exception e) {
		titre = "Not retrieved";
	    }
	    System.out.println("Done");
	    return titre;
	}
	
	
	/**
	 * Trouve toutes les URI Dbpedia qu'il y a dans la page,
	 * et en fait un model RDF.
	 */
	public void dbpediaSpotlightPOO() {

	    List<String> listeURI = new ArrayList<String>();
	    List<String> listeUrlRdf = new ArrayList<String>();	    
	    db c = new db ();  
	    c.configiration(0.0, 0, "non", "Default", "Default", "yes");  
	    
	    try {
			c.evaluate(this.getTexteExtrait());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    listeURI = c.getResuFullURI();
	    
	    
	    // TODO : Peut etre enlever les URI qui n'apparaissent qu'une fois : supprimer les apparitions hasard
	    
	    List<String> listToRemove = new ArrayList<String>();	
	    for(String URI : listeURI)
	    {
	    	int cptOccurence=0;
	    	for(String URI2 : listeURI)
		    {
	    		if(URI.equals(URI2))
	    		{
	    			cptOccurence++;
	    		}
		    }
	    	
	    	if(cptOccurence==1)
	    	{
	    		listToRemove.add(URI);
	    	}
	    }
	    
	    listeURI.removeAll(listToRemove);
	    
	    
	    // TODO : Utiliser les requetes SPARQL pour reduire le model, centre vers le sport.
	    // L'objectif etant d'avoir des models plus petit et plus interessant
	    
	    // On enlève les doublons de la liste
	    Set<String> set = new HashSet<String>() ;
        set.addAll(listeURI) ;
        ArrayList<String> listeURIDistinct = new ArrayList<String>(set) ;
        
	    System.out.println("resource URI : "+listeURIDistinct);
	    System.out.println("resource URI : "+listeURIDistinct.size());
	    
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

		    }
	    }    
	    this.setModel(model);
	}
	
	// ***************************
	// ***** GETTER / SETTER ***** 
	// ***************************
	
	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	
	public List<String> getMotscles() {
		return motscles;
	}


	public void setMotscles(List<String> motscles) {
		this.motscles = motscles;
	}


	public String getUrl() {
		return url;
	}


	public int getClassement() {
		return classement;
	}


	public String getTexteExtrait() {
		return texteExtrait;
	}


	public void setTexteExtrait(String texteExtrait) {
		this.texteExtrait = texteExtrait;
	}


	public Model getModel() {
		return model;
	}


	public void setModel(Model model) {
		this.model = model;
	}


}
