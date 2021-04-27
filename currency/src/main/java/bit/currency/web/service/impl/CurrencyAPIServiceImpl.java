package bit.currency.web.service.impl;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.datarange.DateRange;
import bit.utils.web.service.HistoryLoader;
import bit.currency.web.service.CurrencyAPIService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CurrencyAPIServiceImpl implements CurrencyAPIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyAPIServiceImpl.class);
    private static final DateTimeFormatter REQUEST_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HistoryLoader<Currency> currencyLoader;
    private final ObjectMapper serializeMapper;

    public CurrencyAPIServiceImpl(HistoryLoader<Currency> currencyLoader, ObjectMapper serializeMapper) {
        this.currencyLoader = currencyLoader;
        this.serializeMapper = serializeMapper;
    }

    @Override
    public String loadRangeData(String from, String to) {
        try {
            LocalDate fromDate = LocalDate.parse(from, REQUEST_DATE_FORMATTER);
            LocalDate toDate = LocalDate.parse(to, REQUEST_DATE_FORMATTER);
            DateRange range = new DateRange(fromDate, toDate);

            Map<LocalDate, Currency> rangeData = currencyLoader.loadRangeData(range);

            Writer writer = new StringWriter();
            serializeMapper.writeValue(writer, rangeData);

            return writer.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }
}
