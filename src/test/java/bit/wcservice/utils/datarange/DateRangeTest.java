package bit.wcservice.utils.datarange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeTest {
    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
    }

    @Test
    void notOverlapsWithNonOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.minusDays(1));
        DateRange range2 = new DateRange(today.plusDays(1), today.plusDays(2));
        assertTrue(range1.notOverlaps(range2));
        assertTrue(range2.notOverlaps(range1));
    }

    @Test
    void notOverlapsWithOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        DateRange range2 = new DateRange(today.minusDays(1), today.plusDays(2));
        assertFalse(range1.notOverlaps(range2));
        assertFalse(range2.notOverlaps(range1));
    }

    @Test
    void notOverlapsWithNullRange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        assertTrue(range1.notOverlaps(null));
    }

    @Test
    void rangesOverlapsByBoundaries() {
        DateRange range1 = new DateRange(today.minusDays(2), today);
        DateRange range2 = new DateRange(today, today.plusDays(2));
        assertFalse(range1.notOverlaps(range2));
        assertFalse(range2.notOverlaps(range1));
    }

    @Test
    void overlapsIfSubrange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(2));
        DateRange range2 = new DateRange(today, today.plusDays(1));
        assertFalse(range1.notOverlaps(range2));
        assertFalse(range2.notOverlaps(range1));
    }

    @Test
    void nullUnionRangeWhenNonOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.minusDays(1));
        DateRange range2 = new DateRange(today.plusDays(1), today.plusDays(2));
        assertNull(range1.unionToRange(range2));
        assertNull(range2.unionToRange(range1));
    }

    @Test
    void unionOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        DateRange range2 = new DateRange(today.minusDays(1), today.plusDays(2));
        DateRange union1 = range1.unionToRange(range2);
        DateRange union2 = range2.unionToRange(range1);
        assertNotNull(union1);
        assertNotNull(union2);
        assertEquals(today.minusDays(2), union1.getStart());
        assertEquals(today.plusDays(2), union1.getEnd());
        assertEquals(union2.getStart(), union1.getStart());
        assertEquals(union2.getEnd(), union1.getEnd());
    }

    @Test
    void nullUnionWithNullRange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        assertNull(range1.unionToRange(null));
    }

    @Test
    void unionTouchingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today);
        DateRange range2 = new DateRange(today, today.plusDays(2));
        DateRange union1 = range1.unionToRange(range2);
        DateRange union2 = range2.unionToRange(range1);
        assertNotNull(union1);
        assertNotNull(union2);
        assertEquals(today.minusDays(2), union1.getStart());
        assertEquals(today.plusDays(2), union1.getEnd());
        assertEquals(union2.getStart(), union1.getStart());
        assertEquals(union2.getEnd(), union1.getEnd());
    }

    @Test
    void unionWithSubrange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(2));
        DateRange range2 = new DateRange(today, today.plusDays(1));
        DateRange union1 = range1.unionToRange(range2);
        DateRange union2 = range2.unionToRange(range1);
        assertNotNull(union1);
        assertNotNull(union2);
        assertEquals(range1.getStart(), union1.getStart());
        assertEquals(range1.getEnd(), union1.getEnd());
        assertEquals(union2.getStart(), union1.getStart());
        assertEquals(union2.getEnd(), union1.getEnd());
    }

    @Test
    void insideWithNonOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.minusDays(1));
        DateRange range2 = new DateRange(today.plusDays(1), today.plusDays(2));
        assertFalse(range1.inside(range2));
        assertFalse(range2.inside(range1));
    }

    @Test
    void insideWithOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        DateRange range2 = new DateRange(today.minusDays(1), today.plusDays(2));
        assertFalse(range1.inside(range2));
        assertFalse(range2.inside(range1));
    }

    @Test
    void insideWithNullRange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        assertFalse(range1.inside(null));
    }

    @Test
    void pointInsideRange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(2));
        DateRange range2 = new DateRange(today, today);
        assertFalse(range1.inside(range2));
        assertTrue(range2.inside(range1));
    }

    @Test
    void subrangeInsideRange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(2));
        DateRange range2 = new DateRange(today, today.plusDays(1));
        assertFalse(range1.inside(range2));
        assertTrue(range2.inside(range1));
    }

    @Test
    void leastCoveringRangeOfNonOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.minusDays(1));
        DateRange range2 = new DateRange(today.plusDays(1), today.plusDays(2));
        List<DateRange> ranges = new ArrayList<>();
        ranges.add(range1);
        ranges.add(range2);
        DateRange leastCoveringRange = DateRange.leastCoveringRange(ranges);
        assertNotNull(leastCoveringRange);
        assertEquals(today.minusDays(2), leastCoveringRange.getStart());
        assertEquals(today.plusDays(2), leastCoveringRange.getEnd());
    }

    @Test
    void leastCoveringRangeOfOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        DateRange range2 = new DateRange(today.minusDays(1), today.plusDays(2));
        List<DateRange> ranges = new ArrayList<>();
        ranges.add(range1);
        ranges.add(range2);
        DateRange leastCoveringRange = DateRange.leastCoveringRange(ranges);
        assertNotNull(leastCoveringRange);
        assertEquals(today.minusDays(2), leastCoveringRange.getStart());
        assertEquals(today.plusDays(2), leastCoveringRange.getEnd());
    }

    @Test
    void leastCoveringRangeOfEmptyListIsNull() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        DateRange leastCoveringRange = DateRange.leastCoveringRange(Collections.emptyList());
        assertNull(leastCoveringRange);
    }

    @Test
    void leastCoveringRangeOfTouchingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today);
        DateRange range2 = new DateRange(today, today.plusDays(2));
        List<DateRange> ranges = new ArrayList<>();
        ranges.add(range1);
        ranges.add(range2);
        DateRange leastCoveringRange = DateRange.leastCoveringRange(ranges);
        assertNotNull(leastCoveringRange);
        assertEquals(today.minusDays(2), leastCoveringRange.getStart());
        assertEquals(today.plusDays(2), leastCoveringRange.getEnd());
    }

    @Test
    void leastCoveringRangeWithPoint() {
        DateRange range1 = new DateRange(today.minusDays(2), today);
        DateRange range2 = new DateRange(today.plusDays(2), today.plusDays(2));
        List<DateRange> ranges = new ArrayList<>();
        ranges.add(range1);
        ranges.add(range2);
        DateRange leastCoveringRange = DateRange.leastCoveringRange(ranges);
        assertNotNull(leastCoveringRange);
        assertEquals(today.minusDays(2), leastCoveringRange.getStart());
        assertEquals(today.plusDays(2), leastCoveringRange.getEnd());
    }

    @Test
    void leastCoveringRangeWithSubrange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(2));
        DateRange range2 = new DateRange(today, today.plusDays(1));
        List<DateRange> ranges = new ArrayList<>();
        ranges.add(range1);
        ranges.add(range2);
        DateRange leastCoveringRange = DateRange.leastCoveringRange(ranges);
        assertNotNull(leastCoveringRange);
        assertEquals(today.minusDays(2), leastCoveringRange.getStart());
        assertEquals(today.plusDays(2), leastCoveringRange.getEnd());
    }

    @Test
    void iterationTest() {
        int rangeLength = 10;
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(rangeLength);
        DateRange range = new DateRange(startDate, today);
        for (LocalDate date : range) {
            assertEquals(today.minusDays(rangeLength), date);
            --rangeLength;
        }

        assertEquals(rangeLength, -1);
    }

    @Test
    void streamToListCollectCorrectSize() {
        int rangeLength = 10;
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(rangeLength);
        DateRange range = new DateRange(startDate, today);
        List<LocalDate> dateRange = StreamSupport.stream(range.spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(rangeLength + 1, dateRange.size());
    }

    @Test
    void testCoveringRange() {
        LocalDate today = LocalDate.now();
        Set<LocalDate> datesSet = new HashSet<>();
        datesSet.add(today);
        datesSet.add(today.minusDays(3));
        datesSet.add(today.plusDays(4));
        DateRange coveringRange = DateRange.coveringRange(datesSet);

        DateRange correctRange = new DateRange(today.minusDays(3), today.plusDays(4));

        for (LocalDate date : correctRange) {
            coveringRange.contains(date);
        }

        for (LocalDate date : coveringRange) {
            correctRange.contains(date);
        }
    }
}