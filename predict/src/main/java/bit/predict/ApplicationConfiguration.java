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
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableEurekaClient
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
    public String currencyServiceName() {
        return "currency-service";
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @LoadBalanced
    public WebClient apiWebCurrencyLoader(WebClient.Builder webClientBuilder,
                                          String currencyServiceName) {
        return webClientBuilder.baseUrl("http://" + currencyServiceName).build();
    }

    @Bean
    @Autowired
    public APICurrencyLoader apiCurrencyLoader(WebClient apiWebCurrencyLoader,
                                               ObjectMapper serializeMapper) {
        return new APICurrencyLoaderImpl(
                new WebLoader(apiWebCurrencyLoader),
                serializeMapper
        );
    }

    @Bean
    public String weatherServiceName() {
        return "weather-service";
    }

    @Bean
    @Autowired
    public WebClient apiWebWeatherLoader(WebClient.Builder webClientBuilder,
                                         String weatherServiceName) {
        return webClientBuilder.baseUrl("http://" + weatherServiceName).build();
    }

    @Bean
    @Autowired
    public APIWeatherLoader apiWeatherLoader(WebClient apiWebWeatherLoader,
                                             ObjectMapper serializeMapper) {
        return new APIWeatherLoaderImpl(
                new WebLoader(apiWebWeatherLoader),
                serializeMapper
        );
    }

    @Bean
    @Autowired
    public PredictWebServiceImpl predictService(APICurrencyLoader apiCurrencyLoader,
                                                APIWeatherLoader apiWeatherLoader,
                                                PredictModel predictModel) {
        return new PredictWebServiceImpl(apiCurrencyLoader, apiWeatherLoader, predictModel);
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
