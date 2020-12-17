package bit.wcservice.web.service.currency;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.CurrencyWebService;
import bit.wcservice.web.service.HistoryLoader;
import bit.wcservice.web.service.HistoryFormatter;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class CurrencyWebServiceImpl implements CurrencyWebService {
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
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public String loadLastDaysUSDHistory(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        Map<LocalDate, Currency> history;

        try {
            history = usdHistoryLoader.loadRangeData(new DateRange(startDate, endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return historyFormatter.formatHistory(history);
    }
}
