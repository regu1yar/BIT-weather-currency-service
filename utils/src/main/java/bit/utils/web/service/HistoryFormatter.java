package bit.utils.web.service;

import java.time.LocalDate;
import java.util.Map;

public interface HistoryFormatter<T> {
    String formatHistory(Map<LocalDate, ? extends T> history);
}
