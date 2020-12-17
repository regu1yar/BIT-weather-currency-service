package bit.wcservice.web.service.cachedloader.storage;

import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.database.service.WeatherService;
import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.cachedloader.HistoryStorage;

import java.time.LocalDate;
import java.util.Map;

public class WeatherDBLocationHistoryStorage implements HistoryStorage<Weather> {
    private final WeatherService weatherService;
    private final String location;

    public WeatherDBLocationHistoryStorage(WeatherService weatherService, String location) {
        this.weatherService = weatherService;
        this.location = location;
    }

    @Override
    public boolean isEmpty(LocalDate date) {
        return !weatherService.containsDateAndLocation(date, location);
    }

    @Override
    public void put(LocalDate date, Weather data) {
        if (!weatherService.containsDateAndLocation(date, location)) {
            weatherService.insertWeatherRecord(data);
        }
    }

    @Override
    public void putRange(Map<LocalDate, Weather> historyRange) {
        weatherService.deleteRangeByLocation(DateRange.coveringRange(historyRange.keySet()), location);
        weatherService.insertWeatherRecords(historyRange.values());
    }

    @Override
    public Weather get(LocalDate date) {
        return weatherService.getByDateAndLocation(date, location);
    }

    @Override
    public Map<LocalDate, Weather> getHistoryRange(DateRange range) {
        return weatherService.getHistoryOfRangeAtLocation(range, location);
    }
}
