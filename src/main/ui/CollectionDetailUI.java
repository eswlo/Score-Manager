package ui;

import model.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

/**
 * Represents user interface for viewing score detail.
 * Code cited: AlarmSystem
 */
class CollectionDetailUI extends JInternalFrame {
    private static final int WIDTH = 1100;
    private static final int HEIGHT = 700;
//    private static final String SCORE_IMAGE_PATH = "./data/images/score.png";
    private static final ImageIcon SCORE_ICON = new ImageIcon("./data/images/score.png");
    private static final ImageIcon LINK_ICON = new ImageIcon("./data/images/link.png");
    private ScoreCollection scoreCollection;
    private Component theParent;
    private JPanel scorePanel;

    /**
     * Constructor
     *
     * @param sc   the score collection
     * @param parent  the parent component
     */
    public CollectionDetailUI(ScoreCollection sc, Component parent) {
        super("Collection Detail", true, false, true, false);
        scoreCollection = sc;
        theParent = parent;
        setSize(WIDTH, HEIGHT);
        setPosition(parent);

        scorePanel = new JPanel();
//        scorePanel.setLayout(new GridLayout(1, 7));
        JScrollPane scrollPane = new JScrollPane(scorePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        setContentPane(scrollPane);

        setVisible(true);
    }

    /**
     * Sets the position of this remote control UI relative to parent component
     * @param parent   the parent component
     */
    private void setPosition(Component parent) {
        setLocation(0, parent.getHeight() - getHeight());
    }

    // MODIFIES: this
    // EFFECTS: create/update a panel that displays the detail of each score in the collection
    @SuppressWarnings("methodlength")
    public void createScorePanel(List<Score> scoreList) {
        scorePanel.removeAll();

        JButton descendingButton = new JButton("descending");

        descendingButton.addActionListener(e -> {
            List<Score> descendingList = scoreCollection.quicksortYear(scoreCollection.getScoreList());
            createScorePanel(descendingList);
        });

        JButton ascendingButton = new JButton("ascending");

        ascendingButton.addActionListener(e -> {
            List<Score> descendingList = scoreCollection.quicksortYear(scoreCollection.getScoreList());
            Collections.reverse(descendingList);
            createScorePanel(descendingList);
        });

        JButton defaultButton = new JButton("default");

        defaultButton.addActionListener(e -> {
            createScorePanel(scoreCollection.getScoreList());
        });

        JPanel panel = new JPanel(new GridLayout(2 + scoreList.size(), 9));
        panel.add(descendingButton);
        panel.add(ascendingButton);
        panel.add(defaultButton);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        panel.add(new JLabel("No.  "));
        panel.add(new JLabel("Title  "));
        panel.add(new JLabel("Year  "));
        panel.add(new JLabel("Duration  "));
        panel.add(new JLabel("Instrumentation  "));
        panel.add(new JLabel("Price  "));
        panel.add(new JLabel("Difficulty  "));
        panel.add(new JLabel("View Score"));
        panel.add(new JLabel("Link Score"));
        scorePanel.add(panel);

        for (int i = 1; i < 1 + scoreList.size(); i++) {
            Score next = scoreList.get(i - 1);
            panel.add(new JLabel("" + i + "."));
            panel.add(new JLabel(next.getTitle()));
            panel.add(new JLabel(Integer.toString(next.getYearComposed())));
            panel.add(new JLabel(next.getDuration()));
            panel.add(new JLabel(next.getInstrumentation()));
            panel.add(new JLabel(Double.toString(next.getPrice())));

            int diff = next.getDifficulty();
            if (diff == 0) {
                panel.add(new JLabel("Undetermined"));
            } else {
                panel.add(new JLabel(Integer.toString(diff)));
            }

            int scoreFileValue = next.getFileValue();
            String scoreFilePath = next.getFilePath();

            var ref = new Object() {
                int fileValue = scoreFileValue;
                JFileChooser fileChooser = new JFileChooser();
                String filePath = scoreFilePath;
                JButton scoreButton = new JButton("");
            };

            if (next.getFilePath().length() > 0) {
                ref.scoreButton = new JButton("", SCORE_ICON);
            }

            JButton linkButton = new JButton("", LINK_ICON);

            linkButton.addActionListener(e -> {
                ref.fileValue = ref.fileChooser.showOpenDialog(null);
                File selectedFile = ref.fileChooser.getSelectedFile();
                ref.filePath = selectedFile.getPath();
                next.setFilePath(ref.filePath);
                next.setFileValue(ref.fileValue);
                ref.scoreButton.setIcon(SCORE_ICON);
            });

            ref.scoreButton.addActionListener(e -> {
                if (ref.fileValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = new File(ref.filePath);
                        Desktop.getDesktop().open(selectedFile);
                    } catch (NullPointerException ne) {
                        JOptionPane.showMessageDialog(null, "No score file linked! Click the link icon to link one!",
                                "System Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ie) {
                        JOptionPane.showMessageDialog(null, "No score file found! Click the link icon to link again!",
                                "System Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (ref.fileValue == -1) {
                    JOptionPane.showMessageDialog(null, "No score file linked! Click the link icon to link one!",
                            "System Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            panel.add(ref.scoreButton);
            panel.add(linkButton);

            panel.setBorder(BorderFactory.createEtchedBorder());

            scorePanel.add(panel);
        }


        revalidate(); // Rebuild the layout
        repaint(); // Repaint the panel to display the updated scores
    }

    // EFFECTS: Constructs and Returns an ImageIcon
    protected static ImageIcon createImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        return icon;
    }

    public void setScoreCollection(ScoreCollection sc) {
        this.scoreCollection = sc;
    }
}
