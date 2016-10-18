package vue;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Fenetre extends JFrame{
	
	private static String titreFenetre;
	private Point dimensions;
	private Menu menu;
	private BarreDesTaches barreDesTaches;
	private ZoneDeTexte zoneDeTexte;
	private JPanel panneauNord;
	private JPanel panneauEst;
	
	
	
	public Fenetre(String titre,int hauteur,int largeur){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		titreFenetre = titre;
		dimensions = new Point(largeur,hauteur);
		panneauNord = new JPanel();
		panneauEst = new JPanel();
		
	    
	    this.setTitle(titreFenetre);
	    this.setSize(dimensions.x,dimensions.y);
	    this.setLocationRelativeTo(null);

    	zoneDeTexte = new ZoneDeTexte(dimensions.x/3,dimensions.y-30);

	    menu = new Menu();
	    barreDesTaches = new BarreDesTaches();
	    System.out.println("coposants créés");
	    placerComposants();
	    System.out.println("composants placés");
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
