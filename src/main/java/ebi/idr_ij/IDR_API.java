package ebi.idr_ij;
//https://github.com/openssbd/CombineSearch-OMERO/blob/master/CombineSearch-OMERO.ipynb
import com.github.opendevl.JFlat;
import ebi.idr_ij.IDR_mapr.container;
import ebi.idr_ij.IDR_mapr.type;
import org.apache.commons.lang.text.StrSubstitutor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//
public class IDR_API {
    URL INDEX_PAGE = new URL("https://idr.openmicroscopy.org/webclient/?experimenter=-1");
    IDR_API() throws IOException {
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

    public JSONObject JSONObjectScreenWithGene(String gene) throws IOException, JSONException {
        return JSONTypeWith(type.GENE, gene);
    }

    public JSONObject JSONTypeWith(String key, String value) throws IOException, JSONException {

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
        System.out.println(result);
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

    public List<Long> ListTypeWithContainer(String type, String value,String container) throws IOException,
            JSONException {
//        System.out.println(JSONTypeWith(type,value));
        JSONArray screens = JSONTypeWith(type,value).getJSONArray(container);
//        System.out.println("Print screens");
//        System.out.println(screens.toString());
        List<Long> ipArray = Arrays.asList(new Long[screens.length()]);
        for (int i = 0; i < screens.length(); i++) {
            JSONObject current_json = screens.getJSONObject(i);
//            System.out.println(current_json);
//            System.out.println(current_json.getString("id"));
            ipArray.set(i, current_json.getLong("id"));
        }
    return ipArray;
    }

    public JSONObject JsonTypeWithContainerGivenParent(String type, String value,String container, String parent_id,String id) throws IOException, JSONException {
        URL image_url = new URL("https://idr.openmicroscopy.org/mapr/api/{type}/{container}/?value={value}&node={parent_id}&id={id}");
        HashMap<String, String> formatMap = new HashMap<>();
        formatMap.put("type", type);
        formatMap.put("value", value);
        formatMap.put("container",container);
        formatMap.put("parent_id",parent_id);
        formatMap.put("id",id);
        return JSONMappedURL(image_url, formatMap);
    }

    // Only worked for screens before.
    @Deprecated
    public List<JSONObject> JSONListImageWithType(String type,String value) throws IOException, JSONException {
        List<Long> screen_ids = ListTypeWithContainer(type, value, "screens");

        List<JSONObject> json_list_plates = new ArrayList<>();
        List<JSONObject> json_list_images = new ArrayList<>();

        List<Long> list_of_plate_ids = new ArrayList<>();

        for(Long screen_id:screen_ids){
            JSONObject json_plates = JsonTypeWithContainerGivenParent(type, value, "plates", container.SCREEN, String.valueOf(screen_id));
            json_list_plates.add(json_plates);

            String current_plate_id = json_plates.getJSONArray("plates").getJSONObject(0).getString("id");
            list_of_plate_ids.add(Long.parseLong(current_plate_id));
            for(Long plate_ids:list_of_plate_ids) {
                JSONObject json_images = JsonTypeWithContainerGivenParent(type, value, container.IMAGES, container.PLATE, current_plate_id);
                json_list_images.add(json_images);
            }
        }



//        long screen_id = screens.get(0);

//        JSONObject json_plates = JsonTypeWithContainerGivenParent(type, value, "plates", IDR_mapr_container.SCREEN, String.valueOf(screen_id));
//        String current_plate_id = json_plates.getJSONArray("plates").getJSONObject(0).getString("id");
//        JSONObject json_images = JsonTypeWithContainerGivenParent(type, value, "images", IDR_mapr_container.PLATE, String.valueOf(current_plate_id));

//        JsonTypeWithContainerGivenParent(type,value)
//        System.out.println(json_list_images);
        return json_list_images;
    }

    public List<JSONObject> JSONListImageWithTypeIn(String type,String value,String container_type) throws IOException, JSONException {

        String container_top,container_tops,container_mid,container_mids;
        container_top = container_tops= container_mid = container_mids = null;

        if (container_type.equals("screens")) {
            container_top = container.SCREEN;
            container_tops = container.SCREENS;
            container_mid = container.PLATE;
            container_mids = container.PLATES;
        } else if (container_type.equals("projects")) {
            container_top = container.PROJECT;
            container_tops = container.PROJECTS;
            container_mid = container.DATASET;
            container_mids = container.DATASETS;
        }

        List<Long> screen_ids = ListTypeWithContainer(type, value, container_tops);

        List<JSONObject> json_list_plates = new ArrayList<>();
        List<JSONObject> json_list_images = new ArrayList<>();

        List<Long> list_of_plate_ids = new ArrayList<>();

        for (Long screen_id : screen_ids) {
            JSONObject json_plates = JsonTypeWithContainerGivenParent(type, value, container_mids, container_top, String.valueOf(screen_id));
            json_list_plates.add(json_plates);

            String current_plate_id = json_plates.getJSONArray(container_mids).getJSONObject(0).getString("id");
            list_of_plate_ids.add(Long.parseLong(current_plate_id));
            for (Long plate_ids : list_of_plate_ids) {
                JSONObject json_images = JsonTypeWithContainerGivenParent(type, value, container.IMAGES, container_mid, current_plate_id);
                json_list_images.add(json_images);
            }
        }
        return json_list_images;
    }

    List<JSONObject> JSONListImageWithTypeInScreens(String type, String value) throws IOException, JSONException {
        return JSONListImageWithTypeIn(type,value,container.SCREENS);
    }

    List<JSONObject> JSONListImageWithTypeInProjects(String type, String value) throws IOException, JSONException {
        return JSONListImageWithTypeIn(type,value,container.PROJECTS);
    }

//    Object ImagesWithTypeInDataset(String type, String value,){
//        return null;
//    }

    List<Long> LongListImagesWithType(String type, String value) throws IOException, JSONException {
        List<JSONObject> json_list = JSONListImageWithType(type, value);
        ArrayList<Long> long_list = new ArrayList<>();
        
        for(JSONObject json:json_list) {
//            long_list.add(
//                    Long.parseLong(
            JSONArray json_array = json.getJSONArray("images");
                            for(int n = 0; n < json_array.length(); n++){
                                Long image_id = json_array.getJSONObject(n).getLong("id");
                                long_list.add(image_id);
                            }
        }

        return long_list;

    }


    public List<Long> ListofPlatesWithGene(String gene) throws IOException, JSONException {
//        JSONObject json_gene = JSONObjectScreenWithGene(gene);
        List<Long> json_plates = ListTypeWithContainer(type.GENE,gene, container.PLATE);
//        System.out.println(json_plates);
        return json_plates;
    }

    /////// https://github.com/opendevl/Json2Flat /////////



    public JSONObject JSONContainerWith(String type, String value,String container) throws IOException, JSONException {
        URL image_url = new URL("https://idr.openmicroscopy.org/mapr/api/{type}/{container}/?value={value}");
        HashMap<String, String> formatMap = new HashMap<>();
        formatMap.put("type", type);
        formatMap.put("value", value);
        formatMap.put("container",container);
        return JSONMappedURL(image_url, formatMap);
    }

//    MAP_URL = "https://idr.openmicroscopy.org/webclient/api/annotations/?type=map&{type}={image_id}"

    public JSONObject JSONImagesInParentWith(String key,
                                             String value,
                                             String parent_type,
                                             String parent_id) throws IOException, JSONException {
        URL image_url = new URL("https://idr.openmicroscopy.org/mapr/api/{key}/images/?value={value}&node={parent_type}&id={parent_id}");
//        qs = {'key': 'gene', 'value': 'CDC20', 'parent_type': 'plate', 'parent_id': plate_id}
        HashMap<String, String> formatMap = new HashMap<>();
        formatMap.put("key", key);
        formatMap.put("value", value);
        formatMap.put("parent_type", parent_type);
        formatMap.put("parent_id", parent_id);
        return JSONMappedURL(image_url, formatMap);
    }

    public JSONObject JSONImagesMetaData(Long image_id) throws IOException, JSONException {

        URL image_url = new URL("https://idr.openmicroscopy.org/webclient/imgData/{image_id}");
        HashMap<String, String> formatMap = new HashMap<>();
        formatMap.put("image_id", image_id.toString());
        return JSONMappedURL(image_url, formatMap);
//        StrSubstitutor sub = new StrSubstitutor(formatMap, "{", "}");
//        String result = sub.replace(SCREENS_PROJECTS_URL.toString());
//        JSONObject jsonScreensWithGene = JSONfromURL(new URL(result));
//        return jsonScreensWithGene;
    }


    public void JSON2CSV(Path filename, JSONObject jsonobject) throws FileNotFoundException,
            UnsupportedEncodingException {
        JFlat flatMe = new JFlat(jsonobject.toString());
        List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();
        flatMe.write2csv(String.valueOf(filename));
    }


    public JSONObject JSONDatasetsWith(String type, String value) throws IOException, JSONException {
        return JSONContainerWith(type,value, container.DATASET);
    }

    public JSONObject JSONDatasetWithGene(String gene) throws IOException, JSONException {
        return JSONDatasetsWith(type.GENE,gene);
    }

    public JSONObject JSONObjectScreenWithPhenotype(String phenotype) throws IOException, JSONException {
        return JSONTypeWith(type.PHENOTYPE, phenotype);
    }

    @Deprecated //Doesn't work
    public JSONObject JSONImagesWith(String key,String value) throws IOException, JSONException {
        URL image_url = new URL("https://idr.openmicroscopy.org/mapr/api/{key}/images/?value={value}");
        HashMap<String, String> formatMap = new HashMap<>();
        formatMap.put("key", key);
        formatMap.put("value", value);
        return JSONMappedURL(image_url, formatMap);
    }

    @Deprecated //Doesn't work
    public JSONObject JSONImagesWithGene(String gene) throws IOException, JSONException {
        return JSONImagesWith(type.GENE,gene);
    }


//    public ListScreenWithTypeWithScreen(){
//
//    }

//    void JSONObjectDatasetWithGene(JSONObject JSONScreensWith, String gene) throws JSONException, IOException {
//
//
//        JSONArray datasets = JSONObjectScreenWithPhenotype("gene").getJSONArray("dataset");
//
//        ListScreenWithTypeWithContainer(JSONScreensWith(IDR_mapr_const.GENE));
//        return;
//    }


    ////// Need to find screens, then plates in screens, then images in plates in screens

//    IMAGES_URL = "https://idr.openmicroscopy.org/mapr/api/{key}/images/?value={value}&node={parent_type}&id={parent_id}"

//
//
//    URL SCREENS_PROJECTS_URL = new URL("https://idr.openmicroscopy.org/mapr/api/{key}/?value={value}");
//    HashMap<String, String> formatMap = new HashMap<>();
//        formatMap.put("key", "gene");
//        formatMap.put("value", gene);
//    StrSubstitutor sub = new StrSubstitutor(formatMap, "{", "}");
//    String result = sub.replace(SCREENS_PROJECTS_URL.toString());


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
}
