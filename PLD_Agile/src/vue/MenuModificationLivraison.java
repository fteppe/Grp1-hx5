package vue;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modele.Plan;
/**Très proche du menu de création de livraison, on enlève seulement la possibilité de changer la durée d'une livraison
 * 
 * @author florent
 *
 */
public class MenuModificationLivraison extends MenuCreationLivraison {
	/**Constructeur
	 * 
	 * @param fenetre la fenetre dans laquelle ce menu s'inscrit
	 * @param idLivraison l'adresse de la livraison que l'on veut modifier
	 * @param position position sur l'ecran du menu
	 */
	public MenuModificationLivraison(Fenetre fenetre, int idLivraison, Point position) {
		super(fenetre, idLivraison, false, position);
		duree.setVisible(false);
		labelDuree.setVisible(false);
		ok.removeActionListener(ok.getActionListeners()[0]);
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				validationModif();
				setVisible(false);
			}
		});
	}

	private void validationModif() {
		System.out.println("ok");
		fenetre.getControleur().modifierLivraison(idLivraison, horaireActif.isSelected(),
				heureArrive.getHeure() + ":00", heureDepart.getHeure() + ":00");
	}
}
