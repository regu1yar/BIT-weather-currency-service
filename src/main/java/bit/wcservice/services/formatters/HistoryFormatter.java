package bit.wcservice.services.formatters;

import java.time.LocalDate;
import java.util.Map;

public interface HistoryFormatter<DataType> {
    String formatHistory(Map<LocalDate, DataType> history);
}
