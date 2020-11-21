package bit.wcservice.database.service;

import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.util.datarange.DateRange;

import java.time.LocalDate;
import java.util.Map;

public interface WeatherService {
    Weather getByDateAndLocation(LocalDate date, String location);
    Map<LocalDate, Weather> getHistoryOfRangeAtLocation(DateRange range, String location);
    <T extends Weather> void insertWeatherRecord(T weatherRecord);
    <T extends Weather> void insertWeatherRecords(Iterable<T> weatherRecords);
    boolean containsDateAndLocation(LocalDate date, String location);
    void deleteRangeByLocation(DateRange range, String location);
}
