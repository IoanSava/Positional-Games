package entities.players;

import entities.tokens.Token;
import exceptions.InvalidTokenValueException;

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
        return null;
    }
}
