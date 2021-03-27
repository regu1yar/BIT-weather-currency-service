package bit.utils.datarange;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateIteratorTest {

    @Test
    void throwsExceptionIfLocalDateIteratorHasNotNext() {
        LocalDate today = LocalDate.now();
        LocalDateIterator iterator = new LocalDateIterator(today, today);
        assertDoesNotThrow(iterator::next);
        assertThrows(NoSuchElementException.class, iterator::next);
    }

}