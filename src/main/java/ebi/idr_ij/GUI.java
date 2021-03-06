package ebi.idr_ij;
//import javax.swing.*;

import ebi.idr_ij.IDR_mapr.type;
import ebi.idr_ij.IDR_mapr.type;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import omero.ServerError;
import omero.client;
import omero.gateway.Gateway;
import omero.gateway.SecurityContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class GUI extends javax.swing.JFrame {
    private GUI frame;
    private client idr_client;
    private Attributes attributes;
    private Images images;
    private SecurityContext context;
    private Connect connection;
    private JTabbedPane tabbedPanel;
    private JList<String> imageList;
    private JButton openImages;
    private JPanel oneImageTab;
    private JSpinner oneImageLongField;
    private JPanel masterPanel;
    private JButton quickOpenButton;
    private JPanel downloadTab;
    private JFormattedTextField downloadLocation;
    private JButton downloadButton;
    private JButton browseDownloadButton;
    private JPanel listPanel;
    private JPanel buttonsPanel;
    private JTextField geneotypeField;
    private JProgressBar progressBar1;
    private JTextField phenotypeField;
    private JButton populateListGenotypeButton;
    private JButton populateListPhenotype;
    private JScrollPane debugGUI;
    private JComboBox JSONtypeCombo;
    private JComboBox JSONcontainerCombo;
    private JTextField JSONvalue;
    private JButton maprDownloadButton;
    private JPanel maprTab;
    private JButton populateListButton;
    private JPanel gatewayTab;
    private JComboBox gatewayCombo;
    private JScrollPane imageListScroller;

    private Long ongImageLongValue;
    private Gateway gateway;
    private ImageJ ij;
    private String genotypeText;
    private List<Long> image_id_list = new Vector<Long>();

    public GUI(Connect connection, Gateway gateway, SecurityContext context, ImageJ ij, client idr_client)  {
        this.connection = connection;
        this.gateway = gateway;
        this.context = context;
        this.ij = ij;
        this.idr_client = idr_client;
        frame = this;

        initWindowComponents();
        initGUIComponents();
        //        initErrToDebugWindow();
        initListeners();
//        images = new Images();
//        attributes = new Attributes();
        maprDownloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Get path from file choose
                JFileChooser file_chooser_window = new JFileChooser();
                file_chooser_window.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                file_chooser_window
                file_chooser_window.addChoosableFileFilter(new FileNameExtensionFilter("JSON file", "json"));
                file_chooser_window.addChoosableFileFilter(new FileNameExtensionFilter("CSV file", "csv"));
                file_chooser_window.setAcceptAllFileFilterUsed(false);
                int returnVal = file_chooser_window.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = file_chooser_window.getSelectedFile();
                    String extension = file_chooser_window.getFileFilter().getDescription();

                    String path = file.getAbsolutePath();
                    String type = JSONtypeCombo.getSelectedItem().toString();
                    String container = JSONcontainerCombo.getSelectedItem().toString();
                    String value = JSONvalue.getText();
                    try {
                        IDR_API.JSONListImageWithTypeInBothDownload(type,value,container,path);
//                        IDR_API.DownloadJsonArrayAnnotations()
                    } catch (IOException | JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        populateListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = JSONtypeCombo.getSelectedItem().toString();
                String container = JSONcontainerCombo.getSelectedItem().toString();
                String value = JSONvalue.getText();
                try {
                    List<Long> image_list_long = IDR_API.LongListImageWithTypeInBoth(type, value, container);
                    populateList(image_list_long);

                } catch (IOException | JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initErrToDebugWindow() {
        JTextArea display = new JTextArea(16, 58);
        display.setEditable(false); // set textArea non-editable
        GuiOutputStream rawout = new GuiOutputStream(display);
        debugGUI.getViewport().setView(display);
        debugGUI.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        // Set new stream for System.out
        System.setOut(new PrintStream(rawout, true));
    }

    private void initListeners() {

        PropertyChangeListener settingsListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                guiSettingsChange(evt);
            }
        };

        oneImageLongField.addPropertyChangeListener(settingsListener);

        quickOpenButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                guiSettingsChange(null);
                try {
//                    Images.get_image(context,gateway,ongImageLongValue);
                    Dataset ij_image = Images.get_ij_dataset(ij, idr_client, ongImageLongValue);
                    ij.ui().show(ij_image);
                } catch (IOException | ServerError ex) {
                    ex.printStackTrace();
                }
            }
        });

