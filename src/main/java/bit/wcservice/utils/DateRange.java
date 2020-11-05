package bit.wcservice.utils;

import java.time.LocalDate;
import java.util.*;

public class DateRange {
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

    public boolean notOverlaps(DateRange other) {
        return other == null || (end.isBefore(other.start) || start.isAfter(other.end));
    }

    public DateRange cross(DateRange other) {
        if (other == null) {
            return null;
        }

        if (notOverlaps(other)) {
            return null;
        }

        return new DateRange(
                start.isBefore(other.start) ? other.start : start,
                end.isAfter(other.end) ? other.end : end
        );
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

    public List<DateRange> subtract(DateRange other) {
        List<DateRange> subtraction = new ArrayList<>();

        if (notOverlaps(other)) {
            subtraction.add(this);
            return subtraction;
        }

        if (start.isBefore(other.start)) {
            subtraction.add(new DateRange(start, other.start.minusDays(1)));
        }

        if (end.isAfter(other.end)) {
            subtraction.add(new DateRange(other.end.plusDays(1), end));
        }

        return subtraction;
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

        if (leftBound.isEmpty() || rightBound.isEmpty()) {
            return null;
        }

        return new DateRange(leftBound.get(), rightBound.get());
    }
}
