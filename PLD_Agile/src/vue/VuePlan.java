package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import modele.Intersection;
import modele.Itineraire;
import modele.Livraison;
import modele.Plan;
import modele.Troncon;

public class VuePlan extends JPanel implements Observer {
	
	private Plan plan;
	private Fenetre fenetre;
	private List<Troncon> listeTroncon; 
	private double echelle;
	private int tailleFleche = 8;
	private int intersectionSurvol; //vaut -1 si aucune intersection n'est selectionné
	private int livraisonSurvol;
	private static int diametreIntersection = 10;
	private static Color COULEUR_TRONCON = Color.DARK_GRAY;
	private static Color COULEUR_ENTREPOT = Color.red;
	private static Color COULEUR_LIVRAISON = Color.yellow;
	private static Color COULEUR_SURLIGNE = Color.green;
	private static Color COULEUR_INTERSECTION = COULEUR_TRONCON;
	
	
	public VuePlan(Plan plan, Fenetre fenetre)
	{
		super();
		echelle = 0.8;
		this.fenetre = fenetre;
		this.plan = plan; 
		
	    setLivraisonSurvol(-1);
	    setIntersectionSurvol(-1);
		plan.addObserver(this);
		addMouseListener(new MouseListener() {
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					if(plan.getLivraisonAdresse(livraisonSurvol) != null){
						fenetre.clicGaucheLivraison(livraisonSurvol);
					}
				}
				else if(SwingUtilities.isRightMouseButton(e)){
					if(plan.getLivraisonAdresse(livraisonSurvol) != null){

						fenetre.clicDroitLivraison(livraisonSurvol);
					}
					else if(plan.getIntersection(intersectionSurvol) != null){
						fenetre.clicDroitIntersection(intersectionSurvol);
					}

				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				
				int tolerance =(int) (diametreIntersection/(echelle*2));
				fenetre.survolPlan(MiseAEchellePlan(e.getX(), e.getY()), tolerance);
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
		});
	}
	
