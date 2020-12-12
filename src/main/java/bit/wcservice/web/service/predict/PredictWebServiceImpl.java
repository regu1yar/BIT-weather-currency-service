package bit.wcservice.web.service.predict;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.web.service.PredictWebService;
import bit.wcservice.web.service.HistoryLoader;
import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.weather.LocationDispatcher;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictWebServiceImpl implements PredictWebService {
    private static final String WEATHER_REQUEST_CITY = "Moscow";
    private static final int FEATURE_SOURCE_RANGE = 8;

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
            for (LocalDate date : currencyData.keySet()) {
                currencyFeatures.put(date, currencyData.get(date).extractFeatures().get(0));
            }

            Map<LocalDate, Weather> weatherData =
                    weatherLocationDispatcher.getLoaderByLocation(WEATHER_REQUEST_CITY).loadRangeData(range);
            Map<LocalDate, List<Double>> weatherFeatures = new HashMap<>();
            for (LocalDate date : weatherData.keySet()) {
                weatherFeatures.put(date, weatherData.get(date).extractFeatures());
            }

            return String.valueOf(predictModel.predict(currencyFeatures, weatherFeatures));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
