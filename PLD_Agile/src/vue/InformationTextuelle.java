package vue;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class InformationTextuelle extends JPanel{
	
	//un bloc d'informations qui réagit ou non à la souris
	protected JTextArea zoneInformation;
	protected int index;
	protected Fenetre fenetre;

	protected static Color COUEUR_HOVER = new Color(0xA2A5F1);
	protected static Color COULEUR_DEFAUT = Color.white;
	protected static Color COULEUR_GRISE = new Color(0xA6A6A6);
	protected static Color COULEUR_ERREUR = new Color(0xFF7676);
	
	public InformationTextuelle(String information, int index,Fenetre fenetre, boolean cliquable){
		super();
		this.fenetre = fenetre;
		this.index = index;
		zoneInformation = new JTextArea(3,30);
		zoneInformation.setEditable(false);
		zoneInformation.setLineWrap(true);
		zoneInformation.setWrapStyleWord(true);
		zoneInformation.setText(information);
		add(zoneInformation);
		

	}
	
	public InformationTextuelle(String information, int index,Fenetre fenetre){
		this(information, index,fenetre, true);
	}
	
	public void ajouterInformationDansPanneau(JPanel panneau,GridBagConstraints contraintes, int i){
		panneau.add(zoneInformation, contraintes);
	}
	
	public void afficher(String message){
		zoneInformation.setText(message);
	}
	public void ajouter(String message){
		zoneInformation.append(message);
	}
	

	
	protected int getIndex(){
		return index;
	}
}
