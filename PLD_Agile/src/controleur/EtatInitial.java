package controleur;


public class EtatInitial extends EtatDefaut {
	// Etat initial 
	
	@Override
	public void quitter() {
	    System.exit(0);
	}
	
	@Override
	public void undo(ListeDeCdes listeDeCdes){
		listeDeCdes.undo();
	}

	@Override
	public void redo(ListeDeCdes listeDeCdes){
		listeDeCdes.redo();
	}


}
