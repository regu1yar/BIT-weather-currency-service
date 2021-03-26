package bit.wcservice.web.service.cachedloader;

import bit.wcservice.utils.datarange.DateRange;
import bit.wcservice.web.service.HistoryLoader;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CachedHistoryLoader<T> implements HistoryLoader<T> {
    private final HistoryLoader<T> webHistoryLoader;
    private final HistoryStorage<T> historyStorage;
    private DateRange cachedRange = null;

    public CachedHistoryLoader(HistoryLoader<T> webHistoryLoader, HistoryStorage<T> historyStorage) {
        this.webHistoryLoader = webHistoryLoader;
        this.historyStorage = historyStorage;
    }

    @Override
    public Optional<T> loadDailyData(LocalDate date) throws XmlException {
        if (historyStorage.isEmpty(date)) {
            Optional<T> loadedData = webHistoryLoader.loadDailyData(date);
            loadedData.ifPresent(data -> historyStorage.put(date, data));

            return loadedData;
        }

        return Optional.of(historyStorage.get(date));
    }

    @Override
    public Map<LocalDate, T> loadRangeData(DateRange range) throws XmlException {
        if (cachedRange == null) {
            Map<LocalDate, T> currencyValues = webHistoryLoader.loadRangeData(range);
            updateCachedRange(range, currencyValues);
            return currencyValues;
        }

        if (!range.inside(cachedRange)) {
            List<DateRange> ranges = new ArrayList<>();
            ranges.add(cachedRange);
            ranges.add(range);
            DateRange leastCoveringRange = DateRange.leastCoveringRange(ranges);
            updateCachedRange(leastCoveringRange, webHistoryLoader.loadRangeData(leastCoveringRange));
        }

        return historyStorage.getHistoryRange(range);
    }

    private void updateCachedRange(DateRange range, Map<LocalDate, T> rangeValues) {
        historyStorage.putRange(rangeValues);
        if (cachedRange == null) {
            cachedRange = range;
            return;
        }

        cachedRange = cachedRange.unionToRange(range);
    }
}
