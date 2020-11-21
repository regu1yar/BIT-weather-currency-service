package bit.wcservice.database.entity.datarecord;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Entity
public class Currency implements DataRecord {
    private final static NumberFormat FORMAT = NumberFormat.getInstance(Locale.getDefault());

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long currencyRecordId;

    @Getter
    @Setter
    @OrderBy
    private LocalDate date;

    @Getter
    @Setter
    private String currencyValue;

    @Getter
    @Setter
    private String currencyName;

    public Currency(LocalDate date, String currencyValue, String currencyName) {
        this.date = date;
        this.currencyValue = currencyValue;
        this.currencyName = currencyName;
    }

    public Currency() {

    }

    @Override
    public String toString() {
        return currencyName + " value" + " on " + date + ": " + currencyValue;
    }

    @Override
    public List<Double> extractFeatures() throws ParseException {
        Number number = FORMAT.parse(currencyValue);
        return List.of(number.doubleValue());
    }
}
