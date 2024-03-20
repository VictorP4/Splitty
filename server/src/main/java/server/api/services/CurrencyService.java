package server.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final String apiKey;

    /**
     * Constructor for CurrencyService.
     *
     * @param restTemplate The RestTemplate instance used for making HTTP requests.
     * @param apiKey       The API key for accessing the currency exchange service.
     */
    public CurrencyService(RestTemplate restTemplate, @Value("${openexchangerates.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    /**
     * Caches the exchange rates into a file.
     *
     * @param fileName The name of the file to cache the rates.
     * @param rates    The exchange rates to cache.
     */
    private void cacheRates(String fileName, Map<String, Double> rates) {
        File directory = new File("rates");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, Double> entry : rates.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches exchange rates for a specific date from the currency exchange service.
     *
     * @param date The date for which exchange rates are requested.
     * @return ResponseEntity containing a map of currency codes to exchange rates.
     */
    public ResponseEntity<Map<String, Double>> fetchExchangeRates(LocalDate date) {
        String cacheFileName = "rates/" + date + ".txt";

        try {
            if (new File(cacheFileName).exists()) {
                Map<String, Double> cachedRates = readCachedRates(cacheFileName);
                return ResponseEntity.ok(cachedRates);
            }

            String url = "https://openexchangerates.org/api/historical/" + date + ".json?app_id=" + apiKey;
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Double> exchangeRates = (Map<String, Double>) responseEntity.getBody().get("rates");
                cacheRates(cacheFileName, exchangeRates);
                return ResponseEntity.ok(exchangeRates);
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * Converts an amount of currency from one currency to another for a specific date.
     *
     * @param amount       The amount of currency to convert.
     * @param fromCurrency The currency to convert from.
     * @param toCurrency   The currency to convert to.
     * @param date         The date for which the conversion is done.
     * @return ResponseEntity containing the converted amount.
     */
    public ResponseEntity<Double> convertCurrency(double amount, String fromCurrency, String toCurrency, LocalDate date) {
        String cacheFileName = "rates/" + date + ".txt";

        Map<String, Double> exchangeRates = readCachedRates(cacheFileName);

        if (exchangeRates == null || !exchangeRates.containsKey(fromCurrency) || !exchangeRates.containsKey(toCurrency)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        double fromRate = exchangeRates.get(fromCurrency);
        double toRate = exchangeRates.get(toCurrency);
        double convertedAmount = (amount / fromRate) * toRate;

        return ResponseEntity.ok(convertedAmount);
    }

    /**
     * Reads cached exchange rates from a file.
     *
     * @param fileName The name of the file containing cached rates.
     * @return The map of currency codes to exchange rates read from the file.
     */
    private Map<String, Double> readCachedRates(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            Map<String, Double> rates = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                rates.put(parts[0], Double.parseDouble(parts[1]));
            }
            return rates;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
