package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class PopMenuLivraisonAjout extends PopMenu{
	
	private JMenuItem ajouterAvant;
	private JMenuItem ajouterApres;
	private int id;
	
	public PopMenuLivraisonAjout(int id, Fenetre fenetre){
		super();
		ajouterAvant = new JMenuItem("ajouter la livraison avant");
		ajouterApres = new JMenuItem("ajouter la livraison après");
		this.id = id;
		ajouterApres.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.ouvrirMenuAjoutLivraison(id, false);
			}
		});
		
		ajouterAvant.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.ouvrirMenuAjoutLivraison(id, true);
			}
		});
		
		add(ajouterAvant);
		add(ajouterApres);
	}
	
}
