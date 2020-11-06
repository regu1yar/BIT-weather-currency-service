package bit.wcservice;

import bit.wcservice.DateRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
        assertEquals(union1.getStart(), today.minusDays(2));
        assertEquals(union1.getEnd(), today.plusDays(2));
        assertEquals(union1.getStart(), union2.getStart());
        assertEquals(union1.getEnd(), union2.getEnd());
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
        assertEquals(union1.getStart(), today.minusDays(2));
        assertEquals(union1.getEnd(), today.plusDays(2));
        assertEquals(union1.getStart(), union2.getStart());
        assertEquals(union1.getEnd(), union2.getEnd());
    }

    @Test
    void unionWithSubrange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(2));
        DateRange range2 = new DateRange(today, today.plusDays(1));
        DateRange union1 = range1.unionToRange(range2);
        DateRange union2 = range2.unionToRange(range1);
        assertNotNull(union1);
        assertNotNull(union2);
        assertEquals(union1.getStart(), range1.getStart());
        assertEquals(union1.getEnd(), range1.getEnd());
        assertEquals(union1.getStart(), union2.getStart());
        assertEquals(union1.getEnd(), union2.getEnd());
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
        DateRange leastCoveringRange = DateRange.leastCoveringRange(List.of(range1, range2));
        assertNotNull(leastCoveringRange);
        assertEquals(leastCoveringRange.getStart(), today.minusDays(2));
        assertEquals(leastCoveringRange.getEnd(), today.plusDays(2));
    }

    @Test
    void leastCoveringRangeOfOverlappingRanges() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(1));
        DateRange range2 = new DateRange(today.minusDays(1), today.plusDays(2));
        DateRange leastCoveringRange = DateRange.leastCoveringRange(List.of(range1, range2));
        assertNotNull(leastCoveringRange);
        assertEquals(leastCoveringRange.getStart(), today.minusDays(2));
        assertEquals(leastCoveringRange.getEnd(), today.plusDays(2));
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
        DateRange leastCoveringRange = DateRange.leastCoveringRange(List.of(range1, range2));
        assertNotNull(leastCoveringRange);
        assertEquals(leastCoveringRange.getStart(), today.minusDays(2));
        assertEquals(leastCoveringRange.getEnd(), today.plusDays(2));
    }

    @Test
    void leastCoveringRangeWithPoint() {
        DateRange range1 = new DateRange(today.minusDays(2), today);
        DateRange range2 = new DateRange(today.plusDays(2), today.plusDays(2));
        DateRange leastCoveringRange = DateRange.leastCoveringRange(List.of(range1, range2));
        assertNotNull(leastCoveringRange);
        assertEquals(leastCoveringRange.getStart(), today.minusDays(2));
        assertEquals(leastCoveringRange.getEnd(), today.plusDays(2));
    }

    @Test
    void leastCoveringRangeWithSubrange() {
        DateRange range1 = new DateRange(today.minusDays(2), today.plusDays(2));
        DateRange range2 = new DateRange(today, today.plusDays(1));
        DateRange leastCoveringRange = DateRange.leastCoveringRange(List.of(range1, range2));
        assertNotNull(leastCoveringRange);
        assertEquals(leastCoveringRange.getStart(), today.minusDays(2));
        assertEquals(leastCoveringRange.getEnd(), today.plusDays(2));
    }
}