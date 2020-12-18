package bit.wcservice.database.entity.datarecord;

import noNamespace.RootDocument;
import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class WeatherTest {

    private static final String SAMPLE_PATH = "/samples/weather_sample.xml";
    private static final LocalDate SAMPLE_DATE = LocalDate.of(2020, 12, 17);
    private static final String SAMPLE_LOCATION = "Moscow";

    private Weather weatherData;

    @BeforeEach
    void setUp() throws IOException, XmlException {
        InputStream inputStream = null;
        try {
            inputStream = this.getClass().getResourceAsStream(SAMPLE_PATH);

            String weatherXmlData = readFromInputStream(inputStream);
            RootDocument.Root weather = RootDocument.Factory.parse(weatherXmlData).getRoot();
            RootDocument.Root.Forecast.Forecastday[] weatherHistory = weather.getForecast().getForecastdayArray();
            String locationString = weather.getLocation().getName() + ", " + weather.getLocation().getCountry();

            weatherData = new Weather(SAMPLE_DATE, SAMPLE_LOCATION, locationString, weatherHistory[0]);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    void extractFeatures() {
        List<Double> weatherFeatures = weatherData.extractFeatures();
        assertTrue(weatherFeatures.size() > 0);
    }

    @Test
    void checkWeatherFields() {
        assertEquals(SAMPLE_DATE, weatherData.getDate());
        assertEquals(1, weatherData.getAverageTemp(), 1e9);
        assertEquals(SAMPLE_LOCATION, weatherData.getLocation());
        assertEquals("Heavy snow", weatherData.getWeatherCondition());
        assertEquals(95, weatherData.getHumidity(), 1e9);
        assertEquals(1.4, weatherData.getMaxTemp(), 1e9);
        assertEquals(11.9, weatherData.getMaxWind(), 1e9);
        assertEquals(-8.7, weatherData.getMinTemp(), 1e9);
        assertEquals(2.1, weatherData.getPrecipitation(), 1e9);
        assertEquals("Moscow, Russia", weatherData.getPrettyLocation());
        assertEquals("08:55 AM", weatherData.getSunrise());
        assertEquals("03:57 PM", weatherData.getSunset());
        assertEquals(0, weatherData.getUvIndex(), 1e9);
        assertEquals(7.3, weatherData.getVisibility(), 1e9);
    }

    @Test
    void toStringContainsLocationAndDate() {
        String weatherString = weatherData.toString();
        assertTrue(weatherString.contains(String.valueOf(SAMPLE_DATE.getYear())));
        assertTrue(weatherString.contains(String.valueOf(SAMPLE_DATE.getMonthValue())));
        assertTrue(weatherString.contains(String.valueOf(SAMPLE_DATE.getDayOfMonth())));
        assertTrue(weatherString.contains(SAMPLE_LOCATION));
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}