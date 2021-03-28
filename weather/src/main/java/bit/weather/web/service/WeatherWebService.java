package bit.weather.web.service;

public interface WeatherWebService {
    String loadCurrentWeatherIn(String location);
    String loadLastDaysWeatherHistoryInLocation(long days, String location);
}
