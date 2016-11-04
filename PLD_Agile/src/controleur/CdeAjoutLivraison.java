package controleur;


import modele.Livraison;
import modele.Plan;

public class CdeAjoutLivraison implements Commande {
	
	private Plan plan;
	private Livraison livraison;
	
	public CdeAjoutLivraison(Plan p, Livraison l){
		this.plan = p;
		this.livraison = l;
	}

	

	@Override
	public void doCde() {
		// TODO Auto-generated method stub
		plan.ajouterLivraison(livraison.getAdresse().getId(), livraison.getDuree());

	}

	@Override
	public void undoCde() {
		// TODO Auto-generated method stub
		plan.supprimerLivraison(livraison.getAdresse().getId(),livraison.getDuree());

	}

}
