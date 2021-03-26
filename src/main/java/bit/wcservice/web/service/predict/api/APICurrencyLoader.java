package bit.wcservice.web.service.predict.api;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.utils.datarange.DateRange;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.Map;

public interface APICurrencyLoader {
    Map<LocalDate, Currency> loadRangeData(DateRange range) throws JsonProcessingException;
}
