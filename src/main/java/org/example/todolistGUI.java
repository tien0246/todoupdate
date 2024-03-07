package org.example;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import static org.example.Main.checkAppUpdate;

public class todolistGUI extends JFrame {

    private JButton checkButton;
    private JButton saveButton;
    private JPanel dataPanel;
    private JScrollPane scroll;
    private JPanel mainPanel;
    private JButton addButton;
    private JButton refreshButton;
    private int countApp = 0;

    public todolistGUI() {
        super("To Do Update List");
        this.setSize(1200, 540);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        int x = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2);
        int y = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        checkButton.addActionListener(actionEvent -> checkAppUpdate());
        addButton.addActionListener(actionEvent -> {
            addGUI addGUI = new addGUI("", "", feature.ADD);
            addGUI.setVisible(true);
            refreshGUI();
        });

        refreshButton.addActionListener(actionEvent -> refreshGUI());
        saveButton.addActionListener(actionEvent -> JOptionPane.showMessageDialog(null, "No feature yet :)"));
    }

    public void addInfoPanel(String appName, String bundleID, String oldVersion, String newVersion, Image image) {
        infoPanel panel = new infoPanel(appName, bundleID, oldVersion, newVersion, image);
        JPanel contentPane = panel.getContentPane();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = countApp % 3;
        gbc.gridy = countApp / 3;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dataPanel.add(contentPane, gbc);
        countApp++;
    }

    public void reloadGUI() {
        validate();
        repaint();
    }

    public void removeAllInfoPanel() {
        dataPanel.removeAll();
        countApp = 0;
        validate();
        repaint();
    }

    private void refreshGUI() {
        removeAllInfoPanel();
        checkAppUpdate();
    }

    public static class infoPanel extends JPanel {
        private JPanel contentPane;
        private JLabel image;
        private JLabel bundleID;
        private JLabel oldVersion;
        private JLabel newVersion;
        private JLabel name;
        private JButton doneButton;
        private JButton deleteButton;
        private JButton editButton;

        public infoPanel(String appName, String bundleID, String oldVersion, String newVersion, Image image) {
            this.name.setToolTipText(appName);
            this.name.setText(appName);
            this.bundleID.setToolTipText(bundleID);
            this.bundleID.setText(bundleID);
            this.oldVersion.setText(oldVersion);
            this.newVersion.setText(newVersion);
            if (image == null) {
                image = new ImageIcon("src/main/java/icons/notfound.png").getImage();
            }
            this.image.setIcon(new ImageIcon(image));
            doneButton.addActionListener(actionEvent -> {
                Main.doneApp(bundleID, oldVersion, newVersion.split(" ")[0]);
                contentPane.removeAll();
                contentPane.revalidate();
                contentPane.repaint();
            });
            deleteButton.addActionListener(actionEvent -> {
                Main.deleteApp(bundleID, oldVersion);
                contentPane.removeAll();
                contentPane.revalidate();
                contentPane.repaint();
            });
            editButton.addActionListener(actionEvent -> edit());

            this.bundleID.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    super.mouseClicked(mouseEvent);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(bundleID), null);
                }
            });
            this.name.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    super.mouseClicked(mouseEvent);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(appName), null);
                }
            });
        }

        private void edit() {
            addGUI addGUI = new addGUI(bundleID.getText(), oldVersion.getText(), feature.EDIT);
            String tempBundleID = bundleID.getText();
            String tempVer = oldVersion.getText();
            addGUI.setVisible(true);
            this.bundleID.setText(addGUI.bundleId.getText());
            this.oldVersion.setText(addGUI.version.getText());
            String countrySelected = addGUI.getCountryCodesSelected();
            Main.editApp(tempBundleID, tempVer, addGUI.bundleId.getText(), addGUI.version.getText(), countrySelected);
        }

        public JPanel getContentPane() {
            Border roundBorder = new RoundBorder(10, Color.BLACK);
            contentPane.setBorder(roundBorder);
            doneButton.setIcon(new ImageIcon("src/main/java/icons/done.png"));
            deleteButton.setIcon(new ImageIcon("src/main/java/icons/delete.png"));
            editButton.setIcon(new ImageIcon("src/main/java/icons/edit.png"));
            return contentPane;
        }
    }

    static class RoundBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius * 2, radius * 2));
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }

}