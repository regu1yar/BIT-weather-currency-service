package bit.wcservice.web.service.currency.impl;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.datarange.DateRange;
import bit.utils.web.service.HistoryFormatter;
import bit.utils.web.service.HistoryLoader;
import bit.wcservice.web.service.currency.CurrencyWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class CurrencyWebServiceImpl implements CurrencyWebService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyWebServiceImpl.class);

    private final HistoryLoader<Currency> usdHistoryLoader;
    private final HistoryFormatter<Currency> historyFormatter;

    public CurrencyWebServiceImpl(HistoryLoader<Currency> usdHistoryLoader, HistoryFormatter<Currency> historyFormatter) {
        this.usdHistoryLoader = usdHistoryLoader;
        this.historyFormatter = historyFormatter;
    }

    @Override
    public String loadCurrentUSDValue() {
        try {
            Optional<Currency> loadedData = usdHistoryLoader.loadDailyData(LocalDate.now());
            if (!loadedData.isPresent()) {
                return "No current data available";
            } else {
                return loadedData.get().toString();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String loadLastDaysUSDHistory(long days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        Map<LocalDate, Currency> history;

        try {
            history = usdHistoryLoader.loadRangeData(new DateRange(startDate, endDate));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return e.getMessage();
        }

        return historyFormatter.formatHistory(history);
    }
}
