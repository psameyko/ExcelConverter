package ui;

import logic.AppController;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by psameyko on 27.11.15.
 */
public class App extends JFrame {
    private static Logger logger = Logger.getLogger(App.class);
    private JLabel sourceLabel = new JLabel("Исходный Excel файл:");
    private JTextField sourceField = new JTextField(25);
    private JButton browseSourceButton = new JButton("Выбрать..");

    private JLabel targetLabel = new JLabel("Имя файла:");
    private JTextField targetField = new JTextField(18);

    private JButton convertButton = new JButton("Выполнить");

    private JLabel prefixLabel = new JLabel("Текстовая часть префикса:");
    private JTextField prefixField = new JTextField(15);

    private JLabel digitLabel = new JLabel("Количество символов численной части префикса:");
    private JTextField digitField = new JTextField(15);

    private JLabel startNumberLabel = new JLabel("Начинать нумерацию с:");
    private JTextField startNumberField = new JTextField(15);

    private JButton clearButton = new JButton("Очистить");
    private JButton saveSettings = new JButton("Сохранить");

    private JLabel statusLabel = new JLabel("Статус: ");
    private JTextField statusText = new JTextField(43);

    public App() {
        super();
        sourceField.setEditable(false);
        convertButton.setEnabled(false);
        sourceField.setToolTipText("Excel файл, который необходимо преобразовать");
        targetField.setToolTipText("Имя файла для сохранения результата");
        this.setLayout(new BorderLayout());

        this.add(getRootPanel(), BorderLayout.CENTER);

        this.add(getButtonsPanel(), BorderLayout.AFTER_LAST_LINE);
        this.setMinimumSize(new Dimension(600, 500));
        this.setLocationRelativeTo(null);
        this.pack();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        logger.info("---- Application was started ----");
    }

    private JPanel getRootPanel() {
        JPanel p = new JPanel();
        GridBagLayout ll = new GridBagLayout();
        p.setLayout(ll);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.CENTER;
        c.ipady = 0;       //reset to default
        c.insets = new Insets(20, 0, 0, 0);  //top padding
        c.gridx = 1;
        c.gridy = 1;
        p.add(getFilesPanel(), c);
        c.gridy = 2;
        p.add(getSettingsPanel(), c);
        c.gridy = 3;
        p.add(getStatusPanel(), c);
        return p;
    }

    private JPanel getStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(statusLabel);
        panel.add(statusText);
        statusText.setEditable(false);
        statusText.setHorizontalAlignment(SwingConstants.CENTER);
        statusText.setBackground(Color.lightGray);
        return panel;
    }

    private JPanel getButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonsPanel.add(clearButton);
        buttonsPanel.add(convertButton);
        return buttonsPanel;
    }

    private JPanel getSettingsPanel() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(600, 180));
        GridBagLayout ll = new GridBagLayout();
        p.setLayout(ll);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.WEST;
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 1;
        p.add(prefixLabel, c);
        c.gridy = 2;
        p.add(digitLabel, c);
        c.gridy = 3;
        p.add(startNumberLabel, c);

        c.fill = GridBagConstraints.EAST;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.gridy = 1;
        p.add(prefixField, c);
        c.gridy = 2;
        p.add(digitField, c);
        c.gridy = 3;
        p.add(startNumberField, c);
        c.gridx = 1;
        c.gridy = 4;
        c.gridy = 4;
        c.gridx = 2;
        p.add(saveSettings, c);
        p.setBorder(BorderFactory.createTitledBorder("Параметры"));
        return p;
    }

    private JPanel getFilesPanel() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(600, 150));
        GridBagLayout ll = new GridBagLayout();
        p.setLayout(ll);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.WEST;
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 1;
        p.add(sourceLabel, c);
        c.gridy = 2;
        p.add(targetLabel, c);
        c.fill = GridBagConstraints.EAST;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2;
        c.gridy = 1;
        p.add(sourceField, c);
        c.gridy = 2;
        p.add(targetField, c);
        c.gridx = 3;
        c.gridy = 1;
        p.add(browseSourceButton, c);
        c.gridy = 2;
        p.setBorder(BorderFactory.createTitledBorder("Файлы"));
        return p;
    }

    public JLabel getSourceLabel() {
        return sourceLabel;
    }

    public JTextField getSourceField() {
        return sourceField;
    }

    public JButton getBrowseSourceButton() {
        return browseSourceButton;
    }

    public JLabel getTargetLabel() {
        return targetLabel;
    }

    public JTextField getTargetField() {
        return targetField;
    }

    public JButton getConvertButton() {
        return convertButton;
    }

    public JLabel getPrefixLabel() {
        return prefixLabel;
    }

    public JTextField getPrefixField() {
        return prefixField;
    }

    public JLabel getDigitLabel() {
        return digitLabel;
    }

    public JTextField getDigitField() {
        return digitField;
    }

    public JLabel getStartNumberLabel() {
        return startNumberLabel;
    }

    public JTextField getStartNumberField() {
        return startNumberField;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getSaveSettings() {
        return saveSettings;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JTextField getStatusText() {
        return statusText;
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("SetLookAndFeel Error", e);
        }
        App app = new App();
        AppController ctrl = new AppController(app, "settings.properties");
        ctrl.initUIActions();
        app.setVisible(true);
    }
}
