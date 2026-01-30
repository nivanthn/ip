package biscuit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Storage}.
 */
public class StorageTest {

    private static final Path DATA_PATH = Paths.get("data", "biscuit.txt");

    private byte[] backup;
    private boolean hadOriginalFile;

    @BeforeEach
    public void setUp() throws Exception {
        if (Files.exists(DATA_PATH)) {
            hadOriginalFile = true;
            backup = Files.readAllBytes(DATA_PATH);
        } else {
            hadOriginalFile = false;
        }

        Files.createDirectories(DATA_PATH.getParent());
        Files.deleteIfExists(DATA_PATH);
    }

    @AfterEach
    public void tearDown() throws Exception {
        Files.deleteIfExists(DATA_PATH);

        if (hadOriginalFile) {
            Files.createDirectories(DATA_PATH.getParent());
            Files.write(DATA_PATH, backup);
        }
    }

    @Test
    public void loadFileDoesNotExistReturnsEmptyList() throws Exception {
        Files.deleteIfExists(DATA_PATH);

        Storage storage = new Storage();
        List<Task> loaded = storage.load();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    public void saveThenLoadRoundTripPreservesTypesAndFields() throws Exception {
        Storage storage = new Storage();

        Todo t = new Todo("read book");
        Deadline d = new Deadline("return book", LocalDate.of(2026, 2, 6));
        Event e = new Event("project meeting",
                LocalDateTime.of(2026, 8, 6, 14, 0),
                LocalDateTime.of(2026, 8, 6, 16, 0));

        t.mark(); // ensure done flag persists

        storage.save(List.of(t, d, e));

        assertTrue(Files.exists(DATA_PATH));

        String fileContent = Files.readString(DATA_PATH, StandardCharsets.UTF_8);

        // exact tab-separated expectations
        assertTrue(fileContent.contains("T\t1\tread book"));
        assertTrue(fileContent.contains("D\t0\treturn book\t2026-02-06"));
        assertTrue(fileContent.contains("E\t0\tproject meeting\t2026-08-06 14:00\t2026-08-06 16:00"));

        List<Task> loaded = storage.load();
        assertEquals(3, loaded.size());

        assertTrue(loaded.get(0) instanceof Todo);
        assertTrue(loaded.get(1) instanceof Deadline);
        assertTrue(loaded.get(2) instanceof Event);

        assertEquals("read book", loaded.get(0).getDescription());
        assertTrue(loaded.get(0).isDone());

        Deadline loadedDeadline = (Deadline) loaded.get(1);
        assertEquals("return book", loadedDeadline.getDescription());
        assertEquals(LocalDate.of(2026, 2, 6), loadedDeadline.getBy());

        Event loadedEvent = (Event) loaded.get(2);
        assertEquals("project meeting", loadedEvent.getDescription());
        assertEquals(LocalDateTime.of(2026, 8, 6, 14, 0), loadedEvent.getFrom());
        assertEquals(LocalDateTime.of(2026, 8, 6, 16, 0), loadedEvent.getTo());
    }

    @Test
    public void loadCorruptedLineThrowsBiscuitException() throws Exception {
        Files.createDirectories(DATA_PATH.getParent());
        // not enough fields for even a Todo
        Files.writeString(DATA_PATH, "T\t1\n", StandardCharsets.UTF_8);

        Storage storage = new Storage();
        assertThrows(BiscuitException.class, storage::load);
    }

    @Test
    public void load_invalidDoneFlag_throwsBiscuitException() throws Exception {
        Files.createDirectories(DATA_PATH.getParent());
        Files.writeString(DATA_PATH, "T\t9\tread book\n", StandardCharsets.UTF_8);

        Storage storage = new Storage();
        assertThrows(BiscuitException.class, storage::load);
    }
}
