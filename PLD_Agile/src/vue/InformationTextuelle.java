package vue;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InformationTextuelle {
	
	private JTextArea zoneInformation;
	public InformationTextuelle(String information, int index){
		zoneInformation = new JTextArea(3,30);
		zoneInformation.setEditable(false);
		zoneInformation.setLineWrap(true);
		zoneInformation.setWrapStyleWord(true);
		zoneInformation.setText(information);
	}
	
	public void ajouterInformationDansPanneau(JPanel panneau,GridBagConstraints contraintes, int i){
		panneau.add(zoneInformation, contraintes, i);
	}
	
	public void afficher(String message){
		zoneInformation.setText(message);
	}
	public void ajouter(String message){
		zoneInformation.append(message);
	}
}
