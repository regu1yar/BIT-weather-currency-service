package bit.wcservice.database.service.impl;

import bit.utils.database.entity.datarecord.Currency;
import bit.wcservice.database.repository.CurrencyRepository;
import bit.wcservice.database.service.CurrencyService;
import bit.utils.datarange.DateRange;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Currency getByDate(LocalDate date) {
        return currencyRepository.getByDate(date);
    }

    @Override
    public Map<LocalDate, Currency> getHistoryOfRange(DateRange range) {
        List<Currency> currencyHistory =
                currencyRepository.getCurrencyHistory(range.getStart(),range.getEnd());

        return IntStream.range(0, currencyHistory.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> currencyHistory.get(i).getDate(),
                        currencyHistory::get
                ));
    }

    @Override
    public <T extends Currency> void insertCurrencyRecord(T currencyRecord) {
        currencyRepository.saveAndFlush(currencyRecord);
    }

    @Override
    public <T extends Currency> void insertCurrencyRecords(Iterable<T> currencyRecords) {
        currencyRepository.saveAll(currencyRecords);
        currencyRepository.flush();
    }

    @Override
    public boolean containsDate(LocalDate date) {
        return currencyRepository.existsByDate(date);
    }

    @Override
    public void deleteRange(DateRange range) {
        currencyRepository.deleteRange(range.getStart(), range.getEnd());
    }
}
