package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import modele.Plan;

public class MenuCreationLivraison extends JDialog{
	
	private JRadioButton horaireActif;
	private JPanel champsEntree;
	private JPanel boutons;
	private JLabel labelDuree;
	private JLabel labelArrive;
	private JLabel labelDepart;
	private JTextField duree;
	private SelectionHeure heureDepart;
	private SelectionHeure heureArrive;
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
		
		horaireActif = new JRadioButton("Fenêtre de passage sur la livraison?");
		horaireActif.setSelected(false);
		labelDuree = new JLabel("duree livraison (min)");
		labelArrive = new JLabel("heure d'arrivée");
		labelDepart = new JLabel("heure de départ");
		duree = new JTextField(TAILLE_TEXT_FIELD);
		heureArrive = new SelectionHeure();
		heureDepart = new SelectionHeure();
		heureArrive.editable(false);
		heureDepart.editable(false);;
		ok = new JButton("ok");
		annuler = new JButton("annuler");
		
		horaireActif.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				heureArrive.editable(horaireActif.isSelected());
				heureDepart.editable(horaireActif.isSelected());
			}
		});
		
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
		
		placerComposant();
		pack();
		setVisible(true);
	}
	
	
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
		
		if(avant){
			idAvant = plan.getAdresseLivraisonPrecedente(idLivraison);
			idApres = idLivraison;
		}
		else{
			idAvant = idLivraison;
			idApres = plan.getAdresseLivraisonSuivante(idLivraison);
		}
		
		if(idAvant == -1){
			idAvant = plan.getEntrepot().getId();
		}
		else if(idApres == -1){
			idApres = plan.getEntrepot().getId();
		}
		System.out.println(idAvant+" "+idApres);
		if(horaireActif.isSelected()){
			fenetre.getControleur().clicAjouterLivraisonPosition(idAvant, idApres, dureeInt, heureArrive.getHeure()+":00", heureDepart.getHeure()+":00");
		}
		else{
			fenetre.getControleur().clicAjouterLivraisonPosition(idAvant, idApres, dureeInt);
		}
		
		
	}
	
	private void placerComposant(){
		
		champsEntree.setLayout(new GridBagLayout());
		GridBagConstraints contraintes = new GridBagConstraints();
		
		contraintes.anchor = GridBagConstraints.LINE_END;
		contraintes.ipadx = 10;
		contraintes.gridx = 0;
		contraintes.gridy = 0;
		champsEntree.add(labelDuree , contraintes);
		contraintes.gridx++;
		champsEntree.add(duree , contraintes);
		contraintes.gridx = 0;
		contraintes.gridy++;
		champsEntree.add(horaireActif , contraintes);
		contraintes.gridx = 0;
		contraintes.gridy++;
		champsEntree.add(labelArrive , contraintes);
		contraintes.gridx++;
		champsEntree.add(heureArrive , contraintes);
		contraintes.gridx = 0;
		contraintes.gridy++;
		champsEntree.add(labelDepart , contraintes);
		contraintes.gridx++;
		champsEntree.add(heureDepart , contraintes);
		
		setLayout(new BorderLayout());
		boutons.add(ok);
		boutons.add(annuler);
		add(champsEntree,BorderLayout.CENTER);
		add(boutons, BorderLayout.SOUTH);
	}
}