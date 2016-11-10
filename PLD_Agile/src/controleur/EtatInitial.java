package controleur;

public class EtatInitial extends EtatDefaut {
    // Etat initial

    @Override
    public void quitter() {
	System.exit(0);
    }

}
