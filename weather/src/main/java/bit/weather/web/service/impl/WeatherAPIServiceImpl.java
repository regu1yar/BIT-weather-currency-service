package bit.weather.web.service.impl;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.datarange.DateRange;
import bit.weather.web.service.LocationDispatcher;
import bit.weather.web.service.WeatherAPIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class WeatherAPIServiceImpl implements WeatherAPIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherAPIServiceImpl.class);
    private static final DateTimeFormatter REQUEST_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final LocationDispatcher locationDispatcher;
    private final ObjectMapper serializeMapper;

    public WeatherAPIServiceImpl(LocationDispatcher locationDispatcher, ObjectMapper serializeMapper) {
        this.locationDispatcher = locationDispatcher;
        this.serializeMapper = serializeMapper;
    }

    @Override
    public String loadRangeData(String from, String to, String city) {
        try {
            LocalDate fromDate = LocalDate.parse(from, REQUEST_DATE_FORMATTER);
            LocalDate toDate = LocalDate.parse(to, REQUEST_DATE_FORMATTER);
            DateRange range = new DateRange(fromDate, toDate);

            Map<LocalDate, Weather> rangeData = locationDispatcher.getLoaderByLocation(city).loadRangeData(range);

            Writer writer = new StringWriter();
            serializeMapper.writeValue(writer, rangeData);

            return writer.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }
}
