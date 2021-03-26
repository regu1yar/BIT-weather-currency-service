package bit.wcservice.web.service.currency;

import bit.wcservice.utils.datarange.DateRange;
import bit.wcservice.web.service.HistoryLoader;
import bit.wcservice.utils.WebLoader;
import bit.wcservice.database.entity.datarecord.Currency;
import noNamespace.ValCursDocument;
import noNamespace.ValutaDocument;
import org.apache.xmlbeans.XmlException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class WebCurrencyLoader implements HistoryLoader<Currency> {
    private final WebLoader webDataLoader;

    private static final String CURRENCY_CODES_PATH = "/scripts/XML_val.asp";

    private static final MultiValueMap<String, String> CURRENCY_CODES_QUERY_ARGUMENTS =
            new LinkedMultiValueMap<>(Collections.singletonMap("d", Collections.singletonList("0")));

    private static final String CURRENCY_VALUES_PATH = "/scripts/XML_dynamic.asp";
    private static final String RANGE_START_QUERY_PARAMETER_NAME = "date_req1";
    private static final String RANGE_END_QUERY_PARAMETER_NAME = "date_req2";
    private static final String CURRENCY_CODE_QUERY_PARAMETER_NAME = "VAL_NM_RQ";
    private static final DateTimeFormatter REQUEST_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter RESPONSE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String USD_CURRENCY_NAME = "US Dollar";

    private String lastUSDCode = "R01235";

    public WebCurrencyLoader(WebLoader webDataLoader) {
        this.webDataLoader = webDataLoader;
    }

    @Override
    public Optional<Currency> loadDailyData(LocalDate date) throws XmlException {
        Map<LocalDate, Currency> todayData = loadRangeData(new DateRange(date, date));
        if (todayData.containsKey(date) && todayData.get(date) != null) {
            return Optional.of(todayData.get(date));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Map<LocalDate, Currency> loadRangeData(DateRange range) throws XmlException {
        loadCurrencyCode();
        return loadRange(range);
    }

    private void loadCurrencyCode() throws XmlException {
        String xmlResponse = webDataLoader.loadData(CURRENCY_CODES_PATH, CURRENCY_CODES_QUERY_ARGUMENTS);
        ValutaDocument.Valuta currencyCodesList = ValutaDocument.Factory.parse(xmlResponse).getValuta();
        for (ValutaDocument.Valuta.Item currency : currencyCodesList.getItemArray()) {
            if (currency.getEngName().equals(USD_CURRENCY_NAME)) {
                lastUSDCode = currency.getID();
            }
        }
    }

    private Map<LocalDate, Currency> loadRange(DateRange range) throws XmlException {
        MultiValueMap<String, String> queryArguments = new LinkedMultiValueMap<>();
        queryArguments.add(RANGE_START_QUERY_PARAMETER_NAME, range.getStart().format(REQUEST_DATE_FORMATTER));
        queryArguments.add(RANGE_END_QUERY_PARAMETER_NAME, range.getEnd().format(REQUEST_DATE_FORMATTER));
        queryArguments.add(CURRENCY_CODE_QUERY_PARAMETER_NAME, lastUSDCode);

        String xmlResponse = webDataLoader.loadData(CURRENCY_VALUES_PATH, queryArguments);
        ValCursDocument.ValCurs currency = ValCursDocument.Factory.parse(xmlResponse).getValCurs();
        ValCursDocument.ValCurs.Record[] currencyValues = currency.getRecordArray();

        Set<String> loadedDates = Arrays.stream(currencyValues)
                .map(ValCursDocument.ValCurs.Record::getDate)
                .collect(Collectors.toSet());

        List<LocalDate> dateRange = StreamSupport.stream(range.spliterator(), false)
                .filter(localDate -> loadedDates.contains(localDate.format(RESPONSE_DATE_FORMATTER)))
                .collect(Collectors.toList());

        return IntStream.range(0, dateRange.size())
                .boxed()
                .collect(Collectors.toMap(
                        dateRange::get,
                        i -> new Currency(dateRange.get(i), currencyValues[i].getValue(), USD_CURRENCY_NAME)
                ));
    }
}
