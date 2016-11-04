package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controleur.Controleur;
import modele.Plan;

public class Fenetre extends JFrame{
	/*Classe principale du package vue, contient tout les objets de l'interface graphique
	 * Qui comprend la zone de texte, la barre des taches,le menu et la vue du plan
	 * 
	 */
	
	private static String titreFenetre;
	private Vecteur dimensions;
	private Menu menu;
	private BarreDesTaches barreDesTaches;
	private ZoneDeTexte zoneDeTexte;
	private VuePlan vuePlan;
	private JPanel panneauNord;
	private JPanel panneauEst;
	private JScrollPane scroll;
	private PopMenu popupMenu;
	
	protected Controleur controleur;
	
	/*Constructeur Il va creer les differents elements et les placer dans la fenetre
	 * 
	 * @param titre le titre de la fenetre
	 * @param hauteur la hauteur de la fenetre
	 * @param largeur largeur de la fenetre
	 * @param plan le plan du modele pour la vue du plan
	 * @param controleur le controleur qui est nécessaire pour pouvoir mapper des actions utilisateur
	 * sur des actions du controleur
	 */
	public Fenetre(String titre,int hauteur,int largeur, Plan plan, Controleur controleur){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		titreFenetre = titre;
		dimensions = new Vecteur(largeur,hauteur);
		panneauNord = new JPanel();
		panneauEst = new JPanel();

	    this.controleur = controleur;
	    this.setTitle(titreFenetre);
	    this.setSize((int)dimensions.x,(int)dimensions.y);
	    this.setLocationRelativeTo(null);
	    
	    vuePlan = new VuePlan(plan, this);
    	zoneDeTexte = new ZoneDeTexte((int)dimensions.x/3,(int)dimensions.y-30, plan, this);
	    menu = new Menu(controleur);
	    barreDesTaches = new BarreDesTaches(controleur);
		scroll = new JScrollPane(zoneDeTexte);
		//scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    placerComposants();
	    this.setVisible(true);
	}
	
	/*Les composants ont été crés et sont placés dans la fenetre
	 * 
	 */
	private void placerComposants(){
		panneauNord.setLayout(new BorderLayout());
		panneauEst.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		add(panneauNord,BorderLayout.NORTH);
		add(panneauEst, BorderLayout.EAST);
		add(vuePlan, BorderLayout.CENTER);
		panneauNord.add(barreDesTaches, BorderLayout.SOUTH);
		panneauNord.add(menu, BorderLayout.NORTH);
		panneauEst.add(scroll,BorderLayout.CENTER);
		
	}
	
	public void ouvrirMenuSupprimer(int id){
		popupMenu = (PopMenuLivraison) popupMenu;
		popupMenu = new PopMenuLivraison(id, this);
		Point pos = this.getMousePosition();
		popupMenu.show(this, pos.x, pos.y);
	}
	
	public void afficherDetailDemandeLivraison(){
		zoneDeTexte.afficherInformationDemandeLivraison();
	}
	public void afficherMessage(String message){
		zoneDeTexte.getTitre().afficher(message);
	}
	
	protected void clicDroitPlan(Point point){
		controleur.clicDroitPlan(point);
	}
	protected void supprimerLivraison(int id){
		//TODO appel fonction correspondante controleur
	}
	protected Controleur getControleur(){
		return controleur;
	}
}
