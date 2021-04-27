package bit.predict.web.controller;

import bit.predict.web.service.PredictWebService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/predict")
public class PredictController {
    private final PredictWebService predictWebService;

    public PredictController(PredictWebService predictWebService) {
        this.predictWebService = predictWebService;
    }

    @GetMapping
    public String getPrediction() {
        return "<pre>" + predictWebService.predict() + "</pre>";
    }
}