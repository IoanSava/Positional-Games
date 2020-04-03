package entities;

import entities.tokens.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * At the beginning of the game
 * the board contains a given number
 * of tokens.
 *
 * @author Ioan Sava
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Board {
    private Set<Token> tokens = new HashSet<>();

    public void addTokens(Token... tokens) {
        this.tokens.addAll(Arrays.asList(tokens));
    }

    public void removeToken(Token token) {
        this.tokens.remove(token);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**************\n");
        stringBuilder.append("Board:\n");
        int counter = 0;
        for (Token token : tokens) {
            stringBuilder.append(counter).append(". ").append(token).append("   ");
            ++counter;
            if (counter % 4 == 0) {
                stringBuilder.append("\n");
            }
        }

        if (counter % 4 != 0) {
            stringBuilder.append("\n");
        }
        stringBuilder.append("**************\n");
        return stringBuilder.toString();
    }
}
