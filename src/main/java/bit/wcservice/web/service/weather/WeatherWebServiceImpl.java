package bit.wcservice.web.service.weather;

import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.WeatherWebService;
import bit.wcservice.web.service.formatter.HistoryFormatter;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class WeatherWebServiceImpl implements WeatherWebService {
    private final LocationDispatcher locationDispatcher;
    private final HistoryFormatter<Weather> historyFormatter;

    public WeatherWebServiceImpl(LocationDispatcher locationDispatcher, HistoryFormatter<Weather> historyFormatter) {
        this.locationDispatcher = locationDispatcher;
        this.historyFormatter = historyFormatter;
    }

    public String loadCurrentWeatherIn(String location) {
        try {
            Optional<Weather> loadedData = locationDispatcher.getLoaderByLocation(location).loadDailyData(LocalDate.now());
            if (loadedData.isEmpty()) {
                return "No current data available";
            } else {
                return loadedData.get().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String loadLastDaysWeatherHistoryInLocation(int days, String location) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        Map<LocalDate, Weather> history;

        try {
            history = locationDispatcher.getLoaderByLocation(location).loadRangeData(new DateRange(startDate, endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return historyFormatter.formatHistory(history);
    }
}

