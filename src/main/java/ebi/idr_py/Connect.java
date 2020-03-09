package ebi.idr_py;

import omero.gateway.Gateway;
import omero.gateway.LoginCredentials;
import omero.gateway.SecurityContext;
import omero.gateway.exception.DSOutOfServiceException;
import omero.gateway.model.ExperimenterData;
import omero.log.Logger;
import omero.log.SimpleLogger;

public class Connect {
    public static Gateway gateway;
    public static final String username = "public";
    public static final String password = "public";
    public static final String host = "idr.openmicroscopy.org";
    public static final int port = 0;
    public static SecurityContext context;
    private ExperimenterData user;

    Connect() throws DSOutOfServiceException {
        System.out.println("Attempting to connect to IDR ... ");
        LoginCredentials cred = new LoginCredentials(username,password, host, port);
        Logger simpleLogger = new SimpleLogger();
        gateway = new Gateway(simpleLogger);
        user = gateway.connect(cred);
        context = new SecurityContext(user.getGroupId());
        System.out.println("Connected to IDR");
    }

    public Gateway getGateway() {
        return gateway;
    }

    public SecurityContext getContext(){
        return context;
    }
}
