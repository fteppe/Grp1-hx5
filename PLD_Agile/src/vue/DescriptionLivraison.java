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
					fenetre.clicDroitLivraison(index);
				}
			}
		});
		
		
	}
	
	protected void sourisEntree(){
		zoneInformation.setBackground(COUEUR_HOVER);
		fenetre.setLivraisonSurvol(index);
	}
	protected void sourisSortie(){
		zoneInformation.setBackground(background);
		fenetre.setLivraisonSurvol(index);
	}
}
