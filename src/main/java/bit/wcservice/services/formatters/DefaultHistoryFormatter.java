package bit.wcservice.services.formatters;

import bit.wcservice.datarange.DateRange;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DefaultHistoryFormatter<DataType> implements HistoryFormatter<DataType> {
    @Override
    public String formatHistory(Map<LocalDate, DataType> history) {
        if (history.isEmpty()) {
            return "Empty history";
        }

        StringBuilder stringBuilder = new StringBuilder();
        DateRange range = DateRange.coveringRange(history.keySet());
        stringBuilder.append("History of range ").append(range).append(":\n");
        for (LocalDate date : range) {
            if (!history.containsKey(date)) {
                continue;
            }

            stringBuilder
                    .append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyy"))).append(": ")
                    .append(history.get(date)).append('\n');
        }

        return stringBuilder.toString();
    }
}
