package bit.wcservice.web.service.predict.impl;

import bit.utils.datarange.DateRange;
import bit.wcservice.web.service.predict.PredictModel;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegressionPredictModel implements PredictModel {
    private static final int FEATURE_WINDOW_RANGE = 2;

    @Override
    public Double predict(Map<LocalDate, Double> currencyDataset, Map<LocalDate, List<Double>> weatherDataset) {
        if (currencyDataset.isEmpty() || weatherDataset.isEmpty()) {
            return (double) -1;
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        Map<LocalDate, Double> filledCurrencyDataset = fillCurrencyGaps(currencyDataset, weatherDataset);
        DateRange datasetRange = DateRange.coveringRange(weatherDataset.keySet());
        double[] y = new double[weatherDataset.size() - FEATURE_WINDOW_RANGE];
        double[][] x = new double[weatherDataset.size() - FEATURE_WINDOW_RANGE][];
        int i = 0;
        for (LocalDate windowStart : datasetRange) {
            if (windowStart.plusDays(FEATURE_WINDOW_RANGE).isAfter(datasetRange.getEnd())) {
                break;
            }

            x[i] = createSample(
                    filledCurrencyDataset,
                    weatherDataset,
                    new DateRange(windowStart, windowStart.plusDays((long) FEATURE_WINDOW_RANGE - 1))
            ).stream().mapToDouble(Double::doubleValue).toArray();

            y[i] = filledCurrencyDataset.get(windowStart.plusDays(FEATURE_WINDOW_RANGE));

            ++i;
        }

        regression.newSampleData(y, x);

        double[] requestSample = createSample(
                filledCurrencyDataset,
                weatherDataset,
                new DateRange(datasetRange.getEnd().minusDays((long) FEATURE_WINDOW_RANGE - 1), datasetRange.getEnd())
        ).stream().mapToDouble(Double::doubleValue).toArray();

        double[] regressionParameters = regression.estimateRegressionParameters();

        double answer = 0;
        for (int j = 1; j < regressionParameters.length; j++) {
            answer += requestSample[j - 1] * regressionParameters[j];
        }

        return answer + regressionParameters[0];
    }

    private Map<LocalDate, Double> fillCurrencyGaps(Map<LocalDate, Double> currencyDataset,
                                                    Map<LocalDate, List<Double>> weatherDataset) {
        double averageCurrency = 0;
        for (Double currencyFeature : currencyDataset.values()) {
            averageCurrency += currencyFeature;
        }

        averageCurrency /= currencyDataset.size();

        for (LocalDate date : weatherDataset.keySet()) {
            if (!currencyDataset.containsKey(date)) {
                currencyDataset.put(date, averageCurrency);
            }
        }

        return currencyDataset;
    }

    private List<Double> createSample(Map<LocalDate, Double> currencyDataset,
                                      Map<LocalDate, List<Double>> weatherDataset,
                                      DateRange sampleRange) {
        List<Double> sample = new ArrayList<>();
        for (LocalDate date : sampleRange) {
            sample.add(currencyDataset.get(date));
            sample.addAll(weatherDataset.get(date));
        }

        return sample;
    }
}
