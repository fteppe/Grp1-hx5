package main;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;

public class Cluster {

	private List<Page> pages = new ArrayList<Page>();
	private String nom;

	
	public Cluster(List<Page> pages){
		this.pages = pages;
		this.nom = pages.get(0).getMotscles().get(0); // le tout premier mot cle
	}
	
	/**
	 * TODO : AMELIORER la recherche d'un bon nom ... 
	 * Essai de trouver un nom representatif du cluster
	 */
	public void foundNameClusterPOO(){
		
		List<String> listTitlePage = new ArrayList<String>();
		String nameCluster = "";
		String titlePage = "";
		int compteurOccurence = 0;
		
		
		for(Page page : this.getPages())
		{
			try {
				titlePage = page.alchemyAPITitrePage();
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
		
		// Si aucun mot en commun, on prend le titre de la premier page ... 
		if(nameCluster=="")
		{
			nameCluster=listTitlePage.get(0);
		}
		
		this.setNom(nameCluster);
	}
	
	/**
	 * Essai de trouver un nom representatif du cluster
	 * En utilisant l'intersection de toute les pages
	 * Dans cette intersection on prend tous les labels qui existe en anglais
	 * Et on prend les trois premiers labels... c'est pas top.
	 */
	public void foundNameClusterPOOV2(){
		
		List<String> listTitlePage = new ArrayList<String>();
		List<Model> listModels = new ArrayList<Model>();
		List<String> listResource = new ArrayList<String>();
		String nameCluster = "";
		String titlePage = "";
		int compteurOccurence = 0;
		
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
						System.out.println(subject.toString());
					}
				}
			}
			System.out.println("\n \n \n \n");
			
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
			nameCluster=listTitlePage.get(0);
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
}
