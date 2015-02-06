import config.ConfigLoader;
import data.DataLoader;
import rest.ApiTools;
import rest.model.Language;
import rest.model.Organization;
import rest.model.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sgates on 10/1/14.
 */
public class MainWindow extends JFrame{
    private JTabbedPane primaryTabbedPane;
    private JPanel mainPanel;
    private JTextField htmlUrlField;
    private JButton htmlPublishButton;
    private JButton htmlVerifyButton;
    private JTextField htmlNameField;
    private JComboBox htmlLanguageCombo;
    private JComboBox htmlSourceCombo;
    private JPanel buttonPanel;
    private JPanel metaInputPanel;
    private JPanel metaInputLeftCol;
    private JPanel metaInputRightCol;
    private JLabel sp_urlLabel;
    private JLabel sp_nameLabel;
    private JLabel sp_languageLabel;
    private JLabel sp_agencyLabel;
    private JLabel htmlReachableLabel;
    private JLabel htmlExtractableLabel;
    private JLabel htmlMessageLabel;
    private JTextField publicKeyField;
    private JTextField privateKeyField;
    private JTextField secretField;
    private JButton saveSettingsButton;
    private JLabel publicKeyLabel;
    private JLabel privateKeyLabel;
    private JLabel secretLabel;
    private JTextField apiUrlField;
    private JLabel apiUrlLabel;
    private JLabel configStatusLabel;
    private JTextArea keyImportArea;
    private JButton importKeysButton;
    private JTextArea htmlDescriptionArea;
    private JTextArea htmlMessageArea;
    private JButton addNewButton;
    private JList mediaList;
    private JButton editSelectedButton;
    private JButton bulkPublishButton;
    private JProgressBar progressBar1;
    private JButton cancelButton;
    private JTextArea bulkMessageTextArea;
    private JPanel messagePanel;
    private JPanel tagPanelHtml;
    private JPanel layoutPanel;
    private JList htmlTagList;
    private JPanel rightButtonPanelHtml;
    private JButton htmlManageTagsButton;
    private JPanel messagesPanel;
    private JPanel leftSingleItemPanel;
    private JPanel rightSingleItemPanel;
    private JPanel urlInputPanel;
    private JTabbedPane mediaTypePane;
    private JPanel htmlPanel;
    private JPanel ImagePanel;
    private JPanel InfographicPanel;
    private JPanel rightImagePanel;
    private JPanel leftImagePanel;
    private JList imageTagList;
    private JPanel imageTagButtonPanel;
    private JButton imageManageTagsButton;
    private JPanel imageUrlPanel;
    private JPanel imageMetaPanel;
    private JPanel imageMessagePanel;
    private JPanel imageButtonPanel;
    private JTextField imageUrlTextField;
    private JPanel imageMetaLeftPanel;
    private JPanel imageMetaRightPanel;
    private JComboBox imageLanguageCombo;
    private JComboBox imageSourceCombo;
    private JTextField imageNameField;
    private JLabel imageReachableLabel;
    private JLabel imageExtractableLabel;
    private JLabel imageMessageLabel;
    private JButton imagePublishButton;
    private JButton imageVerifyButton;
    private JTextArea imageMessageArea;
    private JTextField imageWidthField;
    private JTextField imageHeightField;
    private JTextField imageAltTextField;
    private JTextArea imageDescriptionTextArea;
    private JComboBox imageFormatCombo;
    private JPanel leftInfoPanel;
    private JPanel rightInfoPanel;
    private JButton infoManageTagsButton;
    private JList infoTagList;
    private JTextField infoUrlTextField;
    private JPanel leftInfoMetaPanel;
    private JPanel rightInfoMetaPanel;
    private JPanel infoUrlPanel;
    private JPanel infoMetaPanel;
    private JPanel infoMessagePanel;
    private JPanel infoButtonPanel;
    private JTextArea infoMessageArea;
    private JButton infoVerifyButton;
    private JButton infoPublishButton;
    private JTextArea infoDescriptionTextArea;
    private JTextField infoNameField;
    private JTextField infoAltTextField;
    private JComboBox infoLanguageCombo;
    private JComboBox infoSourceCombo;
    private JComboBox infoFormatCombo;
    private JTextField infoWidthField;
    private JTextField infoHeightField;
    private JLabel infoReachableLabel;
    private JLabel infoExtractableLabel;
    private JLabel infoMessageLabel;

