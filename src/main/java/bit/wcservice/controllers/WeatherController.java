package bit.wcservice.controllers;

import bit.wcservice.services.weather.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public String getWeather(@RequestParam Optional<Integer> range,
                             @RequestParam(defaultValue = "Moscow") String city) {
        String responseString;
        if (range.isEmpty()) {
            responseString = weatherService.loadCurrentWeatherIn(city.toLowerCase());
        } else {
            responseString = weatherService.loadLastDaysWeatherHistoryInLocation(range.get(), city.toLowerCase());
        }

        return "<pre>" + responseString + "</pre>";
    }
}
