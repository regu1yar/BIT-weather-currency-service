package bit.wcservice.currency;

import bit.wcservice.utils.DateRange;
import org.apache.xmlbeans.XmlException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachedCurrencyLoader implements CurrencyLoader {
    private static final CurrencyLoader WEB_CURRENCY_LOADER = new WebCurrencyLoader();

    private final Map<LocalDate, String> cachedCurrencyValuesByDate = new HashMap<>();
    private DateRange cachedRange = null;

    @Override
    public String loadDailyData(LocalDate date) throws XmlException {
        if (!cachedCurrencyValuesByDate.containsKey(date)) {
            cachedCurrencyValuesByDate.put(date, WEB_CURRENCY_LOADER.loadDailyData(date));
        }

        return cachedCurrencyValuesByDate.get(date);
    }

    @Override
    public Map<LocalDate, String> loadRangeData(DateRange range) throws XmlException {
        Map<LocalDate, String> currencyValues = new HashMap<>();
        if (cachedRange == null) {
            currencyValues = WEB_CURRENCY_LOADER.loadRangeData(range);
            updateCachedRange(range, currencyValues);
            return currencyValues;
        }

        if (!range.inside(cachedRange)) {
            DateRange leastCoveringRange = DateRange.leastCoveringRange(List.of(cachedRange, range));
            updateCachedRange(leastCoveringRange, WEB_CURRENCY_LOADER.loadRangeData(leastCoveringRange));
        }

        for (LocalDate rangeDate = range.getStart(); !rangeDate.isAfter(range.getEnd()); rangeDate = rangeDate.plusDays(1)) {
            currencyValues.put(rangeDate, cachedCurrencyValuesByDate.get(rangeDate));
        }

        return currencyValues;
    }

    private void updateCachedRange(DateRange range, Map<LocalDate, String> rangeValues) {
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
