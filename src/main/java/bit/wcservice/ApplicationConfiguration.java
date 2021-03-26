package bit.wcservice;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.database.service.CurrencyService;
import bit.wcservice.utils.WebLoader;
import bit.wcservice.utils.serialization.LocalDateDeserializer;
import bit.wcservice.utils.serialization.LocalDateKeyDeserializer;
import bit.wcservice.utils.serialization.LocalDateKeySerializer;
import bit.wcservice.utils.serialization.LocalDateSerializer;
import bit.wcservice.web.service.*;
import bit.wcservice.web.service.cachedloader.HistoryStorage;
import bit.wcservice.web.service.cachedloader.storage.CurrencyDBHistoryStorage;
import bit.wcservice.web.service.currency.*;
import bit.wcservice.web.service.cachedloader.CachedHistoryLoader;
import bit.wcservice.web.service.currency.impl.CurrencyAPIServiceImpl;
import bit.wcservice.web.service.currency.impl.CurrencyWebServiceImpl;
import bit.wcservice.web.service.formatter.RecordHistoryFormatter;
import bit.wcservice.web.service.predict.PredictModel;
import bit.wcservice.web.service.predict.api.APICurrencyLoader;
import bit.wcservice.web.service.predict.api.APIWeatherLoader;
import bit.wcservice.web.service.predict.api.impl.APICurrencyLoaderImpl;
import bit.wcservice.web.service.predict.api.impl.APIWeatherLoaderImpl;
import bit.wcservice.web.service.predict.impl.PredictWebServiceImpl;
import bit.wcservice.web.service.predict.impl.RegressionPredictModel;
import bit.wcservice.web.service.weather.*;
import bit.wcservice.web.service.weather.impl.WeatherAPIServiceImpl;
import bit.wcservice.web.service.weather.impl.WeatherWebServiceImpl;
import bit.wcservice.web.service.weather.storagefactory.WeatherDBStorageFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationConfiguration {
    @Bean
    @Autowired
    public HistoryStorage<Currency> currencyHistoryStorage(CurrencyService currencyService) {
        return new CurrencyDBHistoryStorage(currencyService);
    }

    @Bean
    public String currencyWebLoaderBaseURL() {
        return "http://www.cbr.ru";
    }

    @Bean
    public String weatherWebLoaderBaseURL() {
        return "http://api.weatherapi.com/v1";
    }

    @Bean
    public ObjectMapper serializeMapper() {
        ObjectMapper serializeMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule("LocalDateSerializer", new Version(
                1, 0, 0, null, null, null));

        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        module.addKeySerializer(LocalDate.class, new LocalDateKeySerializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        module.addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        serializeMapper.registerModule(module);

        return serializeMapper;
    }

    @Bean
    @Autowired
    public HistoryLoader<Currency> usdHistoryLoader(HistoryStorage<Currency> currencyHistoryStorage,
                                                    String currencyWebLoaderBaseURL) {
        return new CachedHistoryLoader<>(new WebCurrencyLoader(new WebLoader(currencyWebLoaderBaseURL)), currencyHistoryStorage);
    }

    @Bean
    public WeatherStorageFactory weatherStorageFactory() {
        return new WeatherDBStorageFactory();
    }

    @Bean
    @Autowired
    public LocationDispatcher locationDispatcher(WeatherStorageFactory weatherStorageFactory,
                                                 String weatherWebLoaderBaseURL) {
        return new LocationDispatcher(weatherWebLoaderBaseURL, weatherStorageFactory);
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
    public CurrencyAPIService currencyAPIService(HistoryLoader<Currency> usdHistoryLoader,
                                                 ObjectMapper serializeMapper) {
        return new CurrencyAPIServiceImpl(usdHistoryLoader, serializeMapper);
    }

    @Bean
    @Autowired
    public WeatherAPIService weatherAPIService(LocationDispatcher locationDispatcher,
                                               ObjectMapper serializeMapper) {
        return new WeatherAPIServiceImpl(locationDispatcher, serializeMapper);
    }


    @Bean
    public String currencyServiceBaseURL() {
        return "http://localhost:8080";
    }

    @Bean
    @Autowired
    public APICurrencyLoader apiCurrencyLoader(String currencyServiceBaseURL,
                                               ObjectMapper serializeMapper) {
        return new APICurrencyLoaderImpl(new WebLoader(currencyServiceBaseURL), serializeMapper);
    }

    @Bean
    public String weatherServiceBaseURL() {
        return "http://localhost:8080";
    }

    @Bean
    @Autowired
    public APIWeatherLoader apiWeatherLoader(String weatherServiceBaseURL,
                                             ObjectMapper serializeMapper) {
        return new APIWeatherLoaderImpl(new WebLoader(weatherServiceBaseURL), serializeMapper);
    }

    @Bean
    @Autowired
    public PredictWebServiceImpl predictService(APICurrencyLoader apiCurrencyLoader,
                                                APIWeatherLoader apiWeatherLoader,
                                                PredictModel predictModel) {
        return new PredictWebServiceImpl(apiCurrencyLoader, apiWeatherLoader, predictModel);
    }
}
