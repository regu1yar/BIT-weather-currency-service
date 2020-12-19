package bit.wcservice.web.service.predict;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.HistoryLoader;
import bit.wcservice.web.service.PredictWebService;
import bit.wcservice.web.service.weather.LocationDispatcher;
import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PredictWebServiceImplTest {

    @Mock
    private HistoryLoader<Currency> currencyLoader;

    @Mock
    private HistoryLoader<Weather> weatherLoader;

    @Mock
    private LocationDispatcher weatherLocationDispatcher;

    @Mock
    private PredictModel predictModel;

    private PredictWebService predictWebService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        predictWebService = new PredictWebServiceImpl(currencyLoader, weatherLocationDispatcher, predictModel);
    }

    @Test
    public void successfullyPredict() throws XmlException {
        LocalDate today = LocalDate.now();
        Currency currency = new Currency(today, "77.0", "USD");
        Weather weather = new Weather();
        weather.setDate(today.minusDays(1));
        weather.setAverageTemp(0);

        Map<LocalDate, Currency> currencyMap = Collections.singletonMap(today, currency);
        Map<LocalDate, Weather> weatherMap = Collections.singletonMap(today.minusDays(1), weather);

        when(currencyLoader.loadRangeData(any(DateRange.class))).thenReturn(currencyMap);
        when(weatherLocationDispatcher.getLoaderByLocation(anyString())).thenReturn(weatherLoader);
        when(weatherLoader.loadRangeData(any(DateRange.class))).thenReturn(weatherMap);
        when(predictModel.predict(anyMap(), anyMap())).thenReturn(Double.valueOf(42));

        String predictResult = predictWebService.predict();

        assertEquals(String.valueOf(Double.valueOf(42)), predictResult);
        verify(currencyLoader, times(1)).loadRangeData(any(DateRange.class));
        verify(weatherLocationDispatcher, times(1)).getLoaderByLocation(anyString());
        verify(weatherLoader, times(1)).loadRangeData(any(DateRange.class));
        verify(predictModel, times(1)).predict(anyMap(), anyMap());
    }

    @Test
    void exceptionIsThrownWhilePredicting() throws XmlException {
        when(currencyLoader.loadRangeData(any(DateRange.class))).thenThrow(new XmlException("exception"));
        String predictResult = predictWebService.predict();
        assertEquals("exception", predictResult);
        verify(currencyLoader, times(1)).loadRangeData(any(DateRange.class));
    }
}