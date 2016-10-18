package controleur;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import vue.Fenetre;
import xml.DeserialiseurXML;
import xml.ExceptionXML;

public class main {
	public static void main(String[] args){
		
		Fenetre fenetre = new Fenetre("optimod",400,600);
		/*
		try {
			DeserialiseurXML.chargerPlan(null);
			DeserialiseurXML.chargerLivraisons(null);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExceptionXML e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
