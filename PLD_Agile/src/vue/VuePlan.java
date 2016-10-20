package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
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
	private double e = 0.8;
	private int tailleFleche = 8;
	private static int diametreIntersection = 10;
	private static Color COULEUR_TRONCON = Color.blue;
	private static Color COULEUR_ENTREPOT = Color.red;
	private static Color COULEUR_LIVRAISON = Color.blue;
	private static Color COULEUR_INTERSECTION = COULEUR_TRONCON;
	
	
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
		dessinerListeTroncons(g,plan.getListeTroncons(), COULEUR_TRONCON);
		dessinerListeLivraisons(g);
		dessinerListeItinereraires(g, plan.getItineraires());
		Intersection entrepot = plan.getEntrepot();
		if(entrepot != null)
		{
			dessinerIntersection(g, plan.getEntrepot(), COULEUR_ENTREPOT);
		}
		
	}
	public void dessinerListeItinereraires(Graphics g, List<Itineraire> itineraires){
		
		if(itineraires != null){
			for(Itineraire it: itineraires){
				if(it != null)
				{
					dessinerListeTronconsItineraire(g, it.getTroncons(), Color.red );
				}
			}
			
		}
	}
	
	public void dessinerListeTroncons(Graphics g,List<Troncon> troncons, Color c){
		for(Troncon t : troncons){
			dessinerTroncon(g, t, c);
		}
	}
	
	public void dessinerListeTronconsItineraire(Graphics g,List<Troncon> troncons, Color c){
		for(Troncon t : troncons){
			dessinerTroncon(g, t, c);
			dessinerFlecheTroncon(g, t, c);
		}
	}
	
	public void dessinerListeIntersections(Graphics g){
		HashMap<Integer, Intersection> intersections = plan.getListeIntersections();
		for(Intersection i : intersections.values())
		{
			dessinerIntersection(g, i, COULEUR_INTERSECTION);
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
	    Graphics2D g2 = (Graphics2D) g;
            g2.setColor(c);
            g2.setStroke(new BasicStroke(2));
            g2.draw(new Line2D.Float((int)(t.getOrigine().getLongitude() * e),(int) (t.getOrigine().getLatitude() * e),(int) (t.getDestination().getLongitude() * e),(int) (t.getDestination().getLatitude() * e)));
	}
	
	
	private void dessinerIntersection(Graphics g, Intersection i, Color c){
		
		g.setColor(c);
		g.fillOval((int) (i.getLongitude() * e - diametreIntersection / 2),(int) (i.getLatitude() * e - diametreIntersection/2), diametreIntersection, diametreIntersection);
	}
	
	private void dessinerLivraison(Graphics g, Livraison l){
		Intersection i = l.getAdresse();
		dessinerIntersection(g, i, Color.yellow);
	}
	
	private void dessinerFlecheTroncon(Graphics g,Troncon t,Color c){
		Vecteur pointeFleche = new Vecteur((t.getDestination().getLongitude()* e),( t.getDestination().getLatitude() * e));
		Vecteur origine = new Vecteur((t.getOrigine().getLongitude() * e),(t.getOrigine().getLatitude() * e));
		Vecteur direction = new Vecteur(pointeFleche).add(origine.multiply(-1));
		
		//System.out.println(origine);
		//System.out.println(direction);
		double normeDirection = direction.norme();
		
		direction = direction.multiply(1/normeDirection);
		//pointeFleche = pointeFleche.add(direction.multiply(-1).multiply(diametreIntersection/2)); 
		Vecteur orthDir = new Vecteur(direction.y, - direction.x);
		
		
		
		Vecteur coteFleche1 = new Vecteur(pointeFleche).add(direction.multiply(-1).multiply(tailleFleche)).add(orthDir.multiply(tailleFleche/2));
		Vecteur coteFleche2 = new Vecteur(coteFleche1).add(orthDir.multiply(-1).multiply(tailleFleche));

		int[] tabx = new int[]{(int)pointeFleche.x,(int) coteFleche1.x,(int)coteFleche2.x};
		int[] taby = new int[]{(int)pointeFleche.y,(int)coteFleche1.y,(int)coteFleche2.y};
		
		g.setColor(c);
		g.fillPolygon(tabx,taby ,3);
				
	}

}
