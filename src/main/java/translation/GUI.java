package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            Translator translator = new JSONTranslator();
            LanguageCodeConverter langConv = new LanguageCodeConverter();
            CountryCodeConverter countryConv = new CountryCodeConverter();
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            Map<String, String> langNameToCode = new LinkedHashMap<>();
            JPanel translationPanel = new JPanel();
            translationPanel.setLayout(new BoxLayout(translationPanel, BoxLayout.Y_AXIS));
            JLabel resultLabelText = new JLabel("Translation:");
            resultLabelText.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel resultLabel = new JLabel(" ");
            resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            translationPanel.add(resultLabelText);
            translationPanel.add(resultLabel);
            DefaultListModel<String> listModel = new DefaultListModel<>();
            JComboBox<String> languageComboBox = new JComboBox<>();
            Map<String, String> countryNameToCode = new LinkedHashMap<>();
            languagePanel.add(languageComboBox);

            for (String code : translator.getLanguageCodes()) {
                String name = langConv.fromLanguageCode(code);
                langNameToCode.put(name, code);}
            for (String name : langNameToCode.keySet()) {
                languageComboBox.addItem(name);}
            for (String code : translator.getCountryCodes()) {
                String name = countryConv.fromCountryCode(code);
                countryNameToCode.put(name, code);}
            for (String name : countryNameToCode.keySet()) {
                listModel.addElement(name);}

            JList<String> countryList = new JList<>(listModel);
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(countryList);
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(translationPanel);
            mainPanel.add(scrollPane);

            languageComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String langName = (String) languageComboBox.getSelectedItem();
                        String countryName = countryList.getSelectedValue();
                        if (langName == null || countryName == null) {
                            resultLabel.setText(" ");
                            return;
                        }
                        String langCode = langNameToCode.get(langName);
                        String countryCode = countryNameToCode.get(countryName);
                        String result = translator.translate(countryCode, langCode);
                        if (result == null) result = "no translation found!";
                        resultLabel.setText(result);
                    }
                }
            });
            countryList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    String langName = (String) languageComboBox.getSelectedItem();
                    String countryName = countryList.getSelectedValue();
                    if (langName == null || countryName == null) {
                        resultLabel.setText(" ");
                        return;
                    }
                    String langCode = langNameToCode.get(langName);
                    String countryCode = countryNameToCode.get(countryName);
                    String result = translator.translate(countryCode, langCode);
                    if (result == null) result = "no translation found!";
                    resultLabel.setText(result);
                }
            });
            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
