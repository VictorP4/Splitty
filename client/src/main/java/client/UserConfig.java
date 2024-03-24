package client;

import org.springframework.context.annotation.Configuration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

@Configuration
public class UserConfig {

    private final String configPath = "client/src/main/resources/user_configs.properties";


    /**
     * Checks what url is in the config file.
     *
     * @param p the properties object containing the server url
     * @return the url of the session in properties
     */
    public String getServerURLConfig(Properties p) {
        try {
            String url = p.getProperty("serverUrl");
            if (url == null || url.isBlank()) {
                return "http://localhost:5000";
            }
            return url;
        } catch (Error e) {
            System.out.println("Something went wrong. Server changed to the default server");
            return "http://localhost:5000";
        }
    }

    /**
     * Changes the configuration of the server url.
     *
     * @param p the properties object containing the properties
     * @param url the new server url
     */
    public void setServerUrlConfig(Properties p, String url) {
        p.setProperty("serverUrl", url);
        try (OutputStream out = new FileOutputStream(configPath)) {
            p.store(out, "new serverUrl");
        } catch(IOException e) {
            e.printStackTrace();
//            log.log(Level.WARNING, e.getMessage(), e);
        }
    }

    // for the currency
    /**
     * Checks what url is in the config file.
     *
     * @param p the properties object containing the server url
     * @return the url of the session in properties
     */
    public String getCurrencyConfig(Properties p) {
        try {
            String currency = p.getProperty("currency");
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
     * @param p the properties object containing the properties
     * @param url the new server url
     */
    public void setCurrencyConfig(Properties p, String currency) {
        p.setProperty("currency", currency);
        try (OutputStream out = new FileOutputStream(configPath)) {
            p.store(out, "new currency");
        } catch(IOException e) {
            e.printStackTrace();
//            log.log(Level.WARNING, e.getMessage(), e);
        }
    }


    // for the languages. This can be set up next week in case we would want this.

    /**
     * Checks what language is in the config file.
     *
     * @param p the properties object which has the language configuration
     * @return the language in the file
     */
    public String getLanguageConfig(Properties p) {
        try {
            String language = p.getProperty("language");
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
     * @param p the properties object containing the properties
     * @param language the new language
     */
    public void changeLanguageConfig(Properties p, String language) {
        p.setProperty("language", language);
        try (OutputStream out = new FileOutputStream(configPath)) {
            p.store(out, "new language");
        } catch(IOException e) {
            e.printStackTrace();
//            log.log(Level.WARNING, e.getMessage(), e);
        }
    }

    // TODO: method to get the right file for the language and it's contents

}
