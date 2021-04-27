package bit.currency.web.controller;

import bit.currency.web.service.CurrencyWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class CurrencyControllerTest {

    @Mock
    private CurrencyWebService currencyWebService;

    private CurrencyController currencyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        currencyController = new CurrencyController(currencyWebService);
    }

    @Test
    public void testCurrentDayRequest() {
        String resultString = "result";
        when(currencyWebService.loadCurrentUSDValue()).thenReturn(resultString);

        String result = currencyController.getCurrency(Optional.empty());

        assertEquals("<pre>" + resultString + "</pre>", result);
        verify(currencyWebService, times(1)).loadCurrentUSDValue();
    }

    @Test
    public void testRangeRequest() {
        String resultString = "result";
        int days = 2;
        when(currencyWebService.loadLastDaysUSDHistory(days)).thenReturn(resultString);

        String result = currencyController.getCurrency(Optional.of(days));

        assertEquals("<pre>" + resultString + "</pre>", result);
        verify(currencyWebService, times(1)).loadLastDaysUSDHistory(days);
    }

}