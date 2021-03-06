package bit.currency.web.controller;

import bit.currency.web.service.CurrencyAPIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
public class CurrencyAPIController {

    private final CurrencyAPIService currencyAPIService;

    public CurrencyAPIController(CurrencyAPIService currencyAPIService) {
        this.currencyAPIService = currencyAPIService;
    }

    @GetMapping
    String getRangeData(@RequestParam String from, @RequestParam String to) {
        return currencyAPIService.loadRangeData(from, to);
    }

}
