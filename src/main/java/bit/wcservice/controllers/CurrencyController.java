package bit.wcservice.controllers;

import bit.wcservice.services.currency.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public String getCurrency(@RequestParam Optional<Integer> range) {
        String responseString;
        if (range.isEmpty()) {
            responseString = currencyService.loadCurrentUSDValue();
        } else {
            responseString = currencyService.loadLastDaysUSDHistory(range.get());
        }

        return "<pre>" + responseString + "</pre>";
    }
}
