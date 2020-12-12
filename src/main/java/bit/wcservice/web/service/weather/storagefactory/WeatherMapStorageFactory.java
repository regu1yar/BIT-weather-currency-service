package bit.wcservice.web.service.weather.storagefactory;

import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.web.service.cachedloader.HistoryStorage;
import bit.wcservice.web.service.cachedloader.storage.MapHistoryStorage;
import bit.wcservice.web.service.weather.WeatherStorageFactory;

public class WeatherMapStorageFactory implements WeatherStorageFactory {
    @Override
    public HistoryStorage<Weather> createLocationStorage(String location) {
        return new MapHistoryStorage<>();
    }
}
