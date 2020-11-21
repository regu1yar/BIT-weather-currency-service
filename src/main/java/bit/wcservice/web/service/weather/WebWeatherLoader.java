package bit.wcservice.web.service.weather;

import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.HistoryLoader;
import bit.wcservice.util.WebLoader;
import bit.wcservice.database.entity.datarecord.Weather;
import noNamespace.RootDocument;
import noNamespace.RootDocument.Root.Forecast.Forecastday;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WebWeatherLoader implements HistoryLoader<Weather> {
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
    public Optional<Weather> loadDailyData(LocalDate date) throws XmlException {
        Map<LocalDate, Weather> todayData = loadRangeData(new DateRange(date, date));
        if (todayData.containsKey(date) && todayData.get(date) != null) {
            return Optional.of(todayData.get(date));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Map<LocalDate, Weather> loadRangeData(DateRange range) throws XmlException {
        MultiValueMap<String, String> queryArguments = new LinkedMultiValueMap<>();
        queryArguments.add(RANGE_START_QUERY_PARAMETER_NAME, range.getStart().format(DATE_FORMATTER));
        queryArguments.add(RANGE_END_QUERY_PARAMETER_NAME, range.getEnd().format(DATE_FORMATTER));
        queryArguments.add(LOCATION_PARAMETER_NAME, location);
        queryArguments.add(API_KEY_PARAMETER_NAME, API_KEY);

        String xmlResponse = WEB_DATA_LOADER.loadData(HISTORY_PATH, queryArguments);
        RootDocument.Root weather = RootDocument.Factory.parse(xmlResponse).getRoot();
        Forecastday[] weatherHistory = weather.getForecast().getForecastdayArray();
        String locationString = weather.getLocation().getName() + ", " + weather.getLocation().getCountry();

        List<LocalDate> dateRange = Stream.
                iterate(range.getStart(), localDate -> !localDate.isAfter(range.getEnd()), localDate -> localDate.plusDays(1))
                .collect(Collectors.toList());


        return IntStream.range(0, dateRange.size())
                .boxed()
                .collect(Collectors.toMap(
                        dateRange::get,
                        i -> new Weather(dateRange.get(i), locationString, weatherHistory[i])
                ));
    }
}
