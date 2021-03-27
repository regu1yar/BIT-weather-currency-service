package bit.wcservice.database;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.help.ResourceFileReader;
import bit.utils.help.ResourceFileReaderImpl;
import noNamespace.RootDocument;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.time.LocalDate;

public class WeatherSampleFactoryImpl implements WeatherSampleFactory {
    private static final String SAMPLE_PATH = "/samples/weather_sample.xml";
    private static final LocalDate SAMPLE_DATE = LocalDate.of(2020, 12, 17);
    private static final String SAMPLE_LOCATION = "Moscow";

    private static final ResourceFileReader RESOURCE_FILE_READER = new ResourceFileReaderImpl();

    @Override
    public Weather getWeatherSample(LocalDate sampleDate, String sampleLocation) throws IOException, XmlException {
        String weatherXmlData = RESOURCE_FILE_READER.getResourceFileContent(SAMPLE_PATH);
        RootDocument.Root weather = RootDocument.Factory.parse(weatherXmlData).getRoot();
        RootDocument.Root.Forecast.Forecastday[] weatherHistory = weather.getForecast().getForecastdayArray();
        String locationString = weather.getLocation().getName() + ", " + weather.getLocation().getCountry();
        return new Weather(SAMPLE_DATE, SAMPLE_LOCATION, locationString, weatherHistory[0]);
    }
}
