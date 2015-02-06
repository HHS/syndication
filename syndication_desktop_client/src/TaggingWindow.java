import rest.ApiTools;
import rest.model.Tag;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Created by sgates on 12/3/14.
 */
public class TaggingWindow extends JFrame {
    private JTextField tagQ;
    private JList tagQueryList;
    private JButton addTagSButton;
    private JPanel mainPanel;
    private JComboBox combo;
    private JList savedTagList;
    private JButton saveTagsButton;
    private JPanel layoutPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel rightButtonPanel;
    private JButton removeButton;
    private DefaultListModel<Tag> savedTagsModel = new DefaultListModel<Tag>();

    private ApiTools api;
    private JList selectedTags;
    private TaggingWindow callback;

    public TaggingWindow(ApiTools api, JList tagList){
        super("Tag Test");
        callback = this;
        this.api = api;
        selectedTags = tagList;
        addActionListeners();
        setContentPane(mainPanel);
        this.setMinimumSize(new Dimension(640, 500));
        savedTagList.setModel(savedTagsModel);
        initExistingTags();
    }

    private void initExistingTags(){
        ListModel<Tag> existingTags = (ListModel<Tag>) selectedTags.getModel();
        for(int i=0; i<existingTags.getSize(); i++){
            savedTagsModel.addElement(existingTags.getElementAt(i));
        }
    }

    private void addActionListeners(){
        saveTagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTags.setModel((ListModel<Tag>)savedTagList.getModel());
                callback.dispatchEvent(new WindowEvent(callback, WindowEvent.WINDOW_CLOSING));
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Tag> selectedValues = (List<Tag>) savedTagList.getSelectedValuesList();
                for(Tag tag : selectedValues){
                    savedTagsModel.removeElement(tag);
                }
            }
        });

        addTagSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTagsToSavedTagList();
            }
        });

        tagQ.getDocument().addDocumentListener(new DocumentListener() {
            long delay = 500;
            UpdateTask updateTask;
            Timer t = new Timer();
            String latestInput = "";
            String previousInput = "";

            {
                t.schedule(new UpdateTask(), delay);
            }

            synchronized String getLatestInput(){
                return latestInput;
            }

            synchronized void setLatestInput(String input){
                System.out.println("Setting");
                latestInput = input;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setLatestInput(tagQ.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setLatestInput(tagQ.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}

            class UpdateTask extends TimerTask{
                @Override
                public void run() {
                    String latest = getLatestInput();
                    if(latest != null && latest.length() > 0 && latest.compareTo(previousInput)!=0) {
                        previousInput = latest;
                        List tags = api.queryTags(latest);
                        tagQueryList.setListData(tags.toArray());
                        tagQueryList.setSelectedIndex(getNextIndex());
                    }
                    t.schedule(new UpdateTask(), delay);
                }
            }
        });
        tagQ.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_DOWN:
                        tagQueryList.setSelectedIndex(getNextIndex());
                        break;
                    case KeyEvent.VK_UP:
                        tagQueryList.setSelectedIndex(getLastIndex());
                        tagQ.setCaretPosition(tagQ.getText().length());
                        break;
                    case KeyEvent.VK_ENTER:
                        addTagsToSavedTagList();
                        break;
                }
            }
        });

        tagQueryList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_ENTER:
                        addTagsToSavedTagList();
                        break;
                }
            }
        });
    }

    private void addTagsToSavedTagList(){
        List<Tag> selectedValues = (List<Tag>) tagQueryList.getSelectedValuesList();
        for(Tag tag : selectedValues){
            addTagToSavedTagList(tag);
        }
    }

    private void addTagToSavedTagList(Tag tag){
        if(!savedTagsModel.contains(tag)){
            savedTagsModel.addElement(tag);
        }
    }

    private int getNextIndex(){
        int currentIndex = tagQueryList.getSelectedIndex();
        if(currentIndex < 0){
            currentIndex = 0;
        } else if(currentIndex < tagQueryList.getModel().getSize()-1){
            currentIndex++;
        }
        return currentIndex;
    }

    private int getLastIndex(){
        int currentIndex = tagQueryList.getSelectedIndex();
        if(currentIndex < 0){
            currentIndex = 0;
        } else if(currentIndex > 0){
            currentIndex--;
        }
        return currentIndex;
    }
}
