package bit.wcservice.database.entity.datarecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    private Currency currencyData;

    @BeforeEach
    void setUp() {
        currencyData = new Currency();
    }

    @Test
    void successfullyExtractFeatures() {
        String currencyValue = "77.80";
        currencyData.setCurrencyValue(currencyValue);
        List<Double> currencyFeatures = currencyData.extractFeatures();
        assertEquals(1, currencyFeatures.size());
        assertEquals(Double.valueOf(currencyValue), currencyFeatures.get(0));
    }

    @Test
    void successfullyParseCommaSeparatedValue() {
        String currencyValue = "77,80";
        currencyData.setCurrencyValue(currencyValue);
        List<Double> currencyFeatures = currencyData.extractFeatures();
        assertEquals(1, currencyFeatures.size());
        assertEquals(77.80, currencyFeatures.get(0), 1e9);
    }

    @Test
    void toStringContainsCurrencyValueAndDate() {
        String currencyValue = "77.80";
        LocalDate today = LocalDate.now();
        currencyData.setCurrencyValue(currencyValue);
        currencyData.setDate(today);
        assertTrue(currencyData.toString().contains(currencyValue));
        assertTrue(currencyData.toString().contains(String.valueOf(today.getYear())));
        assertTrue(currencyData.toString().contains(String.valueOf(today.getMonthValue())));
        assertTrue(currencyData.toString().contains(String.valueOf(today.getDayOfMonth())));
    }
}