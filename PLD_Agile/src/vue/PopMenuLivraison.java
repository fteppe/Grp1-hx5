package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

/**Menu contextuel lié à un clic droit sur une livraison
 * 
 * @author florent
 *
 */
public class PopMenuLivraison extends PopMenu {
	private JMenuItem supprimerLivraison;
	private JMenuItem intervertirLivraison;
	private JMenuItem modifierLivraison;
	protected int idLivraison;
	protected Fenetre fenetre;

	/**
	 * 
	 * @param id l'id de la livraison
	 * @param fenetre la fenetre dans laquelle s'integre ce menu
	 */
	public PopMenuLivraison(int id, Fenetre fenetre) {
		super();
		this.fenetre = fenetre;
		this.idLivraison = id;

		intervertirLivraison = new JMenuItem("Intervertir cette livraison");
		supprimerLivraison = new JMenuItem("Supprimer cette livraison");
		modifierLivraison = new JMenuItem("Modifier Livraison");
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

		modifierLivraison.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.ouvrirMenuModifLivraison(idLivraison);
			}
		});

		add(supprimerLivraison);
		add(modifierLivraison);
		add(intervertirLivraison);

	}
}
