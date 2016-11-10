package vue;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controleur.Controleur;
import controleur.FenetreFeuilleRoute;
import modele.Plan;

/**
 * Classe principale du package vue, elle constitue l'interface entre le package Controleur 
 * et la Vue
 * 
 * @author florent
 *
 */
public class Fenetre extends JFrame {
	/*

	 * 
	 */

	private static String titreFenetre;
	private Plan plan;
	private Vecteur dimensions;
	private Menu menu;
	private BarreDesTaches barreDesTaches;
	private ZoneDeTexte descriptionPlan;
	private JTextArea console;
	private VuePlan vuePlan;
	private JPanel panneauNord;
	private JPanel panneauEst;
	private JScrollPane scroll;
	private PopMenuLivraison popupMenuLivraison;
	private PopMenuLivraisonAjout popupMenuLivraisonAjout;
	private PopMenuIntersection popupMenuIntersection;
	private MenuCreationLivraison menuCreationLivraison;
	private MenuModificationLivraison menuModifLivraison;

	protected Controleur controleur;

	/**
	 * Constructeur Il va creer les differents elements et les placer dans
	 * la fenetre
	 * 
	 * @param titre le titre de la fenetre
	 * 
	 * @param hauteur la hauteur de la fenetre
	 * 
	 * @param largeur largeur de la fenetre
	 * 
	 * @param plan le plan du modele pour la vue du plan
	 * 
	 * @param controleur le controleur qui est nécessaire pour pouvoir
	 * mapper des actions utilisateur sur des actions du controleur
	 */
	public Fenetre(String titre, int hauteur, int largeur, Plan plan, Controleur controleur) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		titreFenetre = titre;
		this.plan = plan;
		dimensions = new Vecteur(largeur, hauteur);
		panneauNord = new JPanel();
		panneauEst = new JPanel();
		this.controleur = controleur;
		this.setTitle(titreFenetre);
		this.setSize((int) dimensions.x, (int) dimensions.y);
		this.setLocationRelativeTo(null);

