package bit.wcservice.web.service.predict.api.impl;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.WebLoader;
import bit.utils.datarange.DateRange;
import bit.wcservice.web.service.predict.api.APICurrencyLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class APICurrencyLoaderImpl implements APICurrencyLoader {
    private final WebLoader webDataLoader;
    private final ObjectMapper serializeMapper;

    private static final String CURRENCY_API_PATH = "/api/currency";
    private static final String FROM_QUERY_PARAMETER_NAME = "from";
    private static final String TO_QUERY_PARAMETER_NAME = "to";
    private static final DateTimeFormatter REQUEST_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public APICurrencyLoaderImpl(WebLoader webDataLoader, ObjectMapper serializeMapper) {
        this.webDataLoader = webDataLoader;
        this.serializeMapper = serializeMapper;
    }

    @Override
    public Map<LocalDate, Currency> loadRangeData(DateRange range) throws JsonProcessingException {
        MultiValueMap<String, String> queryArguments = new LinkedMultiValueMap<>();
        queryArguments.add(FROM_QUERY_PARAMETER_NAME, range.getStart().format(REQUEST_DATE_FORMATTER));
        queryArguments.add(TO_QUERY_PARAMETER_NAME, range.getEnd().format(REQUEST_DATE_FORMATTER));

        String jsonResponse = webDataLoader.loadData(CURRENCY_API_PATH, queryArguments);

        TypeReference<HashMap<LocalDate, Currency>> typeRef = new TypeReference<HashMap<LocalDate, Currency>>() {};
        return serializeMapper.readValue(jsonResponse, typeRef);
    }
}
