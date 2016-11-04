package controleur;


public class CdeInverse implements Commande{
	private Commande commande;
	
	/**
	 * Cree la commande inverse a la commande cde (de sorte que cde.doCde corresponde a this.undoCde, et vice-versa) 
	 * @param commande
	 */
	public CdeInverse(Commande commande){
		this.commande = commande;
	}

	@Override
	public void doCde() {
	    commande.undoCde();
	}

	@Override
	public void undoCde() {
	    commande.doCde();
	}

}

