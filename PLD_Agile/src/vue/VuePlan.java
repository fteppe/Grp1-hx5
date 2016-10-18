package vue;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import modele.Intersection;
import modele.Plan;
import modele.Troncon;

public class VuePlan extends JPanel implements Observer {
	
	private double echelle;
	private Plan plan;
	private List<Troncon> listeTroncon; 
	private double e = 0.5;
	private static int diametreIntersection = 10;
	
	
	public VuePlan(Plan plan)
	{
		echelle = 0.05;
		this.plan = plan; 
		plan.addObserver(this);
	}
	
	
	public void creerVuePlan(){
	}
	
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		//on doit peindre le plan;
		dessinerListeIntersections();
	}
	
	public void dessinerListeTroncon(){
		
	}
	
	public void dessinerListeIntersections(){
		HashMap<Integer, Intersection> intersections = plan.getListeIntersections();
		for(Integer i : intersections.keySet())
		{
			dessinerIntersection(g, intersections.get(i));
		}
	}

	@Override
	public void update(Observable obs, Object arg) {
		// TODO Auto-generated method stub
		if(arg != null){
		}
		repaint();
		
	}
	
	private void dessinerTroncon(Graphics g, Troncon t){
		
		g.setColor(Color.BLUE);
		g.drawLine((int)(t.getOrigine().getLongitude() * e),(int) (t.getOrigine().getLatitude() * e),(int) (t.getDestination().getLongitude() * e),(int) (t.getDestination().getLatitude() * e));
	}
	
	private void dessinerIntersection(Graphics g, Intersection i){
		
		g.setColor(Color.blue);
		g.fillOval((int) (i.getLongitude() * e - diametreIntersection / 2),(int) (i.getLatitude() * e - diametreIntersection/2), diametreIntersection, diametreIntersection);
	}

}
