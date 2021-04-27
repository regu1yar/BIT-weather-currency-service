package bit.weather.web.service.storagefactory.impl;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.web.service.cachedloader.HistoryStorage;
import bit.utils.web.service.cachedloader.storage.MapHistoryStorage;
import bit.weather.web.service.storagefactory.WeatherStorageFactory;

public class WeatherMapStorageFactory implements WeatherStorageFactory {
    @Override
    public HistoryStorage<Weather> createLocationStorage(String location) {
        return new MapHistoryStorage<>();
    }
}
