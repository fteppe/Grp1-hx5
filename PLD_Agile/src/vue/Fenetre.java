package vue;

import javax.swing.JFrame;

public class Fenetre {
	
	private static String titreFenetre;
	private Point dimensions;
	
	
	public Fenetre(String titre,int hauteur,int largeur){
		titreFenetre = titre;
	    JFrame fenetre = new JFrame();
	    fenetre.setVisible(true);
	    fenetre.setTitle(titreFenetre);
	    fenetre.setSize(hauteur,largeur);
	    fenetre.setLocationRelativeTo(null);
	}

}
