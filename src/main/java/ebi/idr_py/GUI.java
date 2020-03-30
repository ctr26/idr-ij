package ebi.idr_py;

//import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends javax.swing.JFrame {
    private Connect connection;
    private JTabbedPane tabbedPanel;
    private JList imageList;
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
    private Long ongImageLongValue;

    public GUI(Connect connection) {
        this.connection = connection;
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

        initGUIComponents();


    }
    
    void initGUIComponents(){
        System.out.println("Building GUI");
        ActionListener settingsListener = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                guiSettingsChange(e);
            }
        };

        Dimension panelSize = masterPanel.getPreferredSize();
        this.setContentPane(masterPanel);
        this.setLocationRelativeTo(null); //Centering
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
    }

    public void guiSettingsChange(java.awt.event.ActionEvent e){
        try {
            oneImageLongField.commitEdit();
        } catch ( java.text.ParseException ex ) {
            ex.printStackTrace();
        }
        this.ongImageLongValue = (Long) oneImageLongField.getValue();

        this.revalidate();
    }

}
