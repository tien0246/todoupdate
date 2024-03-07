package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

enum feature {
    ADD, EDIT
}

public class addGUI extends JDialog {

    private final feature currentFeature;
    private final List<JCheckBox> controlList = new ArrayList<>();
    JTextField version;
    JTextField bundleId;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel countryPanel;
    private JScrollPane scrollPanel;
    private JTextField searchCountry;


    public addGUI(String bundleID, String version, feature f) {
        setSize(500, 580);
        int x = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - this.getWidth()) / 2);
        int y = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(32);
        this.bundleId.setText(bundleID);
        this.version.setText(version);
        this.currentFeature = f;
        String[] countryCodes = {"AF", "AL", "DZ", "AD", "AO", "AI", "AG", "AR", "AM", "AU", "AT", "AZ", "BS", "BH", "BD", "BB", "BY", "BE", "BZ", "BJ", "BM", "BT", "BO", "BA", "BW", "BR", "BN", "BG", "BF", "CV", "KH", "CM", "CA", "KY", "CF", "TD", "CL", "CN", "CO", "CD", "CG", "CR", "HR", "CY", "CZ", "CI", "DK", "DM", "DO", "EC", "EG", "SV", "EE", "SZ", "ET", "FJ", "FI", "FR", "GA", "GM", "GE", "DE", "GH", "GR", "GD", "GT", "GN", "GW", "GY", "HN", "HK", "HU", "IS", "IN", "ID", "IQ", "IE", "IL", "IT", "JM", "JP", "JO", "KZ", "KE", "KR", "XK", "KW", "KG", "LA", "LV", "LB", "LR", "LY", "LI", "LT", "LU", "MO", "MG", "MW", "MY", "MV", "ML", "MT", "MR", "MU", "MX", "FM", "MD", "MC", "MN", "ME", "MS", "MA", "MZ", "MM", "NA", "NR", "NP", "NL", "NZ", "NI", "NE", "NG", "NO", "OM", "PK", "PW", "PS", "PA", "PG", "PY", "PE", "PH", "PL", "PT", "QA", "MK", "RO", "RU", "RW", "KN", "LC", "VC", "WS", "ST", "SA", "SN", "RS", "SC", "SL", "SG", "SK", "SI", "SB", "ZA", "ES", "LK", "SR", "SE", "CH", "TW", "TJ", "TZ", "TH", "TO", "TT", "TN", "TR", "TM", "TC", "UG", "UA", "AE", "GB", "US", "UY", "UZ", "VU", "VE", "VN", "VG", "YE", "ZM", "ZW"};
        String[] countryNames = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Anguilla", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas (the)", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia (Plurinational State of)", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Cayman Islands (the)", "Central African Republic (the)", "Chad", "Chile", "China", "Colombia", "Congo (the Democratic Republic of the)", "Congo (the)", "Costa Rica", "Croatia", "Cyprus", "Czechia", "Côte d'Ivoire", "Denmark", "Dominica", "Dominican Republic (the)", "Ecuador", "Egypt", "El Salvador", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia (the)", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Korea (the Republic of)", "Kosovo", "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic (the)", "Latvia", "Lebanon", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Mauritania", "Mauritius", "Mexico", "Micronesia (Federated States of)", "Moldova (the Republic of)", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands (the)", "New Zealand", "Nicaragua", "Niger (the)", "Nigeria", "Norway", "Oman", "Pakistan", "Palau", "Palestine, State of", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines (the)", "Poland", "Portugal", "Qatar", "Republic of North Macedonia", "Romania", "Russian Federation (the)", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "South Africa", "Spain", "Sri Lanka", "Suriname", "Sweden", "Switzerland", "Taiwan (Province of China)", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands (the)", "Uganda", "Ukraine", "United Arab Emirates (the)", "United Kingdom of Great Britain and Northern Ireland (the)", "United States of America (the)", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela (Bolivarian Republic of)", "Viet Nam", "Virgin Islands (British)", "Yemen", "Zambia", "Zimbabwe"};
        String[] countryCodesSelected = null;
        if (currentFeature == feature.EDIT) {
            countryCodesSelected = Main.getCountryCodesSelected(bundleID);
        }
        countryPanel.setLayout(new BoxLayout(countryPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < countryCodes.length; i++) {
            JCheckBox checkBox = new JCheckBox(countryNames[i] + " (" + countryCodes[i] + ")");
            checkBox.setActionCommand(countryCodes[i]);
            controlList.add(checkBox);
            if (currentFeature == feature.EDIT) {
                for (String countryCode : countryCodesSelected) {
                    if (countryCode.equals(countryCodes[i])) {
                        checkBox.setSelected(true);
                    }
                }
            }
            countryPanel.add(checkBox);
        }
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
        searchCountry.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                super.keyReleased(keyEvent);
                String search = searchCountry.getText().toLowerCase();
                for (JCheckBox checkBox : controlList) {
                    if (checkBox.getText().toLowerCase().contains(search)) {
                        checkBox.setVisible(true);
                    } else {
                        checkBox.setVisible(false);
                    }
                }
            }
        });
    }

    private void onOK() {
        if (currentFeature == feature.ADD) {
            StringBuilder countrySelected = new StringBuilder();
            for (JCheckBox checkBox : controlList) {
                if (checkBox.isSelected()) {
                    System.out.println(checkBox.getText());
                    countrySelected.append(checkBox.getActionCommand()).append(",");
                }
            }
            if (countrySelected.length() == 0) countrySelected.append("US,"); // default country
            Main.addApp(bundleId.getText(), version.getText(), countrySelected.delete(countrySelected.length() - 1, countrySelected.length()).toString());
        }
        dispose();
    }

    String getCountryCodesSelected() {
        StringBuilder countrySelected = new StringBuilder();
        for (JCheckBox checkBox : controlList) {
            if (checkBox.isSelected()) {
                countrySelected.append(checkBox.getActionCommand()).append(",");
            }
        }
        return countrySelected.delete(countrySelected.length() - 1, countrySelected.length()).toString();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
