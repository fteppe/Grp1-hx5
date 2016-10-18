package vue;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;

import controleur.Controleur;

public class Menu extends JMenuBar{
	
	private JMenu fichiers; 
	private JMenu edition;
	private JMenuItem chargerPlan;
	private JMenuItem chargerDemandeLivraison;
	private JMenuItem quitter;
	private JMenuItem calculerTournee;
	private Controleur controleur;
	
	public Menu(Controleur controleur){
		  fichiers = new JMenu("fichier"); 
		  edition = new JMenu("edition");
		  this.controleur = controleur;
		  this.add(fichiers);
		  this.add(edition);
		  ajouterElementsMenu();
		  
		  }
	public void ajouterElementsMenu()
	{
		chargerPlan = new JMenuItem("charger un plan");
		quitter = new JMenuItem("quitter");
		chargerDemandeLivraison = new JMenuItem("charger demande de livraison");
		calculerTournee = new JMenuItem("calcul de tournee");
		
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
				actionCalculerTournee();
			}
		});
	}

	public void actionQuitter(){
		System.out.println("menu quitter cliqué");
		controleur.quitter();
		
	}
	
	public void actionChargerPlan(){
		System.out.println("menu charger plan cliqué");
		controleur.chargerPlan();
		
	}
	public void actionChargerDemandeLivraison(){
		System.out.println("menu charger deande livraison cliqué");
		controleur.chargerDemandeLivraison();
		
	}
	public void actionCalculerTournee(){
		System.out.println("menu calcul tournee cliqué");
		controleur.calculTournee();
	}
}
