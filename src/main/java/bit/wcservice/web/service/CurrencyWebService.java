package bit.wcservice.web.service;

public interface CurrencyWebService {
    String loadCurrentUSDValue();
    String loadLastDaysUSDHistory(int days);
}
