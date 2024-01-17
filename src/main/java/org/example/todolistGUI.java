package org.example;

import javax.swing.*;
import java.awt.*;
public class todolistGUI extends JFrame {
    private JButton button1;
    private JButton button2;
    private JPanel datapane;
    private JScrollPane sroll;
    private JPanel mainpane;

    public todolistGUI() {
        super("To Do List");
        this.setSize(500, 500);
        this.setContentPane(mainpane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //        this.pack();
        this.setVisible(true);


        for (int i = 0; i < 3; i++) {
            JPanel panel = createInfoPanel("Row " + (i + 1));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            datapane.add(panel, gbc);
        }
        addInfoPanel("Facebook", "com.facebook.Facebook", "1.0", "2.0");

    }
    private JPanel createInfoPanel(String info) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 240, 240));
        //        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //        panel.setPreferredSize(new Dimension(200, 100));

        JLabel label = new JLabel(info);
        //        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label);

        return panel;
    }

    public void addInfoPanel(String appName, String bundleID, String oldVersion, String newVersion) {
        infoPanel panel = new infoPanel(appName, bundleID, oldVersion, newVersion);
        panel.setSize(200, 100);
        GridBagConstraints gbc = new GridBagConstraints();
        //        gbc.gridx = 0;
        //        gbc.gridy = GridBagConstraints.RELATIVE;
        //        gbc.insets = new Insets(10, 10, 10, 10);
        //        gbc.fill = GridBagConstraints.HORIZONTAL;
        //        gbc.weightx = 1.0;
        datapane.add(panel, gbc);
    }
}