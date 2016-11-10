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
    public static String chargerPlan(Plan plan)
	    throws ParserConfigurationException, SAXException, IOException,
	    ExceptionXML {
	String rapport = "";
	File xml;
	try {
	    xml = OuvreurDeFichierXML.getInstance().ouvre(true);
	} catch (Exception e) {
	    throw new ExceptionXML("Chargement annulé");
	}
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
		.newDocumentBuilder();
	Document document = docBuilder.parse(xml);
	Element racine = document.getDocumentElement();
	if (racine.getNodeName().equals("reseau")) {
	    rapport = construirePlanAPartirDeDOMXML(racine, plan);
	} else
	    throw new ExceptionXML(
		    "Document non conforme : le document ne possède pas de racine \"réseau\"");

	return rapport;
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
    public static String chargerLivraisons(Plan plan)
	    throws ParserConfigurationException, SAXException, IOException,
	    ExceptionXML {
	String rapport = "";
	File xml = OuvreurDeFichierXML.getInstance().ouvre(true);
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance()
		.newDocumentBuilder();
	Document document = docBuilder.parse(xml);
	Element racine = document.getDocumentElement();
	if (racine.getNodeName().equals("demandeDeLivraisons")) {
	    try {
		rapport += construireLivraisonsAPartirDeDOMXML(racine, plan);
	    } catch (ExceptionXML | ModeleException e) {
		throw new ExceptionXML(e.getMessage());
	    }
	} else
	    throw new ExceptionXML(
		    "Document non conforme : le document ne possède pas de racine \"demandeDeLivraisons\"");
	return rapport;
    }

    private static String construirePlanAPartirDeDOMXML(Element noeudDOMRacine,
	    Plan plan) {
	String rapport = "";
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
		rapport += "\nCertains noeuds contiennent des attributs \"x\", \"y\" ou \"id\" non entiers.";
	    } catch (ModeleException e) {
		rapport += "\n" + e.getMessage();
	    }
	}
	NodeList listeTroncons = noeudDOMRacine.getElementsByTagName("troncon");
	for (int i = 0; i < listeTroncons.getLength(); i++) {
	    Element eltTroncon = (Element) listeTroncons.item(i);

	    try {
		int destination = Integer
			.parseInt(eltTroncon.getAttribute("destination"));
		int origine = Integer
			.parseInt(eltTroncon.getAttribute("origine"));
		int longueur = Integer
			.parseInt(eltTroncon.getAttribute("longueur"));
		int vitesse = Integer
			.parseInt(eltTroncon.getAttribute("vitesse"));
		String nomRue = eltTroncon.getAttribute("nomRue");

		plan.ajouterTroncon(nomRue, longueur, vitesse, origine,
			destination);
	    } catch (NumberFormatException e) {
		rapport += "\nCertains troncons contiennent des attributs \"destination\", \"origine\", \"longueur\" ou \"vitesse\" non entiers.";
	    } catch (ModeleException e) {
		rapport += "\n" + e.getMessage();
	    }
	}
	return rapport;
    }

    private static String construireLivraisonsAPartirDeDOMXML(
	    Element noeudDOMRacine, Plan plan)
	    throws ExceptionXML, ModeleException {
	String rapport = "";
	if (noeudDOMRacine.getElementsByTagName("entrepot").getLength() == 0) {
	    throw new ExceptionXML(
		    "La demande de livraison ne contient pas d'entrepôt");
	}
	if (noeudDOMRacine.getElementsByTagName("entrepot").getLength() > 1) {
	    rapport += "\nPlusieurs entrepôts ont été détectés dans le document, seul le premier sera pris en compte.";
	}
	Element eltEntrepot = (Element) noeudDOMRacine
		.getElementsByTagName("entrepot").item(0);
	NodeList listeLivraisons = noeudDOMRacine
		.getElementsByTagName("livraison");
	int adresseEntrepot;
	try {
	    adresseEntrepot = Integer
		    .parseInt(eltEntrepot.getAttribute("adresse"));
	} catch (NumberFormatException e) {
	    throw new ExceptionXML(
		    "L'adresse de l'entrepôt n'est pas un identifiant entier.");
	}
	String heureDepartEntrepot = eltEntrepot.getAttribute("heureDepart");

	try {
	    plan.creerDemandeDeLivraison(new Heure(heureDepartEntrepot),
		    adresseEntrepot);
	} catch (ModeleException e) {
	    throw e;
	}

	for (int i = 0; i < listeLivraisons.getLength(); i++) {
	    try {
		Element eltLivraison = (Element) listeLivraisons.item(i);
		int adresse = Integer
			.parseInt(eltLivraison.getAttribute("adresse"));
		int duree = Integer
			.parseInt(eltLivraison.getAttribute("duree"));
		if (eltLivraison.getAttribute("debutPlage") != null
			&& eltLivraison.getAttribute("debutPlage") != "") {
		    String debutPlage = eltLivraison.getAttribute("debutPlage");
		    String finPlage = eltLivraison.getAttribute("finPlage");
		    plan.ajouterLivraisonDemande(adresse, duree, debutPlage,
			    finPlage);
		} else {
		    plan.ajouterLivraisonDemande(adresse, duree);
		}
	    } catch (NumberFormatException e) {
		rapport += "\nL'adresse ou la durée d'une livraison ne sont pas des entiers.";
	    } catch (ModeleException e) {
		rapport += "\n" + e.getMessage();
	    }
	}
	return rapport;
    }

}
