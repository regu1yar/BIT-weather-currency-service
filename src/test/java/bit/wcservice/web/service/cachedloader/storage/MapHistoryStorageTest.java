package bit.wcservice.web.service.cachedloader.storage;

import bit.wcservice.utils.datarange.DateRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapHistoryStorageTest {

    private static final LocalDate TEST_DATE = LocalDate.now();

    private MapHistoryStorage<Integer> mapHistoryStorage;

    @BeforeEach
    void setUp() {
        mapHistoryStorage = new MapHistoryStorage<>();
    }

    @Test
    void initiallyIsEmpty() {
        assertTrue(mapHistoryStorage.isEmpty(TEST_DATE));
    }

    @Test
    void successfullyPutNewData() {
        mapHistoryStorage.put(TEST_DATE, 42);
        assertFalse(mapHistoryStorage.isEmpty(TEST_DATE));
        assertTrue(mapHistoryStorage.isEmpty(TEST_DATE.plusDays(1)));
        assertEquals(42, mapHistoryStorage.get(TEST_DATE));
    }

    @Test
    void successfullyPutNewRange() {
        Map<LocalDate, Integer> newRangeData = new HashMap<>();
        newRangeData.put(TEST_DATE, 0);
        newRangeData.put(TEST_DATE.plusDays(1), 1);
        mapHistoryStorage.putRange(newRangeData);
        assertFalse(mapHistoryStorage.isEmpty(TEST_DATE));
        assertFalse(mapHistoryStorage.isEmpty(TEST_DATE.plusDays(1)));
        assertEquals(0, mapHistoryStorage.get(TEST_DATE));
        assertEquals(1, mapHistoryStorage.get(TEST_DATE.plusDays(1)));
    }

    @Test
    void successfullyPutPartiallyStoredRange() {
        mapHistoryStorage.put(TEST_DATE, 42);

        assertFalse(mapHistoryStorage.isEmpty(TEST_DATE));
        assertTrue(mapHistoryStorage.isEmpty(TEST_DATE.plusDays(1)));
        assertEquals(42, mapHistoryStorage.get(TEST_DATE));

        Map<LocalDate, Integer> newRangeData = new HashMap<>();
        newRangeData.put(TEST_DATE, 42);
        newRangeData.put(TEST_DATE.plusDays(1), 1);

        mapHistoryStorage.putRange(newRangeData);

        assertFalse(mapHistoryStorage.isEmpty(TEST_DATE));
        assertFalse(mapHistoryStorage.isEmpty(TEST_DATE.plusDays(1)));
        assertEquals(42, mapHistoryStorage.get(TEST_DATE));
        assertEquals(1, mapHistoryStorage.get(TEST_DATE.plusDays(1)));
    }

    @Test
    void putAndGetHistoryRange() {
        mapHistoryStorage.put(TEST_DATE.minusDays(1), -1);
        mapHistoryStorage.put(TEST_DATE.plusDays(1), 1);

        Map<LocalDate, Integer> result = mapHistoryStorage.getHistoryRange(new DateRange(TEST_DATE.minusDays(2), TEST_DATE.plusDays(2)));

        assertEquals(2, result.size());
        assertTrue(result.containsKey(TEST_DATE.minusDays(1)));
        assertTrue(result.containsKey(TEST_DATE.plusDays(1)));

        assertEquals(-1, result.get(TEST_DATE.minusDays(1)));
        assertEquals(1, result.get(TEST_DATE.plusDays(1)));
    }
}