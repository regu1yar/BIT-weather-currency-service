package bit.wcservice.database.entity.datarecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}