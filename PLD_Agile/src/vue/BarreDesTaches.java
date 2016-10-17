package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class BarreDesTaches extends JToolBar{
	private JButton chargerPlan;
	private JButton chargerDemandeLivraison;
	private JButton calculTournee;
	
	public BarreDesTaches(){
		this.setFloatable(false);
		
		chargerPlan = new JButton("charger plan");
		chargerDemandeLivraison = new JButton("charger demande de livraison");
		calculTournee = new JButton("calcul de tournée");
		this.add(chargerPlan);
		this.add(chargerDemandeLivraison);
		this.add(calculTournee);
		
		ajouterEcouteurs();
	}
	
	public void ajouterEcouteurs(){
		chargerPlan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionChargerPlan();
			}
		});
		
		chargerDemandeLivraison.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionChargerDemandeDeLivraison();
			}
		});
		
		calculTournee.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionCalculDeTournee();
			}
		});
	}
	
	public void actionChargerPlan(){
		System.out.println("bouton charger plan cliqué");
	}
	
	public void actionChargerDemandeDeLivraison(){
		System.out.println("bouton chargement demande de livraison cliqué");
	}
	
	public void actionCalculDeTournee(){
		System.out.println("calcul de tournée bouton cliqué");
	}
}
