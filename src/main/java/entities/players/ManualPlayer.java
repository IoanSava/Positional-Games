package entities.players;

import entities.Token;
import exceptions.InvalidTokenValueException;

import java.util.Scanner;

/**
 * This player will choose tokens
 * manually.
 *
 * @author Ioan Sava
 */
public final class ManualPlayer extends Player {

    public ManualPlayer(String name) {
        super(name);
    }

    /**
     * The player will use the keyboard in order
     * to choose a token.
     */
    protected Token chooseToken() throws InvalidTokenValueException {
        Scanner scanner = new Scanner(System.in);
        int value = scanner.nextInt();
        return new Token(value);
    }
}
