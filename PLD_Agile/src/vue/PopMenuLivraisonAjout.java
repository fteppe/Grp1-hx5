package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

/**Menu contextuel lorsqu'un clic droit est fait sur une livraison 
 * dans le cadre d'un ajout
 * 
 * @author florent
 *
 */
public class PopMenuLivraisonAjout extends PopMenu {

	private JMenuItem ajouterAvant;
	private JMenuItem ajouterApres;
	private int id;

	/**Constructeur
	 * 
	 * @param id l'id de la livraison cliqué
	 * @param fenetre la fenetre qui sert de cadre au menu
	 */
	public PopMenuLivraisonAjout(int id, Fenetre fenetre) {
		super();
		ajouterAvant = new JMenuItem("Ajouter la livraison avant");
		ajouterApres = new JMenuItem("Ajouter la livraison après");
		this.id = id;
		ajouterApres.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.ouvrirMenuCreationLivraison(id, false);
			}
		});

		ajouterAvant.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.ouvrirMenuCreationLivraison(id, true);
			}
		});

		add(ajouterAvant);
		add(ajouterApres);
	}

}
