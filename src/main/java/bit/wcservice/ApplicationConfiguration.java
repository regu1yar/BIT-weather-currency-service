package bit.wcservice;

import bit.wcservice.services.currency.CurrencyService;
import bit.wcservice.services.formatters.DefaultHistoryFormatter;
import bit.wcservice.services.weather.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public CurrencyService currencyService() {
        return new CurrencyService(new DefaultHistoryFormatter<>());
    }

    @Bean
    public WeatherService weatherService() {
        return new WeatherService(new DefaultHistoryFormatter<>());
    }
}
