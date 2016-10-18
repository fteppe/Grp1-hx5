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

import javax.swing.DefaultSingleSelectionModel;
import javax.swing.JPanel;

import modele.Intersection;
import modele.Itineraire;
import modele.Livraison;
import modele.Plan;
import modele.Troncon;

public class VuePlan extends JPanel implements Observer {
	
	private double echelle;
	private Plan plan;
	private List<Troncon> listeTroncon; 
	private double e = 0.5;
	private static int diametreIntersection = 10;
	private static Color couleurTroncon = Color.blue;
	
	
	public VuePlan(Plan plan)
	{
		echelle = 0.05;
		this.plan = plan; 
		plan.addObserver(this);
	}
	
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		//on doit peindre le plan;
		dessinerListeIntersections(g);
		dessinerListeTroncons(g,plan.getListeTroncons(), couleurTroncon);
		dessinerListeLivraisons(g);
		dessinerListeItinereraires(g, plan.getItineraires());
	}
	public void dessinerListeItinereraires(Graphics g, List<Itineraire> itineraires){
		
		if(itineraires != null){
			for(Itineraire it: itineraires){
				if(it != null)
				{
					dessinerListeTroncons(g, it.getTroncons(), Color.red);
				}
			}
			
		}

	}
	
	public void dessinerListeTroncons(Graphics g,List<Troncon> troncons, Color c){
		for(Troncon t : troncons){
			dessinerTroncon(g, t, c);
		}
	}
	
	public void dessinerListeIntersections(Graphics g){
		HashMap<Integer, Intersection> intersections = plan.getListeIntersections();
		for(Intersection i : intersections.values())
		{
			dessinerIntersection(g, i);
		}
	}
	
	public void dessinerListeLivraisons(Graphics g){
		HashMap<Integer,Livraison> livraisons = plan.getListeLivraisons();
		if (livraisons != null){
			for(Livraison l : livraisons.values()){
				dessinerLivraison(g,l);
			}
		}
	}
	

	@Override
	public void update(Observable obs, Object arg) {
		// TODO Auto-generated method stub
		if(arg != null){
		}
		repaint();
		
	}
	
	private void dessinerTroncon(Graphics g, Troncon t, Color c){
		
		g.setColor(c);
		g.drawLine((int)(t.getOrigine().getLongitude() * e),(int) (t.getOrigine().getLatitude() * e),(int) (t.getDestination().getLongitude() * e),(int) (t.getDestination().getLatitude() * e));
	}
	
	
	private void dessinerIntersection(Graphics g, Intersection i){
		
		g.setColor(Color.blue);
		g.fillOval((int) (i.getLongitude() * e - diametreIntersection / 2),(int) (i.getLatitude() * e - diametreIntersection/2), diametreIntersection, diametreIntersection);
	}
	
	private void dessinerLivraison(Graphics g, Livraison l){
		g.setColor(Color.yellow);
		Intersection i = l.getAdresse();
		g.fillOval((int) (i.getLongitude() * e - diametreIntersection / 2),(int) (i.getLatitude() * e - diametreIntersection/2), diametreIntersection, diametreIntersection);
	}

}
