/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the Unlicense for details:
 *     https://unlicense.org/
 */
package ebi.idr_ij;
import net.imagej.ImageJ;
import omero.*;
import org.json.JSONObject;

import omero.gateway.Gateway;
import omero.gateway.SecurityContext;
import omero.gateway.exception.DSAccessException;
import omero.gateway.exception.DSOutOfServiceException;
import omero.gateway.facility.BrowseFacility;
import omero.gateway.model.DatasetData;
import omero.gateway.model.ProjectData;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * A very simple plugin.
 * <p>
 * The annotation {@code @Plugin} lets ImageJ know that this is a plugin. There
 * are a vast number of possible plugins; {@code Command} plugins are the most
 * common one: they take inputs and produce outputs.
 * </p>
 * <p>
 * A {@link Command} is most useful when it is bound to a menu item; that is
 * what the {@code menuPath} parameter of the {@code @Plugin} annotation does.
 * </p>
 * <p>
 * Each input to the command is specified as a field with the {@code @Parameter}
 * annotation. Each output is specified the same way, but with a
 * {@code @Parameter(type = ItemIO.OUTPUT)} annotation.
 * </p>
 *
 * @author Johannes Schindelin
 * @author Curtis Rueden
 */
@Plugin(type = Command.class, headless = true, menuPath = "Plugins>EBI>IDR")
public class idr_ij implements Command {

	private static SecurityContext context;
	private static Connect connection;
	private static ImageJ ij;
	private static client idr_client;
	private static GUI gui;
	private static IDR_API idr_api;
	@Parameter(label = "What is your name?")
	private String name = "J. Doe";

	@Parameter(type = ItemIO.OUTPUT)
	public static Gateway gateway;
	public static final String username = "public";
	public static final String password = "public";
	public static final String host = "idr.openmicroscopy.org";
	public static final int port = 0;
	public Map<String, Long> IDR_projects;

	@Override
	public void run() {
//		Debug entry point
		try {
			JSONObject screens = idr_api.JSONObjectScreenWithGene("CDC20");
			System.out.println(screens.toString());
//			Unirest session = Connect.getJSONSession();


//			IDR_projects = list_all_experiments(context, gateway);
//			Dataset ij_image = Images.get_ij_dataset(ij, idr_client, 8343617L);
//			ij.ui().show(ij_image);


//			Context context = ij.getContext();
//        	OMEROService dos = context.service(OMEROService.class);
//			Dataset image = dos.downloadImage(idr_client, 2966725L);
//			ImageData image = Images.get_image(context, gateway, 2966725L);
//			ij.ui().show(image);
//			Dataset ij_image_dataset = image.asDataset();
//			ij.ui().show(ij_image);


//			connection.getDatasetImages(2966725L);
//			idr_client.get
//
			String value = "CMPO_0000077";
			String key = "Phenotype Term Accession";
			String ns = "openmicroscopy.org/mapr/phenotype";
//
			List<Long> annotations = Attributes.annotation_ids_by_field(context, gateway,value,key,ns);

			List<String> annotationsString = Images.list_of_images_by_phenotype(context, gateway,value);
			System.out.println("Found images with IDS:\n".concat(annotations.toString()));
//			gui.populateList(annotationsString);

			List<Long> aa = Connect.getImageIDsByAnnotation(annotations.get(0));
			System.out.println(aa.toString());
//			ImageData OME_image = Images.get_image(context, gateway, 2966725L);
//			TransferFacility facility = gateway.getFacility(TransferFacility.class);
//			gateway.getPixelsService()
//			ImagePlus imp = Connect.openImagePlus(2966725L);
//			imp.show();
//			facility.
//			OME_image.asImage();
//			Collection<ImageData> images = Images.images_by_phenotype(context, gateway, value);
//			System.out.println(images);
//			OME_image = images.iterator().next();
////			OME_image.
////			ImagePlus ij_image = ij.convert().convert(OME_image.asDataset(), ImagePlus.class);
////			ConvertService convert = new ConvertService();
////			context = IJ.getContext();
//			Context ij_context = ij.getContext();
//			ConvertService convertService = ij_context.service(ConvertService.class);
////			https://forum.image.sc/t/best-way-imagej-to-open-an-imageplus-from-omero-using-bioformats/26651/17
//			ImagePlus ij_image = convertService.convert(OME_image.asImage(),ImagePlus.class);
//			ij_image.show();

//			convertService = context.service(ConvertService.class)
//			connection.openImagePlus(image);
//			Image imageimage = image.asImage();
//			IJ.createImage(imageimage);


//			image.get
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * A {@code main()} method for testing.
	 * <p>
	 * When developing a plugin in an Integrated Development Environment (such as
	 * Eclipse or NetBeans), it is most convenient to provide a simple
	 * {@code main()} method that creates an ImageJ context and calls the plugin.
	 * </p>
	 * <p>
	 * In particular, this comes in handy when one needs to debug the plugin:
	 * after setting one or more breakpoints and populating the inputs (e.g. by
	 * calling something like
	 * {@code ij.command().run(MyPlugin.class, "inputImage", myImage)} where
	 * {@code inputImage} is the name of the field specifying the input) debugging
	 * becomes a breeze.
	 * </p>
	 *
	 * @param args unused
	 */

	public static void main(final String... args) {
		// Launch ImageJ as usual.
		 ij = new ImageJ();
//		ij.launch(args);
		try {
			connection = new Connect(ij);
			gateway = connection.getGateway();
			context = connection.getContext();
			idr_client = connection.getClient();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection failed");
		}
		try {
			idr_api = new IDR_API();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("API connection failed");
		}

//			ClientConnect clientConnection = new ClientConnect(ij);
//			context = connect();
			gui = new GUI(connection,gateway,context,ij,idr_client);
			new idr_ij().run();
	}


//	public static SecurityContext connect() throws DSOutOfServiceException {
//		System.out.println("Attempting to connect to IDR ... ");
//		LoginCredentials cred = new LoginCredentials(username, password, host, port);
//		Logger simpleLogger = new SimpleLogger();
//		gateway = new Gateway(simpleLogger);
//		ExperimenterData user = gateway.connect(cred);
//		System.out.println("Connected to IDR");
//		return new SecurityContext(user.getGroupId());
//		}

	public static Map<String, Long> list_all_experiments(SecurityContext context,Gateway gateway)
			throws ExecutionException, DSAccessException, DSOutOfServiceException {
		BrowseFacility browse = gateway.getFacility(BrowseFacility.class);
//			ImageData image = browse.getImage(ctx, imageId);
		Collection<ProjectData> projects = browse.getProjects(context);
		Iterator<ProjectData> i = projects.iterator();
		ProjectData project;
		Set<DatasetData> datasets;
		Iterator<DatasetData> j;
		DatasetData dataset;

		Map<String, Long>IDR_projects = new HashMap<String,Long>();

		while (i.hasNext()) {
			project = i.next();
			String name = project.getName();
			long id = project.getId();
			String description = project.getDescription();
//			datasets = project.getDatasets();
			System.out.println("Name: " + name + "ID: " + id + "Description: " + description);
//			j = datasets.iterator();
			IDR_projects.put(name, id);
		}
		return IDR_projects;
//			while (j.hasNext()) {
//				dataset = j.next();
//				// Do something here
//				// dataset.getImages();
//			}
		}

	}

//

