package controleur;

import modele.Livraison;
import modele.Plan;

public class CdeEchangeLivraisons implements Commande {

    private Plan plan;
    private int idLiv1;
    private int idLiv2;

    public CdeEchangeLivraisons(Plan plan, int idLiv1, int idLiv2) {
	this.plan = plan;
	this.idLiv1 = idLiv1;
	this.idLiv2 = idLiv2;
    }

    @Override
    public void doCde() {
	int precLiv1 = plan.getAdresseLivraisonPrecedente(idLiv1);
	int suivLiv1 = plan.getAdresseLivraisonSuivante(idLiv1);
	int precLiv2 = plan.getAdresseLivraisonPrecedente(idLiv2);
	int suivLiv2 = plan.getAdresseLivraisonSuivante(idLiv2);

	Livraison liv1 = plan.retirerLivraisonTournee(idLiv1);
	Livraison liv2 = plan.retirerLivraisonTournee(idLiv2);

	plan.insererLivraisonTournee(idLiv2, liv1.getDuree(), liv1.getDebutPlage().toString(),
		liv1.getFinPlage().toString(), precLiv2, suivLiv2);
	plan.insererLivraisonTournee(idLiv1, liv2.getDuree(), liv2.getDebutPlage().toString(),
		liv2.getFinPlage().toString(), precLiv1, suivLiv1);
    }

    @Override
    public void undoCde() {
	this.doCde();
    }

}
