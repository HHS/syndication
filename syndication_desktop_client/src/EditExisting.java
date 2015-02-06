import data.DataLoader;
import rest.ApiTools;
import rest.model.Language;
import rest.model.Organization;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Created by sgates on 10/3/14.
 */
public class EditExisting extends JFrame{

    private JPanel panel1;
    private JTextField sourceUrlTextField;
    private JTextField nameTextField;
    private JComboBox languageCombo;
    private JComboBox organizationCombo;
    private JTextArea descriptionTextArea;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private DataLoader data;
    private MainWindow gui;

    private EditExisting callback;
    private ApiTools api;
    private long id;

    public EditExisting(DataLoader data, ApiTools api, MainWindow mainWindow, long id){
        super("Edit Media Record");
        callback = this;
        this.id = id;
        setContentPane(panel1);
        this.data = data;
        this.gui = mainWindow;
        this.api = api;
        addActionListeners();
        initDropdowns();
        setValues();
    }

    public void setValues(){
        DataLoader.MediaItem mi = data.getRecord(id);
        sourceUrlTextField.setText(mi.getUrl());
        nameTextField.setText(mi.getName());
        descriptionTextArea.setText(mi.getDesc());

        int indexOfLang = 0;
        for(int i=0; i<languageCombo.getModel().getSize(); i++){
            if(((Language)languageCombo.getModel().getElementAt(i)).getId() == mi.getLang()){
                indexOfLang = i;
            }
        }

        languageCombo.setSelectedIndex(indexOfLang);

        int indexOfOrg = 0;
        for(int i=0; i<organizationCombo.getModel().getSize(); i++){
            if(((Organization)organizationCombo.getModel().getElementAt(i)).getId() == mi.getOrg()){
                indexOfOrg = i;
            }
        }

        organizationCombo.setSelectedIndex(indexOfOrg);
    }

    private void addActionListeners(){
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Saving!");
                long selectedRecordId = gui.getSelectedMediaListItem();
                data.updateMedia(selectedRecordId, nameTextField.getText(), sourceUrlTextField.getText(), descriptionTextArea.getText(), ((Language)languageCombo.getSelectedItem()).getId(), ((Organization)organizationCombo.getSelectedItem()).getId());
                gui.loadDataList();
                callback.dispatchEvent(new WindowEvent(callback, WindowEvent.WINDOW_CLOSING));
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callback.dispatchEvent(new WindowEvent(callback, WindowEvent.WINDOW_CLOSING));
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JOptionPane.showConfirmDialog(callback, "Are you sure?") == 0){
                    long selected = gui.getSelectedMediaListItem();
                    data.deleteMedia(selected);
                    gui.loadDataList();
                    callback.dispatchEvent(new WindowEvent(callback, WindowEvent.WINDOW_CLOSING));
                }
            }
        });
    }

    private void initDropdowns(){
        //Languages ==============================================
        List<Language> languages = api.getLanguages();

        for(Language l : languages){
            languageCombo.addItem(l);
        }

        //Organizations ==============================================
        List<Organization> organizations = api.getOrganizations();

        for(Organization o : organizations){
            organizationCombo.addItem(o);
        }
    }
}
