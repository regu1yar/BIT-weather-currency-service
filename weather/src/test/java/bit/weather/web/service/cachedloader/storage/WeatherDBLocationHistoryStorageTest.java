package bit.weather.web.service.cachedloader.storage;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.datarange.DateRange;
import bit.weather.database.service.WeatherService;
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
class WeatherDBLocationHistoryStorageTest {

    private static final String LOCATION = "Moscow";

    @Mock
    private WeatherService weatherService;

    private WeatherDBLocationHistoryStorage weatherDBLocationHistoryStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherDBLocationHistoryStorage = new WeatherDBLocationHistoryStorage(weatherService, LOCATION);
    }

    @Test
    void isEmpty() {
        LocalDate today = LocalDate.now();
        weatherDBLocationHistoryStorage.isEmpty(today);
        verify(weatherService, times(1)).containsDateAndLocation(today, LOCATION);
    }

    @Test
    void put() {
        LocalDate today = LocalDate.now();
        Weather weather = new Weather();
        weatherDBLocationHistoryStorage.put(today, weather);
        verify(weatherService, times(1)).insertWeatherRecord(weather);
    }

    @Test
    void putRange() {
        LocalDate today = LocalDate.now();
        Weather weather = new Weather();
        weatherDBLocationHistoryStorage.putRange(Collections.singletonMap(today, weather));
        verify(weatherService, times(1)).insertWeatherRecords(anyIterable());
    }

    @Test
    void get() {
        LocalDate today = LocalDate.now();
        Weather weather = new Weather();
        weather.setLocation(LOCATION);
        weather.setDate(today);
        weather.setHumidity(1.5);
        when(weatherService.getByDateAndLocation(today, LOCATION)).thenReturn(weather);

        Weather result = weatherDBLocationHistoryStorage.get(today);

        assertEquals(today, result.getDate());
        assertEquals(LOCATION, result.getLocation());
        assertEquals(1.5, result.getHumidity());
        verify(weatherService, times(1)).getByDateAndLocation(today, LOCATION);
    }

    @Test
    void getHistoryRange() {
        LocalDate today = LocalDate.now();
        DateRange range = new DateRange(today, today);
        Weather weather = new Weather();
        weather.setLocation(LOCATION);
        weather.setDate(today);
        weather.setHumidity(1.5);
        when(weatherService.getHistoryOfRangeAtLocation(range, LOCATION)).thenReturn(Collections.singletonMap(today, weather));

        Map<LocalDate, Weather> result = weatherDBLocationHistoryStorage.getHistoryRange(range);

        assertEquals(1, result.size());
        assertTrue(result.containsKey(today));
        assertEquals(today, result.get(today).getDate());
        assertEquals(LOCATION, result.get(today).getLocation());
        assertEquals(1.5, result.get(today).getHumidity());
        verify(weatherService, times(1)).getHistoryOfRangeAtLocation(range, LOCATION);
    }
}