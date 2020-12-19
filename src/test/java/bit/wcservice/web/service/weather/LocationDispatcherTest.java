package bit.wcservice.web.service.weather;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class LocationDispatcherTest {
    private static final String STUB_URL_STRING = "STUB_URL_STRING";

    @Mock
    private WeatherStorageFactory weatherStorageFactory;

    private LocationDispatcher locationDispatcher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        locationDispatcher = new LocationDispatcher(STUB_URL_STRING, weatherStorageFactory);
    }

    @Test
    public void getLoaderByLocation() {
        String location = "Moscow";
        locationDispatcher.getLoaderByLocation(location);
        locationDispatcher.getLoaderByLocation(location);
        verify(weatherStorageFactory, times(1)).createLocationStorage(location);
    }
}