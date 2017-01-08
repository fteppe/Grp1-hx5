package main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;

public abstract class AnnotationClient {
    public Logger LOG = Logger.getLogger(this.getClass());
    private List<String> RES = new ArrayList<String>();
    private List<String> RESFULL = new ArrayList<String>();

    // Create an instance of HttpClient.
    private static HttpClient client = new HttpClient();
    
    public List<String> getResuFullURI(){
        return RESFULL;     
    }
    
    public List<String> getResu(){
        return RES;     
    }

    /**
     * Effectue une requete au service web de DBPedia Spotlight
     * @param method
     * @return
     * @throws AnnotationException
     */
    public String request(HttpMethod method) throws AnnotationException {
        String response = null;
        
        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                LOG.error("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            
            // Partie présente à l'origine avec un WARNING, correction de celui-ci
            // en passant par un Stream.
            //byte[] responseBody = method.getResponseBody(); 
            //response = new String(responseBody);
                       
            InputStream responseBodyStream = method.getResponseBodyAsStream();
            response = IOUtils.toString(responseBodyStream);


        } catch (HttpException e) {
            LOG.error("Fatal protocol violation: " + e.getMessage());
            throw new AnnotationException("Protocol error executing HTTP request.",e);
        } catch (IOException e) {
            LOG.error("Fatal transport error: " + e.getMessage());
            LOG.error(method.getQueryString());
            throw new AnnotationException("Transport error executing HTTP request.",e);
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return response;

    }

    /**
     * Insere un fichier dans une chaine de caractere unique a partir de son lien
     * @param filePath
     * @return
     * @throws java.io.IOException
     */
    protected static String readFileAsString(String filePath) throws java.io.IOException{
        return readFileAsString(new File(filePath));
    }

    /**
     * Insere un fichier dans une chaine de caractere unique
     * @param file
     * @return
     * @throws IOException
     */
    protected static String readFileAsString(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        @SuppressWarnings("resource")
        BufferedInputStream f = new BufferedInputStream(new FileInputStream(file));
        f.read(buffer);
        return new String(buffer);
    }

    /**
     * Parser pour les resultats des requetes effectuees
     * @author utilisateur
     *
     */
    static abstract class LineParser {

        public abstract String parse(String s) throws ParseException;

        static class ManualDatasetLineParser extends LineParser {
            public String parse(String s) throws ParseException {
                return s.trim();
            }
        }

        static class OccTSVLineParser extends LineParser {
            public String parse(String s) throws ParseException {
                String result = s;
                try {
                    result = s.trim().split("\t")[3];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new ParseException(e.getMessage(), 3);
                }
                return result; 
            }
        }
    }

    public void saveExtractedEntitiesSet(String Question, LineParser parser, int restartFrom) throws Exception {
        String text = Question;
        int i=0;

        for (String snippet: text.split("\n")) {
            String s = parser.parse(snippet);
            if (s!= null && !s.equals("")) {
                i++;

                if (i<restartFrom) continue;

                List<DBpediaResource> entities = new ArrayList<DBpediaResource>();

                try {
                    entities = extract(new Text(snippet.replaceAll("\\s+"," ")));

                } catch (AnnotationException e) {
                    LOG.error(e);
                    System.out.println("Error during DBPedia extraction");
                }
                for (DBpediaResource e: entities) {
                    RES.add(e.uri());
                    RESFULL.add(e.getFullUri());
                }
            }
        }
    }


    public abstract List<DBpediaResource> extract(Text text) throws AnnotationException;

    public void evaluate(String Question) throws Exception {
        evaluateManual(Question,0);
    }

    public void evaluateManual(String Question, int restartFrom) throws Exception {
         saveExtractedEntitiesSet(Question,new LineParser.ManualDatasetLineParser(), restartFrom);
    }
}
