package bit.wcservice.web.service;

import bit.wcservice.util.datarange.DateRange;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface HistoryLoader<T> {
    Optional<T> loadDailyData(LocalDate date) throws XmlException;
    Map<LocalDate, T> loadRangeData(DateRange range) throws XmlException;
}
