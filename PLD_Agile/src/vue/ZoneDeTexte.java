package vue;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout.Constraints;

import modele.Heure;
import modele.Itineraire;
import modele.Livraison;
import modele.Plan;
import modele.Troncon;

public class ZoneDeTexte extends JPanel implements Observer {

    private static int VALEUR_INTERSECTION_VIDE = -1;
    private Plan plan;
    private Fenetre fenetre;
    private InformationTextuelle texte;
    private ArrayList<InformationTextuelle> listeInformation;
    private GridBagConstraints contraintes;
    private int livraisonSurligne;

    public ZoneDeTexte(int largeur, int hauteur, Plan plan, Fenetre fenetre) {
	super();
	this.fenetre = fenetre;
	this.plan = plan;

	plan.addObserver(this);
	listeInformation = new ArrayList<InformationTextuelle>();
	ajouterZoneInformation("", 0, false);
	texte = listeInformation.get(0);
	setLayout(new GridBagLayout());
	contraintes = new GridBagConstraints();
	contraintes.gridwidth = GridBagConstraints.REMAINDER;
	contraintes.anchor = GridBagConstraints.PAGE_START;
	contraintes.fill = GridBagConstraints.HORIZONTAL;
	contraintes.gridy = 0;
	setLivraisonSurligne(-1);
	afficherInformations();
    }

    protected int getLivraisonAvantId(int id) {
		int indexPrec = -1;// la position dans la liste de l'infotextuelle
				   // precedente.
		for (int i = 0; i < listeInformation.size(); i++, indexPrec++) {
		    InformationTextuelle info = listeInformation.get(i);
		    if (info.getIndex() == id) {
			return listeInformation.get(indexPrec).getIndex();
		    }
		}
		return VALEUR_INTERSECTION_VIDE;
    }
    
    protected void setLivraisonSurligne(int idLivraison){
    	if(livraisonSurligne != idLivraison){
    		livraisonSurligne = idLivraison;
    	}
    }
    
    private InformationTextuelle getInfoParIndex(int index){
    	for(InformationTextuelle info : listeInformation){
    		if(info.getIndex() == index){
    			return info;
    		}
    	}
    	return null;
    }
    
    protected int getLivraisonApresId(int id) {
	int indexSuivant = 1;
	for (int i = 0; i < listeInformation.size(); i++, indexSuivant++) {
	    InformationTextuelle info = listeInformation.get(i);
	    if (info.getIndex() == id) {
		return listeInformation.get(indexSuivant).getIndex();
	    }
	}
	return VALEUR_INTERSECTION_VIDE;
    }

    private void ajouterZoneInformation(String information, int index, boolean cliquable) {
	InformationTextuelle info = new InformationTextuelle(information, index, fenetre, cliquable);
	listeInformation.add(info);
    }

    private void ajouterZoneInformation(String information, int index) {
	ajouterZoneInformation(information, index, true);
    }

    private void ajouterDescLivraison(String information, int index, boolean valide) {
	DescriptionLivraison description = new DescriptionLivraison(information, index, fenetre, valide);
	listeInformation.add(description);
    }

    private void insererZoneInformationPosition(String information, int indexInformation, int position) {
	InformationTextuelle info = new InformationTextuelle(information, indexInformation, fenetre);
	listeInformation.add(position, info);
    }

    public InformationTextuelle getTitre() {
	return texte;
    }

    /*
     * Fonction qui génère et affiche les informations d'une demande de
     * livraison
     * 
     */
    public void afficherInformationDemandeLivraison() {
	viderListeInfos();
	List<Livraison> livraisons = plan.getListeLivraisons();
	if (livraisons != null) {
	    getTitre().afficher("Feuille de route de la tournée");
	    ajouterZoneInformation("Départ de l'entrepôt à l'adresse " + plan.getEntrepot().getId() + " prévu à "
		    + plan.getHeureDepart(), plan.getEntrepot().getId());
	    for (Livraison livraison : livraisons) {
		contraintes.gridy = listeInformation.size();
		String plage = "";
		if (livraison.possedePlage()) {
		    plage = " de " + livraison.getDebutPlage().afficherHoraire() + " a "
			    + livraison.getFinPlage().afficherHoraire();
		}
		if (livraison.getHeureArrivee() != null) {
		    plage += "\nHeure d'arrivée : " + livraison.getHeureArrivee().afficherHoraire();
		    if (livraison.possedePlage()) {
			if (livraison.getTpsAttente().toSeconds() != 0) {
			    plage += "\nTemps d'attente : " + livraison.getTpsAttente().afficherHoraire();
			}
		    }
		    plage += "\nHeure de départ : " + livraison.getHeureDepart().afficherHoraire();
		}
		ajouterDescLivraison("Livraison a l'adresse " + livraison.getAdresse().getId() + plage,
			livraison.getAdresse().getId(), livraison.getRespectePlage());
	    }
	    if (plan.getHeureRetour() != null) {
		ajouterZoneInformation("Retour à l'entrepôt à l'adresse " + plan.getEntrepot().getId() + " prévu à "
			+ plan.getHeureRetour(), plan.getEntrepot().getId());
	    }
	    afficherInformations();
	}
    }

    @Override
    public void update(Observable arg0, Object arg1) {
	afficherInformationDemandeLivraison();
    }

    private void supprimerInformation(int position) {
	listeInformation.remove(position);
    }

    public void viderListeInfos() {
	int nbElementSupprimer = listeInformation.size();
	for (int i = 1; i < nbElementSupprimer; i++) {
	    supprimerInformation(1);
	}
    }

    public void afficherInformations() {
	this.removeAll();
	int i = 0;
	for (InformationTextuelle info : listeInformation) {
	    i++;
	    contraintes.gridy = i;
	    add(info, contraintes);
	}
    }
}
