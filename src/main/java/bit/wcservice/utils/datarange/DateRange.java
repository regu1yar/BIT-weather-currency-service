package bit.wcservice.utils.datarange;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateRange implements Iterable<LocalDate> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final LocalDate start;
    private final LocalDate end;

    public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange that = (DateRange) o;
        return start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return start.format(DATE_FORMATTER) + " - " + end.format(DATE_FORMATTER);
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return new LocalDateIterator(start, end);
    }

    public static DateRange coveringRange(Set<LocalDate> dates) {
        LocalDate from = null;
        LocalDate to = null;
        for (LocalDate date : dates) {
            if (from == null || date.isBefore(from)) {
                from = date;
            }

            if (to == null || date.isAfter(to)) {
                to = date;
            }
        }

        return new DateRange(from, to);
    }

    public boolean notOverlaps(DateRange other) {
        return other == null || (end.isBefore(other.start) || start.isAfter(other.end));
    }

    public DateRange unionToRange(DateRange other) {
        if (notOverlaps(other)) {
            return null;
        }

        return new DateRange(
                start.isBefore(other.start) ? start : other.start,
                end.isAfter(other.end) ? end : other.end
        );
    }

    public boolean inside(DateRange other) {
        return other != null && !start.isBefore(other.start) && !end.isAfter(other.end);
    }

    public boolean contains(LocalDate date) {
        return date != null && !start.isAfter(date) && !end.isBefore(date);
    }

    public static DateRange leastCoveringRange(List<DateRange> ranges) {
        Comparator<LocalDate> dateComparator = (date1, date2) -> {
            if (date1.equals(date2)) {
                return 0;
            }

            return date1.isBefore(date2) ? -1 : 1;
        };

        Optional<LocalDate> leftBound =
                ranges.stream()
                        .filter(Objects::nonNull)
                        .map(DateRange::getStart)
                        .min(dateComparator);

        Optional<LocalDate> rightBound =
                ranges.stream()
                        .filter(Objects::nonNull)
                        .map(DateRange::getEnd)
                        .max(dateComparator);

        if (!leftBound.isPresent() || !rightBound.isPresent()) {
            return null;
        }

        return new DateRange(leftBound.get(), rightBound.get());
    }
}
