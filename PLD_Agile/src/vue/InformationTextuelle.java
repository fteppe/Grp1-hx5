package vue;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**Elements de texte affichée dans une ZoneDeTexte
 * 
 * @author florent
 *
 */
public class InformationTextuelle extends JPanel {

	// un bloc d'informations qui réagit ou non à la souris
	protected JTextArea zoneInformation;
	protected int index;
	protected Fenetre fenetre;

	protected static Color COULEUR_DEFAUT = Color.white;
	protected static Color COULEUR_GRISE = new Color(0xA6A6A6);
	protected static Color COULEUR_ERREUR = new Color(0xFF7676);

	/**Constructeur
	 * 
	 * @param information L'information à afficher
	 * @param index L'index de l'information
	 * @param fenetre La fenetre dans laquelle s'inscrit cette information
	 */
	public InformationTextuelle(String information, int index, Fenetre fenetre) {
		super();
		this.fenetre = fenetre;
		this.index = index;
		zoneInformation = new JTextArea(3, 30);
		zoneInformation.setEditable(false);
		zoneInformation.setLineWrap(true);
		zoneInformation.setWrapStyleWord(true);
		zoneInformation.setText(information);
		add(zoneInformation);
	}

	/**Toute zone d'information a un index, cette methode le retourne
	 * 
	 * @return l'index de la zone d'information
	 */
	protected int getIndex() {
		return index;
	}
}
