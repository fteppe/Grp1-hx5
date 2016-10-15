package vue;


import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ZoneDeTexte extends JTextArea{

	
	public ZoneDeTexte(int largeur, int hauteur){
		setLineWrap(true);
		setWrapStyleWord(true);
		setEditable(false);
		setSize(largeur,hauteur);
	}
	
	public void afficherTexte(String texte)
	{
		setText(texte);
	}
}