    private ImageIcon greenCheck;
    private ImageIcon redX;
    private ImageIcon neutral;

    private ConfigLoader configLoader;
    private Map<String, String> config;
    private ApiTools api;
    private DataLoader data;
    private MainWindow callback;
    private ListModel<Tag> selectedTagsModel;

    private BulkPublishJob job;
    private final Dimension windowSize = new Dimension(965, 514);

    public static void main(String[] args){
        new MainWindow();
    }

    public long getSelectedMediaListItem(){
        return ((DataLoader.MediaItem)mediaList.getSelectedValue()).getId();
    }

    public MainWindow() {
        super("Syndication Desktop Client - " + Meta.getAPP_VERSION());
        callback = this;
        configLoader = new ConfigLoader();
        data = new DataLoader();
        loadConfig();
        api = new ApiTools(configLoader, this);
        initIcons();
        initDropdowns();
        setContentPane(mainPanel);
        addActionListeners();
        loadDataList();

        performUITweaks();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        data.getAllRecords();
    }

    private void performUITweaks(){
        this.setPreferredSize(windowSize);
        this.setMinimumSize(windowSize);
    }

    public void loadDataList(){
        List<DataLoader.MediaItem> loadedMedia = data.getAllRecords();
        mediaList.setListData(loadedMedia.toArray());

        MediaListCellRenderer renderer = new MediaListCellRenderer();
        mediaList.setCellRenderer(renderer);
    }

    public void setSelectedTags(ListModel<Tag> tags){
        selectedTagsModel = tags;
        htmlTagList.setModel(selectedTagsModel);
    }

    class MediaListCellRenderer extends DefaultListCellRenderer {
        private JLabel label;
        private Color textSelectionColor = Color.BLACK;
        private Color backgroundSelectionColor = Color.YELLOW;
        private Color textNonSelectionColor = Color.BLACK;
        private Color backgroundNonSelectionColor = Color.WHITE;

        MediaListCellRenderer() {
            label = new JLabel();
            label.setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean expanded) {
            DataLoader.MediaItem mi = (DataLoader.MediaItem) value;

            switch(mi.getStatus()){
                case -1: label.setIcon(redX); break;
                case 0: label.setIcon(neutral); break;
                case 1: label.setIcon(greenCheck); break;
            }
            label.setText(value.toString());

            if (selected) {
                label.setBackground(backgroundSelectionColor);
                label.setForeground(textSelectionColor);
            } else {
                label.setBackground(backgroundNonSelectionColor);
                label.setForeground(textNonSelectionColor);
            }

            return label;
        }
    }

    private void loadConfig(){
        config = configLoader.getConfig();
        publicKeyField.setText(config.get("publicKey"));
        privateKeyField.setText(config.get("privateKey"));
        secretField.setText(config.get("secret"));
        apiUrlField.setText(config.get("apiUrl"));
    }

    private void initDropdowns(){
        //Languages ==============================================
        List<Language> languages = api.getLanguages();

        for(Language l : languages){
            htmlLanguageCombo.addItem(l);
            imageLanguageCombo.addItem(l);
            infoLanguageCombo.addItem(l);
        }

        //Organizations ==============================================
        List<Organization> organizations = api.getOrganizations();

        for(Organization o : organizations){
            htmlSourceCombo.addItem(o);
            imageSourceCombo.addItem(o);
            infoSourceCombo.addItem(o);
        }
    }

