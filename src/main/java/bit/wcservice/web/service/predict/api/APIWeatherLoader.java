package bit.wcservice.web.service.predict.api;

import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.utils.datarange.DateRange;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.Map;

public interface APIWeatherLoader {
    Map<LocalDate, Weather> loadRangeData(DateRange range, String city) throws JsonProcessingException;
}
