package bit.weather;

import bit.utils.WebLoader;
import bit.utils.serialization.LocalDateDeserializer;
import bit.utils.serialization.LocalDateKeyDeserializer;
import bit.utils.serialization.LocalDateKeySerializer;
import bit.utils.serialization.LocalDateSerializer;
import bit.utils.web.service.formatter.RecordHistoryFormatter;
import bit.weather.web.service.LocationDispatcher;
import bit.weather.web.service.WeatherAPIService;
import bit.weather.web.service.WeatherWebService;
import bit.weather.web.service.impl.WeatherAPIServiceImpl;
import bit.weather.web.service.impl.WeatherWebServiceImpl;
import bit.weather.web.service.storagefactory.WeatherStorageFactory;
import bit.weather.web.service.storagefactory.impl.WeatherDBStorageFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationConfiguration {
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
    public WeatherStorageFactory weatherStorageFactory() {
        return new WeatherDBStorageFactory();
    }

    @Bean
    @Autowired
    public WebClient weatherWebClient(String weatherWebLoaderBaseURL) {
        return WebClient.create(weatherWebLoaderBaseURL);
    }

    @Bean
    @Autowired
    public WebLoader weatherWebLoader(WebClient weatherWebClient) {
        return new WebLoader(weatherWebClient);
    }

    @Bean
    @Autowired
    public LocationDispatcher locationDispatcher(WeatherStorageFactory weatherStorageFactory,
                                                 WebLoader weatherWebLoader) {
        return new LocationDispatcher(weatherStorageFactory, weatherWebLoader);
    }

    @Bean
    @Autowired
    public WeatherWebService weatherService(LocationDispatcher locationDispatcher) {
        return new WeatherWebServiceImpl(locationDispatcher, new RecordHistoryFormatter<>());
    }

    @Bean
    @Autowired
    public WeatherAPIService weatherAPIService(LocationDispatcher locationDispatcher,
                                               ObjectMapper serializeMapper) {
        return new WeatherAPIServiceImpl(locationDispatcher, serializeMapper);
    }
}
