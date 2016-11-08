package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MenuCreationLivraison extends JDialog{
	
	private JRadioButton horaire;
	private JPanel champsEntree;
	private JPanel boutons;
	private JLabel labelDuree;
	private JLabel labelArrive;
	private JLabel labelDepart;
	private JTextField duree;
	private JTextField heureArrive;
	private JTextField heureDepart;
	private JButton ok;
	private JButton annuler;
	
	private static int TAILLE_TEXT_FIELD = 15;
	private boolean avant;
	private int idLivraison;
	private Fenetre fenetre;
	
	public MenuCreationLivraison(Fenetre fenetre, int idLivraison,boolean avant, Point position){
		super(fenetre);
		setLocation(position);
		this.fenetre = fenetre;
		this.idLivraison = idLivraison;
		this.avant = avant;
		setResizable(false);
		champsEntree = new JPanel();
		boutons = new JPanel();
		setSize(new Dimension(350, 150));
		
		System.out.println("MENU CREATION LIVRAISON");
		horaire = new JRadioButton("Fenêtre de passage sur la livraison?");
		horaire.setSelected(false);
		labelDuree = new JLabel("duree livraison (min)");
		labelArrive = new JLabel("heure d'arrivée");
		labelDepart = new JLabel("heure de départ");
		duree = new JTextField(TAILLE_TEXT_FIELD);
		heureArrive = new JTextField(TAILLE_TEXT_FIELD);
		heureDepart = new JTextField(TAILLE_TEXT_FIELD);
		champsEntree.add(labelDuree);
		champsEntree.add(duree);
		champsEntree.add(horaire);
		champsEntree.add(labelArrive);
		champsEntree.add(heureArrive);
		champsEntree.add(labelDepart);
		champsEntree.add(heureDepart);
		
		ok = new JButton("ok");
		annuler = new JButton("annuler");
		
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				validation();
				setVisible(false);
			}
		});
		
		annuler.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		setLayout(new BorderLayout());
		
		boutons.add(ok);
		boutons.add(annuler);
		add(champsEntree,BorderLayout.CENTER);
		add(boutons, BorderLayout.SOUTH);
		setVisible(true);
	}
	
	
	private void validation(){
		int idAvant;
		int idApres;
		int dureeInt = Integer.parseInt(duree.getText())*60;
		if(avant){
			idAvant = fenetre.getZoneText().getLivraisonAvantId(idLivraison);
			idApres = idLivraison;
		}
		else{
			idAvant = idLivraison;
			idApres = fenetre.getZoneText().getLivraisonApresId(idLivraison);
		}
		if(horaire.isSelected()){
			fenetre.getControleur().clicAjouterLivraisonPosition(idAvant, idApres, dureeInt, heureArrive.getText()+":00", heureDepart.getText()+":00");
		}
		else{
			fenetre.getControleur().clicAjouterLivraisonPosition(idAvant, idApres, dureeInt);
		}
		
		
	}
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