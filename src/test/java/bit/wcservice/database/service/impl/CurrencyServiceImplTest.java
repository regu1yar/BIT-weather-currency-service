package bit.wcservice.database.service.impl;

import bit.wcservice.database.CurrencyRepositoryFiller;
import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.database.repository.CurrencyRepository;
import bit.wcservice.database.service.CurrencyService;
import bit.wcservice.util.datarange.DateRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CurrencyServiceImplTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2020, 1, 1);
    private static final String USD_CURRENCY_NAME = "USD";

    @Autowired
    private CurrencyRepository currencyRepository;

    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        CurrencyRepositoryFiller.fillRepository(currencyRepository, BASE_DATE, USD_CURRENCY_NAME);
        currencyService = new CurrencyServiceImpl(currencyRepository);
    }

    @Test
    void getByDate() {
        Currency currency = currencyService.getByDate(BASE_DATE.plusDays(1));
        assertNotNull(currency);
        assertEquals(BASE_DATE.plusDays(1), currency.getDate());
        assertEquals(USD_CURRENCY_NAME, currency.getCurrencyName());
        assertEquals("80.0", currency.getCurrencyValue());

        currency = currencyService.getByDate(BASE_DATE);
        assertNotNull(currency);
        assertEquals(BASE_DATE, currency.getDate());
        assertEquals(USD_CURRENCY_NAME, currency.getCurrencyName());
        assertEquals("75.0", currency.getCurrencyValue());

        currency = currencyService.getByDate(BASE_DATE.plusDays(2));
        assertNotNull(currency);
        assertEquals(BASE_DATE.plusDays(2), currency.getDate());
        assertEquals(USD_CURRENCY_NAME, currency.getCurrencyName());
        assertEquals("85.0", currency.getCurrencyValue());
    }

    @Test
    void getHistoryOfRange() {
        Map<LocalDate, Currency> history = currencyService.getHistoryOfRange(new DateRange(BASE_DATE, BASE_DATE.plusDays(1)));

        assertEquals(2, history.size());

        assertTrue(history.containsKey(BASE_DATE));
        assertEquals(USD_CURRENCY_NAME, history.get(BASE_DATE).getCurrencyName());
        assertEquals("75.0", history.get(BASE_DATE).getCurrencyValue());

        assertTrue(history.containsKey(BASE_DATE.plusDays(1)));
        assertEquals(USD_CURRENCY_NAME, history.get(BASE_DATE.plusDays(1)).getCurrencyName());
        assertEquals("80.0", history.get(BASE_DATE.plusDays(1)).getCurrencyValue());
    }

    @Test
    void insertCurrencyRecord() {
        Currency newRecord = new Currency(BASE_DATE.minusDays(1), "60.0", USD_CURRENCY_NAME);

        currencyService.insertCurrencyRecord(newRecord);

        Currency insertedCurrency = currencyService.getByDate(BASE_DATE.minusDays(1));
        assertNotNull(insertedCurrency);
        assertEquals(BASE_DATE.minusDays(1), insertedCurrency.getDate());
        assertEquals(USD_CURRENCY_NAME, insertedCurrency.getCurrencyName());
        assertEquals("60.0", insertedCurrency.getCurrencyValue());
    }

    @Test
    void insertCurrencyRecords() {
        List<Currency> newRecords = new ArrayList<>();

        newRecords.add(new Currency(BASE_DATE.minusDays(1), "60.0", USD_CURRENCY_NAME));
        newRecords.add(new Currency(BASE_DATE.plusDays(60), "10.0", USD_CURRENCY_NAME));

        currencyService.insertCurrencyRecords(newRecords);

        Currency insertedCurrency = currencyService.getByDate(BASE_DATE.minusDays(1));
        assertNotNull(insertedCurrency);
        assertEquals(BASE_DATE.minusDays(1), insertedCurrency.getDate());
        assertEquals(USD_CURRENCY_NAME, insertedCurrency.getCurrencyName());
        assertEquals("60.0", insertedCurrency.getCurrencyValue());

        insertedCurrency = currencyService.getByDate(BASE_DATE.plusDays(60));
        assertNotNull(insertedCurrency);
        assertEquals(BASE_DATE.plusDays(60), insertedCurrency.getDate());
        assertEquals(USD_CURRENCY_NAME, insertedCurrency.getCurrencyName());
        assertEquals("10.0", insertedCurrency.getCurrencyValue());
    }

    @Test
    void containsDate() {
        assertFalse(currencyService.containsDate(BASE_DATE.minusDays(1)));
        assertTrue(currencyService.containsDate(BASE_DATE));
        assertTrue(currencyService.containsDate(BASE_DATE.plusDays(1)));
        assertTrue(currencyService.containsDate(BASE_DATE.plusDays(2)));
        assertFalse(currencyService.containsDate(BASE_DATE.plusDays(3)));
    }

    @Test
    void deleteRange() {
        assertTrue(currencyService.containsDate(BASE_DATE));
        assertTrue(currencyService.containsDate(BASE_DATE.plusDays(1)));
        assertTrue(currencyService.containsDate(BASE_DATE.plusDays(2)));

        currencyService.deleteRange(new DateRange(BASE_DATE, BASE_DATE.plusDays(1)));

        assertFalse(currencyService.containsDate(BASE_DATE));
        assertFalse(currencyService.containsDate(BASE_DATE.plusDays(1)));
        assertTrue(currencyService.containsDate(BASE_DATE.plusDays(2)));
    }
}