package bit.wcservice.web.service;

public interface WeatherWebService {
    String loadCurrentWeatherIn(String location);
    String loadLastDaysWeatherHistoryInLocation(int days, String location);
}
