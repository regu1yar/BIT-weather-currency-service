package bit.wcservice.services.formatters;

import bit.wcservice.datarange.DateRange;
import noNamespace.RootDocument.Root.Forecast.Forecastday;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class WeatherHistoryFormatter implements HistoryFormatter<Forecastday> {
    @Override
    public String formatHistory(Map<LocalDate, Forecastday> history) {
        if (history.isEmpty()) {
            return "Empty history";
        }

        DateRange range = DateRange.coveringRange(history.keySet());
        StringBuilder stringBuilder = new StringBuilder();
        for (LocalDate date : range) {
            if (!history.containsKey(date)) {
                continue;
            }

            stringBuilder
                    .append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyy"))).append(":\n")
                    .append(formatForecast(history.get(date))).append('\n');
        }

        return stringBuilder.toString();
    }

    private String formatForecast(Forecastday forecast) {
        StringBuilder stringBuilder = new StringBuilder();
        Forecastday.Day dayForecast = forecast.getDay();
        Forecastday.Astro astroForecast = forecast.getAstro();

        stringBuilder
                .append("Max temperature: ").append(dayForecast.getMaxtempC()).append(" C\n")
                .append("Min temperature: ").append(dayForecast.getMintempC()).append(" C\n")
                .append("Average temperature: ").append(dayForecast.getAvgtempC()).append(" C\n")
                .append("Max wind: ").append(dayForecast.getMaxwindKph()).append(" Kph\n")
                .append("Total precipitation: ").append(dayForecast.getTotalprecipMm()).append(" mm\n")
                .append("Average visibility: ").append(dayForecast.getAvgvisKm()).append(" km\n")
                .append("Average humidity: ").append(dayForecast.getAvghumidity()).append("%\n")
                .append("UV index: ").append(dayForecast.getUv()).append('\n')
                .append("Weather condition: ").append(dayForecast.getCondition()).append('\n')
                .append("Sunrise: ").append(astroForecast.getSunrise()).append('\n')
                .append("Sunset: ").append(astroForecast.getSunset()).append('\n');

        return stringBuilder.toString();
    }
}
