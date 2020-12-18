package bit.wcservice.database;

import bit.wcservice.database.entity.datarecord.Weather;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;
import java.time.LocalDate;

public interface WeatherSampleFactory {
    Weather getWeatherSample(LocalDate sampleDate, String sampleLocation) throws IOException, XmlException;
}
