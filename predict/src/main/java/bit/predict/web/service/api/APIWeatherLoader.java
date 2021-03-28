package bit.predict.web.service.api;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.datarange.DateRange;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.Map;

public interface APIWeatherLoader {
    Map<LocalDate, Weather> loadRangeData(DateRange range, String city) throws JsonProcessingException;
}
