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
public class AddNewMedia extends JFrame {

    private JPanel panel1;
    private JTextField sourceUrlTextField;
    private JTextField nameTextField;
    private JComboBox languageCombo;
    private JComboBox organizationCombo;
    private JTextArea descriptionTextArea;
    private JButton saveButton;
    private JButton cancelButton;
    private DataLoader data;
    private MainWindow gui;

    private AddNewMedia callback;
    private ApiTools api;

    public AddNewMedia(DataLoader data, ApiTools api, MainWindow mainWindow) {
        super("Add new Media");
        callback = this;
        setContentPane(panel1);
        this.data = data;
        this.gui = mainWindow;
        this.api = api;
        addActionListeners();
        initDropdowns();
    }

    private void addActionListeners(){
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Saving!");
                data.addMedia(nameTextField.getText(), sourceUrlTextField.getText(), descriptionTextArea.getText(), ((Language)languageCombo.getSelectedItem()).getId(), ((Organization)organizationCombo.getSelectedItem()).getId());
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
