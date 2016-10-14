package xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import modele.Plan;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class DeserialiseurXML {
	/**
	 * Ouvre un fichier xml et cree plan a partir du contenu du fichier
	 * @param plan
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ExceptionXML
	 */
	public static void chargerPlan(Plan plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = OuvreurDeFichierXML.getInstance().ouvre(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("reseau")) {
           construirePlanAPartirDeDOMXML(racine, plan);
        }
        else
        	throw new ExceptionXML("Document non conforme");
	}

	/**
	 * Ouvre un fichier xml et charge les livraisons a partir du contenu du fichier
	 * @param plan
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ExceptionXML
	 */
	public static void chargerLivraisons(Plan plan) throws ParserConfigurationException, SAXException, IOException, ExceptionXML{
		File xml = OuvreurDeFichierXML.getInstance().ouvre(true);
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
        Document document = docBuilder.parse(xml);
        Element racine = document.getDocumentElement();
        if (racine.getNodeName().equals("demandeDeLivraisons")) {
           construireLivraisonsAPartirDeDOMXML(racine, plan);
        }
        else
        	throw new ExceptionXML("Document non conforme");
	}
	
    private static void construirePlanAPartirDeDOMXML(Element noeudDOMRacine, Plan plan) throws ExceptionXML, NumberFormatException{
    	NodeList listeNoeuds = noeudDOMRacine.getElementsByTagName("noeud");
    	for(int i = 0 ; i < listeNoeuds.getLength() ; i++){
    		Element eltNoeud = (Element) listeNoeuds.item(i);
    		int id = Integer.parseInt(eltNoeud.getAttribute("id"));
    		int x = Integer.parseInt(eltNoeud.getAttribute("x"));
    		int y = Integer.parseInt(eltNoeud.getAttribute("y"));
    		
    		// TODO - Envoyer id , x et y à plan pour créer l'intersection
    		
    	}
    	NodeList listeTroncons = noeudDOMRacine.getElementsByTagName("troncon");
    	for(int i = 0 ; i < listeTroncons.getLength() ; i++){
    		Element eltTroncon = (Element) listeNoeuds.item(i);
    		
    		int destination = Integer.parseInt(eltTroncon.getAttribute("destination"));
    		int origine = Integer.parseInt(eltTroncon.getAttribute("origine"));
    		int longueur = Integer.parseInt(eltTroncon.getAttribute("longueur"));
    		int vitesse = Integer.parseInt(eltTroncon.getAttribute("vitesse"));
    		String nomRue = eltTroncon.getAttribute("nomRue");
    		
    		// TODO - Envoyer destination, origine, longueur, vitesse et nomRue
    		// 		- à plan pour créer le troncon.
    		
    	}
    }
    

    private static void construireLivraisonsAPartirDeDOMXML(Element noeudDOMRacine, Plan plan) throws ExceptionXML, NumberFormatException{
    	
    }
    
 
}
