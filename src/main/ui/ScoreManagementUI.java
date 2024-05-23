package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.*;


import javax.swing.*;

import model.*;

import java.io.File;
import java.util.*;

import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;


/**
 * Represents application's main window frame.
 * Code cited: AlarmSystem
 */
class ScoreManagementUI extends JFrame {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 900;
    private JComboBox<String> printCombo;
    private JDesktopPane desktop;
    private JInternalFrame controlPanel;

    private CollectionDetailUI cdUI;

    private static final String JSON_STORE = "./data/ScoreCollection.json";

    private static final ImageIcon ADD_ICON = new ImageIcon("./data/images/add.png");
    private static final ImageIcon REMOVE_ICON = new ImageIcon("./data/images/remove.png");
    private static final ImageIcon FILTER_ICON = new ImageIcon("./data/images/filter.png");
    private static final ImageIcon SAVE_ICON = new ImageIcon("./data/images/save.png");
    private static final ImageIcon LOAD_ICON = new ImageIcon("./data/images/load.png");
    private static final ImageIcon STAFF_ICON = new ImageIcon("./data/images/staff.gif");


    private ScoreCollection scoreCollection = new ScoreCollection("My Score Collection");
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    /**
     * Constructor sets up button panel and score info window.
     */
    @SuppressWarnings("methodlength")
    public ScoreManagementUI() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());
        controlPanel = new JInternalFrame("Control Panel", false, false, true, true);
        controlPanel.setLayout(new BorderLayout());

        setContentPane(desktop);
        setTitle("Score Manager");
        setSize(WIDTH, HEIGHT);

        addButtonPanel();

        cdUI = new CollectionDetailUI(scoreCollection, this);
        desktop.add(cdUI);

        controlPanel.pack();
        controlPanel.setVisible(true);
        desktop.add(controlPanel);

//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (scoreCollection.getScoreList().size() == 0) {
                    String[] options = {"Yes", "No"};
                    int choice = JOptionPane.showOptionDialog(null,
                            "Are you sure you want to quit your Score Manager?",
                            "Quit Application?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null, options, "Yes"
                    );
                    if (choice == 0) {
                        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        JOptionPane.showMessageDialog(null, "Ciao!", "",
                                JOptionPane.INFORMATION_MESSAGE);
                        printLog(EventLog.getInstance());
                        System.exit(0);
                    } else {
                        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    }
                } else {
                    if (!isSameCollection()) {
                        String[] options = {"Save", "Don't Save", "Cancel"};
                        int choice = JOptionPane.showOptionDialog(null,
                                "Your collection has been changed. Would you like to save it?",
                                "Save Changes?",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null, options, "Yes"
                        );
                        if (choice == 0) {
                            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                            doSaveScoreCollection();
                            JOptionPane.showMessageDialog(null,
                                    "Saved " + scoreCollection.getName() + " to " + JSON_STORE + "! Ciao!",
                                    "Collection Saved", JOptionPane.INFORMATION_MESSAGE);
                            printLog(EventLog.getInstance());
                            System.exit(0);
                        } else if (choice == 1) {
                            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                            JOptionPane.showMessageDialog(null, "Ciao!", "Collection Not Saved",
                                    JOptionPane.INFORMATION_MESSAGE);
                            printLog(EventLog.getInstance());
                            System.exit(0);
                        } else {
                            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        }
                    } else {
                        String[] options = {"Yes", "No"};
                        int choice = JOptionPane.showOptionDialog(null,
                                "Are you sure you want to quit your Score Manager?",
                                "Quit Application?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null, options, "Yes"
                        );
                        if (choice == 0) {
                            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                            printLog(EventLog.getInstance());
                            System.exit(0);
                        } else {
                            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                        }
                    }
                }

