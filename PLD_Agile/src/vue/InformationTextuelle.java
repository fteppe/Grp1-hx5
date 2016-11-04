package vue;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InformationTextuelle extends JPanel{
	
	//un bloc d'informations qui réagit ou non à la souris
	private JTextArea zoneInformation;
	public InformationTextuelle(String information, int index, boolean cliquable){
		super();
		zoneInformation = new JTextArea(3,30);
		zoneInformation.setEditable(false);
		zoneInformation.setLineWrap(true);
		zoneInformation.setWrapStyleWord(true);
		zoneInformation.setText(information);
		if(cliquable){
			zoneInformation.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					sourisSortie();	
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					sourisEntree();
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		add(zoneInformation);
	}
	
	public InformationTextuelle(String information, int index){
		this(information, index, true);
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
	
	private void sourisEntree(){
		zoneInformation.setBackground(Color.blue);
	}
	private void sourisSortie(){
		zoneInformation.setBackground(Color.WHITE);
	}
}
