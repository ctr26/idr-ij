package ebi.idr_py;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROService;
import omero.ServerError;
import omero.client;
import omero.gateway.Gateway;
import omero.gateway.SecurityContext;
import omero.gateway.exception.DSAccessException;
import omero.gateway.exception.DSOutOfServiceException;
import omero.gateway.facility.BrowseFacility;
import omero.gateway.model.ImageData;
import org.scijava.Context;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Images {
    private static String value = "CMPO_0000077";
    private static String key="Phenotype Term Accession";
    private static String ns="openmicroscopy.org/mapr/phenotype";
    private static Long image_id = 2966725L;

    private static String phenotype = "CMPO_0000077";


    public static Collection<ImageData> images_by_phenotype(SecurityContext context, Gateway gateway, String phenotype) throws ExecutionException, DSAccessException, DSOutOfServiceException {
        Collection<Long> ann_ids = Attributes.annotation_ids_by_field(context, gateway, phenotype, key, ns);
        BrowseFacility browse = gateway.getFacility(BrowseFacility.class);
        return browse.getImages(context, ann_ids);
    }

    public static ImageData get_image(SecurityContext context, Gateway gateway, Long image_id) throws ExecutionException, DSAccessException, DSOutOfServiceException {
        BrowseFacility browse = gateway.getFacility(BrowseFacility.class);
        return browse.getImage(context,image_id);
    }

    public static Dataset get_ij_dataset(ImageJ ij, client idr_client, Long ImageID) throws IOException, ServerError {
        Context context = ij.getContext();
        OMEROService dos = context.service(OMEROService.class);
        return dos.downloadImage(idr_client, ImageID);
    }

    public static List<String> list_of_images_by_phenotype(SecurityContext context, Gateway gateway, String phenotype) {
        List<Long> annotations = Attributes.annotation_ids_by_field(context, gateway,value,key,ns);
        return annotations.stream()
                .map(s->String.valueOf(s))
                .collect(Collectors.toList());
    }


//            """
//    Passes phenotype as the value argument to annotation_ids_by_field
//    and loads Image objects which can be used for loading thumbnails, etc.
//    """
//    ann_ids = annotation_ids_by_field(conn, phenotype)
//    return list(conn.getObjectsByAnnotations("Image", ann_ids))

}
