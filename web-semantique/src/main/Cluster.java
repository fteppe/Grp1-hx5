package main;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
/**
 * Cette classe permet la création et la gestion des Clusters constitués.
 * @author utilisateur
 *
 */
public class Cluster {

	private List<Page> pages = new ArrayList<Page>(); //Pages constituant le cluster
	private String nom; // Nom du cluster
	private Model modele; // Modele du cluster, intersection des modeles de ses pages
	
	/**
	 * Crée un cluster à partir de la liste de pages le constituant
	 * @param pages Pages constituant le cluster
	 */
	public Cluster(List<Page> pages){
		this.pages = pages;
		this.nom = pages.get(0).getTitre(); // le tout premier mot cle
		Model newModele = pages.get(0).getModel();
		for(int i=1; i<pages.size(); i++) {
		    newModele = newModele.intersection(pages.get(i).getModel());
		}
		this.modele = newModele;
	}
	
	/**
	 * Cherche un nom pour le cluster courant, en utilisant l'intersection des modeles des pages
	 * le constituant (en prenant les trois premiers labels existant en anglais), puis le premier mot
	 * du titre de la première page le cas échéant
	 */
	public void foundNameClusterPOOV2(){
		List<String> listTitlePage = new ArrayList<String>();
		List<Model> listModels = new ArrayList<Model>();
		List<String> listResource = new ArrayList<String>();
		String nameCluster = "";
		Model model = this.getPages().get(0).getModel();
		
		for(Page page : this.getPages())
		{
			listModels.add(page.getModel());
		}
		
		
		for(Model modelIte : listModels)
		{
			model = model.intersection(modelIte);
		}
		
		if(model != null) {
			
			Property label = model.createProperty("http://www.w3.org/2000/01/rdf-schema#label") ;
			NodeIterator iter = model.listObjectsOfProperty(label);
			
			while(iter.hasNext()) {
				RDFNode subject = iter.next();
				String lang = subject.asLiteral().getLanguage();
				if(lang.equals("en"))
				{
					if(!subject.isURIResource()){
						listResource.add(subject.toString());
					}
				}
			}
			
			int sizeList = listResource.size();
			if(sizeList>2) {
			nameCluster = listResource.get(0) + " - " + listResource.get(1)+ " - " + listResource.get(2) ;
			}
			else if(sizeList==2){nameCluster = listResource.get(0) + " - " + listResource.get(1);}
			else if(sizeList==1){nameCluster = listResource.get(0);}
			
			nameCluster = nameCluster.replace("_", " ");
			nameCluster = nameCluster.replace("@en", "");
		}
		else {
		    // Si aucun mot en commun, on prend le titre de la premier page ... 
		    if(!listTitlePage.isEmpty()) {
			nameCluster=listTitlePage.get(0);
		    }
		}
		this.setNom(nameCluster);
	}
	
	
	
	
	// ***************************
	// ***** GETTER / SETTER ***** 
	// ***************************
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public List<Page> getPages() {
		return pages;
	}


	public void setPages(List<Page> pages) {
		this.pages = pages;
	}
	
	public void setModel(Model modele) {
		this.modele = modele;
	}
	
	public Model getModel() {
		return this.modele;
	}
}
