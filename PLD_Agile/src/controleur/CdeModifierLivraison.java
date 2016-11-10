package controleur;

import modele.Heure;
import modele.Plan;

public class CdeModifierLivraison implements Commande {

	private Plan plan;
	private boolean ancPlage, nvPlage;
	private Heure nvDebut = null, nvFin = null;
	private Heure ancDebut = null, ancFin = null;
	private int adrLivraison;

	public CdeModifierLivraison(Plan p, int adrLiv, boolean possedePlage, String nvHeureDebut, String nvHeureFin) {
		this.plan = p;
		this.nvPlage = possedePlage;
		if (nvPlage) {
			this.nvDebut = new Heure(nvHeureDebut);
			this.nvFin = new Heure(nvHeureFin);
		}
		if (p.getLivraisonParAdresse(adrLiv).possedePlage()) {
			ancPlage = p.getLivraisonParAdresse(adrLiv).possedePlage();
			ancDebut = p.getLivraisonParAdresse(adrLiv).getDebutPlage();
			ancFin = p.getLivraisonParAdresse(adrLiv).getFinPlage();
		}
		this.adrLivraison = adrLiv;
	}

	@Override
	public void doCde() {
		plan.modifierPlageLivraison(adrLivraison, nvPlage, nvDebut, nvFin);
	}

	@Override
	public void undoCde() {
		plan.modifierPlageLivraison(adrLivraison, ancPlage, ancDebut, ancFin);
	}

}
