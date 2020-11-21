package bit.wcservice.web.service.weather;

import bit.wcservice.database.entity.datarecord.Weather;
import bit.wcservice.web.service.cachedloader.CachedHistoryLoader;
import bit.wcservice.web.service.HistoryLoader;

import java.util.HashMap;
import java.util.Map;

public class LocationDispatcher {
    private final Map<String, HistoryLoader<Weather>> locationLoaders = new HashMap<>();
    private final WeatherStorageFactory storageFactory;

    public LocationDispatcher(WeatherStorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public HistoryLoader<Weather> getLoaderByLocation(String location) {
        if (!locationLoaders.containsKey(location)) {
            locationLoaders.put(location, new CachedHistoryLoader<>(
                    new WebWeatherLoader(location), storageFactory.createLocationStorage(location)));
        }

        return locationLoaders.get(location);
    }
}
