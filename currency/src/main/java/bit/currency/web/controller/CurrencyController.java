package bit.currency.web.controller;

import bit.currency.web.service.CurrencyWebService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    private final CurrencyWebService currencyWebService;

    public CurrencyController(CurrencyWebService currencyWebService) {
        this.currencyWebService = currencyWebService;
    }

    @GetMapping
    public String getCurrency(@RequestParam Optional<Integer> range) {
        String responseString;
        if (!range.isPresent()) {
            responseString = currencyWebService.loadCurrentUSDValue();
        } else {
            responseString = currencyWebService.loadLastDaysUSDHistory(range.get());
        }

        return "<pre>" + responseString + "</pre>";
    }
}
