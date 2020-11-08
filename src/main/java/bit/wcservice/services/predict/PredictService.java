package bit.wcservice.services.predict;

import bit.wcservice.services.dataloaders.HistoryLoader;
import bit.wcservice.services.datarange.DateRange;
import bit.wcservice.services.datarecord.DataRecord;
import bit.wcservice.services.weather.LocationDispatcher;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictService {
    private static final String WEATHER_REQUEST_CITY = "Moscow";
    private static final int FEATURE_SOURCE_RANGE = 7;

    private final HistoryLoader<DataRecord> currencyLoader;
    private final LocationDispatcher weatherLocationDispatcher;
    private final PredictModel predictModel;

    public PredictService(HistoryLoader<DataRecord> currencyLoader,
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
            Map<LocalDate, DataRecord> currencyData = currencyLoader.loadRangeData(range);
            Map<LocalDate, Double> currencyFeatures = new HashMap<>();
            for (LocalDate date : currencyData.keySet()) {
                currencyFeatures.put(date, currencyData.get(date).extractFeatures().get(0));
            }

            Map<LocalDate, DataRecord> weatherData =
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
