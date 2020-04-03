package entities.players;

import entities.tokens.ArithmeticProgressionToken;
import entities.tokens.Token;
import exceptions.InvalidTokenValueException;

import java.util.Iterator;
import java.util.Random;

/**
 * A smart player should try to extend its
 * largest arithmetic progression,
 * while not allowing others to extend theirs.
 *
 * @author Ioan Sava
 */
public class SmartPlayer extends Player {
    public SmartPlayer(String name) {
        super(name);
    }

    /**
     * Smart strategy to chose a token
     */
    @Override
    protected Token chooseToken() throws InvalidTokenValueException {
        if (getGame().getBoard().getTokens().contains(new ArithmeticProgressionToken(0))) {
            return new ArithmeticProgressionToken(0);
        } else {
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
}