	/*va peindre touts les composants à afficher
	 * 
	 * @param g l'objet qui permet de peindre dans un JPanel
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		
		BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		super.paintComponent(g2d);
		//on doit peindre le plan;
		
		//dessinerListeIntersections(g, plan.getListeIntersections());
		dessinerListeTroncons(g2d,plan.getListeTroncons(), COULEUR_TRONCON);
		dessinerListeLivraisons(g2d, plan.getListeLivraisons());
		dessinerListeItinereraires(g2d, plan.getItineraires());
		Intersection entrepot = plan.getEntrepot();
		Intersection intersectionSurvolObj = plan.getIntersection(intersectionSurvol);
		Livraison livraisonSurvolObj = plan.getLivraisonAdresse(livraisonSurvol);
		if(entrepot != null)
		{
			dessinerIntersection(g2d, plan.getEntrepot(), COULEUR_ENTREPOT);
		}
		
		if(intersectionSurvolObj != null)
		{
			dessinerIntersection(g2d, intersectionSurvolObj, COULEUR_INTERSECTION);
		}
		if(livraisonSurvolObj != null){
			dessinerIntersection(g2d, livraisonSurvolObj.getAdresse(), COULEUR_SURLIGNE);
		}
		
		Graphics2D g2dComponent = (Graphics2D) g;
		g2dComponent.drawImage(bufferedImage,null,0,0);
	}
	
	protected void setIntersectionSurvol(int idIntersection){
		intersectionSurvol = idIntersection;
		update(getGraphics());
	}
	
	protected void setLivraisonSurvol(int idLivraison){
		livraisonSurvol = idLivraison;
		update(getGraphics());
	}
	
	/* dessine une liste d'itineraire 
	 * @param g l'objet qui permet de dessiner dans un JPanel
	 * @param itineraires la liste des itineraires a dessiner
	 */
	public void dessinerListeItinereraires(Graphics g, List<Itineraire> itineraires){

		int i = 0;
		double[] colorMap = new double[3*256];
        colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.515600;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.531300;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.546900;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.562500;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.578100;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.593800;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.609400;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.625000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.640600;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.656300;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.671900;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.687500;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.703100;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.718800;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.734400;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.750000;
        colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.765600;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.781300;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.796900;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.812500;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.828100;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.843800;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.859400;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.875000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.890600;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.906300;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.921900;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.937500;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.953100;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.968800;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.984400;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	1.000000;
        colorMap[i++]=	0.000000;	colorMap[i++]=	0.015600;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.031300;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.046900;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.062500;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.078100;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.093800;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.109400;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.125000;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.140600;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.156300;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.171900;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.187500;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.203100;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.218800;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.234400;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.250000;	colorMap[i++]=	1.000000;
        colorMap[i++]=	0.000000;	colorMap[i++]=	0.265600;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.281300;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.296900;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.312500;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.328100;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.343800;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.359400;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.375000;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.390600;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.406300;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.421900;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.437500;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.453100;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.468800;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.484400;	colorMap[i++]=	1.000000;
        colorMap[i++]=	0.000000;	colorMap[i++]=	0.500000;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.515600;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.531300;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.546900;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.562500;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.578100;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.593800;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.609400;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.625000;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.640600;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.656300;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.671900;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.687500;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.703100;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.718800;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.734400;	colorMap[i++]=	1.000000;
        colorMap[i++]=	0.000000;	colorMap[i++]=	0.750000;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.765600;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.781300;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.796900;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.812500;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.828100;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.843800;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.859400;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.875000;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.890600;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.906300;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.921900;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.937500;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.953100;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.968800;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.000000;	colorMap[i++]=	0.984400;	colorMap[i++]=	1.000000;
        colorMap[i++]=	0.000000;	colorMap[i++]=	1.000000;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.015600;	colorMap[i++]=	1.000000;	colorMap[i++]=	1.000000;		colorMap[i++]=	0.031300;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.984400;		colorMap[i++]=	0.046900;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.968800;		colorMap[i++]=	0.062500;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.953100;		colorMap[i++]=	0.078100;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.937500;		colorMap[i++]=	0.093800;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.921900;		colorMap[i++]=	0.109400;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.906300;		colorMap[i++]=	0.125000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.890600;		colorMap[i++]=	0.140600;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.875000;		colorMap[i++]=	0.156300;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.859400;		colorMap[i++]=	0.171900;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.843800;		colorMap[i++]=	0.187500;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.828100;		colorMap[i++]=	0.203100;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.812500;		colorMap[i++]=	0.218800;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.796900;		colorMap[i++]=	0.234400;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.781300;
        colorMap[i++]=	0.250000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.765600;		colorMap[i++]=	0.265600;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.750000;		colorMap[i++]=	0.281300;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.734400;		colorMap[i++]=	0.296900;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.718800;		colorMap[i++]=	0.312500;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.703100;		colorMap[i++]=	0.328100;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.687500;		colorMap[i++]=	0.343800;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.671900;		colorMap[i++]=	0.359400;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.656300;		colorMap[i++]=	0.375000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.640600;		colorMap[i++]=	0.390600;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.625000;		colorMap[i++]=	0.406300;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.609400;		colorMap[i++]=	0.421900;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.593800;		colorMap[i++]=	0.437500;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.578100;		colorMap[i++]=	0.453100;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.562500;		colorMap[i++]=	0.468800;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.546900;		colorMap[i++]=	0.484400;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.531300;
        colorMap[i++]=	0.500000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.515600;		colorMap[i++]=	0.515600;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.500000;		colorMap[i++]=	0.531300;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.484400;		colorMap[i++]=	0.546900;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.468800;		colorMap[i++]=	0.562500;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.453100;		colorMap[i++]=	0.578100;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.437500;		colorMap[i++]=	0.593800;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.421900;		colorMap[i++]=	0.609400;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.406300;		colorMap[i++]=	0.625000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.390600;		colorMap[i++]=	0.640600;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.375000;		colorMap[i++]=	0.656300;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.359400;		colorMap[i++]=	0.671900;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.343800;		colorMap[i++]=	0.687500;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.328100;		colorMap[i++]=	0.703100;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.312500;		colorMap[i++]=	0.718800;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.296900;		colorMap[i++]=	0.734400;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.281300;
        colorMap[i++]=	0.750000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.265600;		colorMap[i++]=	0.765600;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.250000;		colorMap[i++]=	0.781300;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.234400;		colorMap[i++]=	0.796900;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.218800;		colorMap[i++]=	0.812500;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.203100;		colorMap[i++]=	0.828100;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.187500;		colorMap[i++]=	0.843800;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.171900;		colorMap[i++]=	0.859400;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.156300;		colorMap[i++]=	0.875000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.140600;		colorMap[i++]=	0.890600;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.125000;		colorMap[i++]=	0.906300;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.109400;		colorMap[i++]=	0.921900;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.093800;		colorMap[i++]=	0.937500;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.078100;		colorMap[i++]=	0.953100;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.062500;		colorMap[i++]=	0.968800;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.046900;		colorMap[i++]=	0.984400;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.031300;
        colorMap[i++]=	1.000000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.015600;		colorMap[i++]=	1.000000;	colorMap[i++]=	1.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.984400;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.968800;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.953100;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.937500;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.921900;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.906300;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.890600;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.875000;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.859400;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.843800;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.828100;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.812500;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.796900;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.781300;	colorMap[i++]=	0.000000;
        colorMap[i++]=	1.000000;	colorMap[i++]=	0.765600;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.750000;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.734400;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.718800;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.703100;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.687500;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.671900;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.656300;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.640600;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.625000;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.609400;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.593800;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.578100;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.562500;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.546900;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.531300;	colorMap[i++]=	0.000000;
        colorMap[i++]=	1.000000;	colorMap[i++]=	0.515600;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.500000;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.484400;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.468800;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.453100;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.437500;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.421900;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.406300;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.390600;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.375000;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.359400;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.343800;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.328100;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.312500;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.296900;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.281300;	colorMap[i++]=	0.000000;
        colorMap[i++]=	1.000000;	colorMap[i++]=	0.265600;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.250000;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.234400;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.218800;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.203100;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.187500;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.171900;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.156300;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.140600;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.125000;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.109400;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.093800;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.078100;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.062500;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.046900;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.031300;	colorMap[i++]=	0.000000;
        colorMap[i++]=	1.000000;	colorMap[i++]=	0.015600;	colorMap[i++]=	0.000000;		colorMap[i++]=	1.000000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.984400;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.968800;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.953100;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.937500;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.921900;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.906300;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.890600;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.875000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.859400;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.843800;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.828100;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.812500;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.796900;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.781300;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;
        colorMap[i++]=	0.765600;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.750000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.734400;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.718800;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.703100;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.687500;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.671900;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.656300;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.640600;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.625000;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.609400;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.593800;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.578100;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.562500;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.546900;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;		colorMap[i++]=	0.531300;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;
        colorMap[i++]=	0.515600;	colorMap[i++]=	0.000000;	colorMap[i++]=	0.000000;

        int x = 0;
		if(itineraires != null){
			int increment = 256/Math.max(itineraires.size(),1);
			for(Itineraire it: itineraires){
				if(it != null)
				{

		            float red =(float)colorMap[Math.min(x*3,3*256-1)];
		            float green =(float)colorMap[Math.min(x*3+1,3*256-1)];
		            float blue =(float)colorMap[Math.min(x*3+2,3*256-1)];

		            Color couleur = new Color(red, green, blue);
					dessinerListeTronconsItineraire(g, it.getTroncons(), couleur );
					
					x+= increment;
					
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
	public void dessinerListeLivraisons(Graphics g, List<Livraison> livraisons){
		 
		if (livraisons != null){
			for(Livraison l : livraisons){
				if(l.getAdresse().getId() == livraisonSurvol)
				{
					dessinerLivraison(g, l, COULEUR_SURLIGNE);
				}
				else
				{
					dessinerLivraison(g,l, COULEUR_LIVRAISON);
				}
				
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
            g2.draw(new Line2D.Float((int)(t.getOrigine().getLongitude() * echelle),(int) (t.getOrigine().getLatitude() * echelle),(int) (t.getDestination().getLongitude() * echelle),(int) (t.getDestination().getLatitude() * echelle)));
	}
	
	
	private void dessinerIntersection(Graphics g, Intersection i, Color c){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(c);
		g2.fillOval((int) (i.getLongitude() * echelle - diametreIntersection / 2),(int) (i.getLatitude() * echelle - diametreIntersection/2), diametreIntersection, diametreIntersection);
	}
	
	private void dessinerLivraison(Graphics g, Livraison l, Color c){
		Intersection i = l.getAdresse();
		dessinerIntersection(g, i, c);
	}

	/*Fonction qui permet d'ajouter une fleche au bout d'un troncon
	 * 
	 * @param g l'objet qui permet de dessiner dans un JPanel
	 * @parma troncon le troncon au bout duquel on veut dessiner une fleche
	 * @param color la couleur de la fleche
	 */
	private void dessinerFlecheTroncon(Graphics g,Troncon t,Color c){
		Vecteur pointeFleche = new Vecteur((t.getDestination().getLongitude()* echelle),( t.getDestination().getLatitude() * echelle));
		Vecteur origine = new Vecteur((t.getOrigine().getLongitude() * echelle),(t.getOrigine().getLatitude() * echelle));
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
	
	private Point MiseAEchellePlan(int x,int y){
		Point position =  new Point(x,y);
		position.x/=echelle;
		position.y/=echelle;
		
		return position;
		
	}

}
