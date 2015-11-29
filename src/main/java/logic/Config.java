package logic;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Created by psameyko on 27.11.15.
 */
public class Config {

    private Logger logger = Logger.getLogger(Config.class);
    public static final String CONFIG_FILE_NAME = "config.properties";

    public static final String PREFIX_TEXT = "prefix.text";
    public static final String PREFIX_DIGIT_COUNT = "prefix.digits.number";
    public static final String START_NUMBER = "prefix.incrementation.start.number";
    public static final String COLS_WITH_DIGITS = "index.of.columns.with.digital.value";

    private Properties props;
    private File propsFile;

    public Config(String path) {
        props = new Properties();
        propsFile = new File(path);
        try {
            InputStreamReader r = new InputStreamReader(new FileInputStream(propsFile), "UTF-8");
            props.load(r);
            r.close();
        } catch (IOException e) {
            if (propsFile.exists()) {
                logger.error("Ошибка чтения файла конфигурации", e);
            } else {
                logger.info("Файл конфигурации не найден. Используется конфигураци по умолчанию (все значения пусты)");
            }
            setDefaultConfigValues();
            propsFile = new File(CONFIG_FILE_NAME);
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public void setProperty(String key, String value) {
        props.put(key, value);
    }

    public void saveConfig() throws IOException {
        props.store(new FileWriter(propsFile), "app configuration");
    }

    private void setDefaultConfigValues() {
        props.put(PREFIX_DIGIT_COUNT, "");
        props.put(PREFIX_TEXT, "");
        props.put(START_NUMBER, "");
        props.put(COLS_WITH_DIGITS, "");
    }
}
