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
	
	/*va peindre touts les composants à afficher
	 * 
	 * @param g l'objet qui permet de peindre dans un JPanel
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//on doit peindre le plan;
		
		//dessinerListeIntersections(g, plan.getListeIntersections());
		dessinerListeTroncons(g,plan.getListeTroncons(), COULEUR_TRONCON);
		dessinerListeLivraisons(g, plan.getListeLivraisons());
		dessinerListeItinereraires(g, plan.getItineraires());
		Intersection entrepot = plan.getEntrepot();
		if(entrepot != null)
		{
			dessinerIntersection(g, plan.getEntrepot(), COULEUR_ENTREPOT);
		}
		
	}
	
	/* dessine une liste d'itineraire 
	 * @param g l'objet qui permet de dessiner dans un JPanel
	 * @param itineraires la liste des itineraires a dessiner
	 */
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
	
	/* dessine une liste de troncons
	 *  
	 * @param g l'objet qui permet de dessiner dans un JPanel
	 * @param troncon la liste des troncons a dessiner
	 * @param c la couleur des troncons a dessiner
	 */
	public void dessinerListeTroncons(Graphics g,List<Troncon> troncons, Color c){
		for(Troncon t : troncons){
			dessinerTroncon(g, t, c);
		}
	}
	
	/*dessine une liste de troncons dans le cadre de l'affichage d'un itinéraire
	 * 
	 * @param g l'objet qui permet de dessiner dans un JPanel
	 * @param troncon la liste des troncons a dessiner
	 * @param c la couleur des troncons a dessiner
	 */
	public void dessinerListeTronconsItineraire(Graphics g,List<Troncon> troncons, Color c){
		for(Troncon t : troncons){
			dessinerTroncon(g, t, c);
			dessinerFlecheTroncon(g, t, c);
		}
	}
	
	/* permet de desssiner collection d'intersections
	 * 

	 */
	public void dessinerListeIntersections(Graphics g, HashMap<Integer, Intersection> intersections){
		for(Intersection i : intersections.values())
		{
			dessinerIntersection(g, i, COULEUR_INTERSECTION);
		}
	}
	
	/*permet de dessiner une collection de livraisons
	 * 
	 * @param g l'objet qui permet de dessiner dans un JPanel
	 * @param livraisons la collection des livraisons a dessiner
	 */
	public void dessinerListeLivraisons(Graphics g,HashMap<Integer,Livraison> livraisons){
		 
		if (livraisons != null){
			for(Livraison l : livraisons.values()){
				dessinerLivraison(g,l);
			}
		}
	}
	

	/*actions effectuées lorsque le plan est mis à jour
	 * 
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable obs, Object arg) {
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

	/*Fonction qui permet d'ajouter une fleche au bout d'un troncon
	 * 
	 * @param g l'objet qui permet de dessiner dans un JPanel
	 * @parma troncon le troncon au bout duquel on veut dessiner une fleche
	 * @param color la couleur de la fleche
	 */
	private void dessinerFlecheTroncon(Graphics g,Troncon t,Color c){
		Vecteur pointeFleche = new Vecteur((t.getDestination().getLongitude()* e),( t.getDestination().getLatitude() * e));
		Vecteur origine = new Vecteur((t.getOrigine().getLongitude() * e),(t.getOrigine().getLatitude() * e));
		Vecteur direction = new Vecteur(pointeFleche).add(origine.multiply(-1));
		
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
