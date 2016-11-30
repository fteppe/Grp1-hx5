package controleur;

import java.awt.ScrollPane;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import modele.Plan;

public class FenetreFeuilleRoute extends JDialog {
	private JScrollPane scroll;
	private JTextArea feuilleRoute;

	public FenetreFeuilleRoute(Plan plan) {
		super();
		feuilleRoute = new JTextArea(40, 30);
		feuilleRoute.setEditable(false);
		setResizable(false);
		feuilleRoute.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		scroll = new JScrollPane(feuilleRoute);
		add(scroll);
		pack();
		setTitle("feuille de route");
		setVisible(true);
	}
}
