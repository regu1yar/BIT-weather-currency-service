package bit.currency.web.service.cachedloader.storage;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.datarange.DateRange;
import bit.utils.web.service.cachedloader.HistoryStorage;
import bit.currency.database.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class CurrencyDBHistoryStorage implements HistoryStorage<Currency> {
    private final CurrencyService currencyService;

    public CurrencyDBHistoryStorage(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public boolean isEmpty(LocalDate date) {
        return !currencyService.containsDate(date);
    }

    @Override
    public void put(LocalDate date, Currency data) {
        if (isEmpty(date)) {
            currencyService.insertCurrencyRecord(data);
        }
    }

    @Override
    public void putRange(Map<LocalDate, Currency> historyRange) {
        currencyService.deleteRange(DateRange.coveringRange(historyRange.keySet()));
        currencyService.insertCurrencyRecords(historyRange.values());
    }

    @Override
    public Currency get(LocalDate date) {
        return currencyService.getByDate(date);
    }

    @Override
    public Map<LocalDate, Currency> getHistoryRange(DateRange range) {
        return currencyService.getHistoryOfRange(range);
    }

    @Override
    public Optional<LocalDate> getOldestDate() {
        return currencyService.getOldestDate();
    }

    @Override
    public Optional<LocalDate> getLatestDate() {
        return currencyService.getLatestDate();
    }

    @Override
    public void clearRange(DateRange range) {
        currencyService.deleteRange(range);
    }
}
