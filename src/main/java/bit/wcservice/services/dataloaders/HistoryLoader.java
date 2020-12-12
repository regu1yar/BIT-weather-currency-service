package bit.wcservice.services.dataloaders;

import bit.wcservice.services.datarange.DateRange;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface HistoryLoader<DataType> {
    Optional<DataType> loadDailyData(LocalDate date) throws XmlException;
    Map<LocalDate, DataType> loadRangeData(DateRange range) throws XmlException;
}
