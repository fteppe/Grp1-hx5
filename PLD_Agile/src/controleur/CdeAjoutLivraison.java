package controleur;


import modele.Intersection;
import modele.Livraison;
import modele.Plan;

public class CdeAjoutLivraison implements Commande {
	
	private Plan plan;
	private int idIntersection;
	private Intersection intersection;
	private int idPrec;
	private int idSuiv;
	private String debutPlage;
	private String finPlage;
	private int duree;
	
	
	public CdeAjoutLivraison(Plan p, int idLivraison){
		this.plan = p;
		this.idIntersection = idLivraison;
	}
	
	public CdeAjoutLivraison(Plan p, Intersection intersection){
		this.plan = p;
		this.idIntersection = intersection.getId();
		this.intersection = intersection;
	}
	
	public CdeAjoutLivraison(Plan p, Intersection intersection, int duree, String debutPlage, String finPlage){
		this.plan = p;
		this.idIntersection = intersection.getId();
		this.intersection = intersection;
		this.debutPlage = debutPlage;
		this.finPlage = finPlage;
		this.idPrec = plan.getAdresseLivraisonPrecedente(idIntersection);
		this.idSuiv = plan.getAdresseLivraisonSuivante(idIntersection);
	}

	

	@Override
	public void doCde() {
	    // Appel à insererLivraisonTournee avec id des intersections
	    // precedentes et suivantes
	    
	    plan.insererLivraisonTournee(idIntersection, duree, debutPlage, finPlage, idPrec, idSuiv);

	}
	
	@Override
	public void undoCde() {
		plan.retirerLivraisonTournee(idIntersection);
	}

}
