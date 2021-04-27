package bit.weather.web.controller;

import bit.weather.web.service.WeatherAPIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherAPIController {

    private final WeatherAPIService weatherAPIService;

    public WeatherAPIController(WeatherAPIService weatherAPIService) {
        this.weatherAPIService = weatherAPIService;
    }

    @GetMapping
    String getRangeData(@RequestParam String from,
                        @RequestParam String to,
                        @RequestParam(defaultValue = "Moscow") String city) {
        return weatherAPIService.loadRangeData(from, to, city.toLowerCase());
    }

}
