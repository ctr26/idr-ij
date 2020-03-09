package ebi.idr_py;

import omero.gateway.Gateway;
import omero.gateway.SecurityContext;

import java.util.Map;

public class Attributes {

    String value = "ASH2L";
    String name ="Gene Symbol";
    String ns = "openmicroscopy.org/mapr/gene";
    String ns2 = "openmicroscopy.org/mapr/phenotype";
    String name2;
    int s_id;

//    Return a list of neighbours attributes
//    for given case insensitive attribute value. (Uses the python blitz gateway)

    Map<String, String> attributes_by_attributes(SecurityContext context, Gateway gateway, String name,
                                                 String value,
                                                 String ns,
                                                 String ns2,
                                                 String name2,
                                                 int s_id){
        return null;
    }
}


