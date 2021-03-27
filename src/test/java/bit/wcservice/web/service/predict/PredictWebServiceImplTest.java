package bit.wcservice.web.service.predict;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.database.entity.datarecord.Weather;
import bit.utils.datarange.DateRange;
import bit.wcservice.web.service.predict.api.APICurrencyLoader;
import bit.wcservice.web.service.predict.api.APIWeatherLoader;
import bit.wcservice.web.service.predict.impl.PredictWebServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PredictWebServiceImplTest {

    @Mock
    private APICurrencyLoader apiCurrencyLoader;

    @Mock
    private APIWeatherLoader apiWeatherLoader;

    @Mock
    private PredictModel predictModel;

    private PredictWebService predictWebService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        predictWebService = new PredictWebServiceImpl(apiCurrencyLoader, apiWeatherLoader, predictModel);
    }

    @Test
    public void successfullyPredict() throws JsonProcessingException {
        LocalDate today = LocalDate.now();
        Currency currency = new Currency(today, "77.0", "USD");
        Weather weather = new Weather();
        weather.setDate(today.minusDays(1));
        weather.setAverageTemp(0);

        Map<LocalDate, Currency> currencyMap = Collections.singletonMap(today, currency);
        Map<LocalDate, Weather> weatherMap = Collections.singletonMap(today.minusDays(1), weather);

        when(apiCurrencyLoader.loadRangeData(any(DateRange.class))).thenReturn(currencyMap);
        when(apiWeatherLoader.loadRangeData(any(DateRange.class), anyString())).thenReturn(weatherMap);
        when(predictModel.predict(anyMap(), anyMap())).thenReturn(Double.valueOf(42));

        String predictResult = predictWebService.predict();

        assertEquals(String.valueOf(Double.valueOf(42)), predictResult);
        verify(apiCurrencyLoader, times(1)).loadRangeData(any(DateRange.class));
        verify(apiWeatherLoader, times(1)).loadRangeData(any(DateRange.class), anyString());
        verify(predictModel, times(1)).predict(anyMap(), anyMap());
    }
}