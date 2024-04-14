package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

@Configuration
@Component
public class UserConfig {

    private final String configPath = "src/main/resources/user_configs.properties";
    private final Properties properties = new Properties();

    @Autowired
    private ConfigurableEnvironment environment;

    /**
     * Initialized the userConfig and properties with the properties in the user_config file.
     */
    public UserConfig() {
        environment = new StandardEnvironment();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Alternative constructor for a thing in the server folder
     * @param path since it's relative to the path of the class you're calling it in
     */
    public UserConfig(String path) {
        environment = new StandardEnvironment();
        try (FileInputStream fis = new FileInputStream(path)) {
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
            String url = properties.getProperty("server.url");
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
        properties.setProperty("server.url", url);
        try (OutputStream out = new FileOutputStream(configPath)) {
            properties.store(out, "new serverUrl");
        } catch(IOException e) {
            e.printStackTrace();
//            log.log(Level.WARNING, e.getMessage(), e);
        }
    }

    /**
     * Gets the user's email
     * @return the email of the user
     */
    public String getUserEmail() {

        String email = properties.getProperty("userEmail");

        return email;

    }

    /**
     * Changes the user's email
     * @param email of the user
     */
    public void setUserEmail(String email) {
        properties.setProperty("userEmail", email);
        try (OutputStream out = new FileOutputStream(configPath)) {
            properties.store(out, "new email");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the user's password
     * @return the user's password
     */
    public String getUserPass() {
        String email = properties.getProperty("userPass");
        return email;
    }

    /**
     * Changes the user's password
     * @param pass of the user
     */
    public void setUserPass(String pass) {
        properties.setProperty("userPass", pass);
        try (OutputStream out = new FileOutputStream(configPath)) {
            properties.store(out, "new password");
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
                return "en";
            }
            return language;
        } catch (Error e) {
            System.out.println("Something went wrong. Language changed to default: english");
            return "en";
        }
    }

    /**
     * It's to fix a bug with the flags, the file needs to be reloaded when the language is changed
     */
    public void reloadLanguageFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(configPath);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        }
    }

}
