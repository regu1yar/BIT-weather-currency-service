package bit.currency;

import bit.currency.web.service.CurrencyAPIService;
import bit.currency.web.service.CurrencyWebService;
import bit.currency.web.service.WebCurrencyLoader;
import bit.utils.WebLoader;
import bit.utils.database.entity.datarecord.Currency;
import bit.utils.serialization.LocalDateDeserializer;
import bit.utils.serialization.LocalDateKeyDeserializer;
import bit.utils.serialization.LocalDateKeySerializer;
import bit.utils.serialization.LocalDateSerializer;
import bit.utils.web.service.HistoryLoader;
import bit.utils.web.service.cachedloader.CachedHistoryLoader;
import bit.utils.web.service.cachedloader.HistoryStorage;
import bit.utils.web.service.formatter.RecordHistoryFormatter;
import bit.currency.database.service.CurrencyService;
import bit.currency.web.service.cachedloader.storage.CurrencyDBHistoryStorage;
import bit.currency.web.service.impl.CurrencyAPIServiceImpl;
import bit.currency.web.service.impl.CurrencyWebServiceImpl;
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
    @Autowired
    public CurrencyWebService currencyService(HistoryLoader<Currency> usdHistoryLoader) {
        return new CurrencyWebServiceImpl(usdHistoryLoader, new RecordHistoryFormatter<>());
    }

    @Bean
    @Autowired
    public CurrencyAPIService currencyAPIService(HistoryLoader<Currency> usdHistoryLoader,
                                                 ObjectMapper serializeMapper) {
        return new CurrencyAPIServiceImpl(usdHistoryLoader, serializeMapper);
    }
}
