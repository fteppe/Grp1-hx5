package xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

public class OuvreurDeFichierXML extends FileFilter {// Singleton

    private static OuvreurDeFichierXML instance = null;

    private OuvreurDeFichierXML() {
    }

    protected static OuvreurDeFichierXML getInstance() {
	if (instance == null)
	    instance = new OuvreurDeFichierXML();
	return instance;
    }

    public File ouvre(boolean lecture) throws ExceptionXML {
	int returnVal;
	JFileChooser jFileChooserXML = new JFileChooser();
	String path = lireFichier("./pathFolder.txt");
	if (path != null) {
	    jFileChooserXML.setCurrentDirectory(new File(path));
	}
	jFileChooserXML.setFileFilter(this);
	jFileChooserXML.setFileSelectionMode(JFileChooser.FILES_ONLY);
	if (lecture)
	    returnVal = jFileChooserXML.showOpenDialog(null);
	else
	    returnVal = jFileChooserXML.showSaveDialog(null);
	if (returnVal != JFileChooser.APPROVE_OPTION)
	    throw new ExceptionXML("Probleme a l'ouverture du fichier");
	path = jFileChooserXML.getSelectedFile().getAbsolutePath();
	ecrireFichier("./pathFolder.txt", path);
	return new File(path);
    }

    @Override
    public boolean accept(File f) {
	if (f == null)
	    return false;
	if (f.isDirectory())
	    return true;
	String extension = getExtension(f);
	if (extension == null)
	    return false;
	return extension.contentEquals("xml");
    }

    @Override
    public String getDescription() {
	return "Fichier XML";
    }

    private String getExtension(File f) {
	String filename = f.getName();
	int i = filename.lastIndexOf('.');
	if (i > 0 && i < filename.length() - 1)
	    return filename.substring(i + 1).toLowerCase();
	return null;
    }

    private void ecrireFichier(String adresseDuFichier, String texte) {
	try {
	    FileWriter fw = new FileWriter(adresseDuFichier, false);
	    BufferedWriter output = new BufferedWriter(fw);
	    output.write(texte);
	    output.flush();
	    output.close();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
    }

    private String lireFichier(String fichier) {
	String ligne = "";
	try {
	    InputStream ips = new FileInputStream(fichier);
	    InputStreamReader ipsr = new InputStreamReader(ips);
	    BufferedReader br = new BufferedReader(ipsr);
	    ligne = br.readLine();
	    br.close();
	    return ligne;
	} catch (Exception e) {
	    return null;
	}

    }

}
