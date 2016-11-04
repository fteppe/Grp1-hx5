package controleur;

import modele.Livraison;
import modele.Plan;

public class CdeModifierLivraison implements Commande {

	private Plan plan;
	private Livraison anciennelivraison, nouvelleLivraison;
	
	public CdeModifierLivraison(Plan p, Livraison l, Livraison n){
		this.plan = p;
		this.anciennelivraison = l;
		this.nouvelleLivraison=n;
	}

	

	@Override
	public void doCde() {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void undoCde() {
		// TODO Auto-generated method stub
		
	}


}
