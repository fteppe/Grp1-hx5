package vue;

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
		
	}
}
