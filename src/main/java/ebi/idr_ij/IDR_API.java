package ebi.idr_ij;

import org.apache.commons.lang.text.StrSubstitutor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

//

public class IDR_API {


    IDR_API() throws IOException {
        URL INDEX_PAGE = new URL("https://idr.openmicroscopy.org/webclient/?experimenter=-1");
//        url = new URL("https://idr.openmicroscopy.org/webclient/?experimenter=-1");
        HttpURLConnection con = connectToUrl(INDEX_PAGE);
        con.connect();
        int response = con.getResponseCode();
        if (response != 200) {
            throw new IOException("Bad response");
        }
        System.out.println("API connection successful");
        con.getInputStream().close();
    }

    HttpURLConnection connectToUrl(URL url) throws IOException {
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestMethod("GET");
        return httpCon;
    }

//        import requests
//
//                INDEX_PAGE = "https://idr.openmicroscopy.org/webclient/?experimenter=-1"
//
//# create http session
//        with requests.Session() as session:
//        request = requests.Request('GET', INDEX_PAGE)
//        prepped = session.prepare_request(request)
//        response = session.send(prepped)
//        if response.status_code != 200:
//        response.raise_for_status()
//    }

    //    public JSONObject JSONScreensWithGene(String gene) throws IOException, JSONException {
//
//        URL SCREENS_PROJECTS_URL = new URL("https://idr.openmicroscopy.org/mapr/api/{key}/?value={value}");
//        HashMap<String, String> formatMap = new HashMap<>();
//            formatMap.put("key", "gene");
//            formatMap.put("value", gene);
//        StrSubstitutor sub = new StrSubstitutor(formatMap, "{", "}");
//        String result = sub.replace(SCREENS_PROJECTS_URL.toString());
//        System.out.println("JSON");
////        String jsonInputString = JSONRequest.toString();
//        HttpURLConnection con = connectToUrl(new URL(result));
//        con.setRequestMethod("GET");
////        con.setRequestProperty("Accept", "application/json");
//        con.setDoOutput(true);
//        con.connect();
//
//        JSONObject jsonObject;
//        try(BufferedReader br = new BufferedReader(
//                new InputStreamReader(con.getInputStream(), "utf-8"))) {
//            StringBuilder response = new StringBuilder();
//            String responseLine = null;
//            while ((responseLine = br.readLine()) != null) {
//                response.append(responseLine.trim());
//            }
//            System.out.println(response.toString());
//            jsonObject = new JSONObject(response.toString());
//        }
//
//        return jsonObject;
//    }
//
    public JSONObject JSONObjectScreenWithGene(String gene) throws IOException, JSONException {
        return JSONScreensWith("gene", gene);
    }

    public JSONObject JSONScreensWith(String key, String value) throws IOException, JSONException {

        URL SCREENS_PROJECTS_URL = new URL("https://idr.openmicroscopy.org/mapr/api/{key}/?value={value}");
        HashMap<String, String> formatMap = new HashMap<>();
        formatMap.put("key", key);
        formatMap.put("value", value);
//        StrSubstitutor sub = new StrSubstitutor(formatMap, "{", "}");
//        String result = sub.replace(SCREENS_PROJECTS_URL.toString());
//        JSONObject jsonScreensWithGene = JSONfromURL(new URL(result));
        return JSONMappedURL(SCREENS_PROJECTS_URL, formatMap);
    }

    public JSONObject JSONMappedURL(URL url, HashMap formatMap) throws IOException, JSONException {
//        URL SCREENS_PROJECTS_URL = new URL("https://idr.openmicroscopy.org/mapr/api/{key}/?value={value}");
//        HashMap<String, String> formatMap = new HashMap<>();
//        formatMap.put("key", key);
//        formatMap.put("value", value);
        StrSubstitutor sub = new StrSubstitutor(formatMap, "{", "}");
        String result = sub.replace(url.toString());
//        JSONObject jsonScreensWithGene = JSONfromURL(new URL(result));
        return JSONfromURL(new URL(result));
    }

    public JSONObject JSONfromURL(URL url) throws IOException {
//        System.out.println("JSON");
//        String jsonInputString = JSONRequest.toString();
        HttpURLConnection con = connectToUrl(url);
        con.setRequestMethod("GET");
//        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.connect();

        JSONObject jsonObject = null;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
//            System.out.println(response.toString());
            jsonObject = new JSONObject(response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject JSONObjectScreenWithPhenotype(String phenotype) throws IOException, JSONException {
        return JSONScreensWith("phenotype", phenotype);
    }

    public JSONObject JSONImagesMetaData(Long image_id) throws IOException, JSONException {

        URL image_url = new URL("https://idr.openmicroscopy.org/webclient/imgData/{image_id}/");
        HashMap<String, String> formatMap = new HashMap<>();
        formatMap.put("image_id", image_id.toString());
        return JSONMappedURL(image_url, formatMap);
//        StrSubstitutor sub = new StrSubstitutor(formatMap, "{", "}");
//        String result = sub.replace(SCREENS_PROJECTS_URL.toString());
//        JSONObject jsonScreensWithGene = JSONfromURL(new URL(result));
//        return jsonScreensWithGene;
    }

    public JSONObject JSONImagesWith(String key,String value) throws IOException, JSONException {
        URL image_url = new URL("https://idr.openmicroscopy.org/mapr/api/{key}/images/?value={value}");
        HashMap<String, String> formatMap = new HashMap<>();
        formatMap.put("key", key);
        formatMap.put("value", value);
        return JSONMappedURL(image_url, formatMap);
    }

    public JSONObject JSONImagesWithGene(String gene) throws IOException, JSONException {
        return JSONImagesWith("gene",gene);
    }
//    MAP_URL = "https://idr.openmicroscopy.org/webclient/api/annotations/?type=map&{type}={image_id}"


//
//
//    URL SCREENS_PROJECTS_URL = new URL("https://idr.openmicroscopy.org/mapr/api/{key}/?value={value}");
//    HashMap<String, String> formatMap = new HashMap<>();
//        formatMap.put("key", "gene");
//        formatMap.put("value", gene);
//    StrSubstitutor sub = new StrSubstitutor(formatMap, "{", "}");
//    String result = sub.replace(SCREENS_PROJECTS_URL.toString());
}
