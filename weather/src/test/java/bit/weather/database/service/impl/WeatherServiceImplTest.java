package bit.weather.database.service.impl;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.datarange.DateRange;
import bit.weather.database.WeatherSampleFactory;
import bit.weather.database.WeatherSampleFactoryImpl;
import bit.weather.database.repository.WeatherRepository;
import bit.weather.database.service.WeatherService;
import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class WeatherServiceImplTest {

    private static final LocalDate SAMPLE_DATE = LocalDate.of(2020, 12, 17);
    private static final String SAMPLE_LOCATION = "Moscow";

    private static final WeatherSampleFactory WEATHER_SAMPLE_FACTORY = new WeatherSampleFactoryImpl();

    @Autowired
    private WeatherRepository weatherRepository;

    private WeatherService weatherService;

    @BeforeEach
    void setUp() throws IOException, XmlException {
        weatherRepository.saveAndFlush(WEATHER_SAMPLE_FACTORY.getWeatherSample(SAMPLE_DATE, SAMPLE_LOCATION));
        weatherService = new WeatherServiceImpl(weatherRepository);
    }

    @Test
    void getByDateAndLocation() {
        Weather weatherRecord = weatherService.getByDateAndLocation(SAMPLE_DATE, SAMPLE_LOCATION);
        assertEquals(SAMPLE_DATE, weatherRecord.getDate());
        assertEquals(SAMPLE_LOCATION, weatherRecord.getLocation());
    }

    @Test
    void getHistoryOfRangeAtLocation() {
        Map<LocalDate, Weather> weatherHistory = weatherService.getHistoryOfRangeAtLocation(
                new DateRange(SAMPLE_DATE, SAMPLE_DATE), SAMPLE_LOCATION);

        assertEquals(1, weatherHistory.size());
        assertTrue(weatherHistory.containsKey(SAMPLE_DATE));
        assertEquals(SAMPLE_DATE, weatherHistory.get(SAMPLE_DATE).getDate());
        assertEquals(SAMPLE_LOCATION, weatherHistory.get(SAMPLE_DATE).getLocation());
    }

    @Test
    void insertWeatherRecord() {
        Weather newWeatherRecord = new Weather();
        newWeatherRecord.setDate(SAMPLE_DATE.plusDays(1));
        newWeatherRecord.setLocation("Sochi");
        weatherService.insertWeatherRecord(newWeatherRecord);

        Map<LocalDate, Weather> weatherHistory = weatherService.getHistoryOfRangeAtLocation(
                new DateRange(SAMPLE_DATE, SAMPLE_DATE.plusDays(1)), "Sochi");

        assertEquals(1, weatherHistory.size());
        assertTrue(weatherHistory.containsKey(SAMPLE_DATE.plusDays(1)));
        assertEquals(SAMPLE_DATE.plusDays(1), weatherHistory.get(SAMPLE_DATE.plusDays(1)).getDate());
        assertEquals("Sochi", weatherHistory.get(SAMPLE_DATE.plusDays(1)).getLocation());
    }

    @Test
    void insertWeatherRecords() {
        List<Weather> newWeatherRecords = new ArrayList<>();

        Weather moscowWeatherRecord = new Weather();
        moscowWeatherRecord.setDate(SAMPLE_DATE.plusDays(1));
        moscowWeatherRecord.setLocation(SAMPLE_LOCATION);
        newWeatherRecords.add(moscowWeatherRecord);

        Weather sochiWeatherRecord = new Weather();
        sochiWeatherRecord.setDate(SAMPLE_DATE.plusDays(1));
        sochiWeatherRecord.setLocation("Sochi");
        newWeatherRecords.add(sochiWeatherRecord);

        weatherService.insertWeatherRecords(newWeatherRecords);

        Map<LocalDate, Weather> weatherHistory = weatherService.getHistoryOfRangeAtLocation(
                new DateRange(SAMPLE_DATE, SAMPLE_DATE.plusDays(1)), SAMPLE_LOCATION);

        assertEquals(2, weatherHistory.size());

        assertTrue(weatherHistory.containsKey(SAMPLE_DATE));
        assertEquals(SAMPLE_DATE, weatherHistory.get(SAMPLE_DATE).getDate());
        assertEquals(SAMPLE_LOCATION, weatherHistory.get(SAMPLE_DATE).getLocation());

        assertTrue(weatherHistory.containsKey(SAMPLE_DATE.plusDays(1)));
        assertEquals(SAMPLE_DATE.plusDays(1), weatherHistory.get(SAMPLE_DATE.plusDays(1)).getDate());
        assertEquals(SAMPLE_LOCATION, weatherHistory.get(SAMPLE_DATE.plusDays(1)).getLocation());
    }

    @Test
    void containsDateAndLocation() {
        assertTrue(weatherService.containsDateAndLocation(SAMPLE_DATE, SAMPLE_LOCATION));
        assertFalse(weatherService.containsDateAndLocation(SAMPLE_DATE.plusDays(1), SAMPLE_LOCATION));
        assertFalse(weatherService.containsDateAndLocation(SAMPLE_DATE, "Sochi"));
    }

    @Test
    void deleteRangeByLocation() {
        assertTrue(weatherService.containsDateAndLocation(SAMPLE_DATE, SAMPLE_LOCATION));
        weatherService.deleteRangeByLocation(new DateRange(SAMPLE_DATE, SAMPLE_DATE), SAMPLE_LOCATION);
        assertFalse(weatherService.containsDateAndLocation(SAMPLE_DATE, SAMPLE_LOCATION));
    }
}