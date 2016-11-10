package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public class PopMenuIntersection extends PopMenu {
	private JMenuItem ajouterLivraison;
	private int id;
	private Fenetre fenetre;

	public PopMenuIntersection(int id, Fenetre fenetre) {
		super();
		this.fenetre = fenetre;
		this.id = id;
		ajouterLivraison = new JMenuItem("Ajouter une livraison");

		ajouterLivraison.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.actionAjouterLivraison(id);
			}
		});
		add(ajouterLivraison);
	}
}
