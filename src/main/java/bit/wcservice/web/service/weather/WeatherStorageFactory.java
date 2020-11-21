package bit.wcservice.web.service.weather;

import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.web.service.cachedloader.HistoryStorage;

public interface WeatherStorageFactory {
    HistoryStorage<Weather> createLocationStorage(String location);
}
