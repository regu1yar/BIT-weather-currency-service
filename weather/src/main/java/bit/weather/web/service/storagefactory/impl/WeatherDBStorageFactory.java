package bit.weather.web.service.storagefactory.impl;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.web.service.cachedloader.HistoryStorage;
import bit.weather.database.service.WeatherService;
import bit.weather.web.service.cachedloader.storage.WeatherDBLocationHistoryStorage;
import bit.weather.web.service.storagefactory.WeatherStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherDBStorageFactory implements WeatherStorageFactory {
    @Autowired
    private WeatherService weatherService;

    @Override
    public HistoryStorage<Weather> createLocationStorage(String location) {
        return new WeatherDBLocationHistoryStorage(weatherService, location);
    }
}
