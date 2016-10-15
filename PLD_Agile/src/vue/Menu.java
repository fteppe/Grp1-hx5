package vue;

import java.awt.Graphics;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;

public class Menu extends JMenuBar{
	
	private JMenu fichiers; 
	private JMenu edition;
	private JMenuItem chargerPlan;
	private JMenuItem chargerDemandeLivraison;
	private JMenuItem quitter;
	private JMenuItem calculerTournee;
	
	public Menu(){
		  fichiers = new JMenu("fichier"); 
		  edition = new JMenu("edition");
		  
		
		  this.add(fichiers);
		  this.add(edition);
		  ajouterElementsMenu();
		  
		  }
	public void ajouterElementsMenu()
	{
		chargerPlan = new JMenuItem("charger un plan");
		quitter = new JMenuItem("quitter");
		chargerDemandeLivraison = new JMenuItem("charger demande de livraison");
		calculerTournee = new JMenuItem("calcul de tournée");
		
		fichiers.add(chargerPlan);
		fichiers.add(chargerDemandeLivraison);
		fichiers.add(quitter);
		edition.add(calculerTournee);
	}
}
