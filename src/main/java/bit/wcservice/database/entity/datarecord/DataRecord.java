package bit.wcservice.database.entity.datarecord;

import java.text.ParseException;
import java.util.List;

public interface DataRecord {
    List<Double> extractFeatures() throws ParseException;
}
