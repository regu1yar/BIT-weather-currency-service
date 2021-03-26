package bit.wcservice.web.service.currency;

public interface CurrencyWebService {
    String loadCurrentUSDValue();
    String loadLastDaysUSDHistory(long days);
}
