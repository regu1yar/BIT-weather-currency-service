package bit.wcservice.database.entity;

import java.text.ParseException;
import java.util.List;

public interface DataRecord {
    List<Double> extractFeatures() throws ParseException;
}
