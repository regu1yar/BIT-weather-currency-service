package bit.utils.database.entity.datarecord;

import bit.utils.database.entity.DataRecord;
import lombok.Getter;
import lombok.Setter;
import noNamespace.RootDocument.Root.Forecast.Forecastday;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "weather")
public class Weather implements DataRecord {
    @Getter
    @Setter
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "weather_id_seq"
    )
    @SequenceGenerator(
            name = "weather_id_seq",
            sequenceName = "weather_id_sequence",
            allocationSize = 1
    )
    @Column(
            name = "id",
            unique = true,
            updatable = false,
            nullable = false
    )
    private long weatherRecordId;

    @Getter
    @Setter
    @Column(name = "date")
    @OrderBy
    private LocalDate date;

    @Getter
    @Setter
    @Column(name = "location")
    private String location;

    @Getter
    @Setter
    @Column(name = "pretty_location")
    private String prettyLocation;

    @Getter
    @Setter
    @Column(name = "max_temp")
    private double maxTemp;

    @Getter
    @Setter
    @Column(name = "min_temp")
    private double minTemp;

    @Getter
    @Setter
    @Column(name = "average_temp")
    private double averageTemp;

    @Getter
    @Setter
    @Column(name = "max_wind")
    private double maxWind;

    @Getter
    @Setter
    @Column(name = "precipitation")
    private double precipitation;

    @Getter
    @Setter
    @Column(name = "visibility")
    private double visibility;

    @Getter
    @Setter
    @Column(name = "humidity")
    private double humidity;

    @Getter
    @Setter
    @Column(name = "uv_index")
    private double uvIndex;

    @Getter
    @Setter
    @Column(name = "weather_condition")
    private String weatherCondition;

    @Getter
    @Setter
    @Column(name = "sunrise")
    private String sunrise;

    @Getter
    @Setter
    @Column(name = "sunset")
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
        return Collections.singletonList(averageTemp);
    }
}
