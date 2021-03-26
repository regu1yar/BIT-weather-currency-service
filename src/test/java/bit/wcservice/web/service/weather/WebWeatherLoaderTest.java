package bit.wcservice.web.service.weather;

import bit.wcservice.ResourceFileReader;
import bit.wcservice.ResourceFileReaderImpl;
import bit.wcservice.database.entity.datarecord.Weather;
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
class WebWeatherLoaderTest {
    private static final String DAILY_WEATHER_SAMPLE_FILE_PATH = "/samples/weather_sample.xml";
    private static final String HISTORY_WEATHER_SAMPLE_FILE_PATH = "/samples/weather_history_sample.xml";
    private static final ResourceFileReader RESOURCE_FILE_READER = new ResourceFileReaderImpl();
    private static final String TEST_LOCATION = "Moscow";
    private static final LocalDate START_DATE = LocalDate.of(2020, 12, 17);
    private static final LocalDate END_DATE = LocalDate.of(2020, 12, 19);

    @Mock
    private WebLoader webLoader;

    private WebWeatherLoader webWeatherLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        webWeatherLoader = new WebWeatherLoader(webLoader, TEST_LOCATION);
    }

    @Test
    void successfullyLoadDailyData() throws IOException, XmlException {
        when(webLoader
                .loadData(anyString(), any(MultiValueMap.class)))
                .thenReturn(RESOURCE_FILE_READER.getResourceFileContent(DAILY_WEATHER_SAMPLE_FILE_PATH));

        Optional<Weather> loadedWeather = webWeatherLoader.loadDailyData(START_DATE);

        assertTrue(loadedWeather.isPresent());
        assertEquals(START_DATE, loadedWeather.get().getDate());
        assertEquals(TEST_LOCATION, loadedWeather.get().getLocation());
        verify(webLoader, times(1)).loadData(anyString(), any(MultiValueMap.class));
    }

    @Test
    void loadRangeData() throws IOException, XmlException {
        when(webLoader
                .loadData(anyString(), any(MultiValueMap.class)))
                .thenReturn(RESOURCE_FILE_READER.getResourceFileContent(HISTORY_WEATHER_SAMPLE_FILE_PATH));

        DateRange requestRange = new DateRange(START_DATE, END_DATE);
        Map<LocalDate, Weather> loadedWeather = webWeatherLoader.loadRangeData(requestRange);

        for (Map.Entry<LocalDate, Weather> dateWeatherEntry : loadedWeather.entrySet()) {
            assertEquals(TEST_LOCATION, dateWeatherEntry.getValue().getLocation());
            assertTrue(requestRange.contains(dateWeatherEntry.getKey()));
            assertEquals(dateWeatherEntry.getValue().getDate(), dateWeatherEntry.getKey());
        }

        verify(webLoader, times(1)).loadData(anyString(), any(MultiValueMap.class));
    }
}