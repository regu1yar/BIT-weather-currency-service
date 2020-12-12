package bit.wcservice.controllers;

import bit.wcservice.services.predict.PredictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/predict")
public class PredictController {
    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @GetMapping
    public String getPrediction() {
        return "<pre>" + predictService.predict() + "</pre>";
    }
}