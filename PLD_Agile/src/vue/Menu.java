package vue;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import controleur.Controleur;

/**Barre de menu en haut de l'application.
 * 
 * @author florent
 *
 */
public class Menu extends JMenuBar {

	private JMenu fichiers;
	private JMenu edition;
	private JMenuItem chargerPlan;
	private JMenuItem chargerDemandeLivraison;
	private JMenuItem quitter;
	private JMenuItem calculerTournee;
	private JMenuItem annuler;
	private JMenuItem restaurer;
	private JMenuItem generationFeuilleRoute;
	private Fenetre fenetre;

	/**constructeur
	 * 
	 * @param fenetre la fenetre dans laquelle va s'insérer le menu
	 */
	public Menu(Fenetre fenetre) {
		fichiers = new JMenu("Fichier");
		edition = new JMenu("Edition");
		this.fenetre = fenetre;
		this.add(fichiers);
		this.add(edition);
		ajouterElementsMenu();

	}

	private void ajouterElementsMenu() {
		chargerPlan = new JMenuItem("Charger un plan");
		quitter = new JMenuItem("Quitter");
		chargerDemandeLivraison = new JMenuItem("Charger une demande de livraison");
		calculerTournee = new JMenuItem("Calculer une tournée");
		annuler = new JMenuItem("Annuler");
		restaurer = new JMenuItem("Restaurer");
		generationFeuilleRoute = new JMenuItem("Générer la feuille de route");

		fichiers.add(chargerPlan);
		fichiers.add(chargerDemandeLivraison);
		fichiers.add(quitter);
		edition.add(calculerTournee);
		edition.add(annuler);
		edition.add(restaurer);
		edition.add(generationFeuilleRoute);

		ajouterLesEcouteurs();
	}

	private void ajouterLesEcouteurs() {

		quitter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.actionQuitter();
			}
		});

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

		calculerTournee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingWorker<Boolean, Object> worker = new SwingWorker<Boolean, Object>() {

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

		generationFeuilleRoute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fenetre.actionGenerationFeuilleDeRoute();
			}
		});
	}
}
