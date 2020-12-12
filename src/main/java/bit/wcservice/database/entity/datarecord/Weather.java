package bit.wcservice.database.entity.datarecord;

import bit.wcservice.database.entity.DataRecord;
import lombok.Getter;
import lombok.Setter;
import noNamespace.RootDocument.Root.Forecast.Forecastday;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Weather implements DataRecord {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long weatherRecordId;

    @Getter
    @Setter
    @OrderBy
    private LocalDate date;

    @Getter
    @Setter
    private String location;

    @Getter
    @Setter
    private String prettyLocation;

    @Getter
    @Setter
    private double maxTemp;

    @Getter
    @Setter
    private double minTemp;

    @Getter
    @Setter
    private double averageTemp;

    @Getter
    @Setter
    private double maxWind;

    @Getter
    @Setter
    private double precipitation;

    @Getter
    @Setter
    private double visibility;

    @Getter
    @Setter
    private double humidity;

    @Getter
    @Setter
    private double uvIndex;

    @Getter
    @Setter
    private String weatherCondition;

    @Getter
    @Setter
    private String sunrise;

    @Getter
    @Setter
    private String sunset;

    public Weather(LocalDate date, String location, String prettyLocation, Forecastday forecast) {
        this.date = date;
        this.location = location;
        this.prettyLocation = prettyLocation;

        Forecastday.Day dayForecast = forecast.getDay();
        Forecastday.Astro astroForecast = forecast.getAstro();
        this.maxTemp = dayForecast.getMaxtempC();
        this.minTemp = dayForecast.getMintempC();
        this.averageTemp = dayForecast.getAvgtempC();
        this.maxWind = dayForecast.getMaxwindKph();
        this.precipitation = dayForecast.getTotalprecipMm();
        this.visibility = dayForecast.getAvgvisKm();
        this.humidity = dayForecast.getAvghumidity();
        this.uvIndex = dayForecast.getUv();
        this.weatherCondition = dayForecast.getCondition().getText();
        this.sunrise = astroForecast.getSunrise();
        this.sunset = astroForecast.getSunset();
    }

    public Weather() {

    }

    @Override
    public String toString() {
        return "Weather in " + prettyLocation + " on " + date + ":\n" +
                "Max temperature: " + maxTemp + " C\n" +
                "Min temperature: " + minTemp + " C\n" +
                "Average temperature: " + averageTemp + " C\n" +
                "Max wind: " + maxWind + " Kph\n" +
                "Total precipitation: " + precipitation + " mm\n" +
                "Average visibility: " + visibility + " km\n" +
                "Average humidity: " + humidity + "%\n" +
                "UV index: " + uvIndex + '\n' +
                "Weather condition: " + weatherCondition + '\n' +
                "Sunrise: " + sunrise + '\n' +
                "Sunset: " + sunset + '\n';
    }

    @Override
    public List<Double> extractFeatures() {
        return List.of(averageTemp);
    }
}
