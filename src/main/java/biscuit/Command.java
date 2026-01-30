package biscuit;

/**
 * Represents a user command that can be executed by Biscuit.
 */
public enum Command {
    ADD("add"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    BYE("bye");

    private static final String MESSAGE_INVALID_COMMAND = "Not a valid command";

    private final String text;

    private Command(String text) {
        this.text = text;
    }

    /**
     * Returns the corresponding {@code Command} for the given raw user input.
     *
     * @param raw Raw command text entered by the user.
     * @return Matching {@code Command}.
     * @throws BiscuitException If the input does not map to a known command.
     */
    public static Command parse(String raw) throws BiscuitException {
        if (raw == null) {
            throw new BiscuitException(MESSAGE_INVALID_COMMAND);
        }

        String trimmed = raw.trim().toLowerCase();
        for (Command command : Command.values()) {
            if (command.text.equals(trimmed)) {
                return command;
            }
        }
        throw new BiscuitException(MESSAGE_INVALID_COMMAND);
    }
}
