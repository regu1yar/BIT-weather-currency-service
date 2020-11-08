package bit.wcservice.services.currency;

import bit.wcservice.services.datarange.DateRange;
import bit.wcservice.services.dataloaders.HistoryLoader;
import bit.wcservice.services.datarecord.DataRecord;
import bit.wcservice.services.formatters.HistoryFormatter;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class CurrencyService {
    private final HistoryLoader<DataRecord> usdHistoryLoader;
    private final HistoryFormatter<DataRecord> historyFormatter;

    public CurrencyService(HistoryLoader<DataRecord> usdHistoryLoader, HistoryFormatter<DataRecord> historyFormatter) {
        this.usdHistoryLoader = usdHistoryLoader;
        this.historyFormatter = historyFormatter;
    }

    public String loadCurrentUSDValue() {
        try {
            Optional<DataRecord> loadedData = usdHistoryLoader.loadDailyData(LocalDate.now());
            if (loadedData.isEmpty()) {
                return "No current data available";
            } else {
                return loadedData.get().getRepresentation();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String loadLastDaysUSDHistory(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        Map<LocalDate, DataRecord> history;

        try {
            history = usdHistoryLoader.loadRangeData(new DateRange(startDate, endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return historyFormatter.formatHistory(history);
    }
}
