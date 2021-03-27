package bit.wcservice.web.service.weather;

import bit.utils.database.entity.datarecord.Weather;
import bit.utils.WebLoader;
import bit.utils.web.service.HistoryLoader;
import bit.utils.web.service.cachedloader.CachedHistoryLoader;

import java.util.HashMap;
import java.util.Map;

public class LocationDispatcher {
    private final String weatherWebLoaderBaseURL;
    private final Map<String, HistoryLoader<Weather>> locationLoaders = new HashMap<>();
    private final WeatherStorageFactory storageFactory;

    public LocationDispatcher(String weatherWebLoaderBaseURL, WeatherStorageFactory storageFactory) {
        this.weatherWebLoaderBaseURL = weatherWebLoaderBaseURL;
        this.storageFactory = storageFactory;
    }

    public HistoryLoader<Weather> getLoaderByLocation(String location) {
        if (!locationLoaders.containsKey(location)) {
            locationLoaders.put(location, new CachedHistoryLoader<>(
                    new WebWeatherLoader(new WebLoader(weatherWebLoaderBaseURL), location),
                    storageFactory.createLocationStorage(location)));
        }

        return locationLoaders.get(location);
    }
}
