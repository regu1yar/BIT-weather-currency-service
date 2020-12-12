package bit.wcservice.services.datarecord;

import noNamespace.RootDocument.Root.Forecast.Forecastday;

import java.util.List;
import java.util.stream.Collectors;

public class WeatherRecord implements DataRecord {
    private final Forecastday forecast;
    private final String location;

    public WeatherRecord(Forecastday forecast, String location) {
        this.forecast = forecast;
        this.location = location;
    }

    @Override
    public String getRepresentation() {
        StringBuilder stringBuilder = new StringBuilder();
        Forecastday.Day dayForecast = forecast.getDay();
        Forecastday.Astro astroForecast = forecast.getAstro();

        stringBuilder
                .append("Weather in ").append(location).append(":\n")
                .append("Max temperature: ").append(dayForecast.getMaxtempC()).append(" C\n")
                .append("Min temperature: ").append(dayForecast.getMintempC()).append(" C\n")
                .append("Average temperature: ").append(dayForecast.getAvgtempC()).append(" C\n")
                .append("Max wind: ").append(dayForecast.getMaxwindKph()).append(" Kph\n")
                .append("Total precipitation: ").append(dayForecast.getTotalprecipMm()).append(" mm\n")
                .append("Average visibility: ").append(dayForecast.getAvgvisKm()).append(" km\n")
                .append("Average humidity: ").append(dayForecast.getAvghumidity()).append("%\n")
                .append("UV index: ").append(dayForecast.getUv()).append('\n')
                .append("Weather condition: ").append(dayForecast.getCondition().getText()).append('\n')
                .append("Sunrise: ").append(astroForecast.getSunrise()).append('\n')
                .append("Sunset: ").append(astroForecast.getSunset()).append('\n');

        return stringBuilder.toString();
    }

    @Override
    public List<Double> extractFeatures() {
        Forecastday.Day dayForecast = forecast.getDay();

        List<Float> floatFeatureList = List.of(
                dayForecast.getAvgtempC()
        );

        return floatFeatureList.stream().map(Double::valueOf).collect(Collectors.toList());
    }
}
