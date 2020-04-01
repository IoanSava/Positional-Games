package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

/**
 * At the beginning of the game
 * the board contains a given number
 * of tokens, each token having a
 * distinct value from 1 to m.
 * Also, a token may be blank,
 * meaning that it can take
 * the place of any value.
 *
 * @author Ioan Sava
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Board {
    private Set<Token> tokens = new HashSet<>();

    public void addTokens(Token... tokens) {
        this.tokens.addAll(Arrays.asList(tokens));
    }

    public void removeToken(Token token) {
        this.tokens.remove(token);
    }
}
