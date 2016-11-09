package vue;

import java.awt.Point;

import modele.Plan;

public class MenuModificationLivraison extends MenuCreationLivraison{
	public MenuModificationLivraison(Fenetre fenetre, int idLivraison,boolean avant, Point position)
	{
		super(fenetre, idLivraison, avant, position);
	}
	
	@Override
	private void validation(){
		int idAvant;
		int idApres;
		int dureeInt;
		
		Plan plan = fenetre.getPlan();
		
		if(duree.getText().isEmpty()){
			dureeInt = 0;
		}
		else
		{
			dureeInt = Integer.parseInt(duree.getText())*60;
		}
		
		if(idAvant == -1){
			idAvant = plan.getEntrepot().getId();
		}
		else if(idApres == -1){
			idApres = plan.getEntrepot().getId();
		}
		if(horaireActif.isSelected()){
			fenetre.getControleur().clicAjouterLivraisonPosition(idAvant, idApres, dureeInt, heureArrive.getHeure()+":00", heureDepart.getHeure()+":00");
		}
		else{
			fenetre.getControleur().clicAjouterLivraisonPosition(idAvant, idApres, dureeInt);
		}
		
		
	}
}
