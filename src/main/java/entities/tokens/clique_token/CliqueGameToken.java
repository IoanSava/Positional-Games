package entities.tokens;

import lombok.Getter;

/**
 * An instance of this class will hold
 * an edge of an undirected graph.
 *
 * @author Ioan Sava
 */
@Getter
public class CliqueGameToken extends Token {
    

    @Override
    public int compareTo(Token token) {
        return 0;
    }
}
