package vue;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import modele.Plan;

public class VuePlan extends JPanel implements Observeur {
	
	private Plan planDuModele;
	List<VueTroncon> listeVueTroncon;
	
	public void creerVuePlan(){
		listeVueTroncon= new ArrayList<VueTroncon>();
		
		//juste pour voir si ça marche
		listeVueTroncon.add(new VueTroncon(2, 2, 50, 50));
	}
	
	public VuePlan(Plan plan)
	{
		planDuModele = plan; 
	}
	
	public void dessinerPlan(Graphics g){
		for(int i=0; i<listeVueTroncon.size(); i++){
			listeVueTroncon.get(i).dessiner(g);
		}
	}

}
