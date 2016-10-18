package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controleur.Controleur;
import modele.Plan;

public class Fenetre extends JFrame{
	
	private static String titreFenetre;
	private Point dimensions;
	private Menu menu;
	private BarreDesTaches barreDesTaches;
	private ZoneDeTexte zoneDeTexte;
	private VuePlan vuePlan;
	private JPanel panneauNord;
	private JPanel panneauEst;
	
	private Controleur controleur;
	
	
	public Fenetre(String titre,int hauteur,int largeur, Plan plan, Controleur controleur){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		titreFenetre = titre;
		dimensions = new Point(largeur,hauteur);
		panneauNord = new JPanel();
		panneauEst = new JPanel();
		
	    this.controleur = controleur;
	    this.setTitle(titreFenetre);
	    this.setSize(dimensions.x,dimensions.y);
	    this.setLocationRelativeTo(null);
	    
	    vuePlan = new VuePlan(plan);
    	zoneDeTexte = new ZoneDeTexte(dimensions.x/3,dimensions.y-30);
	    menu = new Menu();
	    barreDesTaches = new BarreDesTaches();
	    placerComposants();
	    this.setVisible(true);
	    dessinerPlan();
	    
	}
	
	public void placerComposants(){
		panneauNord.setLayout(new BorderLayout());
		panneauEst.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		add(panneauNord,BorderLayout.NORTH);
		add(panneauEst, BorderLayout.EAST);
		add(vuePlan, BorderLayout.CENTER);
		panneauNord.add(barreDesTaches, BorderLayout.SOUTH);
		panneauNord.add(menu, BorderLayout.NORTH);
		panneauEst.add(zoneDeTexte,BorderLayout.CENTER);
		
	}
	
	public void afficherMessage(String message){
		zoneDeTexte.afficherTexte(message);
	}
	
	public void dessinerPlan(){
		vuePlan.setBackground(Color.BLACK);
		vuePlan.creerVuePlan();
		vuePlan.dessinerPlan(vuePlan.getGraphics());
	}

}
