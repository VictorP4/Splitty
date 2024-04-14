package server.api.controller.tests;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.controllers.CurrencyController;
import server.api.services.CurrencyService;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @Test
    public void testGetExchangeRatesEndpointReturns200StatusCode() {
        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        when(currencyService.fetchExchangeRates(LocalDate.now())).thenReturn(ResponseEntity.ok(exchangeRates));
        ResponseEntity<Map<String, Double>> responseEntity = currencyController.getExchangeRates(LocalDate.now());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testConvertCurrencyEndpointReturns200StatusCode() {
        double convertedAmount = 85.0;
        when(currencyService.convertCurrency(100.0, "USD", "EUR", LocalDate.now())).thenReturn(ResponseEntity.ok(convertedAmount));
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(100.0, "USD", "EUR", LocalDate.now());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetExchangeRatesEndpointReturnsMapOfExchangeRates() {
        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        when(currencyService.fetchExchangeRates(LocalDate.now())).thenReturn(ResponseEntity.ok(exchangeRates));
        ResponseEntity<Map<String, Double>> responseEntity = currencyController.getExchangeRates(LocalDate.now());
        assertEquals(exchangeRates, responseEntity.getBody());
    }

    @Test
    public void testGetExchangeRatesEndpointReturnsCorrectExchangeRatesForGivenDate() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        when(currencyService.fetchExchangeRates(date)).thenReturn(ResponseEntity.ok(exchangeRates));
        ResponseEntity<Map<String, Double>> responseEntity = currencyController.getExchangeRates(date);
        assertEquals(exchangeRates, responseEntity.getBody());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsCorrectConvertedAmount() {
        double convertedAmount = 85.0;
        when(currencyService.convertCurrency(100.0, "USD", "EUR", LocalDate.now())).thenReturn(ResponseEntity.ok(convertedAmount));
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(100.0, "USD", "EUR", LocalDate.now());
        assertEquals(convertedAmount, responseEntity.getBody());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsBadRequestForInvalidAmount() {
        when(currencyService.convertCurrency(0.0, "USD", "EUR", LocalDate.now())).thenReturn(ResponseEntity.badRequest().build());
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(0.0, "USD", "EUR", LocalDate.now());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsBadRequestForInvalidCurrency() {
        when(currencyService.convertCurrency(100.0, "", "EUR", LocalDate.now())).thenReturn(ResponseEntity.badRequest().build());
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(100.0, "", "EUR", LocalDate.now());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsBadRequestForInvalidDate() {
        when(currencyService.convertCurrency(100.0, "USD", "EUR", LocalDate.of(2022, 1, 1))).thenReturn(ResponseEntity.badRequest().build());
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(100.0, "USD", "EUR", LocalDate.of(2022, 1, 1));
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsInternalServerErrorWhenServiceFails() {
        when(currencyService.convertCurrency(100.0, "USD", "EUR", LocalDate.now())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(100.0, "USD", "EUR", LocalDate.now());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsUnsupportedMediaTypeForNonNumericAmount() {
        when(currencyService.convertCurrency(Double.NaN, "USD", "EUR", LocalDate.now())).thenReturn(ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build());
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(Double.NaN, "USD", "EUR", LocalDate.now());
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, responseEntity.getStatusCode());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsUnsupportedMediaTypeForInvalidDate() {
        when(currencyService.convertCurrency(100.0, "USD", "EUR", null)).thenReturn(ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build());
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(100.0, "USD", "EUR", null);
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, responseEntity.getStatusCode());
    }

    @Test
    public void testGetExchangeRatesEndpointReturnsNotFoundForInvalidDate() {
        when(currencyService.fetchExchangeRates(LocalDate.of(2022, 1, 1))).thenReturn(ResponseEntity.notFound().build());
        ResponseEntity<Map<String, Double>> responseEntity = currencyController.getExchangeRates(LocalDate.of(2022, 1, 1));
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetExchangeRatesEndpointReturnsUnauthorizedForMissingAuthentication() {
        when(currencyService.fetchExchangeRates(LocalDate.now())).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        ResponseEntity<Map<String, Double>> responseEntity = currencyController.getExchangeRates(LocalDate.now());
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsUnauthorizedForMissingAuthentication() {
        when(currencyService.convertCurrency(100.0, "USD", "EUR", LocalDate.now())).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(100.0, "USD", "EUR", LocalDate.now());
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testConvertCurrencyEndpointReturnsForbiddenForInsufficientPermissions() {
        when(currencyService.convertCurrency(100.0, "USD", "EUR", LocalDate.now())).thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        ResponseEntity<Double> responseEntity = currencyController.convertCurrency(100.0, "USD", "EUR", LocalDate.now());
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }
    @AfterAll
    public static void clean(){
        File file = new File("rates/");
        file.delete();
    }
}
