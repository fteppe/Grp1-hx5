package xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import modele.Heure;
import modele.ModeleException;
import modele.Plan;

public class DeserialiseurXML {
    /**
     * Ouvre un fichier xml et cree plan a partir du contenu du fichier
     * 
     * @param plan
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public static void chargerPlan(Plan plan)
	    throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
	File xml;
	try {
	    xml = OuvreurDeFichierXML.getInstance().ouvre(true);
	} catch (Exception e) {
	    throw new ExceptionXML("Chargement annulé");
	}
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document document = docBuilder.parse(xml);
	Element racine = document.getDocumentElement();
	if (racine.getNodeName().equals("reseau")) {
	    construirePlanAPartirDeDOMXML(racine, plan);
	} else
	    throw new ExceptionXML("Document non conforme : le document ne possède pas de racine \"réseau\"");
    }

    /**
     * Ouvre un fichier xml et charge les livraisons a partir du contenu du
     * fichier
     * 
     * @param plan
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws ExceptionXML
     */
    public static void chargerLivraisons(Plan plan)
	    throws ParserConfigurationException, SAXException, IOException, ExceptionXML {
	File xml = OuvreurDeFichierXML.getInstance().ouvre(true);
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document document = docBuilder.parse(xml);
	Element racine = document.getDocumentElement();
	if (racine.getNodeName().equals("demandeDeLivraisons")) {
	    construireLivraisonsAPartirDeDOMXML(racine, plan);
	} else
	    throw new ExceptionXML(
		    "Document non conforme : le document ne possède pas de racine \"demandeDeLivraisons\"");
    }

    private static void construirePlanAPartirDeDOMXML(Element noeudDOMRacine, Plan plan) throws ExceptionXML {
	NodeList listeNoeuds = noeudDOMRacine.getElementsByTagName("noeud");
	plan.viderPlan();
	for (int i = 0; i < listeNoeuds.getLength(); i++) {
	    Element eltNoeud = (Element) listeNoeuds.item(i);
	    int id;
	    int x;
	    int y;
	    try {
		id = Integer.parseInt(eltNoeud.getAttribute("id"));
		x = Integer.parseInt(eltNoeud.getAttribute("x"));
		y = Integer.parseInt(eltNoeud.getAttribute("y"));
		plan.ajouterIntersection(id, x, y);
	    } catch (NumberFormatException e) {
		throw new ExceptionXML("Certains noeuds contiennent des attributs \"x\", \"y\" ou \"id\" non entiers.");
	    } catch (ModeleException e) {
		throw new ExceptionXML(e.getMessage());
	    }
	}
	NodeList listeTroncons = noeudDOMRacine.getElementsByTagName("troncon");
	for (int i = 0; i < listeTroncons.getLength(); i++) {
	    Element eltTroncon = (Element) listeTroncons.item(i);

	    try {
		int destination = Integer.parseInt(eltTroncon.getAttribute("destination"));
		int origine = Integer.parseInt(eltTroncon.getAttribute("origine"));
		int longueur = Integer.parseInt(eltTroncon.getAttribute("longueur"));
		int vitesse = Integer.parseInt(eltTroncon.getAttribute("vitesse"));
		String nomRue = eltTroncon.getAttribute("nomRue");

		plan.ajouterTroncon(nomRue, longueur, vitesse, origine, destination);
	    } catch (NumberFormatException e) {
		throw new ExceptionXML(
			"Certains troncons contiennent des attributs \"destination\", \"origine\", \"longueur\" ou \"vitesse\" non entiers.");
	    } catch (ModeleException e) {
		throw new ExceptionXML(e.getMessage());
	    }

	}
    }

    private static void construireLivraisonsAPartirDeDOMXML(Element noeudDOMRacine, Plan plan)
	    throws ExceptionXML, NumberFormatException, IndexOutOfBoundsException {
	if(noeudDOMRacine.getElementsByTagName("entrepot").getLength() > 1) {
	    throw new ExceptionXML("Plusieurs entrepôts ont été détectés dans le document, seul le premier sera pris en compte.");
	}
	Element eltEntrepot = (Element) noeudDOMRacine.getElementsByTagName("entrepot").item(0);
	NodeList listeLivraisons = noeudDOMRacine.getElementsByTagName("livraison");

	int adresseEntrepot = Integer.parseInt(eltEntrepot.getAttribute("adresse"));
	String heureDepartEntrepot = eltEntrepot.getAttribute("heureDepart");

	// TODO - Gerer les exceptions

	plan.creerDemandeDeLivraison(new Heure(heureDepartEntrepot), adresseEntrepot);

	for (int i = 0; i < listeLivraisons.getLength(); i++) {
	    Element eltLivraison = (Element) listeLivraisons.item(i);
	    int adresse = Integer.parseInt(eltLivraison.getAttribute("adresse"));
	    int duree = Integer.parseInt(eltLivraison.getAttribute("duree"));
	    if (eltLivraison.getAttribute("debutPlage") != null && eltLivraison.getAttribute("debutPlage") != "") {
		String debutPlage = eltLivraison.getAttribute("debutPlage");
		String finPlage = eltLivraison.getAttribute("finPlage");
		plan.ajouterLivraisonDemande(adresse, duree, debutPlage, finPlage);
	    } else {
		plan.ajouterLivraisonDemande(adresse, duree);
	    }
	}

    }

}
