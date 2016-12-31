package main;

import java.util.ArrayList;
import java.util.List;

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
