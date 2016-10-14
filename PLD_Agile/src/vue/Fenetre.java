package vue;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.RootPaneUI;

public class Fenetre extends JFrame{
	
	private static String titrethis;
	private Point dimensions;
	private RootPaneUI panneauRacine;
	private Menu panneau;
	//private BarreDesTaches barreDesTaches;
	
	
	
	public Fenetre(String titre,int hauteur,int largeur){
		titrethis = titre;
		dimensions = new Point(largeur,hauteur);
	    this.setVisible(true);
	    this.setTitle(titrethis);
	    this.setSize(dimensions.x,dimensions.y);
	    this.setLocationRelativeTo(null);
	    
	    panneau = new Menu();
	    //barreDesTaches = new BarreDesTaches();
	    //this.add(barreDesTaches);
	    this.setJMenuBar(panneau);
	    
	}

}
