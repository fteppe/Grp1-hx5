package controleur;

import vue.Fenetre;
import modele.Plan;

public abstract class EtatDefaut implements Etat {
	// Definition des comportements par defaut des methodes
	
	public void chargerPlan(Controleur controleur, Plan plan, Fenetre fenetre){}
	
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre){}

	public void calculerTournee(Controleur controleur, Plan plan, Fenetre fenetre){}

	public void quitter(){}

	public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre){}

	public void clicGauche(Plan plan, Fenetre fenetre){}
}
