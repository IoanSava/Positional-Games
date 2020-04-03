package entities.tokens;

/**
 * Generic type of token of a positional game
 *
 * @author Ioan Sava
 */
public abstract class Token implements Comparable<Token> {
    abstract public int compareTo(Token token);
}
