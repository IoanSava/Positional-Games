package entities.players;

import entities.tokens.Token;

import java.util.Iterator;
import java.util.Random;

/**
 * This player will choose tokens
 * in a random way.
 *
 * @author Ioan Sava
 */
public class RandomPlayer extends Player {

    public RandomPlayer(String name) {
        super(name);
    }

    /**
     * Choose a random token from board.
     */
    @Override
    protected Token chooseToken() {
        int numberOfTokens = getGame().getBoard().getTokens().size();
        Random random = new Random();
        int randomToken = random.nextInt(numberOfTokens);
        Iterator<Token> iterator = getGame().getBoard().getTokens().iterator();
        for (int i = 0; i < randomToken; ++i) {
            iterator.next();
        }
        return iterator.next();
    }
}
