package bit.wcservice.database.service;

import bit.wcservice.database.entity.datarecord.Currency;
import bit.wcservice.utils.datarange.DateRange;

import java.time.LocalDate;
import java.util.Map;

public interface CurrencyService {
    Currency getByDate(LocalDate date);
    Map<LocalDate, Currency> getHistoryOfRange(DateRange range);
    <T extends Currency> void insertCurrencyRecord(T currencyRecord);
    <T extends Currency> void  insertCurrencyRecords(Iterable<T> currencyRecord);
    boolean containsDate(LocalDate date);
    void deleteRange(DateRange range);
}
