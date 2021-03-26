package bit.wcservice.web.service.weather;

public interface WeatherWebService {
    String loadCurrentWeatherIn(String location);
    String loadLastDaysWeatherHistoryInLocation(long days, String location);
}
