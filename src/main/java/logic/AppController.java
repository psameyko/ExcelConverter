package logic;

import org.apache.log4j.Logger;
import ui.App;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by psameyko on 27.11.15.
 */
public class AppController {
    private Logger logger = Logger.getLogger(AppController.class);
    private App view;
    private List<JTextField> textFields;
    private Config config;
    private JFileChooser fileChooser;

    public AppController(App view, String configPath) {
        this.view = view;
        textFields = new ArrayList<>();
        textFields.add(view.getSourceField());
        textFields.add(view.getTargetField());
        textFields.add(view.getPrefixField());
        textFields.add(view.getDigitField());
        textFields.add(view.getStartNumberField());
        config = new Config(configPath);
        fileChooser = new JFileChooser();
    }

    public void initUIActions() {
        setStatus(Status.FIELDS_EMPTY);
        initTextFieldActions();
        initButtonActions();
    }

    private void initButtonActions() {
        view.getBrowseSourceButton().addActionListener(e -> setBrowseAction());
        view.getClearButton().addActionListener(e -> setClearAction());
        view.getConvertButton().addActionListener(e -> setConvertAction());
        view.getSaveSettings().addActionListener(e -> setSaveAction());
    }

    private void setBrowseAction() {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = fileChooser.showOpenDialog(view);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            view.getSourceField().setText(selectedFile.getAbsolutePath());
        }
    }

    private void setClearAction() {
        for (JTextField tf : textFields) {
            tf.setText("");
            setStatus(Status.FIELDS_EMPTY);
        }
    }

    private void setSaveAction() {
        try {
            config.saveConfig();
        } catch (IOException e1) {
            logger.error("Ошибка сохранения конфигурации", e1);
            showMessage("При сохранении параметров произошла ошибка.",
                    "Параметры не были сохранены", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setConvertAction() {
        String src = view.getSourceField().getText();
        String trgt = view.getTargetField().getText();
        if (new File(trgt).exists()) {
            int response =
                    JOptionPane.showConfirmDialog(
                            view, "Файл \"" + trgt + "\" уже существует. Заменить?", "", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.NO_OPTION) {
                view.getTargetField().setText("");
                return;
            }
        }
        enableUI(false);
        new Executor() {
            @Override
            protected void action() throws Exception {
                Converter converter = new Converter();
                converter.setConfig(config).setSource(src).setTarget(trgt);
                setStatus(Status.IN_PROCESS);
                converter.convert();
            }

            @Override
            public void onFail(Exception e) {
                enableUI(true);
                setStatus(Status.ERRORS + ": " + e.getMessage());
            }

            @Override
            public void onSuccess() {
                enableUI(true);
                view.getTargetField().setText("");
                setStatus(Status.DONE + " Файл с результатом - " + trgt);
            }
        }.execute();
    }

    private void enableUI(boolean lock) {
        for (JTextField f : textFields) {
            f.setEnabled(lock);
        }
        view.getConvertButton().setEnabled(lock);
        view.getClearButton().setEnabled(lock);
        view.getSaveSettings().setEnabled(lock);
        view.getBrowseSourceButton().setEnabled(lock);
    }

    public void setStatus(String status) {
        view.getStatusText().setText(status);
    }

    private void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(view, message, title, type);
    }

    private void initTextFieldActions() {
        view.getPrefixField().setText(config.getProperty(Config.PREFIX_TEXT));
        view.getStartNumberField().setText(config.getProperty(Config.START_NUMBER));
        view.getDigitField().setText(config.getProperty(Config.PREFIX_DIGIT_COUNT));
        FieldListener listener = new FieldListener(this);
        for (JTextField tf : textFields) {
            tf.getDocument().addDocumentListener(listener);
        }
    }

    public void onFieldValueChange() {
        String pref = view.getPrefixField().getText();
        String dig = view.getDigitField().getText();
        String start = view.getStartNumberField().getText();
        String source = view.getSourceField().getText();
        String target = view.getTargetField().getText();
        config.setProperty(Config.PREFIX_TEXT, pref);
        config.setProperty(Config.PREFIX_DIGIT_COUNT, dig);
        config.setProperty(Config.START_NUMBER, start);
        if (pref.isEmpty() || dig.isEmpty() || start.isEmpty() || source.isEmpty() || target.isEmpty()) {
            setStatus(Status.FIELDS_EMPTY);
            view.getConvertButton().setEnabled(false);
        } else {
            setStatus(Status.READY);
            view.getConvertButton().setEnabled(true);
        }
    }
}
