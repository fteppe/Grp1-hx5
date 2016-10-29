package vue;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout.Constraints;

import modele.Itineraire;
import modele.Livraison;
import modele.Plan;
import modele.Troncon;

public class ZoneDeTexte extends JPanel implements Observer{

	private Plan plan;
	private InformationTextuelle texte;
	private ArrayList<InformationTextuelle> listeInformation;
	private GridBagConstraints contraintes;
	
	public ZoneDeTexte(int largeur, int hauteur, Plan plan){
		super();
		this.plan = plan;
		plan.addObserver(this);
		listeInformation = new ArrayList<InformationTextuelle>();
		AjouterZoneInformation("", 0);
		texte = listeInformation.get(0);
		setLayout(new GridBagLayout());
		contraintes = new GridBagConstraints();
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		contraintes.anchor = GridBagConstraints.PAGE_START;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		//contraintes.ipady = 3;
		contraintes.gridy = 1;
	}
	
	private void AjouterZoneInformation(String information, int index){
		InformationTextuelle info = new InformationTextuelle(information, index);
		listeInformation.add(info);
		info.ajouterInformationDansPanneau(this,contraintes, 0);
		
	}
	
	/*remplace le texte actuellement affiché par le parametre
	 * 
	 * @param texte le texte qui sera affiché par la zone de texte
	 */
	public void afficherTexte(String message)
	{
		texte.afficher(message);
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
		texte.ajouter(ligne+'\n');
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
	public void afficherInformationDemandeLivraison(){
		
		HashMap<Integer,Livraison> livraisons = plan.getListeLivraisons();
		if(livraisons != null){
			ajouterLigne("LIVRAISONS :");
			
			for(Livraison livraison : livraisons.values()){
				AjouterZoneInformation("lo                                                                            l", livraison.getAdresse().getId());;
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//viderZoneDeTexte();
		//afficherInformationDemandeLivraison();
		
	}
}