//        populateListGenotypeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            }
//        });

        populateListPhenotype.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accession_type = (String) gatewayCombo.getSelectedItem().toString();
                System.out.println("From phentotype: ".concat(phenotypeField.getText()));
                List<Long> annotationsLong = Images.list_of_images_by(context, gateway,type.PHENOTYPE,
                        phenotypeField.getText());
                System.out.println("Found images with IDS:\n".concat(String.join(",", annotationsLong.toString())));
                populateList(annotationsLong);
            }
        });

        openImages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Long current_id_long : image_id_list) {
                    try {
//                        int current_id_int = Integer.parseInt(current_id_string);
//                        Long current_id_long = Long.parseLong(current_id_string);
                        Dataset ij_image = Images.get_ij_dataset(ij, idr_client, current_id_long);
                        ij.ui().show(ij_image);
                    } catch (IOException | ServerError ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void initWindowComponents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    connection.closeConnection();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    void initGUIComponents() {
        System.out.println("Building GUI");

        createComboBoxesMapr();
        createComboBoxesGateway();

        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(oneImageLongField, "#");
        oneImageLongField.setEditor(editor);

        Dimension panelSize = masterPanel.getPreferredSize();
        this.setContentPane(masterPanel);
        this.setLocationRelativeTo(null); //Centering
        this.pack();
//        this.setResizable(false);
        this.setVisible(true);

        // Setup jspinner numbers


//        this.setSize(panelSize);
//        this.pack();


    }

    void createComboBoxesGateway(){
        try {
            populateComboFromInterface(gatewayCombo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    void createComboBoxesMapr() {
        try {
            populateComboFromInterface(JSONtypeCombo);
            JSONcontainerCombo.addItem("projects");
            JSONcontainerCombo.addItem("screens");
            JSONcontainerCombo.addItem("both");
//            for(Field containers : containers.class.getFields()) JSONcontainerCombo.addItem((String) containers.get(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    void populateComboFromInterface(JComboBox CombobBox) throws IllegalAccessException {
        for(Field type : type.class.getFields()) CombobBox.addItem((String) type.get(null));
    }

    public void guiSettingsChange(PropertyChangeEvent e){
        try {
            oneImageLongField.commitEdit();
        } catch ( java.text.ParseException ex ) {
            ex.printStackTrace();
        }
        this.ongImageLongValue = Long.valueOf((Integer)oneImageLongField.getValue());

//        System.out.println(ongImageLongValue);
        this.revalidate();
    }

    public void populateList(List<Long> annoationsLong) {
        image_id_list = new ArrayList<Long>();
        image_id_list.addAll(annoationsLong);

        List<String> image_list_string = image_id_list.stream().map(Object::toString)
                .collect(Collectors.toList());
        Vector<String> image_vector_string = new Vector<String>();
        image_vector_string.addAll(image_list_string);
        imageList.setListData(image_vector_string);
    }

    public List<Long> readList(){
//        imageList.getSelectedValuesList();
//        imageList.get
        return image_id_list;
    }

//    public void populateList(List<Long> annotationsLong) {
//        vct = new Vector<Long>();
//        vct.addAll(annotationsLong);
//        imageList.setListData(vct);
//    }

    private class GuiOutputStream extends OutputStream {
        JTextArea textArea;

        public GuiOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int data) throws IOException {
            textArea.append(new String(new byte[] { (byte) data }));
        }
    }

}
