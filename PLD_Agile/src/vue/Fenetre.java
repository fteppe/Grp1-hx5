package vue;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	
	
	public Fenetre(String titre,int hauteur,int largeur, Plan plan){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		titreFenetre = titre;
		dimensions = new Point(largeur,hauteur);
		panneauNord = new JPanel();
		panneauEst = new JPanel();
		
	    
	    this.setTitle(titreFenetre);
	    this.setSize(dimensions.x,dimensions.y);
	    this.setLocationRelativeTo(null);
	    
	    vuePlan = new VuePlan(plan);
    	zoneDeTexte = new ZoneDeTexte(dimensions.x/3,dimensions.y-30);
	    menu = new Menu();
	    barreDesTaches = new BarreDesTaches();
	    placerComposants();
	    this.setVisible(true);
	    
	}
	
	public void placerComposants()
	{
		panneauNord.setLayout(new BorderLayout());
		panneauEst.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		add(panneauNord,BorderLayout.NORTH);
		add(panneauEst, BorderLayout.EAST);
		panneauNord.add(barreDesTaches, BorderLayout.SOUTH);
		panneauNord.add(menu, BorderLayout.NORTH);
		panneauEst.add(zoneDeTexte,BorderLayout.CENTER);
		
	}

}
