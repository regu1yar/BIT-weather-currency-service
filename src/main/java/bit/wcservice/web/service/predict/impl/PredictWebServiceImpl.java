package bit.wcservice.web.service.predict.impl;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.utils.datarange.DateRange;
import bit.wcservice.web.service.predict.PredictModel;
import bit.wcservice.web.service.predict.PredictWebService;
import bit.wcservice.web.service.predict.api.APICurrencyLoader;
import bit.wcservice.web.service.predict.api.APIWeatherLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictWebServiceImpl implements PredictWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PredictWebServiceImpl.class);

    private static final String WEATHER_REQUEST_CITY = "Moscow";
    private static final long FEATURE_SOURCE_RANGE = 8;

    private final APICurrencyLoader apiCurrencyLoader;
    private final APIWeatherLoader apiWeatherLoader;
    private final PredictModel predictModel;

    public PredictWebServiceImpl(APICurrencyLoader apiCurrencyLoader,
                                 APIWeatherLoader apiWeatherLoader,
                                 PredictModel predictModel) {
        this.apiCurrencyLoader = apiCurrencyLoader;
        this.apiWeatherLoader = apiWeatherLoader;
        this.predictModel = predictModel;
    }

    public String predict() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(FEATURE_SOURCE_RANGE - 1);
        DateRange range = new DateRange(startDate, endDate);
        try {
            Map<LocalDate, Currency> currencyData = apiCurrencyLoader.loadRangeData(range);
            Map<LocalDate, Double> currencyFeatures = new HashMap<>();
            for (Map.Entry<LocalDate, Currency> entry : currencyData.entrySet()) {
                currencyFeatures.put(entry.getKey(), entry.getValue().extractFeatures().get(0));
            }

            Map<LocalDate, Weather> weatherData =
                    apiWeatherLoader.loadRangeData(range, WEATHER_REQUEST_CITY);
            Map<LocalDate, List<Double>> weatherFeatures = new HashMap<>();
            for (Map.Entry<LocalDate, Weather> entry : weatherData.entrySet()) {
                weatherFeatures.put(entry.getKey(), entry.getValue().extractFeatures());
            }

            return String.valueOf(predictModel.predict(currencyFeatures, weatherFeatures));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }
}
