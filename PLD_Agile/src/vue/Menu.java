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
	
	/*Constructeur
	 * Création des différents menu et ajout des elements dans le menu
	 * ajouts des ecouteurs d'action utilisateurs qui sont mappés sur des actions du controleur
	 */
	public Menu(Controleur controleur){
		  fichiers = new JMenu("fichier"); 
		  edition = new JMenu("edition");
		  this.controleur = controleur;
		  this.add(fichiers);
		  this.add(edition);
		  ajouterElementsMenu();
		  
		  }
	private void ajouterElementsMenu()
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
	
	private void ajouterLesEcouteurs(){
		
		
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

	/*
	 * Lorsqu'on clique sur cliquer dans le menu. Appel de l'action correspondante du controleur
	 */
	public void actionQuitter(){
		System.out.println("menu quitter cliqué");
		controleur.quitter();
	}
	
	/*
	 * Lorsqu'on clique sur le bouton chargerPlan du menu. Appel de l'action correspondante du controleur
	 */
	public void actionChargerPlan(){
		System.out.println("menu charger plan cliqué");
		controleur.chargerPlan();
	}
	/*
	 * Lorsqu'on clique sur le bouton demande de livraison du menu. Appel de l'action correspondante du controleur
	 */
	public void actionChargerDemandeLivraison(){
		System.out.println("menu charger deande livraison cliqué");
		controleur.chargerDemandeLivraison();
	}
	/*
	 * Lorsqu'on clique sur le bouton cacul de tournee du menu. Appel de l'action correspondante du controleur
	 */
	public void actionCalculerTournee(){
		System.out.println("menu calcul tournee cliqué");
		controleur.calculTournee();
	}
}
