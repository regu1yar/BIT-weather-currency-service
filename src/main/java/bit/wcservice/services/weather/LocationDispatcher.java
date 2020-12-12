package bit.wcservice.services.weather;

import bit.wcservice.services.dataloaders.CachedHistoryLoader;
import bit.wcservice.services.dataloaders.HistoryLoader;
import bit.wcservice.services.datarecord.DataRecord;

import java.util.HashMap;
import java.util.Map;

public class LocationDispatcher {
    private final Map<String, HistoryLoader<DataRecord>> locationLoaders = new HashMap<>();

    public HistoryLoader<DataRecord> getLoaderByLocation(String location) {
        if (!locationLoaders.containsKey(location)) {
            locationLoaders.put(location, new CachedHistoryLoader<>(new WebWeatherLoader(location)));
        }

        return locationLoaders.get(location);
    }
}
