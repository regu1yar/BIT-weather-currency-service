package bit.weather.web.service;

import bit.utils.WebLoader;
import bit.utils.database.entity.datarecord.Weather;
import bit.utils.web.service.HistoryLoader;
import bit.utils.web.service.cachedloader.CachedHistoryLoader;
import bit.weather.web.service.storagefactory.WeatherStorageFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class LocationDispatcher {
    private final Map<String, HistoryLoader<Weather>> locationLoaders = new HashMap<>();
    private final WeatherStorageFactory storageFactory;
    private final WebLoader weatherWebLoader;

    public LocationDispatcher(WeatherStorageFactory storageFactory, WebLoader weatherWebLoader) {
        this.storageFactory = storageFactory;
        this.weatherWebLoader = weatherWebLoader;
    }

    public HistoryLoader<Weather> getLoaderByLocation(String location) {
        if (!locationLoaders.containsKey(location)) {
            locationLoaders.put(location, new CachedHistoryLoader<>(
                    new WebWeatherLoader(weatherWebLoader, location),
                    storageFactory.createLocationStorage(location)));
        }

        return locationLoaders.get(location);
    }
}
