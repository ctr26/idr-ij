package ebi.idr_py;
//import javax.swing.*;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import omero.ServerError;
import omero.client;
import omero.gateway.Gateway;
import omero.gateway.SecurityContext;
import omero.gateway.exception.DSAccessException;
import omero.gateway.exception.DSOutOfServiceException;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class GUI extends javax.swing.JFrame {
    private client idr_client;
    private Attributes attributes;
    private Images images;
    private SecurityContext context;
    private Connect connection;
    private JTabbedPane tabbedPanel;
    private JList<String> imageList;
    private JButton openImages;
    private JButton populateList;
    private JPanel oneImageTab;
    private JSpinner oneImageLongField;
    private JPanel masterPanel;
    private JButton quickOpenButton;
    private JPanel byGenesTab;
    private JPanel downloadTab;
    private JFormattedTextField formattedTextField1;
    private JButton downloadButton;
    private JButton browseDownloadButton;
    private JPanel listPanel;
    private JPanel buttonsPanel;
    private JTextField textField1;
    private JProgressBar progressBar1;
    private JTextField textField2;

    private Long ongImageLongValue;
    private Gateway gateway;
    private ImageJ ij;

    public GUI(Connect connection, Gateway gateway, SecurityContext context, ImageJ ij, client idr_client) {
        this.connection = connection;
        this.gateway = gateway;
        this.context = context;
        this.ij = ij;
        this.idr_client = idr_client;

        initWindowComponents();
        initGUIComponents();
        initListeners();
//        images = new Images();
//        attributes = new Attributes();
    }

    private void initListeners() {

        PropertyChangeListener settingsListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                guiSettingsChange(evt);
            }
        };

        oneImageLongField.addPropertyChangeListener(settingsListener);

        ActionListener buttonListener = new ActionListener() {
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
        };

        quickOpenButton.addActionListener(buttonListener);
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

    void initGUIComponents(){
        System.out.println("Building GUI");
        Dimension panelSize = masterPanel.getPreferredSize();
        this.setContentPane(masterPanel);
        this.setLocationRelativeTo(null); //Centering
        this.setResizable(false);
        this.setVisible(true);

        // Setup jspinner numbers
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(oneImageLongField, "#");
        oneImageLongField.setEditor(editor);


        this.pack();
    }

    public void guiSettingsChange(PropertyChangeEvent e){
        try {
            oneImageLongField.commitEdit();
        } catch ( java.text.ParseException ex ) {
            ex.printStackTrace();
        }
        this.ongImageLongValue = Long.valueOf((Integer)oneImageLongField.getValue());
        System.out.println(ongImageLongValue);
        this.revalidate();
    }

    public void populateList(List<String> annotationsString) {
        Vector<String> vct = new Vector<String>();
        vct.addAll(annotationsString);
        imageList.setListData(vct);
    }
}
