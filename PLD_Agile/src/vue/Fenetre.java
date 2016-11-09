package vue;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controleur.Controleur;
import modele.Plan;

public class Fenetre extends JFrame{
	/*Classe principale du package vue, contient tout les objets de l'interface graphique
	 * Qui comprend la zone de texte, la barre des taches,le menu et la vue du plan
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
	
	
	protected Controleur controleur;
	
	/*Constructeur Il va creer les differents elements et les placer dans la fenetre
	 * 
	 * @param titre le titre de la fenetre
	 * @param hauteur la hauteur de la fenetre
	 * @param largeur largeur de la fenetre
	 * @param plan le plan du modele pour la vue du plan
	 * @param controleur le controleur qui est nécessaire pour pouvoir mapper des actions utilisateur
	 * sur des actions du controleur
	 */
	public Fenetre(String titre,int hauteur,int largeur, Plan plan, Controleur controleur){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		titreFenetre = titre;
		this.plan = plan;
		dimensions = new Vecteur(largeur,hauteur);
		panneauNord = new JPanel();
		panneauEst = new JPanel();		
	    this.controleur = controleur;
	    this.setTitle(titreFenetre);
	    this.setSize((int)dimensions.x,(int)dimensions.y);
	    this.setLocationRelativeTo(null);

	    //menuCreationLivraison = new MenuCreationLivraison(this, 25, true, new Point(0,0));
	    console = new JTextArea(3, 10);
	    console.setEditable(false);
	    console.setBorder(BorderFactory.createLoweredBevelBorder());
	    vuePlan = new VuePlan(plan, this);
    	descriptionPlan = new ZoneDeTexte((int)dimensions.x/3,(int)dimensions.y-30, plan, this);
	    menu = new Menu(this);
	    barreDesTaches = new BarreDesTaches(this);
		scroll = new JScrollPane(descriptionPlan);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    placerComposants();
	    this.setVisible(true);
	}
	
	/*Les composants ont été crés et sont placés dans la fenetre
	 * 
	 */
	private void placerComposants(){
		panneauNord.setLayout(new BorderLayout());
		panneauEst.setLayout(new BorderLayout());
		setLayout(new BorderLayout());
		add(panneauNord,BorderLayout.NORTH);
		add(panneauEst, BorderLayout.EAST);
		add(vuePlan, BorderLayout.CENTER);
		add(console, BorderLayout.SOUTH);
		panneauNord.add(barreDesTaches, BorderLayout.SOUTH);
		panneauNord.add(menu, BorderLayout.NORTH);
		panneauEst.add(scroll,BorderLayout.CENTER);
		
	}
	
	public void ouvrirPopMenuIntersection(int id){
		popupMenuIntersection = new PopMenuIntersection(id, this);
		Point pos = this.getMousePosition();
		popupMenuIntersection.show(this, pos.x, pos.y);
	}
	
	public void setIntersectionSurvol(int idIntersection){
		vuePlan.setIntersectionSurvol(idIntersection);
		if(plan.getIntersection(idIntersection) != null){
			afficherMessage("intersection a l'adresse: "+idIntersection);
		}
	}
	
	public void setLivraisonSurvol(int idLivraison){
		vuePlan.setLivraisonSurvol(idLivraison);
		descriptionPlan.setLivraisonSurligne(idLivraison);
	}

	
	public void afficherMessage(String message){
		console.setText(message);
	}
	
	protected ZoneDeTexte getZoneText(){
		return descriptionPlan;
	}
	
	protected Plan getPlan(){
		return plan;
	}
	
	protected void clicDroitLivraison(int idLivraison){
		controleur.clicDroitLivraison(idLivraison);
	}
	
	protected void clicDroitIntersection(int idIntersection){
		controleur.clicDroitIntersection(idIntersection);
	}
	
	public void ouvrirPopMenuLivraison(int idLivraison){
		Point arg0 = getMousePosition();
		popupMenuLivraison = new PopMenuLivraison(idLivraison, this);
		popupMenuLivraison.show(this.getComponentAt(arg0),(int) arg0.getX(),(int) arg0.getY());
	}
	
	public void ouvrirPopMenuLivraisonInsertion(int idLivraison){
		Point arg0 = getMousePosition();
		popupMenuLivraisonAjout = new PopMenuLivraisonAjout(idLivraison, this);
		popupMenuLivraisonAjout.show(this,(int) arg0.getX(),(int)arg0.getY());
	}
	
	protected void clicIntervertirLivraison(int idLivraison){
		controleur.clicEchangerLivraison(idLivraison);
	}
	
	protected void clicGaucheLivraison(int idLivraison){
		controleur.clicGaucheLivraison(idLivraison);
	}
	
	protected void survolPlan(Point point,int tolerance){
		
		controleur.survolPlan(point, tolerance);
	}
	
	protected void actionAjouterLivraison(int idIntersection){
		vuePlan.setIntersectionSurvol(-1);
		controleur.clicAjouterLivraison(idIntersection);
	}
	
	public void ouvrirMenuCreationLivraison(int position, boolean avant){
		menuCreationLivraison = new MenuCreationLivraison(this, position,avant, new Point(500,300));
	}
	
	protected void supprimerLivraison(int id){
	    controleur.supprimerLivraison(id);
	}
	
	
	protected Controleur getControleur(){
		return controleur;
	}
	
	
	protected void actionChargerPlan(){
		controleur.chargerPlan();
	}
	
	protected void actionChargerDemandeDeLivraison(){
		controleur.chargerDemandeLivraison();
	}
	
	protected boolean actionCalculDeTournee(){
		controleur.calculTournee();
		return true;
	}
	
	protected void actionGenerationFeuilleDeRoute(){
		
	}
	
	protected void actionAnnuler(){
		controleur.undo();
	}
	
	protected void actionRestaurer(){
		controleur.redo();
	}
	
	protected void actionStopCalcul(){
		controleur.arreterCalculTournee();
	}
	protected void actionQuitter(){
		System.out.println("menu quitter cliqué");
		controleur.quitter();
	}
}
