package bit.wcservice.web.service.formatter;

import bit.wcservice.util.datarange.DateRange;
import bit.wcservice.database.entity.datarecord.DataRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RecordHistoryFormatter<DataType> implements HistoryFormatter<DataType> {
    @Override
    public String formatHistory(Map<LocalDate, ? extends DataType> history) {
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
                    .append(history.get(date).toString()).append('\n');
        }

        return stringBuilder.toString();
    }
}
