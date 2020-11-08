package bit.wcservice;

import bit.wcservice.services.currency.CurrencyService;
import bit.wcservice.services.currency.WebCurrencyLoader;
import bit.wcservice.services.dataloaders.CachedHistoryLoader;
import bit.wcservice.services.dataloaders.HistoryLoader;
import bit.wcservice.services.datarecord.DataRecord;
import bit.wcservice.services.formatters.RecordHistoryFormatter;
import bit.wcservice.services.predict.PredictModel;
import bit.wcservice.services.predict.PredictService;
import bit.wcservice.services.predict.RegressionPredictModel;
import bit.wcservice.services.weather.LocationDispatcher;
import bit.wcservice.services.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public HistoryLoader<DataRecord> usdHistoryLoader() {
        return new CachedHistoryLoader<>(new WebCurrencyLoader());
    }

    @Bean
    public LocationDispatcher locationDispatcher() {
        return new LocationDispatcher();
    }

    @Bean
    @Autowired
    public CurrencyService currencyService(HistoryLoader<DataRecord> usdHistoryLoader) {
        return new CurrencyService(usdHistoryLoader, new RecordHistoryFormatter());
    }

    @Bean
    @Autowired
    public WeatherService weatherService(LocationDispatcher locationDispatcher) {
        return new WeatherService(locationDispatcher, new RecordHistoryFormatter());
    }

    @Bean
    public PredictModel predictModel() {
        return new RegressionPredictModel();
    }

    @Bean
    @Autowired
    public PredictService predictService(HistoryLoader<DataRecord> usdHistoryLoader,
                                         LocationDispatcher locationDispatcher,
                                         PredictModel predictModel) {
        return new PredictService(usdHistoryLoader, locationDispatcher, predictModel);
    }
}