    private void addActionListeners() {
        //Infographic Listeners =============================================================
        infoPublishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map map = new LinkedHashMap();
                map.put("language", ((Language) infoLanguageCombo.getSelectedItem()).getId());
                map.put("source", ((Organization) infoSourceCombo.getSelectedItem()).getId());
                map.put("sourceUrl", infoUrlTextField.getText());
                map.put("name", infoNameField.getText());
                map.put("description", infoDescriptionTextArea.getText());
                map.put("width", infoWidthField.getText());
                map.put("height", infoHeightField.getText());
                map.put("imageFormat", infoFormatCombo.getSelectedItem());
                map.put("altText", infoAltTextField.getText());
                map.put("tags", api.getTagIdsFromModel(infoTagList.getModel()));

                String jsonBody = api.getJsonBodyForPublishRequest(map);

                System.out.println("Publishing:");
                System.out.println(jsonBody);

                String resp = api.publish(jsonBody, "infographics");
                infoMessageArea.setText(resp);
                if(resp.startsWith("Errors")){
                    infoMessageLabel.setText("Error");
                    infoMessageLabel.setIcon(redX);
                } else{
                    infoMessageLabel.setText("Success");
                    infoMessageLabel.setIcon(greenCheck);
                }
            }
        });

        infoVerifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map verification = api.verifyImage(infoUrlTextField.getText());
                if((Boolean) verification.get("valid") == true){
                    infoMessageLabel.setText("No Problems Detected.");
                    infoMessageLabel.setIcon(greenCheck);
                } else{
                    infoMessageLabel.setText((String) verification.get("msg"));
                    infoMessageLabel.setIcon(redX);
                }
            }
        });

        //Spawn a manage tags button
        infoManageTagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaggingWindow tags = new TaggingWindow(api, infoTagList);
                tags.pack();
                tags.setVisible(true);
            }
        });

        // Image Listeners ==================================================================
        imageVerifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map verification = api.verifyImage(imageUrlTextField.getText());
                if((Boolean) verification.get("valid") == true){
                    imageMessageLabel.setText("No Problems Detected.");
                    imageMessageLabel.setIcon(greenCheck);
                } else{
                    imageMessageLabel.setText((String) verification.get("msg"));
                    imageMessageLabel.setIcon(redX);
                }
            }
        });

        imagePublishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map map = new LinkedHashMap();
                map.put("language", ((Language) imageLanguageCombo.getSelectedItem()).getId());
                map.put("source", ((Organization) imageSourceCombo.getSelectedItem()).getId());
                map.put("sourceUrl", imageUrlTextField.getText());
                map.put("name", imageNameField.getText());
                map.put("description", imageDescriptionTextArea.getText());
                map.put("width", imageWidthField.getText());
                map.put("height", imageHeightField.getText());
                map.put("imageFormat", imageFormatCombo.getSelectedItem());
                map.put("altText", imageAltTextField.getText());
                map.put("tags", api.getTagIdsFromModel(imageTagList.getModel()));

                String jsonBody = api.getJsonBodyForPublishRequest(map);

                System.out.println("Publishing:");
                System.out.println(jsonBody);

                String resp = api.publish(jsonBody, "images");
                imageMessageArea.setText(resp);
                if(resp.startsWith("Errors")){
                    imageMessageLabel.setText("Error");
                    imageMessageLabel.setIcon(redX);
                } else{
                    imageMessageLabel.setText("Success");
                    imageMessageLabel.setIcon(greenCheck);
                }
            }
        });

        //Spawn a manage tags button
        imageManageTagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaggingWindow tags = new TaggingWindow(api, imageTagList);
                tags.pack();
                tags.setVisible(true);
            }
        });

        // Html Listeners ===================================================================

        //Spawn a manage tags button
        htmlManageTagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaggingWindow tags = new TaggingWindow(api, htmlTagList);
                tags.pack();
                tags.setVisible(true);
            }
        });

        htmlVerifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                htmlMessageArea.setText("");
                String content = api.reachable(htmlUrlField.getText());
                if(content != null && content.length() > 0){
                    htmlReachableLabel.setText("Content Reachable");
                    htmlReachableLabel.setIcon(greenCheck);
                    htmlMessageLabel.setText("No Problems Detected.");
                    htmlMessageLabel.setIcon(greenCheck);
                } else{
                    htmlReachableLabel.setText("Content Not Reachable!");
                    htmlReachableLabel.setIcon(redX);
                    htmlMessageLabel.setText("Content must be public and accessible to the web.");
                    htmlMessageLabel.setIcon(redX);
                    return;
                }

                String extracted = api.markupFound(content);
                if(extracted != null && extracted.length() > 0){
                    htmlExtractableLabel.setText("Markup Found");
                    htmlExtractableLabel.setIcon(greenCheck);
                    htmlMessageLabel.setText("No Problems Detected.");
                    htmlMessageLabel.setIcon(greenCheck);
                } else{
                    htmlExtractableLabel.setText("No Syndication Markup Found!");
                    htmlExtractableLabel.setIcon(redX);
                    htmlMessageLabel.setText("Content must be marked up with a <div> element containing the class 'syndicate'");
                    htmlMessageLabel.setIcon(redX);
                    return;
                }
            }
        });

        htmlPublishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jsonBody = api.getJsonBodyForPublishRequest(
                        ((Language) htmlLanguageCombo.getSelectedItem()).getId(),
                        ((Organization) htmlSourceCombo.getSelectedItem()).getId(),
                        htmlUrlField.getText(),
                        htmlNameField.getText(),
                        htmlDescriptionArea.getText(),
                        htmlTagList.getModel()
                );

                System.out.println("Publishing:");
                System.out.println(jsonBody);

                String resp = api.publish(jsonBody, "htmls");
                htmlMessageArea.setText(resp);
                if(resp.startsWith("Errors")){
                    htmlMessageLabel.setText("Error");
                    htmlMessageLabel.setIcon(redX);
                } else{
                    htmlMessageLabel.setText("Success");
                    htmlMessageLabel.setIcon(greenCheck);
                }
            }
        });

        importKeysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = configLoader.importKeys(keyImportArea.getText());
                if(result.compareTo("success") == 0){
                    configStatusLabel.setIcon(greenCheck);
                    configStatusLabel.setText("Settings Imported Successfully "+ new Date());
                    loadConfig();
                }else{
                    configStatusLabel.setIcon(redX);
                    configStatusLabel.setText(result +" "+ new Date());
                }
            }
        });

        saveSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = configLoader.saveConfig(publicKeyField.getText(), privateKeyField.getText(), secretField.getText(), apiUrlField.getText());
                if(result.compareTo("success") == 0){
                    configStatusLabel.setIcon(greenCheck);
                    configStatusLabel.setText("Settings Saved Successfully "+ new Date());
                } else{
                    configStatusLabel.setIcon(redX);
                    configStatusLabel.setText("Settings Not Saved, Error! "+ new Date());
                }
                api.initGenerator();
            }
        });

        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNewMedia addNew = new AddNewMedia(data, api, callback);
                addNew.pack();
                addNew.setVisible(true);
            }
        });

        bulkPublishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(job != null){
                    job.interrupt();
                    job = null;
                }
                job = new BulkPublishJob();
                job.start();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(job != null){
                    job.interrupt();
                    job = null;
                    progressBar1.setValue(0);
                    for(int i=0; i< mediaList.getModel().getSize(); i++){
                        ((DataLoader.MediaItem)mediaList.getModel().getElementAt(i)).setStatus(0);
                    }
                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            }
        });

        editSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long mediaId = ((DataLoader.MediaItem)mediaList.getSelectedValue()).getId();
                EditExisting edit = new EditExisting(data, api, callback, mediaId);
                edit.pack();
                edit.setVisible(true);
            }
        });
    }

    class BulkPublishJob extends Thread{
        @Override
        public void run() {
            progressBar1.setValue(0);
            progressBar1.setMaximum(mediaList.getModel().getSize());
            for(int i=0; i< mediaList.getModel().getSize(); i++){
                ((DataLoader.MediaItem)mediaList.getModel().getElementAt(i)).setStatus(0);
            }
            mainPanel.revalidate();
            mainPanel.repaint();
            for(int i=0; i< mediaList.getModel().getSize(); i++){
                if(Thread.interrupted()){
                    return;
                }

                DataLoader.MediaItem record = ((DataLoader.MediaItem)mediaList.getModel().getElementAt(i));

                String jsonRequest = api.getJsonBodyForPublishRequest(
                        record.getLang(),
                        record.getOrg(),
                        record.getUrl(),
                        record.getName(),
                        record.getDesc(),
                        record.getTags()
                );

                String response = api.publish(jsonRequest, "htmls");
                if(response.startsWith("Error")){
                    System.out.println(response);
                    bulkMessageTextArea.setText(response);
                    record.setStatus(-1);
                } else{
                    record.setStatus(1);
                }

                progressBar1.setValue(i+1);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        }
    }

    private void initIcons(){
        greenCheck = createImageIcon("images/check.png", "A green check symbol");
        redX = createImageIcon("images/x.png", "A red x symbol");
        neutral = createImageIcon("images/neutral.png", "A gray neutral symbol");
    }

    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
