package vue;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;



public class DescriptionLivraison extends InformationTextuelle{
	
	private PopMenuLivraison popupMenuLivraison;
	private PopMenuLivraisonAjout popupMenuLivraisonAjout;
	private boolean valide;

	protected Color background;
	
	public DescriptionLivraison(String information, int index,Fenetre fenetre, boolean valide){
		super(information,index,fenetre);
		this.valide = valide;
		
		background = COULEUR_DEFAUT;
		if(!valide){
			background = COULEUR_ERREUR;
			zoneInformation.setBackground(background);
		}
		
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
				setFocusDescription(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setFocusDescription(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if(SwingUtilities.isRightMouseButton(arg0)){
					fenetre.clicDroitLivraison(index);
				}
			}
		});
		
		
	}
	
	protected void setSurbrillance(boolean surbrillance){
		if(surbrillance){

			zoneInformation.setBackground(COUEUR_HOVER);
		}
		else{
			zoneInformation.setBackground(background);
		}
	}
	
	protected void setFocusDescription(boolean focused){
		if(focused){
			fenetre.setLivraisonSurvol(index);
		}
		else{
			fenetre.setLivraisonSurvol(-1);
		}
	}
}
