package bit.wcservice.web.service.currency;

import bit.wcservice.ResourceFileReader;
import bit.wcservice.ResourceFileReaderImpl;
import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.utils.WebLoader;
import bit.wcservice.utils.datarange.DateRange;
import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class WebCurrencyLoaderTest {
    private static final String CURRENCY_WEB_LOADER_CODES_PATH = "/scripts/XML_val.asp";
    private static final String CURRENCY_WEB_LOADER_VALUES_PATH = "/scripts/XML_dynamic.asp";
    private static final String DAILY_CURRENCY_SAMPLE_FILE_PATH = "/samples/currency_daily_sample.xml";
    private static final String HISTORY_CURRENCY_SAMPLE_FILE_PATH = "/samples/currency_history_sample.xml";
    private static final String CURRENCY_CODES_FILE_PATH = "/samples/codes.xml";
    private static final ResourceFileReader RESOURCE_FILE_READER = new ResourceFileReaderImpl();
    private static final LocalDate START_DATE = LocalDate.of(2001, 3, 2);
    private static final LocalDate END_DATE = LocalDate.of(2001, 3, 14);

    @Mock
    private WebLoader webLoader;

    private WebCurrencyLoader webCurrencyLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        webCurrencyLoader = new WebCurrencyLoader(webLoader);
    }

    @Test
    void successfullyLoadDailyData() throws IOException, XmlException {
        when(webLoader
                .loadData(eq(CURRENCY_WEB_LOADER_VALUES_PATH), any(MultiValueMap.class)))
                .thenReturn(RESOURCE_FILE_READER.getResourceFileContent(DAILY_CURRENCY_SAMPLE_FILE_PATH));
        when(webLoader
                .loadData(eq(CURRENCY_WEB_LOADER_CODES_PATH), any(MultiValueMap.class)))
                .thenReturn(RESOURCE_FILE_READER.getResourceFileContent(CURRENCY_CODES_FILE_PATH));

        Optional<Currency> loadedCurrency = webCurrencyLoader.loadDailyData(START_DATE);

        assertTrue(loadedCurrency.isPresent());
        assertEquals("US Dollar", loadedCurrency.get().getCurrencyName());
        assertEquals(START_DATE, loadedCurrency.get().getDate());
        assertEquals("28,6200", loadedCurrency.get().getCurrencyValue());
        verify(webLoader, times(1))
                .loadData(eq(CURRENCY_WEB_LOADER_VALUES_PATH), any(MultiValueMap.class));
        verify(webLoader, times(1))
                .loadData(eq(CURRENCY_WEB_LOADER_CODES_PATH), any(MultiValueMap.class));
    }

    @Test
    void loadRangeData() throws IOException, XmlException {
        when(webLoader
                .loadData(eq(CURRENCY_WEB_LOADER_VALUES_PATH), any(MultiValueMap.class)))
                .thenReturn(RESOURCE_FILE_READER.getResourceFileContent(HISTORY_CURRENCY_SAMPLE_FILE_PATH));
        when(webLoader
                .loadData(eq(CURRENCY_WEB_LOADER_CODES_PATH), any(MultiValueMap.class)))
                .thenReturn(RESOURCE_FILE_READER.getResourceFileContent(CURRENCY_CODES_FILE_PATH));

        DateRange requestRange = new DateRange(START_DATE, END_DATE);
        Map<LocalDate, Currency> loadedCurrency = webCurrencyLoader.loadRangeData(requestRange);

        for (Map.Entry<LocalDate, Currency> dateCurrencyEntry : loadedCurrency.entrySet()) {
            assertEquals("US Dollar", dateCurrencyEntry.getValue().getCurrencyName());
            assertTrue(requestRange.contains(dateCurrencyEntry.getKey()));
            assertEquals(dateCurrencyEntry.getValue().getDate(), dateCurrencyEntry.getKey());

        }

        verify(webLoader, times(1))
                .loadData(eq(CURRENCY_WEB_LOADER_VALUES_PATH), any(MultiValueMap.class));
        verify(webLoader, times(1))
                .loadData(eq(CURRENCY_WEB_LOADER_CODES_PATH), any(MultiValueMap.class));

    }
}