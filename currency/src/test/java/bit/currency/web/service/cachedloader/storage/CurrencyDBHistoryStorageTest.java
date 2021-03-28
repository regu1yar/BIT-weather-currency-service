package bit.currency.web.service.cachedloader.storage;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.datarange.DateRange;
import bit.currency.database.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class CurrencyDBHistoryStorageTest {

    @Mock
    private CurrencyService currencyService;

    private CurrencyDBHistoryStorage currencyDBHistoryStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        currencyDBHistoryStorage = new CurrencyDBHistoryStorage(currencyService);
    }

    @Test
    void isEmpty() {
        LocalDate today = LocalDate.now();
        currencyDBHistoryStorage.isEmpty(today);
        verify(currencyService, times(1)).containsDate(today);
    }

    @Test
    void put() {
        LocalDate today = LocalDate.now();
        Currency currency = new Currency();
        currencyDBHistoryStorage.put(today, currency);
        verify(currencyService, times(1)).insertCurrencyRecord(currency);
    }

    @Test
    void putRange() {
        LocalDate today = LocalDate.now();
        Currency currency = new Currency();
        currencyDBHistoryStorage.putRange(Collections.singletonMap(today, currency));
        verify(currencyService, times(1)).insertCurrencyRecords(anyIterable());
    }

    @Test
    void get() {
        LocalDate today = LocalDate.now();
        Currency currency = new Currency(today, "77.0", "USD");
        when(currencyService.getByDate(today)).thenReturn(currency);

        Currency result = currencyDBHistoryStorage.get(today);

        assertEquals(today, result.getDate());
        assertEquals("77.0", result.getCurrencyValue());
        assertEquals("USD", result.getCurrencyName());
        verify(currencyService, times(1)).getByDate(today);
    }

    @Test
    void getHistoryRange() {
        LocalDate today = LocalDate.now();
        DateRange range = new DateRange(today, today);
        Currency currency = new Currency(today, "77.0", "USD");
        when(currencyService.getHistoryOfRange(range)).thenReturn(Collections.singletonMap(today, currency));

        Map<LocalDate, Currency> result = currencyDBHistoryStorage.getHistoryRange(range);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(today));
        assertEquals(today, result.get(today).getDate());
        assertEquals("77.0", result.get(today).getCurrencyValue());
        assertEquals("USD", result.get(today).getCurrencyName());
        verify(currencyService, times(1)).getHistoryOfRange(range);
    }
}