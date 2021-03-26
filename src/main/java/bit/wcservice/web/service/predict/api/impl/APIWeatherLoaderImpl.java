package bit.wcservice.web.service.predict.api.impl;

import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.utils.WebLoader;
import bit.wcservice.utils.datarange.DateRange;
import bit.wcservice.web.service.predict.api.APIWeatherLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class APIWeatherLoaderImpl implements APIWeatherLoader {
    private final WebLoader webDataLoader;
    private final ObjectMapper serializeMapper;

    private static final String WEATHER_API_PATH = "/api/weather";
    private static final String FROM_QUERY_PARAMETER_NAME = "from";
    private static final String TO_QUERY_PARAMETER_NAME = "to";
    private static final String CITY_QUERY_PARAMETER_NAME = "city";
    private static final DateTimeFormatter REQUEST_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public APIWeatherLoaderImpl(WebLoader webDataLoader, ObjectMapper serializeMapper) {
        this.webDataLoader = webDataLoader;
        this.serializeMapper = serializeMapper;
    }

    @Override
    public Map<LocalDate, Weather> loadRangeData(DateRange range, String city) throws JsonProcessingException {
        MultiValueMap<String, String> queryArguments = new LinkedMultiValueMap<>();
        queryArguments.add(FROM_QUERY_PARAMETER_NAME, range.getStart().format(REQUEST_DATE_FORMATTER));
        queryArguments.add(TO_QUERY_PARAMETER_NAME, range.getEnd().format(REQUEST_DATE_FORMATTER));
        queryArguments.add(CITY_QUERY_PARAMETER_NAME, city);

        String jsonResponse = webDataLoader.loadData(WEATHER_API_PATH, queryArguments);

        TypeReference<HashMap<LocalDate, Weather>> typeRef = new TypeReference<HashMap<LocalDate, Weather>>() {};
        return serializeMapper.readValue(jsonResponse, typeRef);
    }
}
