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
		ajouterZoneInformation("", 0);
		texte = listeInformation.get(0);
		setLayout(new GridBagLayout());
		contraintes = new GridBagConstraints();
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		contraintes.anchor = GridBagConstraints.PAGE_START;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.gridy = 0;
		
		afficherInformations();
	}
	
	private void ajouterZoneInformation(String information, int index){
		InformationTextuelle info = new InformationTextuelle(information, index);
		listeInformation.add(info);
	}
	
	private void insererZoneInformationPosition(String information, int indexInformation, int position){
		InformationTextuelle info = new InformationTextuelle(information, indexInformation);
		listeInformation.add(position, info);
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
		viderListeInfos();
		HashMap<Integer,Livraison> livraisons = plan.getListeLivraisons();
		if(livraisons != null){
			ajouterLigne("\nLIVRAISONS :");
			int i=1;
			for(Livraison livraison : livraisons.values()){
				contraintes.gridy = listeInformation.size();
				System.out.println(contraintes.gridy);
				ajouterZoneInformation("lol "+i, livraison.getAdresse().getId());
			}
			afficherInformations();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
	}
	
	private void supprimerInformation(int position){
		listeInformation.remove(position);
	}
	
	public void viderListeInfos(){
		int nbElementSupprimer = listeInformation.size();
		for(int i=1; i< nbElementSupprimer; i++){
			supprimerInformation(1);
		}
		System.out.println("size liste info "+listeInformation.size());
	}
	public void afficherInformations(){
		this.removeAll();
		int i = 0;
		for(InformationTextuelle info : listeInformation){
			i++;
			contraintes.gridy = i;
			info.ajouterInformationDansPanneau(this, contraintes, 0);
		}
	}
}
