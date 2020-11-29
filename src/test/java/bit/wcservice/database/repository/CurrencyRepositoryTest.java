package bit.wcservice.database.repository;

import bit.wcservice.database.entity.datarecord.Currency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CurrencyRepositoryTest {

    private final LocalDate BASE_DATE = LocalDate.of(2020, 1, 1);
    private final String USD_CURRENCY_NAME = "USD";

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        currencyRepository.saveAll(List.of(
                new Currency(BASE_DATE, "75.0", USD_CURRENCY_NAME),
                new Currency(BASE_DATE.plusDays(1), "80.0", USD_CURRENCY_NAME),
                new Currency(BASE_DATE.plusDays(2), "85.0", USD_CURRENCY_NAME)
        ));

        currencyRepository.flush();
    }

    @AfterEach
    void tearDown() {
        currencyRepository.deleteAll();
    }

    @Test
    public void checkInitializationByDate() {
        Currency currency = currencyRepository.getByDate(BASE_DATE.plusDays(1));
        assertNotNull(currency);
        assertEquals(BASE_DATE.plusDays(1), currency.getDate());
        assertEquals(USD_CURRENCY_NAME, currency.getCurrencyName());
        assertEquals("80.0", currency.getCurrencyValue());

        currency = currencyRepository.getByDate(BASE_DATE);
        assertNotNull(currency);
        assertEquals(BASE_DATE, currency.getDate());
        assertEquals(USD_CURRENCY_NAME, currency.getCurrencyName());
        assertEquals("75.0", currency.getCurrencyValue());

        currency = currencyRepository.getByDate(BASE_DATE.plusDays(2));
        assertNotNull(currency);
        assertEquals(BASE_DATE.plusDays(2), currency.getDate());
        assertEquals(USD_CURRENCY_NAME, currency.getCurrencyName());
        assertEquals("85.0", currency.getCurrencyValue());
    }

    @Test
    void testGetCurrencyHistory() {
        List<Currency> history = currencyRepository.getCurrencyHistory(BASE_DATE, BASE_DATE.plusDays(1));

        assertEquals(2, history.size());

        assertEquals(BASE_DATE, history.get(0).getDate());
        assertEquals(USD_CURRENCY_NAME, history.get(0).getCurrencyName());
        assertEquals("75.0", history.get(0).getCurrencyValue());

        assertEquals(BASE_DATE.plusDays(1), history.get(1).getDate());
        assertEquals(USD_CURRENCY_NAME, history.get(1).getCurrencyName());
        assertEquals("80.0", history.get(1).getCurrencyValue());
    }

    @Test
    void testExistsByDate() {
        assertFalse(currencyRepository.existsByDate(BASE_DATE.minusDays(1)));
        assertTrue(currencyRepository.existsByDate(BASE_DATE));
        assertTrue(currencyRepository.existsByDate(BASE_DATE.plusDays(1)));
        assertTrue(currencyRepository.existsByDate(BASE_DATE.plusDays(2)));
        assertFalse(currencyRepository.existsByDate(BASE_DATE.plusDays(3)));
    }

    @Test
    void deleteSubrange() {
        currencyRepository.deleteRange(BASE_DATE.plusDays(1), BASE_DATE.plusDays(2));
        List<Currency> residualRecords = currencyRepository.findAll();

        assertEquals(1, residualRecords.size());

        assertEquals(BASE_DATE, residualRecords.get(0).getDate());
        assertEquals(USD_CURRENCY_NAME, residualRecords.get(0).getCurrencyName());
        assertEquals("75.0", residualRecords.get(0).getCurrencyValue());
    }

    @Test
    void deleteOverlappingRange() {
        currencyRepository.deleteRange(BASE_DATE.minusDays(1), BASE_DATE.plusDays(1));
        List<Currency> residualRecords = currencyRepository.findAll();

        assertEquals(1, residualRecords.size());

        assertEquals(BASE_DATE.plusDays(2), residualRecords.get(0).getDate());
        assertEquals(USD_CURRENCY_NAME, residualRecords.get(0).getCurrencyName());
        assertEquals("85.0", residualRecords.get(0).getCurrencyValue());
    }

    @Test
    void deleteOneRecord() {
        currencyRepository.deleteRange(BASE_DATE.plusDays(1), BASE_DATE.plusDays(1));
        List<Currency> residualRecords = currencyRepository.getCurrencyHistory(BASE_DATE, BASE_DATE.plusDays(2));

        assertEquals(2, residualRecords.size());

        assertEquals(BASE_DATE, residualRecords.get(0).getDate());
        assertEquals(USD_CURRENCY_NAME, residualRecords.get(0).getCurrencyName());
        assertEquals("75.0", residualRecords.get(0).getCurrencyValue());

        assertEquals(BASE_DATE.plusDays(2), residualRecords.get(1).getDate());
        assertEquals(USD_CURRENCY_NAME, residualRecords.get(1).getCurrencyName());
        assertEquals("85.0", residualRecords.get(1).getCurrencyValue());
    }
}
