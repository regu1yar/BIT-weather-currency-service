package bit.wcservice.dataloaders;

import bit.wcservice.DateRange;
import currency.wcservice.bit.ValutaDocument;
import currency.wcservice.bit.ValCursDocument;
import org.apache.xmlbeans.XmlException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WebCurrencyLoader implements HistoryLoader<String> {
    private static final String BASE_URL = "http://www.cbr.ru";
    private static final WebLoader WEB_DATA_LOADER = new WebLoader(BASE_URL);

    private static final String CURRENCY_CODES_PATH = "/scripts/XML_val.asp";

    private static final MultiValueMap<String, String> CURRENCY_CODES_QUERY_ARGUMENTS =
            new LinkedMultiValueMap<>(Map.of("d", List.of("0")));

    private static final String CURRENCY_VALUES_PATH = "/scrips/XML_dynamics.asp";
    private static final String RANGE_START_QUERY_PARAMETER_NAME = "date_req1";
    private static final String RANGE_END_QUERY_PARAMETER_NAME = "date_req2";
    private static final String CURRENCY_CODE_QUERY_PARAMETER_NAME = "VAL_NM_RQ";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String USD_CURRENCY_NAME = "US Dollar";

    private String lastUSDCode = "R01235";

    @Override
    public String loadDailyData(LocalDate date) throws XmlException {
        return loadRangeData(new DateRange(date, date)).get(date);
    }

    @Override
    public Map<LocalDate, String> loadRangeData(DateRange range) throws XmlException {
        loadCurrencyCode();
        return loadRange(range);
    }

    private void loadCurrencyCode() throws XmlException {
        String xmlResponse = WEB_DATA_LOADER.loadData(CURRENCY_CODES_PATH, CURRENCY_CODES_QUERY_ARGUMENTS);
        ValutaDocument.Valuta currencyCodesList = ValutaDocument.Factory.parse(xmlResponse).getValuta();
        for (ValutaDocument.Valuta.Item currency : currencyCodesList.getItemArray()) {
            if (currency.getEngName().equals(USD_CURRENCY_NAME)) {
                lastUSDCode = currency.getID();
            }
        }
    }

    private Map<LocalDate, String> loadRange(DateRange range) throws XmlException {
        MultiValueMap<String, String> queryArguments = new LinkedMultiValueMap<>();
        queryArguments.add(RANGE_START_QUERY_PARAMETER_NAME, range.getStart().format(DATE_FORMATTER));
        queryArguments.add(RANGE_END_QUERY_PARAMETER_NAME, range.getEnd().format(DATE_FORMATTER));
        queryArguments.add(CURRENCY_CODE_QUERY_PARAMETER_NAME, lastUSDCode);

        String xmlResponse = WEB_DATA_LOADER.loadData(CURRENCY_VALUES_PATH, queryArguments);
        ValCursDocument.ValCurs.Record[] currencyValues =
                ValCursDocument.Factory.parse(xmlResponse).getValCurs().getRecordArray();

        List<LocalDate> dateRange = Stream.
                iterate(range.getStart(), localDate -> !localDate.isAfter(range.getEnd()), localDate -> localDate.plusDays(1))
                .collect(Collectors.toList());

        return IntStream.range(0, dateRange.size())
                .boxed()
                .collect(Collectors.toMap(dateRange::get, i -> currencyValues[i].getValue()));
    }
}
