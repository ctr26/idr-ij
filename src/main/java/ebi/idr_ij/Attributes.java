package ebi.idr_ij;

import omero.RType;
import omero.rtypes;
import omero.gateway.Gateway;
import omero.gateway.SecurityContext;
import omero.sys.ParametersI;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Attributes {

    String value = "ASH2L";
    String name ="Gene Symbol";
    String ns = "openmicroscopy.org/mapr/gene";
    String ns2 = "openmicroscopy.org/mapr/phenotype";
    String name2;

//    Return a list of neighbours attributes
//    for given case insensitive attribute value. (Uses the python blitz gateway)

    public static Map<String, String> attributes_by_attributes(SecurityContext context, Gateway gateway, String name,
                                                 String value,
                                                 String ns,
                                                 String ns2,
                                                 String name2,
                                                 int s_id){
        return null;

    }

    public static List<Long> annotation_ids_by_field(SecurityContext context, Gateway gateway,
                                                        String value,String key,String ns){

        try {
            ParametersI params = new ParametersI();
            params.add("value", rtypes.wrap(value));
            params.add("key", rtypes.wrap(key));
            params.add("ns", rtypes.wrap(ns));
        String q = ("select a.id from MapAnnotation a join a.mapValue as mv where a.ns = :ns and mv.name = :key and mv.value = :value");
//            System.out.println(gateway.getQueryService(context).projection(q, params));
//            List<RType> projections = gateway.getQueryService(context).projection(q, params).get(0);
            List<List<RType>> projections = gateway.getQueryService(context).projection(q, params);
            System.out.println(projections.size());
            return projections.stream().map((n) -> (Long) rtypes.unwrap(n.get(0))).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static get_phenotypes_for_gene(session,
//                                              gene_name,
//                                              screenid=None,
//                                              idr_base_url="https://idr.openmicroscopy.org"):
//                                value="CMPO_0000077",
//                                key="Phenotype Term Accession",
//                                ns="openmicroscopy.org/mapr/phenotype"):
//            """
//    Return a list of IDs for map annotations with the given namespace
//    that have a key=value pair matching the given parameters.
//    """
//
//    params = ParametersI()
//    params.addString("value", value)
//            params.addString("key", key)
//            params.addString("ns", ns)
//    q = ("select a.id from MapAnnotation a join a.mapValue as mv "
//            "where a.ns = :ns and mv.name = :key and mv.value = :value")
//
//            return unwrap(conn.getQueryService().projection(q, params))[0]
//
//
}


