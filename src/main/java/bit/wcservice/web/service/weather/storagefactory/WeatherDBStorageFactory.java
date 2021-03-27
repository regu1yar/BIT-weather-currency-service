package bit.wcservice.web.service.weather.storagefactory;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.web.service.cachedloader.HistoryStorage;
import bit.wcservice.database.service.WeatherService;
import bit.wcservice.web.service.cachedloader.storage.WeatherDBLocationHistoryStorage;
import bit.wcservice.web.service.weather.WeatherStorageFactory;
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
