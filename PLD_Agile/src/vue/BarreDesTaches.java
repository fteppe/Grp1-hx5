package vue;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class BarreDesTaches extends JToolBar{
	private JButton chargerPlan;
	
	public BarreDesTaches(){
		chargerPlan = new JButton("charger plan");
		this.add(chargerPlan);
	}
}
