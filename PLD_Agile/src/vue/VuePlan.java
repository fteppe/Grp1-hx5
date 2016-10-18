package vue;

import modele.Plan;

public class VuePlan implements Observeur{
	
	private Plan planDuModele;
	
	public void creerVuePlan(){
		
	}
	
	public VuePlan(Plan plan)
	{
		planDuModele = plan; 
	}

}
