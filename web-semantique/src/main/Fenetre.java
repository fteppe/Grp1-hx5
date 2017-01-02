package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;


public class Fenetre {

	
	private JFrame frmSemanticSearcher;
	private JTextField barreRecherche = new JTextField();
	private JButton btnRecherche = new JButton("Rechercher");
	private JPanel panelRecherche = new JPanel();
	private JPanel panelClusters = new JPanel();
	private JPanel panelPages = new JPanel();

	public static void main(String[] args) throws Exception {
        
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fenetre window = new Fenetre();
					window.frmSemanticSearcher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
		
	/**
	 * Creation de l'application.
	 */
	public Fenetre() {
		initialize();
	}

	/**
	 * Initialisation du contenu de la fenetre.
	 */
	private void initialize() {
		
		// Fenetre principale
		frmSemanticSearcher = new JFrame();
		frmSemanticSearcher.setBackground(SystemColor.text);
		frmSemanticSearcher.setFont(new Font("Calibri Light", Font.PLAIN, 12));
		frmSemanticSearcher.setForeground(new Color(255, 255, 255));
		frmSemanticSearcher.setTitle("Sport'IF Semantic Searcher");
		frmSemanticSearcher.setBounds(100, 100, 1127, 958);
		frmSemanticSearcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSemanticSearcher.getContentPane().setLayout(null);
		// On pourrait changer l'icone
		//frmSemanticSearcher.setIconImage(Toolkit.getDefaultToolkit().getImage(""));
		
		
		// Label Titre
		JLabel lblTitle = new JLabel("Sport'IF Semantic Searcher");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(434, 13, 250, 25);
		frmSemanticSearcher.getContentPane().add(lblTitle);
		
		
		// Panel de Recherche
		panelRecherche.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelRecherche.setBounds(12, 51, 1070, 100);
		frmSemanticSearcher.getContentPane().add(panelRecherche);
		panelRecherche.setLayout(null);
		
		
		// Bouton "Rechercher"
		btnRecherche.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnRecherche.setBounds(851, 20, 158, 50);
		btnRecherche.setVisible(true);
		btnRecherche.addActionListener(new Recherche());
		panelRecherche.add(btnRecherche);
				
		// Barre de recherche
		barreRecherche.setBounds(50, 35, 750, 30);
		barreRecherche.setHorizontalAlignment(JTextField.CENTER);
		panelRecherche.add(barreRecherche);
		
		
		// Panel des Clusters
		panelClusters.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelClusters.setBounds(12, 181, 1070, 200);
		GridLayout layoutCluster = new GridLayout(0,2);
		panelClusters.setLayout(layoutCluster);
		frmSemanticSearcher.getContentPane().add(panelClusters);
		
		// Panel d'affichages des pages
		panelPages.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelPages.setBounds(12, 400, 1070, 500);
		GridLayout layoutPages = new GridLayout(0,1);
		panelPages.setLayout(layoutPages);
		frmSemanticSearcher.getContentPane().add(panelPages);
					
	}
	
	

	/**
	 * @author Nathan
	 * TODO : Ajouter peut etre un label d'attente pendant le calcul
	 * Handler du bouton "Rechercher"
	 */
	public class Recherche implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
        	btnRecherche.setEnabled(false);
        	panelRecherche.repaint();
        	
        	String requete = barreRecherche.getText();
        	List<Cluster> listClusters = new ArrayList<Cluster>();
        	try {
				Sportif sportif = new Sportif();
				listClusters = sportif.execution(requete);
				receptionResultats(listClusters);
				panelPages.repaint();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        	panelPages.repaint();
        }
        
        
        /**
         * Cree les boutons Cluster et les labels d'affichages des pages.
         * @param listeClusters
         */
        public void receptionResultats(List<Cluster> listeClusters)
        {
        	int nbCluster = listeClusters.size();
        	List<Page> listPagesDesordre = new ArrayList<Page>();
        	List<Page> listPagesOrdre = new ArrayList<Page>();
        	List<JLabel> listLabels = new ArrayList<JLabel>();
        	
        	
        	// Creation des boutons de cluster
        	for(Cluster cluster : listeClusters)
        	{
        		String nom = cluster.getNom();
        		List<Page> listPageCluster = cluster.getPages();
        		
        		for(Page page : listPageCluster){
        			listPagesDesordre.add(page);
        		}
        		
        		// TODO : Améliorer lorsque pas de titre. + Ameliorer coupure des noms.
        		// Si pas de nom, on prend le premier mot cle de la premiere page 
        		if(nom=="")
        		{
        			nom = cluster.getPages().get(0).getMotscles().get(0);
        		}

        		int taille = nom.length();
        		String newNom = new String();
        		JButton btnCluster= new JButton(nom);
    		
        		if(taille>39)
        		{
            		newNom = nom.substring(0,39)+ "YYY" +nom.substring(39);
            		btnCluster.setText("<html>" + newNom.replaceAll("YYY", "<br>") + "</html>");
        		}
        		    		
        		btnCluster.setMargin(new Insets(0, 0, 0, 0));
        		btnCluster.setFont(new Font("Tahoma", Font.PLAIN, 14));
        		btnCluster.setBounds((1070/nbCluster)*listeClusters.indexOf(cluster), 0, 1070/nbCluster, 200);
        		btnCluster.addActionListener(new ModificationAffichage(cluster, listPagesOrdre,listLabels));
        		btnCluster.setVisible(true);
        		panelClusters.add(btnCluster);
        		panelClusters.repaint();	
        	}
        	
        	
        	// Creation des labels
        	// Solution fonctionnelle mais degeulasse
        	int number=0;
        	for(int y=0; y<10; y++)
        	{
	        	for(Page page : listPagesDesordre)
	        	{
	        		if(page.getClassement()==number)
	        		{
	        			listPagesOrdre.add(page);
	        			number++;
	        			break;
	        		}
	        	}
        	}
        	
    		for(Page page : listPagesOrdre)
    		{
    			JLabel lblPage = new JLabel(page.getUrl());
    			lblPage.setFont(new Font("Tahoma", Font.PLAIN, 18));
        		lblPage.setHorizontalAlignment(SwingConstants.CENTER);
        		lblPage.setBounds(10, 20, 250, 25);
        		
        		lblPage.addMouseListener(new MouseAdapter()  
        		{  
        			// Si on clique sur le lien, on accede a la page via le navigateur par defaut
        		    public void mouseClicked(MouseEvent e)  
        		    {  
        		    	URI uri = null;
						try {
							uri = new URI(lblPage.getText());
							java.awt.Desktop.getDesktop().browse(uri);
						} catch (URISyntaxException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
       
        		    }
        		    
        		    // Quand on passe la souris sur le lien, le curseur devient une main
        		    public void mouseEntered(MouseEvent e) {
        		        Cursor _cursor = new Cursor(Cursor.HAND_CURSOR);
        		        lblPage.setCursor(_cursor);
        		    }
        		    
        		}); 
        		
        		panelPages.add(lblPage);
        		panelPages.repaint();
        		
        		listLabels.add(lblPage);
    		}
    		panelPages.repaint();
        }
    }
	
	
	/**
	 * Handler des boutons clusters
	 * Lorsque l'on clique sur un bouton d'un cluster, on laisse apparaitre que les liens de notre cluster.
	 * Si on clique un nouvelle fois, tous les liens reapparaissent.
	 */
	public class ModificationAffichage implements ActionListener
    {
		private Cluster cluster;
		private List<Page> pagesCluster = new ArrayList<Page>();
		private List<Page> allPages = new ArrayList<Page>();
		private List<JLabel> listLabels = new ArrayList<JLabel>();
		
	      public ModificationAffichage(Cluster cluster, List<Page> listPagesOrdre, List<JLabel> listLabels) {
			this.cluster = cluster;
			this.pagesCluster = cluster.getPages();
			this.allPages = listPagesOrdre;
			this.listLabels = listLabels;
		}

		public void actionPerformed(ActionEvent e)
	        {
			
				// TODO : Trouver une solution plus propre
	    	  	for(Page page : allPages)
	    	  	{
	    	  		if(!listLabels.get(page.getClassement()).isVisible())
	    	  		{
	    	  			for(Page pageBuff : allPages)
	    	    	  	{
	    	  				listLabels.get(pageBuff.getClassement()).setVisible(true);
		    	  			panelPages.repaint();
	    	    	  	}
	    	  			break;
	    	  		}
    	  			
	    	  		if(!pagesCluster.contains(page))
	    	  		{
	    	  			// On rends invisible
	    	  			if(listLabels.get(page.getClassement()).isVisible())
	    	  			{
		    	  			listLabels.get(page.getClassement()).setVisible(false);
		    	  			panelPages.repaint();
	    	  			}
	    	  			else
	    	  			{
	    	  				listLabels.get(page.getClassement()).setVisible(true);
		    	  			panelPages.repaint();
	    	  			}
	    	  		}
	    	  	}
	        }
    }
    
}
