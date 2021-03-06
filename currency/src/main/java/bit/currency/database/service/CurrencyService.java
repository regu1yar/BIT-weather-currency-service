package bit.currency.database.service;

import bit.utils.database.entity.datarecord.Currency;
import bit.utils.datarange.DateRange;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface CurrencyService {
    Currency getByDate(LocalDate date);
    Map<LocalDate, Currency> getHistoryOfRange(DateRange range);
    <T extends Currency> void insertCurrencyRecord(T currencyRecord);
    <T extends Currency> void  insertCurrencyRecords(Iterable<T> currencyRecord);
    boolean containsDate(LocalDate date);
    void deleteRange(DateRange range);
    Optional<LocalDate> getOldestDate();
    Optional<LocalDate> getLatestDate();
}
