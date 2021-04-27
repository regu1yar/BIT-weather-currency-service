package bit.weather.web.service.storagefactory;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.web.service.cachedloader.HistoryStorage;

public interface WeatherStorageFactory {
    HistoryStorage<Weather> createLocationStorage(String location);
}
