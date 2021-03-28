package bit.currency.database;

import bit.utils.database.entity.datarecord.Currency;
import bit.currency.database.repository.CurrencyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepositoryFiller {

    public static void fillRepository(CurrencyRepository currencyRepository, LocalDate baseDate, String usdCurrencyName) {
        List<Currency> testData = new ArrayList<>();
        testData.add(new Currency(baseDate, "75.0", usdCurrencyName));
        testData.add(new Currency(baseDate.plusDays(1), "80.0", usdCurrencyName));
        testData.add(new Currency(baseDate.plusDays(2), "85.0", usdCurrencyName));
        currencyRepository.saveAll(testData);
        currencyRepository.flush();
    }

}
