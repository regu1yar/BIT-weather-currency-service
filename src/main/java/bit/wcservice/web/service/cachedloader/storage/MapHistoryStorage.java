package bit.wcservice.web.service.cachedloader.storage;

import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.web.service.cachedloader.HistoryStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapHistoryStorage<T> implements HistoryStorage<T> {
    private final Map<LocalDate, T> cachedCurrencyValuesByDate = new HashMap<>();

    @Override
    public boolean isEmpty(LocalDate date) {
        return !cachedCurrencyValuesByDate.containsKey(date);
    }

    @Override
    public void put(LocalDate date, T data) {
        cachedCurrencyValuesByDate.put(date, data);
    }

    @Override
    public void putRange(Map<LocalDate, T> historyRange) {
        cachedCurrencyValuesByDate.putAll(historyRange);
    }

    @Override
    public T get(LocalDate date) {
        return cachedCurrencyValuesByDate.get(date);
    }

    @Override
    public Map<LocalDate, T> getHistoryRange(DateRange range) {
        return cachedCurrencyValuesByDate.entrySet().stream()
                .filter(entry -> range.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
