package vue;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import modele.Plan;
import modele.Troncon;

public class VuePlan extends JPanel implements Observer {
	
	private double echelle;
	private Plan planDuModele;
	private List<Troncon> listeTroncon;
	
	public VuePlan(Plan plan)
	{
		echelle = 0.05;
		planDuModele = plan; 
		plan.addObserver(this);
	}
	
	
	public void creerVuePlan(){
	}
	
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		//on doit peindre le plan;
		
	}

	@Override
	public void update(Observable obs, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("update plan");
		if(arg != null){
			System.out.println(obs);
			System.out.println(arg);

		}
		repaint();
		
	}

}
