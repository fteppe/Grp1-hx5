package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controleur.Controleur;
import modele.Plan;

public class Fenetre extends JFrame{
	
	private static String titreFenetre;
	private Vecteur dimensions;
	private Menu menu;
	private BarreDesTaches barreDesTaches;
	private ZoneDeTexte zoneDeTexte;
	private VuePlan vuePlan;
	private JPanel panneauNord;
	private JPanel panneauEst;
	private JScrollPane scroll;
	
	protected Controleur controleur;
	
	
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
	    
	    vuePlan = new VuePlan(plan);
    	zoneDeTexte = new ZoneDeTexte((int)dimensions.x/3,(int)dimensions.y-30, plan);
	    menu = new Menu(controleur);
	    barreDesTaches = new BarreDesTaches(controleur);
		scroll = new JScrollPane(zoneDeTexte);
	    placerComposants();
	    this.setVisible(true);
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
		panneauEst.add(scroll,BorderLayout.CENTER);
		
	}
	
	public void afficherMessage(String message){
		zoneDeTexte.afficherTexte(message);
	}
	
	public void afficherFeuilleDeRoute(){
		zoneDeTexte.afficherFeuilleDeRoute();
	}
}
