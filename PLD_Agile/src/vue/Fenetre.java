package vue;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.RootPaneUI;

public class Fenetre extends JFrame{
	
	private static String titreFenetre;
	private Point dimensions;
	private Menu menu;
	private BarreDesTaches barreDesTaches;
	private ZoneDeTexte zoneDeTexte;
	private JTextArea text = new JTextArea();
	private JPanel panneauNord;
	private JPanel panneauEst;
	
	
	
	public Fenetre(String titre,int hauteur,int largeur){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		titreFenetre = titre;
		dimensions = new Point(largeur,hauteur);
		panneauNord = new JPanel();
		panneauEst = new JPanel();
		
	    this.setVisible(true);
	    this.setTitle(titreFenetre);
	    this.setSize(dimensions.x,dimensions.y);
	    this.setLocationRelativeTo(null);

    	zoneDeTexte = new ZoneDeTexte(dimensions.x/3,dimensions.y-30);

	    menu = new Menu();
	    barreDesTaches = new BarreDesTaches();
	    System.out.println("coposants créés");
	    placerComposants();
	    System.out.println("composants placés");
	    
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