//                System.exit(0);
            }
        });
        centreOnScreen();
        setVisible(true);
    }

    /**
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    /**
     * Helper to add control buttons.
     */
    private void addButtonPanel() {
        AddScoreAction addScoreAction = new AddScoreAction();
        addScoreAction.putValue(Action.SMALL_ICON, ADD_ICON);

        RemoveScoreAction removeScoreAction = new RemoveScoreAction();
        removeScoreAction.putValue(Action.SMALL_ICON, REMOVE_ICON);

        FilterCollectionAction filterCollectionAction = new FilterCollectionAction();
        filterCollectionAction.putValue(Action.SMALL_ICON, FILTER_ICON);

        SaveCollectionAction saveCollectionAction = new SaveCollectionAction();
        saveCollectionAction.putValue(Action.SMALL_ICON, SAVE_ICON);

        LoadCollectionAction loadCollectionAction = new LoadCollectionAction();
        loadCollectionAction.putValue(Action.SMALL_ICON, LOAD_ICON);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,6));
        buttonPanel.add(new JButton(addScoreAction));
        buttonPanel.add(new JButton(removeScoreAction));
        buttonPanel.add(new JButton(filterCollectionAction));
        buttonPanel.add(new JButton(saveCollectionAction));
        buttonPanel.add(new JButton(loadCollectionAction));
        buttonPanel.add(new JButton());
