package bit.wcservice.web.service.cachedloader;

import bit.wcservice.utils.datarange.DateRange;
import bit.wcservice.web.service.HistoryLoader;
import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class CachedHistoryLoaderTest {

    @Mock
    private HistoryLoader<Integer> historyLoader;

    @Mock
    private HistoryStorage<Integer> historyStorage;

    private CachedHistoryLoader<Integer> cachedHistoryLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cachedHistoryLoader = new CachedHistoryLoader<>(historyLoader, historyStorage);
    }

    @Test
    public void loadDailyDataWhenStorageIsEmpty() throws XmlException {
        LocalDate today = LocalDate.now();
        when(historyStorage.isEmpty(today)).thenReturn(true);
        when(historyLoader.loadDailyData(today)).thenReturn(Optional.of(42));

        Optional<Integer> result = cachedHistoryLoader.loadDailyData(today);

        assertTrue(result.isPresent());
        assertEquals(42, result.get());
        verify(historyStorage, times(1)).isEmpty(today);
        verify(historyStorage, times(1)).put(today, 42);
        verify(historyLoader, times(1)).loadDailyData(today);
    }

    @Test
    public void loadDailyDataWhenStorageIsNotEmpty() throws XmlException {
        LocalDate today = LocalDate.now();
        when(historyStorage.isEmpty(today)).thenReturn(false);
        when(historyStorage.get(today)).thenReturn(42);

        Optional<Integer> result = cachedHistoryLoader.loadDailyData(today);

        assertTrue(result.isPresent());
        assertEquals(42, result.get());
        verify(historyStorage, times(1)).isEmpty(today);
        verify(historyStorage, never()).put(today, 42);
        verify(historyLoader, never()).loadDailyData(any());
        verify(historyLoader, never()).loadRangeData(any());
    }

    @Test
    public void loadHistoryDataWhenStorageIsEmpty() throws XmlException {
        LocalDate today = LocalDate.now();
        DateRange range = new DateRange(today, today.plusDays(1));
        Map<LocalDate, Integer> resultMap = new HashMap<>();
        resultMap.put(today, 0);
        resultMap.put(today.plusDays(1), 1);
        when(historyLoader.loadRangeData(range)).thenReturn(resultMap);

        Map<LocalDate, Integer> result = cachedHistoryLoader.loadRangeData(range);

        assertEquals(2, result.size());
        assertEquals(0, result.get(today));
        assertEquals(1, result.get(today.plusDays(1)));
        verify(historyStorage, times(1)).putRange(result);
        verify(historyLoader, times(1)).loadRangeData(range);
    }

    @Test
    public void loadPartlyStoredHistoryData() throws XmlException {
        LocalDate today = LocalDate.now();
        DateRange range = new DateRange(today, today.plusDays(1));
        Map<LocalDate, Integer> resultMap = new HashMap<>();
        resultMap.put(today, 0);
        resultMap.put(today.plusDays(1), 1);
        when(historyLoader.loadRangeData(range)).thenReturn(resultMap);

        Map<LocalDate, Integer> result = cachedHistoryLoader.loadRangeData(range);

        DateRange newRange = new DateRange(today, today.plusDays(2));
        Map<LocalDate, Integer> newResultMap = new HashMap<>();
        newResultMap.put(today, 0);
        newResultMap.put(today.plusDays(1), 1);
        newResultMap.put(today.plusDays(2), 2);
        when(historyStorage.getHistoryRange(newRange)).thenReturn(newResultMap);
        when(historyLoader.loadRangeData(newRange)).thenReturn(newResultMap);

        Map<LocalDate, Integer> newResult = cachedHistoryLoader.loadRangeData(newRange);

        assertEquals(3, newResult.size());
        assertEquals(0, newResult.get(today));
        assertEquals(1, newResult.get(today.plusDays(1)));
        assertEquals(2, newResult.get(today.plusDays(2)));

        verify(historyStorage, times(2)).putRange(any());
        verify(historyLoader, times(2)).loadRangeData(any());
    }
}