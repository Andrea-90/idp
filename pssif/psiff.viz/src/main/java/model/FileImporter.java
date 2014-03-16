package model;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.tum.pssif.core.model.Model;
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.MapperFactory;


public class FileImporter {

  private JComboBox<String>       filetype;
  private TreeMap<String, String> comboBoxValues;

  public FileImporter() {
    comboBoxValues = new TreeMap<String, String>();

    comboBoxValues.put("Umsatzorientierte Funktionsplanung", MapperFactory.UOFP);
    comboBoxValues.put("BPMN", MapperFactory.BPMN);
    comboBoxValues.put("EPK", MapperFactory.EPK);
    comboBoxValues.put("SysML", MapperFactory.SYSML);
    comboBoxValues.put("PSS-IF", MapperFactory.PSSIF);
    comboBoxValues.put("ReqIf", MapperFactory.REQ_IF);
  }

  /**
   * Read the selected file. Select the correct importer. Load the Nodes and Edges with the ModelBuilder
   * @param file the selected file which should be imported
   * @return true if no errors occured, false otherwise
   */
  private boolean importFile(File file) {
    // Read the Inputfile 
    InputStream in;
    try {
      in = new FileInputStream(file);

      String selectedFileType = String.valueOf(filetype.getSelectedItem());

      Mapper importer = MapperFactory.getMapper(comboBoxValues.get(selectedFileType));

      Model model;
      try {
        model = importer.read(ModelBuilder.getMetamodel(), in);

        // Create the Viz Model
        ModelBuilder.addModel(ModelBuilder.getMetamodel(), model);

        return true;
      }

      catch (Exception e) {
	        e.printStackTrace();
	        JPanel errorPanel = new JPanel();
	
	        errorPanel.add(new JLabel("There was a problem transforming the selected file. Was the file type chosen correctly?"));
	
	        JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    } catch (FileNotFoundException e) {
    		e.printStackTrace();
	      JPanel errorPanel = new JPanel();
	
	      errorPanel.add(new JLabel("There was a problem importing the selected file."));
	
	      JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  /**
   * Display the popup to the User
   * @param caller the component which called to display the popup
   * @return true if the entire import went fine, otherwise false
   */
  public boolean showPopup(Component caller) {
    JFileChooser openFile = new JFileChooser();

    //openFile.setCurrentDirectory(new File("C:\\Users\\Luc\\Desktop\\Dropbox\\IDP-PSS-IF-Shared"));

    openFile.setCurrentDirectory(new File("D:\\Dropbox\\IDP-PSS-IF-Shared\\Modelle PE"));

    JPanel panel1 = (JPanel) openFile.getComponent(3);
    JPanel panel2 = (JPanel) panel1.getComponent(2);

    JPanel importeType = new JPanel(new FlowLayout());
    JLabel label1 = new JLabel("Select Import File:");
    importeType.add(label1);
    filetype = new JComboBox<String>(comboBoxValues.keySet().toArray(new String[0]));
    filetype.setSelectedIndex(0);
    importeType.add(filetype);

    panel2.add(importeType);

    int returnVal = openFile.showOpenDialog(caller);

    if (returnVal == JFileChooser.APPROVE_OPTION) {

      File file = openFile.getSelectedFile();

      return importFile(file);

    }
    else {
      return false;
    }
  }

}
