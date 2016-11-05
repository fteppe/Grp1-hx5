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
	private JTextArea zoneInformation;
	private int index;
	private Fenetre fenetre;
	private PopMenuLivraison popupMenu;
	
	private static Color couleurHover = new Color(0xA2A5F1);
	private static Color couleurDefaut = Color.white;
	
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

					if(SwingUtilities.isRightMouseButton(arg0) && fenetre.getControleur().clicDroitZoneTextuellePossible()){
						popupMenu = new PopMenuLivraison(index, fenetre);
						popupMenu.show(zoneInformation, arg0.getX(), arg0.getY());
					}
					
				}
			});
		}

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
	
	private void sourisEntree(){
		zoneInformation.setBackground(couleurHover);
		fenetre.surlignerLivraison(index);
	}
	private void sourisSortie(){
		zoneInformation.setBackground(couleurDefaut);
		fenetre.surlignerLivraison(-1);
	}
}
