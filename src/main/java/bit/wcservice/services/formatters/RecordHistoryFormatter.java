package bit.wcservice.services.formatters;

import bit.wcservice.services.datarange.DateRange;
import bit.wcservice.services.datarecord.DataRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RecordHistoryFormatter implements HistoryFormatter<DataRecord> {
    @Override
    public String formatHistory(Map<LocalDate, DataRecord> history) {
        if (history.isEmpty()) {
            return "Empty history";
        }

        StringBuilder stringBuilder = new StringBuilder();
        DateRange range = DateRange.coveringRange(history.keySet());
        stringBuilder.append("History of range ").append(range).append(":\n\n");
        for (LocalDate date : range) {
            if (!history.containsKey(date)) {
                continue;
            }

            stringBuilder
                    .append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyy"))).append(": ")
                    .append(history.get(date).getRepresentation()).append('\n');
        }

        return stringBuilder.toString();
    }
}
