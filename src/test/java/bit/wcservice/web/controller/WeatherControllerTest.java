package bit.wcservice.web.controller;

import bit.wcservice.web.service.weather.WeatherWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
class WeatherControllerTest {

    @Mock
    private WeatherWebService weatherWebService;

    private WeatherController weatherController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherController = new WeatherController(weatherWebService);
    }

    @Test
    public void testCurrentDayRequest() {
        String resultString = "result";
        String location = "Sochi";
        when(weatherWebService.loadCurrentWeatherIn(location.toLowerCase())).thenReturn(resultString);

        String result = weatherController.getWeather(Optional.empty(), location);

        assertEquals("<pre>" + resultString + "</pre>", result);
        verify(weatherWebService, times(1)).loadCurrentWeatherIn(location.toLowerCase());
    }

    @Test
    public void testRangeRequest() {
        String resultString = "result";
        int days = 2;
        String location = "Moscow";
        when(weatherWebService.loadLastDaysWeatherHistoryInLocation(days, location.toLowerCase())).thenReturn(resultString);

        String result = weatherController.getWeather(Optional.of(days), location);

        assertEquals("<pre>" + resultString + "</pre>", result);
        verify(weatherWebService, times(1)).loadLastDaysWeatherHistoryInLocation(days, location.toLowerCase());
    }

}