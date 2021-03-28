package bit.predict;

import bit.utils.WebLoader;
import bit.utils.serialization.LocalDateDeserializer;
import bit.utils.serialization.LocalDateKeyDeserializer;
import bit.utils.serialization.LocalDateKeySerializer;
import bit.utils.serialization.LocalDateSerializer;
import bit.predict.web.service.PredictModel;
import bit.predict.web.service.api.APICurrencyLoader;
import bit.predict.web.service.api.APIWeatherLoader;
import bit.predict.web.service.api.impl.APICurrencyLoaderImpl;
import bit.predict.web.service.api.impl.APIWeatherLoaderImpl;
import bit.predict.web.service.impl.PredictWebServiceImpl;
import bit.predict.web.service.impl.RegressionPredictModel;
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
    public PredictModel predictModel() {
        return new RegressionPredictModel();
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
        return "http://localhost:8081";
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
