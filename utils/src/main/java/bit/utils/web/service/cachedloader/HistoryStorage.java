package bit.utils.web.service.cachedloader;

import bit.utils.datarange.DateRange;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface HistoryStorage<T> {
    boolean isEmpty(LocalDate date);
    void put(LocalDate date, T data);
    void putRange(Map<LocalDate, T> historyRange);
    T get(LocalDate date);
    Map<LocalDate, T> getHistoryRange(DateRange range);
    Optional<LocalDate> getOldestDate();
    Optional<LocalDate> getLatestDate();
    void clearRange(DateRange range);
}
