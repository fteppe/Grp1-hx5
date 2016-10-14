package vue;

import java.awt.Graphics;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;

public class Menu extends JMenuBar{
	
	private JMenu fichiers = new JMenu("fichier"); 
	private JMenu edition = new JMenu("edition");
	public Menu(){
		  fichiers = new JMenu("fichier"); 
		  edition = new JMenu("edition");
		  this.add(fichiers);
		  this.add(edition);
		  }
}
