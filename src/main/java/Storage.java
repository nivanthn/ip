import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes the task list to disk.
 */
public class Storage {

    private static final Path DATA_PATH = Paths.get("data", "biscuit.txt");

    /**
     * Loads tasks from disk.
     * If the data file (or its folder) does not exist, returns an empty list.
     *
     * @return List of loaded tasks.
     * @throws BiscuitException If the file exists but cannot be read/parsed.
     */
    public List<Task> load() throws BiscuitException {
        if (Files.notExists(DATA_PATH)) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                tasks.add(parseLine(line));
            }
        } catch (IOException e) {
            throw new BiscuitException("Failed to read data file: " + DATA_PATH);
        }

        return tasks;
    }

    /**
     * Saves tasks to disk. Creates the data folder if needed.
     *
     * @param tasks Tasks to save.
     * @throws BiscuitException If the data file cannot be written.
     */
    public void save(List<Task> tasks) throws BiscuitException {
        try (BufferedWriter writer = Files.newBufferedWriter(DATA_PATH, StandardCharsets.UTF_8)) {
            for (Task task : tasks) {
                writer.write(serializeTask(task));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new BiscuitException("Failed to save data file: " + DATA_PATH);
        }
    }

    private static Task parseLine(String line) throws BiscuitException {
        // Format (tab-separated):
        // T\t1\tdescription
        // D\t0\tdescription\tby
        // E\t0\tdescription\tfrom\tto
        String[] parts = line.split("   ", -1);
        if (parts.length < 3) {
            throw new BiscuitException("Corrupted data line: " + line);
        }

        String type = parts[0];
        boolean isDone = parseDoneFlag(parts[1], line);
        String description = unescape(parts[2]);

        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new BiscuitException("Corrupted deadline line: " + line);
            }
            task = new Deadline(description, unescape(parts[3]));
            break;
        case "E":
            if (parts.length < 5) {
                throw new BiscuitException("Corrupted event line: " + line);
            }
            task = new Event(description, unescape(parts[3]), unescape(parts[4]));
            break;
        default:
            throw new BiscuitException("Unknown task type in data: " + type);
        }

        if (isDone) {
            task.mark();
        }
        return task;
    }

    private static boolean parseDoneFlag(String raw, String line) throws BiscuitException {
        if (raw.equals("1")) {
            return true;
        }
        if (raw.equals("0")) {
            return false;
        }
        throw new BiscuitException("Invalid done flag in data line: " + line);
    }

    private static String serializeTask(Task task) throws BiscuitException {
        String done = task.isDone() ? "1" : "0";
        String description = escape(task.getDescription());

        if (task instanceof Todo) {
            return String.join("\t", "T", done, description);
        }
        if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            return String.join("\t", "D", done, description, escape(d.getBy()));
        }
        if (task instanceof Event) {
            Event e = (Event) task;
            return String.join("\t", "E", done, description, escape(e.getFrom()), escape(e.getTo()));
        }

        throw new BiscuitException("Unsupported task type: " + task.getClass().getSimpleName());
    }

    private static String escape(String s) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
            case '\\':
                out.append("\\\\");
                break;
            case '\t':
                out.append("\\t");
                break;
            case '\n':
                out.append("\\n");
                break;
            case '\r':
                out.append("\\r");
                break;
            default:
                out.append(c);
                break;
            }
        }
        return out.toString();
    }

    private static String unescape(String s) throws BiscuitException {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '\\') {
                out.append(c);
                continue;
            }

            if (i + 1 >= s.length()) {
                throw new BiscuitException("Invalid escape sequence in data: " + s);
            }

            char next = s.charAt(i + 1);
            switch (next) {
            case '\\':
                out.append('\\');
                break;
            case 't':
                out.append('\t');
                break;
            case 'n':
                out.append('\n');
                break;
            case 'r':
                out.append('\r');
                break;
            default:
                throw new BiscuitException("Invalid escape sequence in data: \\" + next);
            }
            i++;
        }
        return out.toString();
    }
}
