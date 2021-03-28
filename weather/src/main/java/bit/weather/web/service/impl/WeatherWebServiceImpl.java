package bit.weather.web.service.impl;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.datarange.DateRange;
import bit.utils.web.service.HistoryFormatter;
import bit.weather.web.service.LocationDispatcher;
import bit.weather.web.service.WeatherWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class WeatherWebServiceImpl implements WeatherWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherWebServiceImpl.class);

    private final LocationDispatcher locationDispatcher;
    private final HistoryFormatter<Weather> historyFormatter;

    public WeatherWebServiceImpl(LocationDispatcher locationDispatcher, HistoryFormatter<Weather> historyFormatter) {
        this.locationDispatcher = locationDispatcher;
        this.historyFormatter = historyFormatter;
    }

    public String loadCurrentWeatherIn(String location) {
        try {
            Optional<Weather> loadedData = locationDispatcher.getLoaderByLocation(location).loadDailyData(LocalDate.now());
            if (!loadedData.isPresent()) {
                return "No current data available";
            } else {
                return loadedData.get().toString();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }

    public String loadLastDaysWeatherHistoryInLocation(long days, String location) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        Map<LocalDate, Weather> history;

        try {
            history = locationDispatcher.getLoaderByLocation(location).loadRangeData(new DateRange(startDate, endDate));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }

        return historyFormatter.formatHistory(history);
    }
}

