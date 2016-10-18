package controleur;

import modele.Plan;
import vue.Fenetre;

public class EtatTourneeCalculee extends EtatDefaut {
	// Etat atteint une fois que la tournée est calculée

	@Override
	public void chargerPlan(Controleur controleur, Plan plan, Fenetre fenetre) {
		
	}
	
	@Override
	public void chargerDemandeLivraison(Controleur controleur, Plan plan, Fenetre fenetre) {
		
	}
	
	@Override
	public void quitter() {
		
	}
	
	/*
	@Override
	public void genererFeuilleDeRoute(Plan plan, Fenetre fenetre) {
		
	}
	*/
}
