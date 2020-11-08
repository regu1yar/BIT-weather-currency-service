package bit.wcservice.services.datarecord;

import java.text.ParseException;
import java.util.List;

public interface DataRecord {
    String getRepresentation();
    List<Double> extractFeatures() throws ParseException;
}
