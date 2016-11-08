package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class PopMenuLivraison extends PopMenu{ 
	private JMenuItem supprimerLivraison;
	protected int idLivraison;
	protected Fenetre fenetre;
	public PopMenuLivraison(int id, Fenetre fenetre){
		super();
		this.fenetre = fenetre;
		this.idLivraison = id;
		supprimerLivraison = new JMenuItem("Supprimer livraison");
		supprimerLivraison.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.supprimerLivraison(id);
			}
		});
		add(supprimerLivraison);
	}
}
