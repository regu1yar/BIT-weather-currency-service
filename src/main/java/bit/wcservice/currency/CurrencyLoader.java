package bit.wcservice.currency;

import bit.wcservice.utils.DateRange;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.Map;

public interface CurrencyLoader {
    String loadDailyData(LocalDate date) throws XmlException;
    Map<LocalDate, String> loadRangeData(DateRange range) throws XmlException;
}
