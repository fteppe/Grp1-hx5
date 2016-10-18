package xml;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import modele.Heure;
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
    		
    		//System.out.println("Noeud : id = \""+id+"\" - x = \""+x+"\" - y = \""+y+"\"");
    		// TODO - Envoyer id , x et y à plan pour créer l'intersection
    		plan.ajouterIntersection(id, x, y);
    	}
    	NodeList listeTroncons = noeudDOMRacine.getElementsByTagName("troncon");
    	for(int i = 0 ; i < listeTroncons.getLength() ; i++){
    		Element eltTroncon = (Element) listeTroncons.item(i);
    		
    		int destination = Integer.parseInt(eltTroncon.getAttribute("destination"));
    		int origine = Integer.parseInt(eltTroncon.getAttribute("origine"));
    		int longueur = Integer.parseInt(eltTroncon.getAttribute("longueur"));
    		int vitesse = Integer.parseInt(eltTroncon.getAttribute("vitesse"));
    		String nomRue = eltTroncon.getAttribute("nomRue");

    		//System.out.println("Troncon : origine = \""+origine+"\" - destination = \""+destination+"\" - longueur = \""+longueur+"\" - vitesse = \""+vitesse+"\" - nomRue = \""+nomRue+"\"");
    		// TODO - Envoyer destination, origine, longueur, vitesse et nomRue
    		// 		- à plan pour créer le troncon.
    		plan.ajouterTroncon(nomRue, longueur, vitesse, origine, destination);
    		
    	}
    }
    

    private static void construireLivraisonsAPartirDeDOMXML(Element noeudDOMRacine, Plan plan) throws ExceptionXML, NumberFormatException, IndexOutOfBoundsException{
    	Element eltEntrepot = (Element)noeudDOMRacine.getElementsByTagName("entrepot").item(0);
    	NodeList listeLivraisons = noeudDOMRacine.getElementsByTagName("livraison");
    	
    	int adresseEntrepot = Integer.parseInt(eltEntrepot.getAttribute("adresse"));
    	String heureDepartEntrepot = eltEntrepot.getAttribute("heureDepart");
    	
    	//System.out.println("Entrepot : adresse =\""+adresseEntrepot+"\" - heureDepart = \""+heureDepartEntrepot+"\"");
    	// TODO - Envoyer adresseEntrepot et heureDepartEntrepot au plan pour creer l'entrepôt
    	
		plan.creerDemandeDeLivraison(new Heure(heureDepartEntrepot), adresseEntrepot);
		
    	
    	for(int i = 0 ; i < listeLivraisons.getLength() ; i++){
    		Element eltLivraison = (Element) listeLivraisons.item(i);
    		int adresse = Integer.parseInt(eltLivraison.getAttribute("adresse"));
    		int duree = Integer.parseInt(eltLivraison.getAttribute("duree"));
    		String debutPlage = eltLivraison.getAttribute("debutPlage");
    		String finPlage = eltLivraison.getAttribute("finPlage");
    		
    		//System.out.println("Livraison : adresse = \""+adresse+"\" - duree = \""+duree+"\" - debutPlage = \""+debutPlage+"\" - finPlage = \""+finPlage+"\"");
    		// TODO - Envoyer adresse, duree, debutPLage et finPlage au plan
    		//		- pour créer la livraison
    		plan.ajouterLivraison(adresse, duree);
    	}
    	
    }
    
 
}
