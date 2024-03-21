package server.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.api.services.CurrencyService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    /**
     * Constructor for CurrencyController.
     *
     * @param currencyService The CurrencyService instance to be injected.
     */
    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    /**
     * Endpoint for retrieving exchange rates for a specific date.
     *
     * @param date The date for which exchange rates are requested.
     * @return ResponseEntity with a map of currency codes to exchange rates.
     */
    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangeRates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return currencyService.fetchExchangeRates(date);
    }

    /**
     * Endpoint for converting currency for a specific date.
     *
     * @param amount       The amount of currency to convert.
     * @param fromCurrency The currency to convert from.
     * @param toCurrency   The currency to convert to.
     * @param date         The date for which the conversion is done.
     * @return ResponseEntity with the converted amount.
     */
    @GetMapping("/convert")
    public ResponseEntity<Double> convertCurrency(
            @RequestParam double amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return currencyService.convertCurrency(amount, fromCurrency.toUpperCase(), toCurrency.toUpperCase(), date);
    }
}
