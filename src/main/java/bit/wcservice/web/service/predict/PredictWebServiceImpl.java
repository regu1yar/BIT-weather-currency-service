package bit.wcservice.web.service.predict;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.web.service.PredictWebService;
import bit.wcservice.web.service.HistoryLoader;
import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.weather.LocationDispatcher;
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

    private final HistoryLoader<Currency> currencyLoader;
    private final LocationDispatcher weatherLocationDispatcher;
    private final PredictModel predictModel;

    public PredictWebServiceImpl(HistoryLoader<Currency> currencyLoader,
                                 LocationDispatcher weatherLocationDispatcher,
                                 PredictModel predictModel) {
        this.currencyLoader = currencyLoader;
        this.weatherLocationDispatcher = weatherLocationDispatcher;
        this.predictModel = predictModel;
    }

    public String predict() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(FEATURE_SOURCE_RANGE - 1);
        DateRange range = new DateRange(startDate, endDate);
        try {
            Map<LocalDate, Currency> currencyData = currencyLoader.loadRangeData(range);
            Map<LocalDate, Double> currencyFeatures = new HashMap<>();
            for (Map.Entry<LocalDate, Currency> entry : currencyData.entrySet()) {
                currencyFeatures.put(entry.getKey(), entry.getValue().extractFeatures().get(0));
            }

            Map<LocalDate, Weather> weatherData =
                    weatherLocationDispatcher.getLoaderByLocation(WEATHER_REQUEST_CITY).loadRangeData(range);
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
