package main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dbpedia.spotlight.exceptions.AnnotationException;
import org.dbpedia.spotlight.model.DBpediaResource;
import org.dbpedia.spotlight.model.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class db extends AnnotationClient {

	private static String  API_URL    = "http://spotlight.sztaki.hu:2222/";
    private static  double  CONFIDENCE = 0.0;
    private static  int     SUPPORT    = 0;
    private static  String  powered_by ="non";
    private static  String  spotter ="Default";//"LingPipeSpotter"=Annotate all spots 
                                                //AtLeastOneNounSelector"=No verbs and adjs.    
                                                //"CoOccurrenceBasedSelector" =No 'common words'
                                                //"NESpotter"=Only Per.,Org.,Loc.
    private static String  disambiguator ="Default";//Default ;Occurrences=Occurrence-centric;Document=Document-centric
    private static String  showScores ="yes";

@SuppressWarnings("static-access")
public void configiration(double CONFIDENCE,int SUPPORT,
                          String powered_by,String spotter,String disambiguator,String showScores){
    this.CONFIDENCE=CONFIDENCE;
    this.SUPPORT=SUPPORT;
    this.powered_by=powered_by;
    this.spotter=spotter;
    this.disambiguator=disambiguator;
    this.showScores=showScores;

}
    public List<DBpediaResource> extract(Text text) throws AnnotationException {
        //LOG.info("Querying API.");
        String spotlightResponse;
        try {
            String Query=API_URL + "rest/annotate/?" +
                    "confidence=" + CONFIDENCE
                  + "&support=" + SUPPORT
                  + "&spotter=" + spotter
                  + "&disambiguator=" + disambiguator
                  + "&showScores=" + showScores
                  + "&powered_by=" + powered_by
                  + "&text=" + URLEncoder.encode(text.text(), "utf-8");
            //LOG.info(Query);

            GetMethod getMethod = new GetMethod(Query);
            getMethod.addRequestHeader(new Header("Accept", "application/json"));
            spotlightResponse = request(getMethod);

        } catch (UnsupportedEncodingException e) {
            throw new AnnotationException("Could not encode text.", e);
        }
        assert     spotlightResponse != null;
        JSONObject resultJSON         = null;
        JSONArray  entities           = null;

        try {                   
            resultJSON = new JSONObject(spotlightResponse);
            entities = resultJSON.getJSONArray("Resources");

        } catch (JSONException e) {
            //throw new AnnotationException("Received invalid response from DBpedia Spotlight API.");
        }

        LinkedList<DBpediaResource> resources = new LinkedList<DBpediaResource>();
        if(entities!=null) 
        for(int i = 0; i < entities.length(); i++) {
            try {
                JSONObject entity = entities.getJSONObject(i);
                resources.add(
                        new DBpediaResource(entity.getString("@URI"),
                        Integer.parseInt(entity.getString("@support"))));
            } catch (JSONException e) {
                LOG.error("JSON exception "+e);
            }
        }
        return resources;
    }

}
