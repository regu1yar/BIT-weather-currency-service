package bit.weather.web.controller;

import bit.weather.web.service.WeatherWebService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherWebService weatherWebService;

    public WeatherController(WeatherWebService weatherWebService) {
        this.weatherWebService = weatherWebService;
    }

    @GetMapping
    public String getWeather(@RequestParam Optional<Integer> range,
                             @RequestParam(defaultValue = "Moscow") String city) {
        String responseString;
        if (!range.isPresent()) {
            responseString = weatherWebService.loadCurrentWeatherIn(city.toLowerCase());
        } else {
            responseString = weatherWebService.loadLastDaysWeatherHistoryInLocation(range.get(), city.toLowerCase());
        }

        return "<pre>" + responseString + "</pre>";
    }
}
