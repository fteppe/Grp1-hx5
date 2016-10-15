package vue;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.RootPaneUI;

public class Fenetre extends JFrame{
	
	private static String titreFenetre;
	private Point dimensions;
	private RootPaneUI panneauRacine;
	private Menu menu;
	private BarreDesTaches barreDesTaches;
	private JPanel top;
	
	
	
	public Fenetre(String titre,int hauteur,int largeur){
		titreFenetre = titre;
		dimensions = new Point(largeur,hauteur);
		top = new JPanel();
		
	    this.setVisible(true);
	    this.setTitle(titreFenetre);
	    this.setSize(dimensions.x,dimensions.y);
	    this.setLocationRelativeTo(null);
	    menu = new Menu();
	    barreDesTaches = new BarreDesTaches();
	    
	    placerComposant();
	    
	}
	
	public void placerComposant()
	{
		top.setLayout(new BorderLayout());
		this.setLayout(new BorderLayout());
		this.add(top,BorderLayout.NORTH);
		top.add(barreDesTaches, BorderLayout.SOUTH);
		top.add(menu, BorderLayout.NORTH);
		
	}

}
