package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

enum feature {
    ADD, DELETE, EDIT, DONE
}

public class addGUI extends JDialog {
    JTextField version;
    JTextField bundleid;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private final feature currentFeature;

    public addGUI(String bundleID, String version, feature feature) {
        setSize(500, 150);
        int x = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2);
        int y = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        this.bundleid.setText(bundleID);
        this.version.setText(version);
        this.currentFeature = feature;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (currentFeature == feature.ADD) {
            Main.addApp(bundleid.getText(), version.getText());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
