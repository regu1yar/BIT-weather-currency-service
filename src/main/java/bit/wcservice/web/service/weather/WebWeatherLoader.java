package bit.wcservice.web.service.weather;

import bit.utils.datarange.DateRange;
import bit.utils.WebLoader;
import bit.utils.database.entity.datarecord.Weather;
import bit.utils.web.service.HistoryLoader;
import noNamespace.RootDocument;
import noNamespace.RootDocument.Root.Forecast.Forecastday;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WebWeatherLoader implements HistoryLoader<Weather> {
    private final WebLoader webDataLoader;

    private static final String HISTORY_PATH = "/history.xml";

    private static final String API_KEY_PARAMETER_NAME = "key";
    private static final String API_KEY = "dcfa94eba2b64066b01194332200611";

    private static final String LOCATION_PARAMETER_NAME = "q";

    private static final String RANGE_START_QUERY_PARAMETER_NAME = "dt";
    private static final String RANGE_END_QUERY_PARAMETER_NAME = "end_dt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String location;

    public WebWeatherLoader(WebLoader webDataLoader, String location) {
        this.webDataLoader = webDataLoader;
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

        String xmlResponse = webDataLoader.loadData(HISTORY_PATH, queryArguments);
        RootDocument.Root weather = RootDocument.Factory.parse(xmlResponse).getRoot();
        Forecastday[] weatherHistory = weather.getForecast().getForecastdayArray();
        String locationString = weather.getLocation().getName() + ", " + weather.getLocation().getCountry();

        List<LocalDate> dateRange = new ArrayList<>();
        for (LocalDate date : range) {
            if (date.isAfter(range.getEnd())) {
                break;
            }

            dateRange.add(date);
        }

        return IntStream.range(0, dateRange.size())
                .boxed()
                .collect(Collectors.toMap(
                        dateRange::get,
                        i -> new Weather(dateRange.get(i), location, locationString, weatherHistory[i])
                ));
    }
}
