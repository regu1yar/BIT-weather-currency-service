package bit.wcservice.database.repository;

import bit.utils.database.entity.datarecord.Weather;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class WeatherRepositoryTest {

    private final LocalDate BASE_DATE = LocalDate.of(2020, 1, 1);

    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeEach
    void setUp() {
        List<Weather> weatherRecords = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            weatherRecords.add(new Weather());
        }

        weatherRecords.get(0).setDate(BASE_DATE);
        weatherRecords.get(0).setLocation("Moscow");
        weatherRecords.get(0).setAverageTemp(-10);

        weatherRecords.get(1).setDate(BASE_DATE.plusDays(1));
        weatherRecords.get(1).setLocation("Moscow");
        weatherRecords.get(1).setAverageTemp(-5);

        weatherRecords.get(2).setDate(BASE_DATE);
        weatherRecords.get(2).setLocation("Sochi");
        weatherRecords.get(2).setAverageTemp(15);

        weatherRecords.get(3).setDate(BASE_DATE.plusDays(2));
        weatherRecords.get(3).setLocation("Sochi");
        weatherRecords.get(3).setAverageTemp(5);

        weatherRepository.saveAll(weatherRecords);
        weatherRepository.flush();
    }

    @AfterEach
    void tearDown() {
        weatherRepository.deleteAll();
    }

    @Test
    public void checkInitializationByDateAndLocation() {
        Weather weather = weatherRepository.getByDateAndLocation(BASE_DATE.plusDays(1), "Moscow");
        assertNotNull(weather);
        assertEquals(BASE_DATE.plusDays(1), weather.getDate());
        assertEquals("Moscow", weather.getLocation());
        assertEquals(-5, weather.getAverageTemp(), 1e-9);

        weather = weatherRepository.getByDateAndLocation(BASE_DATE, "Moscow");
        assertNotNull(weather);
        assertEquals(BASE_DATE, weather.getDate());
        assertEquals("Moscow", weather.getLocation());
        assertEquals(-10, weather.getAverageTemp(), 1e-9);

        weather = weatherRepository.getByDateAndLocation(BASE_DATE.plusDays(2), "Sochi");
        assertNotNull(weather);
        assertEquals(BASE_DATE.plusDays(2), weather.getDate());
        assertEquals("Sochi", weather.getLocation());
        assertEquals(5, weather.getAverageTemp(), 1e-9);

        weather = weatherRepository.getByDateAndLocation(BASE_DATE, "Sochi");
        assertNotNull(weather);
        assertEquals(BASE_DATE, weather.getDate());
        assertEquals("Sochi", weather.getLocation());
        assertEquals(15, weather.getAverageTemp(), 1e-9);
    }

    @Test
    void testGetCurrencyHistory() {
        List<Weather> history = weatherRepository.getWeatherHistoryByLocation(BASE_DATE, BASE_DATE.plusDays(2), "Moscow");

        assertEquals(2, history.size());

        assertNotNull(history.get(0));
        assertEquals(BASE_DATE, history.get(0).getDate());
        assertEquals("Moscow", history.get(0).getLocation());
        assertEquals(-10, history.get(0).getAverageTemp(), 1e-9);

        assertNotNull(history.get(1));
        assertEquals(BASE_DATE.plusDays(1), history.get(1).getDate());
        assertEquals("Moscow", history.get(1).getLocation());
        assertEquals(-5, history.get(1).getAverageTemp(), 1e-9);
    }

    @Test
    void testExistsByDateAndLocation() {
        assertFalse(weatherRepository.existsByDateAndLocation(BASE_DATE.minusDays(1), "Moscow"));
        assertFalse(weatherRepository.existsByDateAndLocation(BASE_DATE.minusDays(1), "Sochi"));
        assertTrue(weatherRepository.existsByDateAndLocation(BASE_DATE, "Moscow"));
        assertTrue(weatherRepository.existsByDateAndLocation(BASE_DATE, "Sochi"));
        assertTrue(weatherRepository.existsByDateAndLocation(BASE_DATE.plusDays(1), "Moscow"));
        assertFalse(weatherRepository.existsByDateAndLocation(BASE_DATE.plusDays(1), "Sochi"));
        assertFalse(weatherRepository.existsByDateAndLocation(BASE_DATE.plusDays(2), "Moscow"));
        assertTrue(weatherRepository.existsByDateAndLocation(BASE_DATE.plusDays(2), "Sochi"));
        assertFalse(weatherRepository.existsByDateAndLocation(BASE_DATE.plusDays(3), "Moscow"));
        assertFalse(weatherRepository.existsByDateAndLocation(BASE_DATE.plusDays(3), "Sochi"));
    }

    @Test
    void testDeleteRangeByLocation() {
        weatherRepository.deleteRangeByLocation(BASE_DATE, BASE_DATE.plusDays(2), "Sochi");

        assertTrue(weatherRepository.getWeatherHistoryByLocation(BASE_DATE, BASE_DATE.plusDays(2), "Sochi").isEmpty());

        List<Weather> residualRecords = weatherRepository.getWeatherHistoryByLocation(BASE_DATE, BASE_DATE.plusDays(2), "Moscow");

        assertEquals(2, residualRecords.size());

        assertNotNull(residualRecords.get(0));
        assertEquals(BASE_DATE, residualRecords.get(0).getDate());
        assertEquals("Moscow", residualRecords.get(0).getLocation());
        assertEquals(-10, residualRecords.get(0).getAverageTemp(), 1e-9);

        assertNotNull(residualRecords.get(1));
        assertEquals(BASE_DATE.plusDays(1), residualRecords.get(1).getDate());
        assertEquals("Moscow", residualRecords.get(1).getLocation());
        assertEquals(-5, residualRecords.get(1).getAverageTemp(), 1e-9);
    }

}
