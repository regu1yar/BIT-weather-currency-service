package bit.utils.database.entity.datarecord;

import bit.utils.database.entity.DataRecord;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "currency")
public class Currency implements DataRecord {
    @Getter
    @Setter
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "currency_id_seq"
    )
    @SequenceGenerator(
            name = "currency_id_seq",
            sequenceName = "currency_id_sequence",
            allocationSize = 1
    )
    @Column(
            name = "id",
            unique = true,
            updatable = false,
            nullable = false
    )
    private long currencyRecordId;

    @Getter
    @Setter
    @Column(name = "date")
    @OrderBy
    private LocalDate date;

    @Getter
    @Setter
    @Column(name = "currency_value")
    private String currencyValue;

    @Getter
    @Setter
    @Column(name = "currency_name")
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
        tmpList.add(Double.parseDouble(currencyValue.replaceAll(",", ".")));
        return tmpList;
    }
}

