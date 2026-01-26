/**
 * A checked exception for all user-facing errors in Biscuit.
 *
 * Throw this whenever the user's input is invalid (unknown command, bad index,
 * wrong date/time format, etc.).
 */
public class BiscuitException extends Exception {
    
    /**
     * Creates a {@code BiscuitException} with the given message.
     *
     * @param message Error message to be shown to the user.
     */
    public BiscuitException(String message) {
        super(message);
    }
}
