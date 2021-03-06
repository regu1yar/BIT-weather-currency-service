package bit.weather.database.service.impl;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.datarange.DateRange;
import bit.weather.database.repository.WeatherRepository;
import bit.weather.database.service.WeatherService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;

    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Override
    public Weather getByDateAndLocation(LocalDate date, String location) {
        return weatherRepository.getByDateAndLocation(date, location);
    }

    @Override
    public Map<LocalDate, Weather> getHistoryOfRangeAtLocation(DateRange range, String location) {
        List<Weather> weatherHistory =
                weatherRepository.getWeatherHistoryByLocation(range.getStart(),range.getEnd(), location);

        return IntStream.range(0, weatherHistory.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> weatherHistory.get(i).getDate(),
                        weatherHistory::get
                ));
    }

    @Override
    public <T extends Weather> void insertWeatherRecord(T weatherRecord) {
        weatherRepository.saveAndFlush(weatherRecord);
    }

    @Override
    public <T extends Weather> void insertWeatherRecords(Iterable<T> weatherRecords) {
        weatherRepository.saveAll(weatherRecords);
        weatherRepository.flush();
    }

    @Override
    public boolean containsDateAndLocation(LocalDate date, String location) {
        return weatherRepository.existsByDateAndLocation(date, location);
    }

    @Override
    public void deleteRangeByLocation(DateRange range, String location) {
        weatherRepository.deleteRangeByLocation(range.getStart(), range.getEnd(), location);
    }

    @Override
    public Optional<LocalDate> getOldestDate() {
        LocalDate oldestDate = weatherRepository.getOldestDate();
        if (oldestDate == null) {
            return Optional.empty();
        } else {
            return Optional.of(oldestDate);
        }
    }

    @Override
    public Optional<LocalDate> getLatestDate() {
        LocalDate latestDate = weatherRepository.getLatestDate();
        if (latestDate == null) {
            return Optional.empty();
        } else {
            return Optional.of(latestDate);
        }
    }
}
