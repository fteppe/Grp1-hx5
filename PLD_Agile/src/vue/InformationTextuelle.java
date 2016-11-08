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
	private PopMenuLivraison popupMenuLivraison;
	private PopMenuLivraisonAjout popupMenuLivraisonAjout;
	private static Color COUEUR_HOVER = new Color(0xA2A5F1);
	private static Color COULEUR_DEFAUT = Color.white;
	private static Color COULEUR_GRISE = new Color(0xA6A6A6);
	
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

					if(SwingUtilities.isRightMouseButton(arg0)){
						if(fenetre.getControleur().clicDroitZoneTextuellePossible()){
							popupMenuLivraison = new PopMenuLivraison(index, fenetre);
							popupMenuLivraison.show(zoneInformation, arg0.getX(), arg0.getY());
						}
						else if(fenetre.getControleur().possibleAjoutLivraison()){
							popupMenuLivraisonAjout = new PopMenuLivraisonAjout(index, fenetre);
							popupMenuLivraisonAjout.show(zoneInformation, arg0.getX(),arg0.getY());
						}
						

					}
					
				}
			});
		}
		else
		{
			zoneInformation.setBackground(COULEUR_GRISE);
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
		zoneInformation.setBackground(COUEUR_HOVER);
		fenetre.surlignerLivraison(index);
	}
	private void sourisSortie(){
		zoneInformation.setBackground(COULEUR_DEFAUT);
		fenetre.surlignerLivraison(-1);
	}
	
	protected int getIndex(){
		return index;
	}
}
