package client;

import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

@Configuration
public class UserConfig {

    private final String configPath = "src\\main\\resources\\user_configs.properties";
    private final Properties properties = new Properties();


    /**
     * Initialized the userConfig and properties with the properties in the user_config file.
     */
    public UserConfig() {
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets the properties of the UserConfig.
     *
     * @return the properties of the UserConfig.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Checks what url is in the config file.
     *
     * @return the url of the session in properties
     */
    public String getServerURLConfig() {
        try {
            String url = properties.getProperty("serverUrl");
            if (url == null || url.isBlank()) {
                throw new Error();
            }
            return url;
        } catch (Error e) {
            System.out.println("Something went wrong. Server changed to the default server");
            return "http://localhost:8080";
        }
    }

    /**
     * Changes the configuration of the server url.
     *
     * @param url the new server url
     */
    public void setServerUrlConfig(String url) {
        properties.setProperty("serverUrl", url);
        try (OutputStream out = new FileOutputStream(configPath)) {
            properties.store(out, "new serverUrl");
        } catch(IOException e) {
            e.printStackTrace();
//            log.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * Checks what server port is in the config file.
     *
     * @return the port of the session in properties
     */
    public int getServerPortConfig() {
        try {
            String port = properties.getProperty("server.port");
            if (port == null || port.isBlank()) {
                throw new Error();
            }
            return Integer.parseInt(port);
        } catch (Error e) {
            System.out.println("Something went wrong. Server changed to the default server");
            return 8080;
        }
    }

    /**
     * Changes the configuration of the server port.
     *
     * @param port the new server port
     */
    public void setServerPortConfig(int port) {
        properties.setProperty("server.port", Integer.toString(port));
        try (OutputStream out = new FileOutputStream(configPath)) {
            properties.store(out, "new server port");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // for the currency
    /**
     * Checks what url is in the config file.
     *
     * @return the url of the session in properties
     */
    public String getCurrencyConfig() {
        try {
            String currency = properties.getProperty("currency");
            if (currency == null || currency.isBlank()) {
                throw new Error();
            }
            return currency;
        } catch (Error e) {
            System.out.println("Something went wrong. Currency changed to default currency.");
            return "EUR";
        }
    }

    /**
     * Changes the configuration of the server url.
     *
     * @param currency the new currency
     */
    public void setCurrencyConfig(String currency) {
        properties.setProperty("currency", currency);
        try (OutputStream out = new FileOutputStream(configPath)) {
            properties.store(out, "new currency");
        } catch(IOException e) {
            e.printStackTrace();
//            log.log(Level.WARNING, e.getMessage(), e);
        }
    }


    // for the languages. This can be set up next week in case we would want this.

    /**
     * Checks what language is in the config file.
     *
     * @return the language in the file
     */
    public String getLanguageConfig() {
        try {
            String language = properties.getProperty("language");
            if (language == null || language.isBlank()) {
                return "english";
            }
            return language;
        } catch (Error e) {
            System.out.println("Something went wrong. Language changed to default: english");
            return "english";
        }
    }

    /**
     * Changes the configuration of the language.
     *
     * @param language the new language
     */
    public void setLanguageConfig(String language) {
        properties.setProperty("language", language);
        try (OutputStream out = new FileOutputStream(configPath)) {
            properties.store(out, "new language");
        } catch(IOException e) {
            e.printStackTrace();
//            log.log(Level.WARNING, e.getMessage(), e);
        }
    }

    // TODO: method to get the right file for the language and it's contents

}
