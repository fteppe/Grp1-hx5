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
		super(30, 30);
		this.plan = plan;
		plan.addObserver(this);
		setLineWrap(true);
		setWrapStyleWord(true);
		setEditable(false);
	}
	
	/*remplace le texte actuellement affiché par le parametre
	 * 
	 * @param texte le texte qui sera affiché par la zone de texte
	 */
	public void afficherTexte(String texte)
	{
		setText(texte);
	}
	
	/*
	 * vide la zone de texte
	 */
	public void viderZoneDeTexte(){
		afficherTexte("");
	}
	/*Ajoute une ligne à la fin de la zone de texte
	 * 
	 * @param ligne le texte qui sera ajouté au bout de la zone de texte
	 */
	public void ajouterLigne(String ligne){
		append(ligne+'\n');
	}
	
	/*fonction qui génère et affiche la feuille de route
	 * 
	 */
	public void afficherFeuilleDeRoute(){
		System.out.println("gene feuille de route");
		afficherTexte("Feuille de route :");
		List<Itineraire> itineraires = plan.getItineraires();
		for(Itineraire it : itineraires){
		    ajouterLigne("\r\n" + it.toString());
			for(Troncon t : it.getTroncons()){
				ajouterLigne(t.toString());
			}
		}
		
	}
	
	/*Fonction qui génère et affiche les informations d'une demande de livraison
	 * 
	 */
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
