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
	
	private void sourisEntree(){
		zoneInformation.setBackground(COUEUR_HOVER);
		fenetre.surlignerLivraison(index);
	}
	private void sourisSortie(){
		zoneInformation.setBackground(background);
		fenetre.surlignerLivraison(-1);
	}
}
