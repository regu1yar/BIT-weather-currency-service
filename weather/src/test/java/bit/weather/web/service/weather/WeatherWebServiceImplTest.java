package bit.weather.web.service.weather;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.datarange.DateRange;
import bit.utils.web.service.HistoryFormatter;
import bit.utils.web.service.HistoryLoader;
import bit.weather.web.service.LocationDispatcher;
import bit.weather.web.service.WeatherWebService;
import bit.weather.web.service.impl.WeatherWebServiceImpl;
import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class WeatherWebServiceImplTest {

    private static final String TEST_LOCATION = "Moscow";
    private static final LocalDate TEST_DATE = LocalDate.now();

    @Mock
    private HistoryLoader<Weather> historyLoader;

    @Mock
    private LocationDispatcher locationDispatcher;

    @Mock
    private HistoryFormatter<Weather> historyFormatter;

    private WeatherWebService weatherWebService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherWebService = new WeatherWebServiceImpl(locationDispatcher, historyFormatter);
        when(locationDispatcher.getLoaderByLocation(TEST_LOCATION)).thenReturn(historyLoader);
    }

    @Test
    public void successfullyLoadCurrentWeather() throws XmlException {
        Weather weather = new Weather();
        weather.setDate(TEST_DATE);
        weather.setLocation(TEST_LOCATION);
        weather.setPrettyLocation(TEST_LOCATION);
        weather.setMaxTemp(5);
        weather.setMinTemp(-5);
        weather.setAverageTemp(0);
        weather.setMaxWind(25);
        weather.setPrecipitation(10);
        weather.setHumidity(40);
        weather.setUvIndex(0);
        weather.setWeatherCondition("Snow");
        weather.setSunrise("8:00");
        weather.setSunset("16:00");

        when(historyLoader.loadDailyData(any(LocalDate.class))).thenReturn(Optional.of(weather));

        String resultWeather = weatherWebService.loadCurrentWeatherIn(TEST_LOCATION);

        assertEquals(weather.toString(), resultWeather);
        verify(locationDispatcher, times(1)).getLoaderByLocation(TEST_LOCATION);
        verify(historyLoader, times(1)).loadDailyData(any(LocalDate.class));
    }

    @Test
    void returnMessageIfCurrentDataIsUnavailable() throws XmlException {
        when(historyLoader.loadDailyData(any(LocalDate.class))).thenReturn(Optional.empty());
        String result = weatherWebService.loadCurrentWeatherIn(TEST_LOCATION);
        assertEquals(result, "No current data available");
        verify(locationDispatcher, times(1)).getLoaderByLocation(TEST_LOCATION);
        verify(historyLoader, times(1)).loadDailyData(any(LocalDate.class));
    }

    @Test
    void reportIfErrorIsOccurWhenLoadingCurrentData() throws XmlException {
        when(historyLoader.loadDailyData(any(LocalDate.class))).thenThrow(new XmlException("exception"));
        String result = weatherWebService.loadCurrentWeatherIn(TEST_LOCATION);
        assertEquals("exception", result);
        verify(locationDispatcher, times(1)).getLoaderByLocation(TEST_LOCATION);
        verify(historyLoader, times(1)).loadDailyData(any(LocalDate.class));
    }

    @Test
    void successfullyLoadHistory() throws XmlException {
        when(historyFormatter.formatHistory(anyMap())).thenReturn("Formatted history");
        String historyResult = weatherWebService.loadLastDaysWeatherHistoryInLocation(2, TEST_LOCATION);
        assertEquals("Formatted history", historyResult);
        verify(locationDispatcher, times(1)).getLoaderByLocation(TEST_LOCATION);
        verify(historyLoader, times(1)).loadRangeData(any(DateRange.class));
        verify(historyFormatter, times(1)).formatHistory(anyMap());
    }

    @Test
    void reportIfErrorIsOccurWhenLoadingHistory() throws XmlException {
        when(historyLoader.loadRangeData(any(DateRange.class))).thenThrow(new XmlException("exception"));
        String result = weatherWebService.loadLastDaysWeatherHistoryInLocation(2, TEST_LOCATION);
        assertEquals("exception", result);
        verify(locationDispatcher, times(1)).getLoaderByLocation(TEST_LOCATION);
        verify(historyLoader, times(1)).loadRangeData(any(DateRange.class));
        verify(historyFormatter, never()).formatHistory(anyMap());
    }
}