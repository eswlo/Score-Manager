package ui;

import model.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

/**
 * Represents user interface for viewing scores filtered by years.
 * Code cited: AlarmSystem; components-ButtonDemoProject
 */
class FilterByYearDisplayUI extends JInternalFrame {
    private static final int WIDTH = 1100;
    private static final int HEIGHT = 700;
    private static final int SHIFT = 5;
    private static final ImageIcon SCORE_ICON = new ImageIcon("./data/images/score.png");
    private Component theParent;
    private JPanel filterPanel;

    /**
     * Constructor
     *
     * @param parent  the parent component
     */
    public FilterByYearDisplayUI(Component parent, String bgn, String end) {
        super("Scores composed between " + bgn + " and " + end,
                true, true, true, true);
        theParent = parent;
        setSize(WIDTH, HEIGHT);
        setPosition(parent);

        filterPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(filterPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        setContentPane(scrollPane);

        setVisible(false);
    }

    /**
     * Sets the position of this remote control UI relative to parent component
     * @param parent   the parent component
     */
    private void setPosition(Component parent) {
        setLocation(SHIFT, parent.getHeight() - getHeight() - SHIFT);
    }

    // MODIFIES: this
    // EFFECTS: create/update a panel that displays the detail of each score after filter
    @SuppressWarnings("methodlength")
    public void createFilterPanel(List<Score> filterScores) {
//        System.out.println("in filter " + filterScores.size());
        filterPanel.removeAll();
        JPanel panel = new JPanel(new GridLayout(1 + filterScores.size(), 8));
        panel.add(new JLabel("No.  "));
        panel.add(new JLabel("Title  "));
        panel.add(new JLabel("Year  "));
        panel.add(new JLabel("Duration  "));
        panel.add(new JLabel("Instrumentation  "));
        panel.add(new JLabel("Price  "));
        panel.add(new JLabel("Difficulty  "));
        panel.add(new JLabel("View Score"));
        filterPanel.add(panel);

        for (int i = 1; i < 1 + filterScores.size(); i++) {
            Score next = filterScores.get(i - 1);
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

            ref.scoreButton.addActionListener(e -> {
                if (ref.fileValue == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = new File(ref.filePath);
                        Desktop.getDesktop().open(selectedFile);
                    } catch (NullPointerException ne) {
                        JOptionPane.showMessageDialog(null, "No score file linked!",
                                "System Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ie) {
                        JOptionPane.showMessageDialog(null, "No score file found!",
                                "System Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                if (ref.fileValue == -1) {
                    JOptionPane.showMessageDialog(null, "No score file linked!",
                            "System Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            panel.add(ref.scoreButton);
            panel.setBorder(BorderFactory.createEtchedBorder());

            filterPanel.add(panel);
        }

        revalidate(); // Rebuild the layout
        repaint(); // Repaint the panel to display the updated scores
    }


    // EFFECTS: Constructs and Returns an ImageIcon
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

}
