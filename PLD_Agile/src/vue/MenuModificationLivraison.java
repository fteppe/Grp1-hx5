package vue;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modele.Plan;

public class MenuModificationLivraison extends MenuCreationLivraison{
	public MenuModificationLivraison(Fenetre fenetre, int idLivraison, Point position)
	{
		super(fenetre, idLivraison, false, position);
		duree.setVisible(false);
		
		ok.removeActionListener(ok.getActionListeners()[0]);
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				validationModif();
				setVisible(false);
			}
		});
	}
	
	private void validationModif(){
		System.out.println("ok");
		fenetre.getControleur().modifierLivraison(idLivraison,horaireActif.isSelected(), heureArrive.getHeure()+":00", heureDepart.getHeure()+":00");
	}
}
