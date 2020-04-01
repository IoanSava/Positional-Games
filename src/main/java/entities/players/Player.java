package entities.players;

import entities.Game;
import entities.Token;
import exceptions.InvalidTokenValueException;
import lombok.*;

import java.util.Objects;

/**
 * Generic player of a positional game
 *
 * @author Ioan Sava
 */
@Getter
@Setter
@ToString
public abstract class Player implements Runnable {
    private String name;
    private Game game;

    public Player(String name) {
        this.name = name;
    }

    /**
     * Strategy for extracting a token
     */
    abstract protected Token chooseToken() throws InvalidTokenValueException;

    /**
     * The player will extract an existing token from
     * the board
     *
     * @throws InvalidTokenValueException in case of invalid token
     */
    protected void extractTokenFromBoard() throws InvalidTokenValueException {
        System.out.println("Choose a token ");
        Token token = chooseToken();

        while (!game.getBoard().getTokens().contains(token)) {
            System.out.println("There is no token with this value on board");
            System.out.println("Choose a token ");
            token = chooseToken();
        }

        System.out.println("Player " + getName() + " selected the following token: " + token);
        game.getBoard().removeToken(token);
        game.addTokenToPlayer(this, token);
    }

    /**
     * Wait-notify approach in order to
     * make sure that players wait their turns
     */
    protected void waitTurn(int playerIndex) {
        synchronized (game) {
            game.notifyAll();
            while (game.getCurrentTurn() != playerIndex && game.getCurrentTurn() != -1) {
                try {
                    game.wait();
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * The player will repeatedly extract one token from the board.
     */
    @Override
    public void run() {
        int playerIndex = game.getListOfPlayers().indexOf(this);
        boolean running = true;
        while (running) {
            waitTurn(playerIndex);

            if (game.getCurrentTurn() == -1) {
                running = false;
            } else {
                try {
                    extractTokenFromBoard();
                } catch (InvalidTokenValueException e) {
                    e.printStackTrace();
                }
                game.update();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
