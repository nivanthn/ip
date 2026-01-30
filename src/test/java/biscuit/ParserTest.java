package biscuit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Parser}.
 */
public class ParserTest {

    @Test
    public void parseDeadlineDate_validFormat_returnsLocalDate() throws Exception {
        LocalDate date = Parser.parseDeadlineDate("2026-02-06");
        assertEquals(LocalDate.of(2026, 2, 6), date);
    }

    @Test
    public void parseDeadlineDate_invalidFormat_throwsBiscuitException() {
        assertThrows(BiscuitException.class, () -> Parser.parseDeadlineDate("06-02-2026"));
        assertThrows(BiscuitException.class, () -> Parser.parseDeadlineDate("2026/02/06"));
        assertThrows(BiscuitException.class, () -> Parser.parseDeadlineDate("not-a-date"));
    }

    @Test
    public void parseEventDateTime_validFormat_returnsLocalDateTime() throws Exception {
        LocalDateTime dt = Parser.parseEventDateTime("2026-01-21 19:00", "event start");
        assertEquals(LocalDateTime.of(2026, 1, 21, 19, 0), dt);
    }

    @Test
    public void parseEventDateTime_invalidFormat_throwsBiscuitException() {
        assertThrows(BiscuitException.class,
                () -> Parser.parseEventDateTime("2026-01-21", "event start"));
        assertThrows(BiscuitException.class,
                () -> Parser.parseEventDateTime("2026-01-21 7pm", "event start"));
        assertThrows(BiscuitException.class,
                () -> Parser.parseEventDateTime("bad", "event start"));
    }

    @Test
    public void requireNonEmpty_blank_throwsBiscuitException() {
        assertThrows(BiscuitException.class, () -> Parser.requireNonEmpty("", "msg"));
        assertThrows(BiscuitException.class, () -> Parser.requireNonEmpty("   ", "msg"));
    }

    @Test
    public void requireNonEmpty_nonBlank_returnsTrimmed() throws Exception {
        assertEquals("hello", Parser.requireNonEmpty("  hello  ", "msg"));
    }
}
