package bit.wcservice.services.dataloaders;

import bit.wcservice.services.datarange.DateRange;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CachedHistoryLoader<DataType> implements HistoryLoader<DataType> {
    private final HistoryLoader<DataType> webHistoryLoader;
    private final Map<LocalDate, DataType> cachedCurrencyValuesByDate = new HashMap<>();
    private DateRange cachedRange = null;

    public CachedHistoryLoader(HistoryLoader<DataType> webHistoryLoader) {
        this.webHistoryLoader = webHistoryLoader;
    }

    @Override
    public Optional<DataType> loadDailyData(LocalDate date) throws XmlException {
        if (!cachedCurrencyValuesByDate.containsKey(date)) {
            Optional<DataType> loadedData = webHistoryLoader.loadDailyData(date);
            loadedData.ifPresent(dataType -> cachedCurrencyValuesByDate.put(date, dataType));

            return loadedData;
        }

        return Optional.of(cachedCurrencyValuesByDate.get(date));
    }

    @Override
    public Map<LocalDate, DataType> loadRangeData(DateRange range) throws XmlException {
        Map<LocalDate, DataType> currencyValues = new HashMap<>();
        if (cachedRange == null) {
            currencyValues = webHistoryLoader.loadRangeData(range);
            updateCachedRange(range, currencyValues);
            return currencyValues;
        }

        if (!range.inside(cachedRange)) {
            DateRange leastCoveringRange = DateRange.leastCoveringRange(List.of(cachedRange, range));
            updateCachedRange(leastCoveringRange, webHistoryLoader.loadRangeData(leastCoveringRange));
        }

        for (LocalDate rangeDate : range) {
            if (cachedCurrencyValuesByDate.containsKey(rangeDate)) {
                currencyValues.put(rangeDate, cachedCurrencyValuesByDate.get(rangeDate));
            }
        }

        return currencyValues;
    }

    private void updateCachedRange(DateRange range, Map<LocalDate, DataType> rangeValues) {
        for (LocalDate localDate : rangeValues.keySet()) {
            cachedCurrencyValuesByDate.put(localDate, rangeValues.get(localDate));
        }

        if (cachedRange == null) {
            cachedRange = range;
            return;
        }

        cachedRange = cachedRange.unionToRange(range);
    }
}
