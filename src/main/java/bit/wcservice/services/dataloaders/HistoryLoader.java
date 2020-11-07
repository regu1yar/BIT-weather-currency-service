package bit.wcservice.services.dataloaders;

import bit.wcservice.datarange.DateRange;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.Map;

public interface HistoryLoader<DataType> {
    DataType loadDailyData(LocalDate date) throws XmlException;
    Map<LocalDate, DataType> loadRangeData(DateRange range) throws XmlException;
}