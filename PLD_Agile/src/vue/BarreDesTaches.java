package vue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

import controleur.Controleur;

public class BarreDesTaches extends JToolBar{
	/*La classe barre destache comporte les différents boutons nécessaire au actions principales de l'application
	 * 
	 */
	private JButton chargerPlan;
	private JButton chargerDemandeLivraison;
	private JButton calculTournee;
	private JButton annuler;
	private JButton restaurer;
	private JButton stopCalcul;
	
	private Fenetre fenetre;
	
	/*Constructeur, va créer le lien avec le controleur
	 * Et va ajouter les différents boutons, ainsi que leur associer des écouteurs de souris
	 */
	public BarreDesTaches(Fenetre fenetre){
		this.setFloatable(false);
		this.fenetre = fenetre;
		chargerPlan = new JButton("Charger un plan");
		chargerDemandeLivraison = new JButton("Charger une demande de livraison");
		calculTournee = new JButton("Calculer une tournée");
		annuler = new JButton("annuler");
		restaurer = new JButton("restaurer");
		stopCalcul = new JButton("stop calcul");
		stopCalcul.setBackground(new Color(0xFF7676));
		this.add(chargerPlan);
		this.add(chargerDemandeLivraison);
		addSeparator();
		this.add(calculTournee);
		add(stopCalcul);
		addSeparator();
		add(annuler);
		add(restaurer);
		ajouterEcouteurs();
	}
	
	/*Ajoute les écouteurs de souris nécessaires pour activer les actions associées aux boutons
	 * 
	 */
	public void ajouterEcouteurs(){
		chargerPlan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fenetre.actionChargerPlan();
			}
		});
		
		chargerDemandeLivraison.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fenetre.actionChargerDemandeDeLivraison();
			}
		});
		
		calculTournee.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			    SwingWorker<Boolean,Object> worker = new SwingWorker<Boolean,Object>(){

				@Override
				protected Boolean doInBackground() throws Exception {
				    return fenetre.actionCalculDeTournee();
				}
				
			    };
			    worker.execute();
			}
		});
		
		annuler.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.actionAnnuler();
			}
		});
		
		restaurer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.actionRestaurer();
			}
		});
		
		stopCalcul.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.actionStopCalcul();
			}
		});
	}
	

}
