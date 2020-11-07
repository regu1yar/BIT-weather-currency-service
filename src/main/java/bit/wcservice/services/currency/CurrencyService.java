package bit.wcservice.services.currency;

import bit.wcservice.datarange.DateRange;
import bit.wcservice.services.dataloaders.CachedHistoryLoader;
import bit.wcservice.services.dataloaders.HistoryLoader;
import bit.wcservice.services.formatters.HistoryFormatter;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.Map;

public class CurrencyService {
    private static final HistoryLoader<String> USD_HISTORY_LOADER = new CachedHistoryLoader<>(new WebCurrencyLoader());
    private final HistoryFormatter<String> historyFormatter;

    public CurrencyService(HistoryFormatter<String> historyFormatter) {
        this.historyFormatter = historyFormatter;
    }

    public String loadCurrentUSDValue() {
        try {
            return USD_HISTORY_LOADER.loadDailyData(LocalDate.now());
        } catch (XmlException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String loadLastDaysUSDHistory(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        Map<LocalDate, String> history;

        try {
            history = USD_HISTORY_LOADER.loadRangeData(new DateRange(startDate, endDate));
        } catch (XmlException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return historyFormatter.formatHistory(history);
    }
}
