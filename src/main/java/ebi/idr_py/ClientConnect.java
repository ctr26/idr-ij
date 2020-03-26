package ebi.idr_py;

import Glacier2.CannotCreateSessionException;
import Glacier2.PermissionDeniedException;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROSession;
import omero.ServerError;
import omero.client;
import net.imagej.omero.OMEROLocation;
import net.imagej.omero.DefaultOMEROService;
import omero.gateway.Gateway;
import omero.gateway.SecurityContext;
import omero.gateway.exception.DSOutOfServiceException;
import omero.gateway.model.ExperimenterData;

import java.io.IOException;
import java.net.URISyntaxException;

public class ClientConnect {
    private ImageJ IJ;
    public static Gateway gateway;
    public static SecurityContext context;
    private ExperimenterData user;

    public static final String USERNAME = "public";
    public static final String PASSWORD = "public";
    public static final String HOST = "idr.openmicroscopy.org";
    public static final int PORT = 443;
    public static final int groupId = -1;
    public DefaultOMEROService Omero_service;
    public client idr_client;

    ClientConnect(ImageJ IJ) throws DSOutOfServiceException, CannotCreateSessionException, PermissionDeniedException, ServerError, URISyntaxException {
        this.IJ = IJ;
        System.out.println("Attempting to connect to IDR ... ");
        idr_client = new client(HOST);
        idr_client.createSession(USERNAME,PASSWORD);
//        OMEROLocation omeroLocation = new OMEROLocation(HOST, PORT, USERNAME, PASSWORD);
//        Omero_service = new DefaultOMEROService();
//        OMEROSession omero_session = Omero_service.createSession(omeroLocation);
//        idr_client = omero_session.getClient();
//        idr_client = new client(HOST, PORT);
//        idr_client.createSession(USERNAME,PASSWORD);

//        LoginCredentials cred = new LoginCredentials(USERNAME,PASSWORD, HOST, PORT);
//        Logger simpleLogger = new SimpleLogger();
//        gateway = new Gateway(simpleLogger);
//        user = gateway.connect(cred);
//        context = new SecurityContext(user.getGroupId());
        System.out.println("Connected to IDR");

    }
    public client getClient(){
        return idr_client;
    }

    public Dataset openImagePlus(long imageId) throws IOException, ServerError {
        return Omero_service.downloadImage(idr_client,imageId);
    }

}
