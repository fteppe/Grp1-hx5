package vue;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SelectionHeure extends JPanel {

    private JComboBox heure;
    private JComboBox minute;

    public SelectionHeure() {
	super();
	heure = new JComboBox();
	minute = new JComboBox();

	for (int i = 0; i < 24; i++) {
	    if (i < 10) {
		heure.addItem("0" + i);
	    } else {
		heure.addItem(i + "");
	    }
	}
	for (int i = 0; i < 12; i++) {
	    if (i < 10) {
		minute.addItem("0" + i);
	    } else {
		minute.addItem(i + "");
	    }
	}
	setLayout(new GridBagLayout());
	GridBagConstraints cstr = new GridBagConstraints();
	cstr.ipadx = 10;
	add(heure);
	cstr.gridx = 1;
	add(minute);
    }

    public String getHeure() {
	return (String) heure.getSelectedItem() + ":"
		+ (String) minute.getSelectedItem() + ":00";
    }

    public void editable(boolean editable) {
	heure.setEnabled(editable);
	minute.setEnabled(editable);
    }

}
