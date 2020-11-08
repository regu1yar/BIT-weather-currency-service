package bit.wcservice.services.datarange;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LocalDateIterator implements Iterator<LocalDate> {
    private LocalDate current;
    private final LocalDate end;

    public LocalDateIterator(LocalDate current, LocalDate end) {
        this.current = current;
        this.end = end;
    }

    @Override
    public boolean hasNext() {
        return !current.isAfter(end);
    }

    @Override
    public LocalDate next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        LocalDate previous = current;
        current = previous.plusDays(1);
        return previous;
    }
}
