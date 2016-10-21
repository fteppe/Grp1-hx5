package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import controleur.Controleur;

public class BarreDesTaches extends JToolBar{
	/*La classe barre destache comporte les différents boutons nécessaire au actions principales de l'application
	 * 
	 */
	private JButton chargerPlan;
	private JButton chargerDemandeLivraison;
	private JButton calculTournee;
	private Controleur controleur;
	
	/*Constructeur, va créer le lien avec le controleur
	 * Et va ajouter les différents boutons, ainsi que leur associer des écouteurs de souris
	 */
	public BarreDesTaches(Controleur controleur){
		this.setFloatable(false);
		this.controleur = controleur;
		chargerPlan = new JButton("charger plan");
		chargerDemandeLivraison = new JButton("charger demande de livraison");
		calculTournee = new JButton("calcul de tournée");
		this.add(chargerPlan);
		this.add(chargerDemandeLivraison);
		this.add(calculTournee);
		ajouterEcouteurs();
	}
	
	/*Ajoute les écouteurs de souris nécessaires pour activer les actions associées aux boutons
	 * 
	 */
	public void ajouterEcouteurs(){
		chargerPlan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionChargerPlan();
			}
		});
		
		chargerDemandeLivraison.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionChargerDemandeDeLivraison();
			}
		});
		
		calculTournee.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actionCalculDeTournee();
			}
		});
	}
	
	public void actionChargerPlan(){
		System.out.println("bouton charger plan cliqué");
		controleur.chargerPlan();
	}
	
	public void actionChargerDemandeDeLivraison(){
		System.out.println("bouton chargement demande de livraison cliqué");
		controleur.chargerDemandeLivraison();
	}
	
	public void actionCalculDeTournee(){
		System.out.println("calcul de tournée bouton clique");
		controleur.calculTournee();
	}
}
