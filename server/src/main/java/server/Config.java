/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    private final String configPath = "client/src/main/resources/user_configs.properties";

    @Bean
    public Random getRandom() {
        return new Random();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // shit for the url

    /**
     * Checks what url is in the config file.
     *
     * @param p the properties object containing the server url
     * @return the url of the session in properties
     */
    public String getServerURLConfiguration(Properties p) {
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
    public void setServerUrlConfiguraton(Properties p, String url) {
        p.setProperty("serverUrl", url);
        try (OutputStream out = new FileOutputStream(configPath)) {
            p.store(out, "new serverUrl");
        } catch(IOException e) {
            e.printStackTrace();
//            log.log(Level.WARNING, e.getMessage(), e);
        }
    }


    // for the languages

    /**
     * Checks what language is in the config file.
     *
     * @param p the properties object which has the language configuration
     * @return the language in the file
     */
    public String getLanguageConfiguration(Properties p) {
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
    public void changeLanguageConfiguration(Properties p, String language) {
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