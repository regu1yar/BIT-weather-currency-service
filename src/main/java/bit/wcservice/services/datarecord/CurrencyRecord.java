package bit.wcservice.services.datarecord;

import noNamespace.ValCursDocument.ValCurs.Record;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class CurrencyRecord implements DataRecord {
    private final static NumberFormat FORMAT = NumberFormat.getInstance(Locale.getDefault());

    private final Record currencyRecord;
    private final String currencyName;

    public CurrencyRecord(Record currencyRecord, String currencyName) {
        this.currencyRecord = currencyRecord;
        this.currencyName = currencyName;
    }

    @Override
    public String getRepresentation() {
        return currencyName + " value: " + currencyRecord.getValue();
    }

    @Override
    public List<Double> extractFeatures() throws ParseException {
        Number number = FORMAT.parse(currencyRecord.getValue());
        return List.of(number.doubleValue());
    }
}
