package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MenuCreationLivraison extends JDialog{
	
	private int idLivraison;
	
	private JRadioButton horaire;
	private JLabel labelArrive;
	private JLabel labelDepart;
	private JTextField heureArrive;
	private JTextField heureDepart;
	private JButton ok;
	private JButton annuler;
	
	private Fenetre fenetre;
	
	public MenuCreationLivraison(Fenetre fenetre, int idLivraison){
		super();
		this.fenetre = fenetre;
		this.idLivraison = idLivraison;
		System.out.println("MENU CREATION LIVRAISON");
		horaire = new JRadioButton("Fenêtre de passage sur la livraison?");
		horaire.setSelected(false);
		labelArrive = new JLabel("heure d'arrivée");
		labelDepart = new JLabel("heure de départ");
		heureArrive = new JTextField();
		heureDepart = new JTextField();
		add(horaire);
		add(labelArrive);
		add(heureArrive);
		add(labelDepart);
		add(heureDepart);
		
		ok = new JButton("ok");
		annuler = new JButton("annuler");
		
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//InfoMenuAjouterLivraison info =retourBoiteDialogue();
				//fenetre.ajouterLivraison(info.idLivraison, info.duree);
				setVisible(false);
			}
		});
		
		annuler.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		add(ok);
		add(annuler);
		
		setVisible(true);
	}
	
	/*public InfoMenuAjouterLivraison retourBoiteDialogue(){
		if(horaire.isSelected() == true)
		{
			//renvoie les informations d'horaire
			//return new InfoMenuAjouterLivraison(idLivraison,  heureDepart.getText(), arrivee)
		}
		else
		{
			//
		}
	}*/
}


class InfoMenuAjouterLivraison{
	protected int duree;
	protected int depart;
	protected int arrivee;
	protected int idLivraison;
	public InfoMenuAjouterLivraison(int idLivraison ,int depart, int arrivee) {
		this.depart = depart;
		this.arrivee = arrivee;
		this.idLivraison = idLivraison;
		duree = depart - arrivee;
	}
}