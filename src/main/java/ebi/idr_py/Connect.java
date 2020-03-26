package ebi.idr_py;

import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;
import ij.ImagePlus;
import ij.IJ;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import loci.plugins.LociImporter;
import net.dongliu.requests.Requests;
import net.imagej.ImageJ;
import omero.ServerError;
import omero.client;
import omero.gateway.Gateway;
import omero.gateway.LoginCredentials;
import omero.gateway.SecurityContext;
import omero.gateway.exception.DSOutOfServiceException;
import omero.gateway.model.ExperimenterData;
import omero.log.Logger;
import omero.log.SimpleLogger;
import net.dongliu.requests.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Connect {
    public static  client idr_client;
    public LoginCredentials cred;
    private ImageJ ij;
    public static Gateway gateway;
    public static SecurityContext context;
    private ExperimenterData user;

    public static final String USERNAME = "public";
    public static final String PASSWORD = "public";
    public static final String HOST = "idr.openmicroscopy.org";
    public static final int PORT = 0;
    public static final int groupId = -1;

    Connect(ImageJ ij) throws DSOutOfServiceException, CannotCreateSessionException, PermissionDeniedException, ServerError {
        this.ij = ij;
        System.out.println("Attempting to connect to IDR - Blitz ... ");
        cred = new LoginCredentials(USERNAME,PASSWORD, HOST, PORT);
        Logger simpleLogger = new SimpleLogger();
        gateway = new Gateway(simpleLogger);
        user = gateway.connect(cred);
        context = new SecurityContext(user.getGroupId());
        System.out.println("Connected to IDR - Blitz");

        System.out.println("Attempting to connect to IDR - Client... ");

        idr_client = new client(HOST);
        idr_client.createSession(USERNAME,PASSWORD);

        System.out.println("Connected to IDR - Client... ");
    }

    public Gateway getGateway() {
        return gateway;
    }

    public SecurityContext getContext(){
        return context;
    }

    public client getClient(){
        return idr_client;
    }


    public SecurityContext getNewContext(){
        context = new SecurityContext(user.getGroupId());
        return context;
    }

    public static URLConnection getGson() throws IOException {
        String idr_base_url = "https://idr.openmicroscopy.org";
        String index_page = idr_base_url.concat("/webclient/?experimenter=-1");

        URL url = new URL(index_page);
        URLConnection request = url.openConnection();
        request.connect();
        return request;
    }


    public static  void getSession(){
        String idr_base_url = "https://idr.openmicroscopy.org";
        String index_page = idr_base_url.concat("/webclient/?experimenter=-1");
//        Requests request = new Requests();
        Session session = Requests.session();
        RequestBuilder request = session.get(index_page);
        String response = request.send().readToText();
        System.out.println(response);

    }
    public static Unirest getJSONSession() throws Exception {
        String idr_base_url = "https://idr.openmicroscopy.org";
        String index_page = idr_base_url.concat("/webclient/?experimenter=-1");

        Unirest session = new Unirest();

        GetRequest request = Unirest.get(index_page)
                .header("accept", "application/json");
//                .queryString("apiKey", "123")
//                .field("parameter", "value")
//                .field("firstname", "Gary")
        HttpResponse<JsonNode> response = request.asJson();
        if(response.getStatus()!=200){
            throw new Exception("Json failed");
        } else {
            System.out.println("JSON succeeded");
        }
//        session.queryString();
//        System.out.println(response.getBody());
        return session;
    }
//    public create_http_session(idr_base_url='https://idr.openmicroscopy.org'):
//
//            """
//    Create and return http session
//    """
//    index_page = "%s/webclient/?experimenter=-1" % idr_base_url
//
//    # create http session
//    with requests.Session() as session:
//    request = requests.Request('GET', index_page)
//    prepped = session.prepare_request(request)
//    response = session.send(prepped)
//            if response.status_code != 200:
//            response.raise_for_status()
//
//            return session

    public static ImagePlus openImagePlus(long imageId)
            throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("location=[OMERO] open=[omero:server=");
        buffer.append(HOST);
        buffer.append("\nuser=");
        buffer.append(USERNAME);
        buffer.append("\nport=");
        buffer.append(PORT);
        buffer.append("\npass=");
        buffer.append(PASSWORD);
        buffer.append("\ngroupID=");
        buffer.append(groupId);
        buffer.append("\niid=");
        buffer.append(imageId);
        buffer.append("]");
        buffer.append(" windowless=true ");
        LociImporter importer = new LociImporter();
        importer.run(buffer.toString());
//        IJ.runPlugIn("loci.plugins.LociImporter", buffer.toString());
        return IJ.getImage();
//        new LociImporter().run(buffer.toString());
//        IJ.runPlugIn("loci.plugins.LociImporter", buffer.toString());
    }

//    void convertImagePlus(Image OmeroImage){
//        ConvertService.convert(OmeroImage, ImagePlus.class);
////    }
//public void getDatasetImages(Long imageId) {
//    Gateway gateway = null;
//    try {
////        LoginCredentials credentials = new LoginCredentials(USERNAME,PASSWORD,HOST,PORT);
////        SimpleLogger simpleLogger = new SimpleLogger();
////        gateway = new Gateway(simpleLogger);
////        gateway.connect(credentials);
////        ExperimenterData ed = gateway.getLoggedInUser();
////        List<GroupData> grda= ed.getGroups();
//
//        //This is the way to get via browsefacility, can be opened with
//        //*
////        BrowseFacility browser = gateway.getFacility(BrowseFacility.class);
////        Iterator<GroupData> gidit=grda.iterator();
////        int counter =0;
////        ImageData id =null;
//
////        IJ.log("ngroups " + grda.size());
////        while (gidit.hasNext() && id==null){
////            try {
////                SecurityContext ctx = new SecurityContext(grda.get(counter).getGroupId());
////                id = browser.getImage(ctx, imageId);
////                cred.setGroupID(ctx.getGroupID());
////                gateway.connect(cred);
////            } catch (Exception e){
////                //IJ.showMessage("Image not found in group " +grda.get(counter).getGroupId());
////            }
////            counter++;
////        }
////        if (id==null) {
////            ij.IJ.showMessage("Image not found");
////            return;
////        }
////        ImageJ ij = new ImageJ();
//        Context context = ij.getContext();
//        OMEROService dos = context.service(OMEROService.class);
//        OMEROLocation ol = new OMEROLocation(HOST,PORT,USERNAME,PASSWORD);
//        OMEROSession os = dos.createSession(ol);
//        client cl = os.getClient();
//        Dataset d = dos.downloadImage(cl,imageId);
//        ImgPlus<? extends RealType<?>> implu = d.getImgPlus();
//        ImagePlus imp = ij.convert().convert(implu, ImagePlus.class);
////        ImagePlus imp = ImageJFunctions.wrap(implu,"My desired imageplus");
//        imp.show();
//    } catch (Exception e) {
//        e.printStackTrace();
////        IJ.log(e.getMessage());
////        StackTraceElement[] t = e.getStackTrace();
////        for (int i=0;i<t.length;i++){
////            IJ.log(t[i].toString());
////        }
////        IJ.showMessage("An error occurred while loading the image.");
////        System.out.println("An error occurred while loading the image.");
//    } finally {
//        if (gateway != null) gateway.disconnect();
//    }
//}
}
