package bit.predict.web.service.api;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.datarange.DateRange;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.Map;

public interface APICurrencyLoader {
    Map<LocalDate, Currency> loadRangeData(DateRange range) throws JsonProcessingException;
}
