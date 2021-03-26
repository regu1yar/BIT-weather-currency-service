package bit.wcservice.web.service.weather;

public interface WeatherAPIService {
    String loadRangeData(String from, String to, String city);
}
