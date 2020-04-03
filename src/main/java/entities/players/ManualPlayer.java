package entities.players;

import entities.tokens.Token;

import java.util.Iterator;
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
    protected Token chooseToken() {
        Scanner scanner = new Scanner(System.in);
        int tokenIndex = scanner.nextInt();
        while (tokenIndex < 0 || tokenIndex >= getGame().getBoard().getTokens().size()) {
            System.out.println("Invalid token. Choose another one");
            tokenIndex = scanner.nextInt();
        }

        Iterator<Token> iterator = getGame().getBoard().getTokens().iterator();
        for (int i = 0; i < tokenIndex; ++i) {
            iterator.next();
        }
        return iterator.next();
    }
}
