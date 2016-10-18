package controleur;



import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import modele.Plan;
import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class EtatInitial extends EtatDefaut {
	// Etat initial 

	
	@Override
	public void quitter() {
	    System.exit(0);
	}
	

}
