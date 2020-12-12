package bit.wcservice.web.service.cachedloader;

import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.HistoryLoader;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CachedHistoryLoader<DataType> implements HistoryLoader<DataType> {
    private final HistoryLoader<DataType> webHistoryLoader;
    private final HistoryStorage<DataType> historyStorage;
    private DateRange cachedRange = null;

    public CachedHistoryLoader(HistoryLoader<DataType> webHistoryLoader, HistoryStorage<DataType> historyStorage) {
        this.webHistoryLoader = webHistoryLoader;
        this.historyStorage = historyStorage;
    }

    @Override
    public Optional<DataType> loadDailyData(LocalDate date) throws XmlException {
        if (!historyStorage.containsDate(date)) {
            Optional<DataType> loadedData = webHistoryLoader.loadDailyData(date);
            loadedData.ifPresent(data -> historyStorage.put(date, data));

            return loadedData;
        }

        return Optional.of(historyStorage.get(date));
    }

    @Override
    public Map<LocalDate, DataType> loadRangeData(DateRange range) throws XmlException {
        if (cachedRange == null) {
            Map<LocalDate, DataType> currencyValues = webHistoryLoader.loadRangeData(range);
            updateCachedRange(range, currencyValues);
            return currencyValues;
        }

        if (!range.inside(cachedRange)) {
            DateRange leastCoveringRange = DateRange.leastCoveringRange(List.of(cachedRange, range));
            updateCachedRange(leastCoveringRange, webHistoryLoader.loadRangeData(leastCoveringRange));
        }

        return historyStorage.getHistoryRange(range);
    }

    private void updateCachedRange(DateRange range, Map<LocalDate, DataType> rangeValues) {
        historyStorage.putRange(rangeValues);
        if (cachedRange == null) {
            cachedRange = range;
            return;
        }

        cachedRange = cachedRange.unionToRange(range);
    }
}
