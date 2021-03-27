package bit.wcservice.web.service.currency;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.datarange.DateRange;
import bit.utils.web.service.HistoryFormatter;
import bit.utils.web.service.HistoryLoader;
import bit.wcservice.web.service.currency.impl.CurrencyWebServiceImpl;
import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class CurrencyWebServiceImplTest {

    @Mock
    private HistoryLoader<Currency> historyLoader;

    @Mock
    private HistoryFormatter<Currency> historyFormatter;

    private CurrencyWebService currencyWebService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        currencyWebService = new CurrencyWebServiceImpl(historyLoader, historyFormatter);
    }

    @Test
    public void successfullyLoadDailyData() throws XmlException {
        LocalDate today = LocalDate.now();
        Currency currency = new Currency(today, "77.0", "USD");
        when(historyLoader.loadDailyData(any(LocalDate.class))).thenReturn(Optional.of(currency));

        String result = currencyWebService.loadCurrentUSDValue();

        assertEquals(currency.toString(), result);
        verify(historyLoader, times(1)).loadDailyData(any(LocalDate.class));
    }

    @Test
    public void currentDataIsUnavailable() throws XmlException {
        when(historyLoader.loadDailyData(any(LocalDate.class))).thenReturn(Optional.empty());

        String result = currencyWebService.loadCurrentUSDValue();

        assertEquals("No current data available", result);
        verify(historyLoader, times(1)).loadDailyData(any(LocalDate.class));
    }

    @Test
    public void exceptionIsThrownWhileLoadingDailyData() throws XmlException {
        when(historyLoader.loadDailyData(any(LocalDate.class))).thenThrow(new XmlException("exception"));

        String result = currencyWebService.loadCurrentUSDValue();

        assertEquals("exception", result);
        verify(historyLoader, times(1)).loadDailyData(any(LocalDate.class));
    }

    @Test
    public void successfullyLoadHistoryData() throws XmlException {
        LocalDate today = LocalDate.now();
        Map<LocalDate, Currency> history = new HashMap<>();
        history.put(today, new Currency(today, "77.0", "USD"));
        history.put(today.minusDays(1), new Currency(today, "82.0", "USD"));
        when(historyLoader.loadRangeData(any(DateRange.class))).thenReturn(history);
        when(historyFormatter.formatHistory(anyMap())).thenReturn("History data");

        String result = currencyWebService.loadLastDaysUSDHistory(2);

        assertEquals("History data", result);
        verify(historyLoader, times(1)).loadRangeData(any(DateRange.class));
        verify(historyFormatter, times(1)).formatHistory(anyMap());
    }

    @Test
    public void exceptionIsThrownWhileLoadingHistoryData() throws XmlException {
        LocalDate today = LocalDate.now();
        Map<LocalDate, Currency> history = new HashMap<>();
        history.put(today, new Currency(today, "77.0", "USD"));
        history.put(today.minusDays(1), new Currency(today, "82.0", "USD"));
        when(historyLoader.loadRangeData(any(DateRange.class))).thenThrow(new XmlException("exception"));

        String result = currencyWebService.loadLastDaysUSDHistory(2);

        assertEquals("exception", result);
        verify(historyLoader, times(1)).loadRangeData(any(DateRange.class));
        verify(historyFormatter, never()).formatHistory(anyMap());
    }
}