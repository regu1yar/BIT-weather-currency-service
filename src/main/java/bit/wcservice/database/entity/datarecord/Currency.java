package bit.wcservice.database.entity.datarecord;

import bit.wcservice.database.entity.DataRecord;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Currency implements DataRecord {
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
    public List<Double> extractFeatures() {
        List<Double> tmpList = new ArrayList<>();
        tmpList.add(Double.parseDouble(currencyValue));
        return tmpList;
    }
}
