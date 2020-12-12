package bit.wcservice.web.service;

import java.time.LocalDate;
import java.util.Map;

public interface HistoryFormatter<DataType> {
    String formatHistory(Map<LocalDate, ? extends DataType> history);
}
