package bit.weather.web.service.weather;

import bit.utils.WebLoader;
import bit.utils.database.entity.datarecord.Weather;
import bit.utils.web.service.cachedloader.HistoryStorage;
import bit.weather.web.service.LocationDispatcher;
import bit.weather.web.service.storagefactory.WeatherStorageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class LocationDispatcherTest {
    @Mock
    private WeatherStorageFactory weatherStorageFactory;

    @Mock
    private WebLoader weatherWebLoader;

    @Mock
    private HistoryStorage<Weather> historyStorage;

    private LocationDispatcher locationDispatcher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        locationDispatcher = new LocationDispatcher(weatherStorageFactory, weatherWebLoader);
    }

    @Test
    public void getLoaderByLocation() {
        String location = "Moscow";
        when(weatherStorageFactory.createLocationStorage(location)).thenReturn(historyStorage);
        locationDispatcher.getLoaderByLocation(location);
        locationDispatcher.getLoaderByLocation(location);
        verify(weatherStorageFactory, times(1)).createLocationStorage(location);
    }
}