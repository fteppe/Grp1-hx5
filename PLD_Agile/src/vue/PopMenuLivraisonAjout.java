package vue;

import javax.swing.JMenuItem;

public class PopMenuLivraisonAjout extends PopMenu{
	
	private JMenuItem ajouterAvant;
	private JMenuItem ajouterApres;
	
	public PopMenuLivraisonAjout(int id, Fenetre fenetre){
		super();
		ajouterAvant = new JMenuItem("ajouter la livraison avant");
		ajouterApres = new JMenuItem("ajouter la livraison après");
	}
	
}
