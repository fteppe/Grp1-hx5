package xml.tests;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteXMLFile {

    public WriteXMLFile() {

	try {

	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

	    // root elements
	    Document doc = docBuilder.newDocument();
	    Element rootElement = doc.createElement("reseau");
	    doc.appendChild(rootElement);

	    // noeud elements
	    Element noeud = doc.createElement("noeud");
	    rootElement.appendChild(noeud);

	    // set attribute to noeud element
	    Attr id = doc.createAttribute("id");
	    id.setValue("1");
	    noeud.setAttributeNode(id);

	    // set attribute to noeud element
	    Attr x = doc.createAttribute("x");
	    x.setValue("1");
	    noeud.setAttributeNode(x);

	    // set attribute to noeud element
	    Attr y = doc.createAttribute("y");
	    y.setValue("1");
	    noeud.setAttributeNode(y);

	    // troncon elements
	    Element troncon = doc.createElement("noeud");
	    rootElement.appendChild(troncon);

	    // set attribute to troncon element
	    Attr destination = doc.createAttribute("destination");
	    id.setValue("1");
	    troncon.setAttributeNode(destination);

	    // set attribute to troncon element
	    Attr longueur = doc.createAttribute("longueur");
	    longueur.setValue("1");
	    troncon.setAttributeNode(longueur);

	    // set attribute to troncon element
	    Attr nomRue = doc.createAttribute("nomRue");
	    nomRue.setValue("1");
	    troncon.setAttributeNode(nomRue);

	    // set attribute to troncon element
	    Attr origine = doc.createAttribute("origine");
	    origine.setValue("1");
	    troncon.setAttributeNode(origine);

	    // set attribute to troncon element
	    Attr vitesse = doc.createAttribute("vitesse");
	    vitesse.setValue("1");
	    troncon.setAttributeNode(vitesse);

	    // write the content into xml file
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);
	    StreamResult result = new StreamResult(new File("D:\\file.xml"));

	    // Output to console for testing
	    // StreamResult result = new StreamResult(System.out);

	    transformer.transform(source, result);

	    System.out.println("File saved!");

	} catch (ParserConfigurationException pce) {
	    pce.printStackTrace();
	} catch (TransformerException tfe) {
	    tfe.printStackTrace();
	}
    }
}