package bit.wcservice.services.weather;

import bit.wcservice.datarange.DateRange;
import bit.wcservice.services.dataloaders.CachedHistoryLoader;
import bit.wcservice.services.dataloaders.HistoryLoader;
import bit.wcservice.services.formatters.HistoryFormatter;
import noNamespace.RootDocument.Root.Forecast.Forecastday;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WeatherService {
    private final Map<String, HistoryLoader<Forecastday>> locationLoaders = new HashMap<>();
    private final HistoryFormatter<Forecastday> historyFormatter;

    public WeatherService(HistoryFormatter<Forecastday> historyFormatter) {
        this.historyFormatter = historyFormatter;
    }

    public String loadCurrentWeatherIn(String location) {
        if (!locationLoaders.containsKey(location)) {
            addDataLoader(location);
        }

        try {
            return locationLoaders.get(location).loadDailyData(LocalDate.now()).toString();
        } catch (XmlException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String loadLastDaysWeatherHistoryInLocation(int days, String location) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        Map<LocalDate, Forecastday> history;

        if (!locationLoaders.containsKey(location)) {
            addDataLoader(location);
        }

        try {
            history = locationLoaders.get(location).loadRangeData(new DateRange(startDate, endDate));
        } catch (XmlException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return historyFormatter.formatHistory(history);
    }

    private void addDataLoader(String location) {
        if (!locationLoaders.containsKey(location)) {
            locationLoaders.put(location, new CachedHistoryLoader<>(new WebWeatherLoader(location)));
        }
    }
}

