package bit.wcservice.services.weather;

import bit.wcservice.datarange.DateRange;
import bit.wcservice.services.dataloaders.HistoryLoader;
import bit.wcservice.services.dataloaders.WebLoader;
import noNamespace.RootDocument;
import noNamespace.RootDocument.Root.Forecast.Forecastday;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WebWeatherLoader implements HistoryLoader<Forecastday> {
    private static final String BASE_URL = "http://api.weatherapi.com/v1";
    private static final WebLoader WEB_DATA_LOADER = new WebLoader(BASE_URL);

    private static final String HISTORY_PATH = "/history.xml";

    private static final String API_KEY_PARAMETER_NAME = "key";
    private static final String API_KEY = "dcfa94eba2b64066b01194332200611";

    private static final String LOCATION_PARAMETER_NAME = "q";

    private static final String RANGE_START_QUERY_PARAMETER_NAME = "dt";
    private static final String RANGE_END_QUERY_PARAMETER_NAME = "end_dt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String location;

    public WebWeatherLoader(String location) {
        this.location = location;
    }

    @Override
    public Forecastday loadDailyData(LocalDate date) throws XmlException {
        return loadRangeData(new DateRange(date, date)).get(date);
    }

    @Override
    public Map<LocalDate, Forecastday> loadRangeData(DateRange range) throws XmlException {
        MultiValueMap<String, String> queryArguments = new LinkedMultiValueMap<>();
        queryArguments.add(RANGE_START_QUERY_PARAMETER_NAME, range.getStart().format(DATE_FORMATTER));
        queryArguments.add(RANGE_END_QUERY_PARAMETER_NAME, range.getEnd().format(DATE_FORMATTER));
        queryArguments.add(LOCATION_PARAMETER_NAME, location);
        queryArguments.add(API_KEY_PARAMETER_NAME, API_KEY);

        String xmlResponse = WEB_DATA_LOADER.loadData(HISTORY_PATH, queryArguments);
        Forecastday[] weatherHistory = RootDocument.Factory.parse(xmlResponse).getRoot().getForecast().getForecastdayArray();

        List<LocalDate> dateRange = Stream.
                iterate(range.getStart(), localDate -> !localDate.isAfter(range.getEnd()), localDate -> localDate.plusDays(1))
                .collect(Collectors.toList());

        return IntStream.range(0, dateRange.size())
                .boxed()
                .collect(Collectors.toMap(dateRange::get, i -> weatherHistory[i]));
    }
}
