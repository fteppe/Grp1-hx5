package util;

import java.io.StringReader;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.Cluster;
import main.Page;
import spark.ResponseTransformer;

/**
 * Classe permettant de transformer des données en objet Json.
 * @author utilisateur
 *
 */
public class JsonTransformer implements ResponseTransformer {

    private Gson gson = new GsonBuilder()
                            .disableHtmlEscaping()
                            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                            .setPrettyPrinting()
                            .serializeNulls()
                            .create();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
    
    /**
     * Transforme une chaîne de caractère en object Json
     * @param data Chaîne à transformer
     * @return Objet Json obtenu
     * @throws JSONException
     */
    public static JSONObject transform(String data) throws JSONException {
	StringBuilder jsonString = new StringBuilder(data);
	JSONObject jsonObj = new JSONObject(jsonString.toString());
	return jsonObj;
    }
    
    /**
     * Transforme une liste de clusters en objet Json
     * @param listClusters Liste de clusters à transformer
     * @return Objet Json obtenu
     * @throws JSONException
     */
    public static JSONObject transform(List<Cluster> listClusters) throws JSONException {
	// create the albums object
        JSONObject jThemes = new JSONObject();
        JSONArray jListClusters = new JSONArray();
        for(Cluster cluster : listClusters) {
            JSONObject jCluster = new JSONObject();
            String nom = cluster.getNom();
            // Si il n'y a pas de nom, on prend le premier mot de la première page
            if(nom=="") {
        	nom = cluster.getPages().get(0).getMotscles().get(0);
            } else {
        	jCluster.put("nom", nom);
            }
            JSONArray listPages = new JSONArray();
            //Pour chaque cluster, on indique les pages lui correspondant (pas d'ordre pour le moment)
            for(Page page : cluster.getPages()){
        	JSONObject jPage = new JSONObject();
        	jPage.put("url", page.getUrl());
        	jPage.put("classement", page.getClassement());
        	listPages.put(jPage);
            }
            jCluster.put("pages", listPages);
            jListClusters.put(jCluster);
        }
        jThemes.put("clusters", jListClusters);
	return jThemes;
    }
}