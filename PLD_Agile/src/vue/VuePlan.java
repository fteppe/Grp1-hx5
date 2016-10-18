package vue;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import modele.Plan;

public class VuePlan extends JPanel implements Observer {
	
	private Plan planDuModele;
	List<VueTroncon> listeVueTroncon;
	
	public VuePlan(Plan plan)
	{
		planDuModele = plan; 
		plan.addObserver(this);
	}
	
	public void creerVuePlan(){
		listeVueTroncon= new ArrayList<VueTroncon>();
		
		//juste pour voir si ça marche
		listeVueTroncon.add(new VueTroncon(2, 2, 50, 50));
	}
	

	
	public void dessinerPlan(Graphics g){

		paintComponent(g);
		

	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		g.fillOval(20, 20, 100, 100);
		
		for(int i=0; i<listeVueTroncon.size(); i++){
			listeVueTroncon.get(i).dessiner(g);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
