package bit.wcservice.services.weather;

import bit.wcservice.services.datarange.DateRange;
import bit.wcservice.services.datarecord.DataRecord;
import bit.wcservice.services.formatters.HistoryFormatter;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class WeatherService {
    private final LocationDispatcher locationDispatcher;
    private final HistoryFormatter<DataRecord> historyFormatter;

    public WeatherService(LocationDispatcher locationDispatcher, HistoryFormatter<DataRecord> historyFormatter) {
        this.locationDispatcher = locationDispatcher;
        this.historyFormatter = historyFormatter;
    }

    public String loadCurrentWeatherIn(String location) {
        try {
            Optional<DataRecord> loadedData = locationDispatcher.getLoaderByLocation(location).loadDailyData(LocalDate.now());
            if (loadedData.isEmpty()) {
                return "No current data available";
            } else {
                return loadedData.get().getRepresentation();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String loadLastDaysWeatherHistoryInLocation(int days, String location) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        Map<LocalDate, DataRecord> history;

        try {
            history = locationDispatcher.getLoaderByLocation(location).loadRangeData(new DateRange(startDate, endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return historyFormatter.formatHistory(history);
    }
}

