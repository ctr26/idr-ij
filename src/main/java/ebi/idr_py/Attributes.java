package ebi.idr_py;

import omero.ApiUsageException;
import omero.RArray;
import omero.RType;
import omero.api.IQueryPrx;
import omero.rtypes;
import omero.rtypes.Conversion;
import omero.gateway.Gateway;
import omero.gateway.SecurityContext;
import omero.sys.ParametersI;
import omero.util.IceMapper;

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
//        R = new RType();
//        String a = R.ice_id();

//        RType value= RType.ice_id("CMPO_0000077");
//        String value = RString("CMPO_0000077");
//        String value = RString.ice_staticId();

//        IceMapper mapper = new IceMapper();
        try {
////            RType value = mapper.toRType("CMPO_0000077");
//            RType r_value = rtypes.wrap(value);
////            RType key = mapper.toRType("Phenotype Term Accession");
//            RType r_key = rtypes.wrap(key);
////            RType ns = mapper.toRType("openmicroscopy.org/mapr/phenotype");
//            RType r_ns = rtypes.wrap(ns);

            ParametersI params = new ParametersI();
            params.add("value", rtypes.wrap(value));
            params.add("key", rtypes.wrap(key));
            params.add("ns", rtypes.wrap(ns));
        String q = ("select a.id from MapAnnotation a join a.mapValue as mv where a.ns = :ns and mv.name = :key and mv.value = :value");
//            IQueryPrx b = gateway.getQueryService(context);
//        List<List<RType>> a = gateway.getQueryService(context).projection(q, params).get(0);
//        List<List<String>> out = (List<List<String>>) rtypes.unwrap((RType) a);
//            List<String> c = out.get(0);

//            System.out.println(gateway.getQueryService(context).projection(q, params));
            List<RType> projections = gateway.getQueryService(context).projection(q, params).get(0);
//            System.out.println(projections);
//            Object projection = rtypes.unwrap(projections.get(0));
//            System.out.println(projection);

            List<Long> out = projections.stream().map((n) -> (Long) rtypes.unwrap(n)).collect(Collectors.toList());
            return out;
//        unwrap(conn.getQueryService().projection(q, params))[0]
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        String key="Phenotype Term Accession";
//        String ns="openmicroscopy.org/mapr/phenotype";

//        params.add("key", key);
//        params.add("ns", ns);
//        q = ("select a.id from MapAnnotation a join a.mapValue as mv where a.ns = :ns and mv.name = :key and mv.value = :value");
//        gateway.getQueryService(context).projection(q,params);
//        unwrap(conn.getQueryService().projection(q, params))[0]
        return null;
    }
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


