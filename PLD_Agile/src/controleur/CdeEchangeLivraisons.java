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

	if (precLiv1 == idLiv2) {
	    Livraison liv2 = plan.retirerLivraisonTournee(idLiv2);
	    if (liv2.possedePlage()) {
		plan.insererLivraisonTournee(idLiv2, liv2.getDuree(), liv2.getDebutPlage().toString(),
			liv2.getFinPlage().toString(), idLiv1, suivLiv1);
	    } else {
		plan.insererLivraisonTournee(idLiv2, liv2.getDuree(), null, null, idLiv1, suivLiv1);
	    }
	} else if (precLiv2 == idLiv1) {
	    Livraison liv1 = plan.retirerLivraisonTournee(idLiv1);
	    if (liv1.possedePlage()) {
		plan.insererLivraisonTournee(idLiv1, liv1.getDuree(), liv1.getDebutPlage().toString(),
			liv1.getFinPlage().toString(), idLiv2, suivLiv2);
	    } else {
		plan.insererLivraisonTournee(idLiv1, liv1.getDuree(), null, null, idLiv2, suivLiv2);
	    }
	} else {
	    Livraison liv1 = plan.retirerLivraisonTournee(idLiv1);
	    Livraison liv2 = plan.retirerLivraisonTournee(idLiv2);
	    if (liv1.possedePlage()) {
		plan.insererLivraisonTournee(idLiv1, liv1.getDuree(), liv1.getDebutPlage().toString(),
			liv1.getFinPlage().toString(), precLiv2, suivLiv2);
	    } else {
		plan.insererLivraisonTournee(idLiv1, liv1.getDuree(), null, null, precLiv2, suivLiv2);
	    }

	    if (liv2.possedePlage()) {
		plan.insererLivraisonTournee(idLiv2, liv2.getDuree(), liv2.getDebutPlage().toString(),
			liv2.getFinPlage().toString(), precLiv1, suivLiv1);
	    } else {
		plan.insererLivraisonTournee(idLiv2, liv2.getDuree(), null, null, precLiv1, suivLiv1);
	    }
	}
    }

    @Override
    public void undoCde() {
	this.doCde();
    }

}