		console = new JTextArea(3, 10);
		console.setEditable(false);
		console.setBorder(BorderFactory.createLoweredBevelBorder());
		vuePlan = new VuePlan(plan, this);
		descriptionPlan = new ZoneDeTexte((int) dimensions.x / 3, (int) dimensions.y - 30, plan, this);
		menu = new Menu(this);
		barreDesTaches = new BarreDesTaches(this);
		scroll = new JScrollPane(descriptionPlan);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		placerComposants();
		this.setVisible(true);
	}

	/**Appelé lorsque un bouton ajouter livraison est cliqué
	 * 
	 * @param idIntersection l'intersection sur laquelle ont veut ajouter la livraison
	 */
	protected void actionAjouterLivraison(int idIntersection) {
		vuePlan.setIntersectionSurvol(-1);
		controleur.clicAjouterLivraison(idIntersection);
	}

	/**Methode appelée lorsqu'un bouton annulé est cliqué
	 * 
	 */
	protected void actionAnnuler() {
		controleur.undo();
	}

	/**Methode appelée lorsqu'un bouton "calculer tournée" est cliqué
	 * 
	 * @return retourne si le calcul s'est bien terminé
	 */
	protected boolean actionCalculDeTournee() {
		controleur.calculTournee();
		return true;
	}

	/**
	 * Methode appelée lorsqu'un bouton "charger demande de livraison" est cliqué
	 */
	protected void actionChargerDemandeDeLivraison() {
		controleur.chargerDemandeLivraison();
	}

	/**
	 * Methode appelée lorsqu'un bouton "charger plan" est cliqué
	 */
	protected void actionChargerPlan() {
		controleur.chargerPlan();
	}

	/**
	 * Methode appelée lorsqu'un bouton "generer feuille de route" est cliqué
	 */
	protected void actionGenerationFeuilleDeRoute() {
		controleur.genererFeuilleDeRoute();

	}

	/**
	 * Methode appelée lorsqu'un bouton "quitter" est cliqué
	 */
	protected void actionQuitter() {
		System.out.println("menu quitter cliqué");
		controleur.quitter();
	}

	/**
	 * Methode appelée lorsqu'un bouton "restaurer" est cliqué
	 */
	protected void actionRestaurer() {
		controleur.redo();
	}

	/**
	 * Methode appelée lorsqu'un bouton "arrêter le calcul" est cliqué
	 */
	protected void actionStopCalcul() {
		controleur.arreterCalculTournee();
	}

	/**Lorsque le controleur ou la vue veut afficher une message dans la console, cette
	 * methode est appelée
	 * 
	 * @param message le message à afficher
	 */
	public void afficherMessage(String message) {
		console.setText(message);
	}

	/**Methode appelée quand un clic droit est fait sur une intersection
	 * 
	 * @param idIntersection l'id de l'intersection cliquée
	 */
	protected void clicDroitIntersection(int idIntersection) {
		controleur.clicDroitIntersection(idIntersection);
	}

	/**Methode appelée quand un clic droit est fait sur une livraison
	 * 
	 * @param idIntersection l'id de la livraison cliquée
	 */
	protected void clicDroitLivraison(int idLivraison) {
		controleur.clicDroitLivraison(idLivraison);
	}

	/**Methode appelée quand un clic gauche est fait sur une livraison
	 * 
	 * @param idIntersection l'id de la livraison cliquée
	 */
	protected void clicGaucheLivraison(int idLivraison) {
		controleur.clicGaucheLivraison(idLivraison);
	}

	/**Methode appelée lorsqu'un bouton "intervertir livraison" est cliqué
	 * 
	 * @param idLivraison l'id de la livraison que l'on veut changer de place
	 */
	protected void clicIntervertirLivraison(int idLivraison) {
		controleur.clicEchangerLivraison(idLivraison);
	}

	protected Controleur getControleur() {
		return controleur;
	}

	protected Plan getPlan() {
		return plan;
	}

	/**Ouvre un menu de créationd de livraison, qui va permettre d'inserer 
	 * avant ou après la livraison "position"
	 * 
	 * @param position la livraison autour de lquelle l'insertion sera faite
	 * @param avant bolléen quidetermine si la livraison sera inséré avant ou après position
	 */
	public void ouvrirMenuCreationLivraison(int position, boolean avant) {
		menuCreationLivraison = new MenuCreationLivraison(this, position, avant, new Point(500, 300));
	}

	/**Ouvre le menu de modifcation de livraison
	 * 
	 * @param position la livraison a modifier
	 */
	protected void ouvrirMenuModifLivraison(int position) {
		menuModifLivraison = new MenuModificationLivraison(this, position, new Point(500, 300));
	}

	/**Ouvre un MenuPopIntersection au niveau de la souris
	 * 
	 * @param id id de l'intersection
	 */
	public void ouvrirPopMenuIntersection(int id) {
		popupMenuIntersection = new PopMenuIntersection(id, this);
		Point pos = this.getMousePosition();
		popupMenuIntersection.show(this, pos.x, pos.y);
	}

	/**Ouvre un MenuPopLivraison au niveau de la souris
	 * 
	 * @param idLivraison id de la livraison
	 */
	public void ouvrirPopMenuLivraison(int idLivraison) {
		Point arg0 = getMousePosition();
		popupMenuLivraison = new PopMenuLivraison(idLivraison, this);
		popupMenuLivraison.show(this.getComponentAt(arg0), (int) arg0.getX(), (int) arg0.getY());
	}

	/**Ouvre un MenuPopLivraisonAjout au niveau de la souris
	 * 
	 * @param idLivraison id de la livraison
	 */
	public void ouvrirPopMenuLivraisonInsertion(int idLivraison) {
		Point arg0 = getMousePosition();
		popupMenuLivraisonAjout = new PopMenuLivraisonAjout(idLivraison, this);
		popupMenuLivraisonAjout.show(this, (int) arg0.getX(), (int) arg0.getY());
	}


	private void placerComposants() {
		panneauNord.setLayout(new BorderLayout());
		panneauEst.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		add(panneauNord, BorderLayout.NORTH);
		add(panneauEst, BorderLayout.EAST);
		add(vuePlan, BorderLayout.CENTER);
		add(console, BorderLayout.SOUTH);
		panneauNord.add(barreDesTaches, BorderLayout.SOUTH);
		panneauNord.add(menu, BorderLayout.NORTH);
		panneauEst.add(scroll, BorderLayout.CENTER);

	}

	/**Set à vuePlan  quelle intersection est survolée par
	 * la souris.
	 * Si idIntersection vaut -1 alors aucune intersection n'est survolé
	 * 
	 * @param idIntersection l'id de l'intersection survolée
	 */
	public void setIntersectionSurvol(int idIntersection) {
		vuePlan.setIntersectionSurvol(idIntersection);
		if (plan.getIntersection(idIntersection) != null) {
			afficherMessage("Intersection à l'adresse: " + idIntersection);
		}
	}

	/**Set à vuePlan et la zone de description du plan quelle intersection est survolée par
	 * la souris.
	 * Si idLivraison vaut -1 alors aucune livraison n'est survolée
	 * 
	 * @param idLivraison l'id de la livraison survolée
	 */
	public void setLivraisonSurvol(int idLivraison) {
		vuePlan.setLivraisonSurvol(idLivraison);
		descriptionPlan.setLivraisonSurligne(idLivraison);
	}

	/**Methode appelée lorsque on clique sur un bouton supprimer livraison
	 * 
	 * @param id l'id de la livraison a supprimer
	 */
	protected void supprimerLivraison(int id) {
		controleur.supprimerLivraison(id);
	}

	/**Methode appelée lorsque le plan est survolépar la souris
	 * 
	 * @param point la position de la souris
	 * @param tolerance la tolérance pour la détection d'éléments sous la souris
	 */
	protected void survolPlan(Point point, int tolerance) {
		controleur.survolPlan(point, tolerance);
	}
}
