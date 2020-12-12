package bit.wcservice.web.service.cachedloader;

import bit.wcservice.util.datarange.DateRange;

import java.time.LocalDate;
import java.util.Map;

public interface HistoryStorage<DataType> {
    boolean containsDate(LocalDate date);
    void put(LocalDate date, DataType data);
    void putRange(Map<LocalDate, DataType> historyRange);
    DataType get(LocalDate date);
    Map<LocalDate, DataType> getHistoryRange(DateRange range);
}
