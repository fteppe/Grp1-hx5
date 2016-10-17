package vue;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		ajouterLesEcouteurs();
	}
	
	public void ajouterLesEcouteurs(){
		
		
		quitter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionQuitter();
			}
		});
		
		chargerPlan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionChargerPlan();
				
			}
		});
		
		chargerDemandeLivraison.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionChargerDemandeLivraison();
			}
		});
		
		calculerTournee.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionCalculerItin�raire();
			}
		});
	}

	public void actionQuitter(){
		System.out.println("menu quitter cliqué");
	}
	
	public void actionChargerPlan(){
		System.out.println("menu charger plan cliqué");
	}
	public void actionChargerDemandeLivraison(){
		System.out.println("menu charger deande livraison cliqué");
		
	}
	public void actionCalculerItin�raire(){
		System.out.println("menu calcul itinéraire cliqué");
	}
}
