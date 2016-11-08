package vue;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	private ZoneDeTexte zoneDeTexte;
	private VuePlan vuePlan;
	private JPanel panneauNord;
	private JPanel panneauEst;
	private JScrollPane scroll;
	private PopMenuLivraison popupMenuLivraison;
	private PopMenuIntersection popupMenuIntersection;
	private MenuCreationLivraison menuCreationLivraison;
	
	private int intersectionSelectionne; //vaut -1 si aucune intersection n'est selectionné
	
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
		intersectionSelectionne = -1;
		this.plan = plan;
		dimensions = new Vecteur(largeur,hauteur);
		panneauNord = new JPanel();
		panneauEst = new JPanel();		
	    this.controleur = controleur;
	    this.setTitle(titreFenetre);
	    this.setSize((int)dimensions.x,(int)dimensions.y);
	    this.setLocationRelativeTo(null);
	    
	    //menuCreationLivraison = new MenuCreationLivraison(this, 25, true, new Point(0,0));
	    
	    vuePlan = new VuePlan(plan, this);
    	zoneDeTexte = new ZoneDeTexte((int)dimensions.x/3,(int)dimensions.y-30, plan, this);
	    menu = new Menu(this);
	    barreDesTaches = new BarreDesTaches(this);
		scroll = new JScrollPane(zoneDeTexte);
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
		panneauNord.add(barreDesTaches, BorderLayout.SOUTH);
		panneauNord.add(menu, BorderLayout.NORTH);
		panneauEst.add(scroll,BorderLayout.CENTER);
		
	}
	
	public void ouvrirMenuSupprimer(int id){
		popupMenuLivraison = new PopMenuLivraison(id, this);
		Point pos = this.getMousePosition();
		popupMenuLivraison.show(this, pos.x, pos.y);
	}
	
	public void setIntersectionSelectionne(int idIntersection){
		
		if(intersectionSelectionne != idIntersection && vuePlan.getGraphics() !=null){
			intersectionSelectionne = idIntersection;
			vuePlan.update(vuePlan.getGraphics());
		}
		
	}
	
	public void afficherDetailDemandeLivraison(){
		zoneDeTexte.afficherInformationDemandeLivraison();
	}
	public void afficherMessage(String message){
		zoneDeTexte.getTitre().afficher(message);
	}
	
	protected ZoneDeTexte getZoneText(){
		return zoneDeTexte;
	}
	
	protected int getIntersectionSelectionne(){
		return intersectionSelectionne;
	}
	
	protected void clicDroitPlan(Point point){
		if(plan.getIntersection(intersectionSelectionne) == null){
			controleur.clicDroitPlan(point);
		}
		else{
			popupMenuIntersection =new PopMenuIntersection(intersectionSelectionne, this);
			Point pos = this.getMousePosition();
			popupMenuIntersection.show(this,pos.x , pos.y);
		}
	}
	
	protected void survolPlan(Point point,int tolerance){
		
		controleur.survolPlan(point, tolerance);
	}
	
	protected void actionAjouterLivraison(int idIntersection){
		intersectionSelectionne = -1;
		controleur.passerEtatAjouterLivraison(idIntersection);
	}
	
	protected void ajouterLivraisonPosition(int position, boolean avant){
		menuCreationLivraison = new MenuCreationLivraison(this, position,avant, new Point(500,300));
	}
	
	protected void supprimerLivraison(int id){
	    controleur.supprimerLivraison(id);
	}
	protected Controleur getControleur(){
		return controleur;
	}
	
	protected void ajouterLivraison(int idLivraison, int duree){
	}
	
	protected void surlignerLivraison(int idLivraison){
		vuePlan.setLivraisonSurligne(idLivraison);
		vuePlan.dessinerListeLivraisons(vuePlan.getGraphics(), plan.getListeLivraisons());
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
