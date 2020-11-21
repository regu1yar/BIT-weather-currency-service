package bit.wcservice.web.service.predict;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PredictModel {
    Double predict(Map<LocalDate, Double> currencyDataset, Map<LocalDate, List<Double>> weatherDataset);
}
