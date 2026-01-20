public enum Command {
    ADD("add"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    BYE("bye");

    private final String text;

    Command(String text) {
        this.text = text;
    }

    public static Command parse(String raw) throws BiscuitException {
        if (raw == null) {
            throw new BiscuitException("Not a valid command");
        }
        String s = raw.trim().toLowerCase();
        for (Command c : Command.values()) {
            if (c.text.equals(s)) {
                return c;
            }
        }
        throw new BiscuitException("Not a valid command");
    }
}
