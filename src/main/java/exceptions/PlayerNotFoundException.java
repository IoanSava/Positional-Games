package exceptions;

public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException(String name) {
        super("Player not found: " + name);
    }
}
