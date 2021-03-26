package bit.wcservice.web.service.formatter;

import bit.wcservice.utils.datarange.DateRange;
import bit.wcservice.web.service.HistoryFormatter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RecordHistoryFormatterTest {
    private final HistoryFormatter<Integer> historyFormatter = new RecordHistoryFormatter<>();

    @Test
    void formatEmptyHistory() {
        assertEquals("Empty history", historyFormatter.formatHistory(Collections.emptyMap()));
    }

    @Test
    void formatHistory() {
        LocalDate today = LocalDate.now();
        DateRange range = new DateRange(today, today.plusDays(2));
        Map<LocalDate, Integer> history = new HashMap<>();
        history.put(today, 0);
        history.put(today.plusDays(2), 2);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        assertEquals(
                "History of range " + range + ":\n" +
                "\n" +
                today.format(dateFormat) + ": 0\n" +
                today.plusDays(2).format(dateFormat) + ": 2\n", historyFormatter.formatHistory(history));
    }
}