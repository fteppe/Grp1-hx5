package vue;


import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import modele.Itineraire;
import modele.Livraison;
import modele.Plan;
import modele.Troncon;

public class ZoneDeTexte extends JTextArea implements Observer{

	private Plan plan;
	public ZoneDeTexte(int largeur, int hauteur, Plan plan){
		super(30,25);
		this.plan = plan;
		plan.addObserver(this);
		setLineWrap(true);
		setWrapStyleWord(true);
		setEditable(false);
	}
	
	
	public void afficherTexte(String texte)
	{
		setText(texte);
	}
	
	public void viderZoneDeTexte(){
		afficherTexte("");
	}
	
	public void ajouterLigne(String ligne){
		append(ligne+'\n');
	}
	
	public void afficherFeuilleDeRoute(){
		System.out.println("gene feuille de route");
		ajouterLigne("feuille de route");
		List<Itineraire> itineraires = plan.getItineraires();
		for(Itineraire it : itineraires){
			for(Troncon t : it.getTroncons()){
				ajouterLigne(t.getNom());
			}
		}
		
	}
	
	private void afficherInformationDemandeLivraison(){
		
		HashMap<Integer,Livraison> livraisons = plan.getListeLivraisons();
		if(livraisons != null){
			ajouterLigne("LIVRAISONS :");
			
			for(Livraison livraison : livraisons.values()){
				ajouterLigne(livraison.getAdresse()+" "+livraison.getheureDepart()+" "+livraison.getheureArrivee());
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//viderZoneDeTexte();
		//afficherInformationDemandeLivraison();
		
	}
}
