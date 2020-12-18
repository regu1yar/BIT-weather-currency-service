package bit.wcservice.database;

import bit.wcservice.database.entity.datarecord.Weather;
import noNamespace.RootDocument;
import org.apache.xmlbeans.XmlException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;

public class WeatherSampleFactoryImpl implements WeatherSampleFactory {
    private static final String SAMPLE_PATH = "/samples/weather_sample.xml";
    private static final LocalDate SAMPLE_DATE = LocalDate.of(2020, 12, 17);
    private static final String SAMPLE_LOCATION = "Moscow";

    @Override
    public Weather getWeatherSample(LocalDate sampleDate, String sampleLocation) throws IOException, XmlException {
        InputStream inputStream = null;
        Weather weatherData;
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

        return weatherData;
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
