package main;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import util.JsonTransformer;


/**
 * Cette classe permet de lancer un serveur 
 * traitant les requêtes externes concernant la détection de thèmes à partir d'un ensemble
 * de sites web.
 * @author utilisateur
 *
 */
public class Serveur {
    public static Services services = new Services();
    
    /**
     * Méthode permettant le lancement du serveur sur l'adresse http://localhost:4567 
     * @param args 
     */
    public static void main(String[] args) {
	enableCORS("*", "*", "*"); //On autorise les requêtes cross-domain
        get("/hello", (req, res) -> "Hello World"); //Chemin de test
        
        get("/themes", (request, response) -> { //Chemin principal
            String userRequest = request.queryParams("request");
            System.out.println(userRequest);
            List<Cluster> listClusters = services.execution(userRequest); //On forme des clusters a partir des résultats reçus
            JSONObject jThemes = JsonTransformer.transform(listClusters); //On transforme ces résultats pour les communiquer sous la forme d'une variable json
            System.out.println(jThemes);
            return jThemes.toString();
        });
    }
    
 // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
    

}
