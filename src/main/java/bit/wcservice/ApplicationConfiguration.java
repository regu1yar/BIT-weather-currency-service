package bit.wcservice;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.web.service.CurrencyWebService;
import bit.wcservice.web.service.WeatherWebService;
import bit.wcservice.web.service.cachedloader.HistoryStorage;
import bit.wcservice.web.service.cachedloader.storage.CurrencyDBHistoryStorage;
import bit.wcservice.web.service.currency.CurrencyWebServiceImpl;
import bit.wcservice.web.service.currency.WebCurrencyLoader;
import bit.wcservice.web.service.cachedloader.CachedHistoryLoader;
import bit.wcservice.web.service.HistoryLoader;
import bit.wcservice.web.service.formatter.RecordHistoryFormatter;
import bit.wcservice.web.service.predict.PredictModel;
import bit.wcservice.web.service.predict.PredictWebServiceImpl;
import bit.wcservice.web.service.predict.RegressionPredictModel;
import bit.wcservice.web.service.weather.LocationDispatcher;
import bit.wcservice.web.service.weather.WeatherStorageFactory;
import bit.wcservice.web.service.weather.WeatherWebServiceImpl;
import bit.wcservice.web.service.weather.storagefactory.WeatherDBStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public HistoryStorage<Currency> currencyHistoryStorage() {
        return new CurrencyDBHistoryStorage();
    }

    @Bean
    @Autowired
    public HistoryLoader<Currency> usdHistoryLoader(HistoryStorage<Currency> currencyHistoryStorage) {
        return new CachedHistoryLoader<>(new WebCurrencyLoader(), currencyHistoryStorage);
    }

    @Bean
    public WeatherStorageFactory weatherStorageFactory() {
        return new WeatherDBStorageFactory();
    }

    @Bean
    @Autowired
    public LocationDispatcher locationDispatcher(WeatherStorageFactory weatherStorageFactory) {
        return new LocationDispatcher(weatherStorageFactory);
    }

    @Bean
    @Autowired
    public CurrencyWebService currencyService(HistoryLoader<Currency> usdHistoryLoader) {
        return new CurrencyWebServiceImpl(usdHistoryLoader, new RecordHistoryFormatter<>());
    }

    @Bean
    @Autowired
    public WeatherWebService weatherService(LocationDispatcher locationDispatcher) {
        return new WeatherWebServiceImpl(locationDispatcher, new RecordHistoryFormatter<>());
    }

    @Bean
    public PredictModel predictModel() {
        return new RegressionPredictModel();
    }

    @Bean
    @Autowired
    public PredictWebServiceImpl predictService(HistoryLoader<Currency> usdHistoryLoader,
                                                LocationDispatcher locationDispatcher,
                                                PredictModel predictModel) {
        return new PredictWebServiceImpl(usdHistoryLoader, locationDispatcher, predictModel);
    }
}
