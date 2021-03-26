package bit.wcservice.web.service.predict;

import bit.wcservice.web.service.predict.impl.RegressionPredictModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RegressionPredictModelTest {
    private static final LocalDate BASE_DATE = LocalDate.now();

    private final PredictModel predictModel = new RegressionPredictModel();

    @Test
    void unableToPredictWhenCurrencyFeaturesAreEmpty() {
        assertEquals(-1, predictModel.predict(
                Collections.emptyMap(),
                Collections.singletonMap(BASE_DATE, Collections.singletonList(42.0))
        ));
    }

    @Test
    void unableToPredictWhenWeatherFeaturesAreEmpty() {
        assertEquals(-1, predictModel.predict(
                Collections.singletonMap(BASE_DATE, 42.0),
                Collections.emptyMap()
        ));
    }

    @Test
    void throwsExceptionWhenWeatherDataSetIsTooSmall() {
        Map<LocalDate, Double> currencyFeatures = new HashMap<>();
        currencyFeatures.put(BASE_DATE, 77.0);
        currencyFeatures.put(BASE_DATE.plusDays(2), 85.0);

        Map<LocalDate, List<Double>> weatherFeatures = new HashMap<>();
        weatherFeatures.put(BASE_DATE.plusDays(1), Collections.singletonList(5.0));

        assertThrows(NegativeArraySizeException.class, () -> predictModel.predict(currencyFeatures, weatherFeatures));
    }

    @Test
    void successfullyPredictWhenWeatherFeaturesAreEnough() {
        Map<LocalDate, Double> currencyFeatures = new HashMap<>();
        currencyFeatures.put(BASE_DATE, 77.0);
        currencyFeatures.put(BASE_DATE.plusDays(1), 85.0);

        Map<LocalDate, List<Double>> weatherFeatures = new HashMap<>();
        weatherFeatures.put(BASE_DATE.minusDays(2), Collections.singletonList(8.0));
        weatherFeatures.put(BASE_DATE.minusDays(1), Collections.singletonList(10.0));
        weatherFeatures.put(BASE_DATE, Collections.singletonList(5.0));
        weatherFeatures.put(BASE_DATE.plusDays(1), Collections.singletonList(7.0));
        weatherFeatures.put(BASE_DATE.plusDays(2), Collections.singletonList(-1.0));
        weatherFeatures.put(BASE_DATE.plusDays(3), Collections.singletonList(5.0));
        weatherFeatures.put(BASE_DATE.plusDays(4), Collections.singletonList(-6.0));

        Double predictResult = predictModel.predict(currencyFeatures, weatherFeatures);

        assertTrue(predictResult > 0);
    }
}