//        buttonPanel.add(createPrintCombo());

        controlPanel.add(buttonPanel, BorderLayout.NORTH);
    }


    /**
     * Represents the action to be taken when the user wants to add a new
     * score to the collection.
     */
    private class AddScoreAction extends AbstractAction {

        // Constructor
        AddScoreAction() {
            super("Add Score");
        }

        // MODIFIES: cdUI, this
        // EFFECTS: check if score that user wants to add is already in collection; if so inform the user, otherwise
        // add it into the collection and update the display
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent evt) {
            String scoreTitle = JOptionPane.showInputDialog(null,
                    "Score Title?",
                    "Enter score title to add",
                    JOptionPane.QUESTION_MESSAGE);

            if (scoreTitle == null) {
                return;
            } else if (isInCollection(scoreTitle)) {
                JOptionPane.showMessageDialog(null, scoreTitle + "is already in collection!",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Score newScore = new Score("", 0, "", "", 0.0, "", -1);
                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(6, 6));

                JTextField yearField = new JTextField();
                JTextField durHrField = new JTextField();
                JTextField durMinField = new JTextField();
                JTextField durSecField = new JTextField();
                JTextField instField = new JTextField();
                JTextField priceField = new JTextField();

                panel.add(new JLabel("Year Composed:"));
                panel.add(yearField);
                panel.add(new JLabel("Duration of Hour (2 digits):"));
                panel.add(durHrField);
                panel.add(new JLabel("Duration of Minute (2 digits):"));
                panel.add(durMinField);
                panel.add(new JLabel("Duration of Second (2 digits):"));
                panel.add(durSecField);
                panel.add(new JLabel("Instrumentation:"));
                panel.add(instField);
                panel.add(new JLabel("Price for Sale:"));
                panel.add(priceField);

                int result = JOptionPane.showConfirmDialog(null, panel,
                        "Enter score information for " + scoreTitle, JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int yearComposed = Integer.parseInt(yearField.getText());
                        String durHr = durHrField.getText();
                        String durMin = durMinField.getText();
                        String durSec = durSecField.getText();
                        String inst = instField.getText();
                        Double price = Double.parseDouble(priceField.getText());

                        setterAddScore(newScore, scoreTitle, yearComposed, durHr + ":" + durMin + ":" + durSec,
                                inst, price);

                        if (scoreCollection.addScore(newScore)) {
                            JOptionPane.showMessageDialog(null, "\"" + scoreTitle + "\" was added successfully!",
                                    "Score Added", JOptionPane.INFORMATION_MESSAGE);
                            cdUI.createScorePanel(scoreCollection.getScoreList());
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                scoreTitle + " was not added successfully...",
                                "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Represents action to be taken when user wants to remove a score
     * from the system.
     */
    private class RemoveScoreAction extends AbstractAction {

        // Constructor
        RemoveScoreAction() {
            super("Remove Score");
        }

        // MODIFIES: cdUI, this
        // EFFECTS: check if score that user wants to remove is not in collection; if not, inform the user, otherwise
        // remove it from scoreCollection and update the display
        @Override
        public void actionPerformed(ActionEvent evt) {
            String scoreTitle = JOptionPane.showInputDialog(null,
                    "Score Title?",
                    "Enter score title to remove",
                    JOptionPane.QUESTION_MESSAGE);

            if (!isInCollection(scoreTitle)) {
                JOptionPane.showMessageDialog(null, scoreTitle + " is NOT in collection!",
                        "System Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean returnVal = scoreCollection.removeScore(scoreTitle);
                if (returnVal) {
                    String printMsg = "\"" + scoreTitle + "\" was removed successfully!";
                    JOptionPane.showMessageDialog(null, printMsg,
                            "Score Added", JOptionPane.INFORMATION_MESSAGE);
                    cdUI.setScoreCollection(scoreCollection);
                    cdUI.createScorePanel(scoreCollection.getScoreList());
                } else {
                    String printMsg = "Failed to remove " + "\"" + scoreTitle + "\" successfully...";
                    JOptionPane.showMessageDialog(null, printMsg,
                            "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Represents action to be taken when user wants to filter the collection
     * to the system.
     */
    private class FilterCollectionAction extends AbstractAction {

        // Constructor
        FilterCollectionAction() {
            super("Filter Collection");
        }

        // EFFECTS: filter the scores based on user's request types, and produce a new display panel
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (scoreCollection.getScoreList().size() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Your Score Collection is empty!",
                        "Empty Collection", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String[] options = {"by year", "by duration", "by instrumentation"};
                int choice = JOptionPane.showOptionDialog(null,
                        "How would you like to filter?",
                        "",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, options, null
                );

                switch (choice) {
                    case 0:
                        doFilterByYear();
                        break;
//                    case 1:
//                        doFilterByDuration();
//                        break;
//                    case 2:
//                        doFilterByInst();
//                        break;
                }
            }
        }
    }

    /**
     * Represents action to be taken when user wants to save the collection
     * to the system.
     */
    private class SaveCollectionAction extends AbstractAction {

        // Constructor
        SaveCollectionAction() {
            super("Save Collection");
        }

        // EFFECTS: save the current score collection into the json file and inform user the result
        @SuppressWarnings("methodlength")
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (scoreCollection.getScoreList().size() == 0) {
                String[] options = {"Yes", "No"};
                int choice = JOptionPane.showOptionDialog(null,
                        "Your current collection is empty. Are you sure you want to save it?",
                        "Save empty collection?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, options, "Yes"
                );
                if (choice == 0) {
                    boolean saveResult = doSaveScoreCollection();
                    if (saveResult) {
                        JOptionPane.showMessageDialog(null,
                                "Saved empty " + scoreCollection.getName() + " to " + JSON_STORE,
                                "Empty Collection Saved", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Unable to write to file: " + JSON_STORE,
                                "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Empty collection was not saved",
                            "Empty Collection Not Saved", JOptionPane.INFORMATION_MESSAGE);
                    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            } else {
                boolean saveResult = doSaveScoreCollection();
                if (saveResult) {
                    JOptionPane.showMessageDialog(null,
                            "Saved " + scoreCollection.getName() + " to " + JSON_STORE,
                            "Collection Saved", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Unable to write to file: " + JSON_STORE,
                            "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Represents action to be taken when user wants to load the collection
     * from the system.
     */
    private class LoadCollectionAction extends AbstractAction {

        // Constructor
        LoadCollectionAction() {
            super("Load Collection");
        }

        // MODIFIES: cdUI
        // EFFECTS: load the score collection from json file and inform the user
        @Override
        public void actionPerformed(ActionEvent evt) {
            boolean loadResult = doLoadScoreCollection();
            if (loadResult) {
//                System.out.println("SMUI" + scoreCollection.getScoreList().get(0).getFileValue());
                cdUI.setScoreCollection(scoreCollection);
                cdUI.createScorePanel(scoreCollection.getScoreList());
                JOptionPane.showMessageDialog(null,
                        "Loaded " + scoreCollection.getName() + " from " + JSON_STORE,
                        "Collection Loaded", JOptionPane.INFORMATION_MESSAGE, STAFF_ICON);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Unable to read from file: " + JSON_STORE,
                        "System Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {

        // EFFECTS: switch focus when user clicks desktop
        @Override
        public void mouseClicked(MouseEvent e) {
            ScoreManagementUI.this.requestFocusInWindow();
        }
    }

    // EFFECTS: return true if score of given title is in collection, otherwise false.
    private boolean isInCollection(String title) {
        Score result = findScoreUsingTitle(title);
        return result != null;
    }

    // EFFECTS: return score found by searching using the title. Return null if not found
    private Score findScoreUsingTitle(String title) {
        Score result = null;
        for (Score next : scoreCollection.getScoreList()) {
            if (next.getTitle().equals(title)) {
                result = next;
            }
        }
        return result;
    }

    // REQUIRES: numerical values of year, duration, price >= 0
    // MODIFIES: score
    // EFFECTS: set provided details to the given score
    private void setterAddScore(Score newScore, String title, int yearComposed, String dur, String inst, double price) {
        newScore.setTitle(title);
        newScore.setYearComposed(yearComposed);
        newScore.setDuration(dur);
        newScore.setInstrumentation(inst);
        newScore.setPrice(price);
    }

    // EFFECTS; Constructs and Returns an ImageIcon
    protected static ImageIcon createImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        return icon;
//        java.net.URL imgURL = ScoreManagementUI.class.getResource(path);
//        if (imgURL != null) {
//            return new ImageIcon(imgURL);
//        } else {
//            System.err.println("Couldn't find file: " + path);
//            return null;
//        }
    }

    // EFFECTS: saves the score collection to file
    private boolean doSaveScoreCollection() {
        boolean result = scoreCollection.saveToJson(jsonWriter);
        return result;

//        try {
//            jsonWriter.open();
//            jsonWriter.write(scoreCollection);
//            jsonWriter.close();
//            return true;
//        } catch (FileNotFoundException e) {
//            return false;
//        }
    }

    // MODIFIES: this
    // EFFECTS: loads score collection from file
    private boolean doLoadScoreCollection() {
        try {
            scoreCollection = scoreCollection.loadFromJson(jsonReader);
//            scoreCollection = jsonReader.read();
//            System.out.println(scoreCollection.getScoreList().get(0).getTitle());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // EFFECTS: return true if the current scoreCollection is the same as the one saved in json file
    private boolean isSameCollection() {
        boolean isSame = false;
        try {
            ScoreCollection savedScoreCollection = jsonReader.read();
            if (savedScoreCollection.getScoreList().size() != scoreCollection.getScoreList().size()) {
                isSame = false;
            }
            for (Score i : savedScoreCollection.getScoreList()) {
                for (Score j : scoreCollection.getScoreList()) {
                    if (i.getTitle().equals(j.getTitle()) && i.getYearComposed() == j.getYearComposed()
                            && i.getDuration().equals(j.getDuration())
                            && i.getInstrumentation().equals(j.getInstrumentation())
                            && i.getPrice() == j.getPrice() && i.getDifficulty() == j.getDifficulty()
                            && i.getFilePath().equals(j.getFilePath()) && i.getFileValue() == j.getFileValue()) {
                        isSame = true;
                    } else {
                        isSame = false;
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
        return isSame;
    }

    // MODIFIES: fyUI
    // EFFECTS: filter collection by specified range of years and generate a new display window
    @SuppressWarnings("methodlength")
    private void doFilterByYear() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        JTextField beginYearField = new JTextField();
        JTextField endYearField = new JTextField();

        panel.add(new JLabel("Starting range:"));
        panel.add(beginYearField);
        panel.add(new JLabel("Ending range:"));
        panel.add(endYearField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Filter by Year", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (!(beginYearField.getText().equals("")) && !(endYearField.getText().equals(""))) {
                List<Score> returnList = new ArrayList<>();
                int bgn = Integer.parseInt(beginYearField.getText());
                int end = Integer.parseInt(endYearField.getText());
                returnList = scoreCollection.filterScoresByYear(bgn, end);
//                for (Score next : scoreCollection.getScoreList()) {
//                    if (next.isComposedBetween(Integer.parseInt(beginYearField.getText()),
//                            Integer.parseInt(endYearField.getText()))) {
//                        returnList.add(next);
//                    }
//                }
//                System.out.println(returnList.size());
                FilterByYearDisplayUI fyUI = new FilterByYearDisplayUI(this,
                        beginYearField.getText(), endYearField.getText());
                fyUI.createFilterPanel(returnList);
                fyUI.setVisible(true);
                desktop.add(fyUI);
                fyUI.toFront();
            }
        }

    }

//    public static void main(String[] args) {
//        new ScoreManagementUI();
//    }

    // EFFECTS: print log
    public void printLog(EventLog el) {
        System.out.println("\n" + "----- Event Log -----" + "\n");
        for (Event next : el) {
            String log = next.toString();
            System.out.println(log + "\n");
        }
    }

}





