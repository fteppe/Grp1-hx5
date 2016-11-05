package controleur;


import modele.Livraison;
import modele.Plan;

public class CdeAjoutLivraison implements Commande {
	
	private Plan plan;
	private int idLivraison;
	private Livraison livraison;
	
	public CdeAjoutLivraison(Plan p, int idLivraison){
		this.plan = p;
		this.idLivraison = idLivraison;
	}
	
	public CdeAjoutLivraison(Plan p, Livraison livraison){
		this.plan = p;
		this.idLivraison = livraison.getAdresse().getId();
		this.livraison = livraison;
	}

	

	@Override
	public void doCde() {
	    // Appel à insererLivraisonTournee avec id des intersections
	    // precedentes et suivantes
	    // plan.insererLivraisonTournee(livraison, adrPrec, adrSuiv);

	}
	
	@Override
	public void undoCde() {
		plan.retirerLivraisonTournee(idLivraison);
	}

}
