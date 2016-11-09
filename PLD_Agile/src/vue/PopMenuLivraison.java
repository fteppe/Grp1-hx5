package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class PopMenuLivraison extends PopMenu{ 
	private JMenuItem supprimerLivraison;
	private JMenuItem intervertirLivraison;
	protected int idLivraison;
	protected Fenetre fenetre;
	public PopMenuLivraison(int id, Fenetre fenetre){
		super();
		this.fenetre = fenetre;
		this.idLivraison = id;
		
		intervertirLivraison = new JMenuItem("Intervertir cette livraison");
		supprimerLivraison = new JMenuItem("Supprimer cette livraison");
		supprimerLivraison.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.supprimerLivraison(id);
			}
		});
		intervertirLivraison.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.clicIntervertirLivraison(id);
			}
		});
		
		add(supprimerLivraison);
		add(intervertirLivraison);
	}
}
