package server.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final String apiKey;

    public CurrencyService(RestTemplate restTemplate, @Value("${openexchangerates.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }


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
