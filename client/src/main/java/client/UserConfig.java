package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
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

    public String getUserEmail() {
        try {
            String email = properties.getProperty("userEmail");
            if (email == null || email.isBlank()) {
                throw new Error();
            }
            return email;
        } catch (Error e) {
            System.out.println("Something went wrong. Email changed to the default");
            return "ooppteam58@gmail.com";
        }
    }

    public void setUserEmail(String email, String path) {
        properties.setProperty("userEmail", email);
        try (OutputStream out = new FileOutputStream(configPath)) {
            properties.store(out, "new email");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserPass() {
        try {
            String email = properties.getProperty("userPass");
            if (email == null || email.isBlank()) {
                throw new Error();
            }
            return email;
        } catch (Error e) {
            System.out.println("Something went wrong. Password changed to the default");
            return "npxruthvatcivuqz";
        }
    }

    public void setUserPass(String pass, String path) {
        properties.setProperty("userPass", pass);
        try (OutputStream out = new FileOutputStream(path)) {
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
