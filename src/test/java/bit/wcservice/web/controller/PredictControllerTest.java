package bit.wcservice.web.controller;

import bit.wcservice.web.service.PredictWebService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PredictControllerTest {

    @Mock
    private PredictWebService predictWebService;

    private PredictController predictController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        predictController = new PredictController(predictWebService);
    }

    @Test
    void getPrediction() {
        String resultString = "result";
        when(predictWebService.predict()).thenReturn(resultString);

        String result = predictController.getPrediction();

        assertEquals("<pre>" + resultString + "</pre>", result);
        verify(predictWebService, times(1)).predict();
    }
}