package entities.games;

import entities.Board;
import entities.TimeKeeper;
import entities.players.Player;
import entities.tokens.Token;
import exceptions.InvalidDurationOfGameException;
import exceptions.InvalidTimeException;
import exceptions.PlayerNotFoundException;
import lombok.Getter;

import java.util.*;

/**
 * Generic type of positional game
 *
 * @author Ioan Sava
 * @see <a href="https://en.wikipedia.org/wiki/Positional_game">https://en.wikipedia.org/wiki/Positional_game</a>
 */
@Getter
public abstract class Game {
    protected Board board;

    /**
     * Duration of the game in minutes.
     */
    protected int durationOfTheGame;

    /**
     * The order number of the player who will choose a token.
     */
    protected int currentTurn;

    /**
     * The list of players who joined the game
     */
    protected List<Player> listOfPlayers = new ArrayList<>();

    /**
     * The list of the tokens held by each player
     */
    protected List<Set<Token>> playersTokens = new ArrayList<>();

    /**
     * This daemon thread will display the running time
     * of the game and it will stop the game if it exceeds a certain time limit
     */
    protected TimeKeeper timeKeeper;

    public Game(Board board, int durationOfTheGame) throws
            InvalidDurationOfGameException, InvalidTimeException {
        this.board = board;
        setDurationOfTheGame(durationOfTheGame);
        timeKeeper = new TimeKeeper(durationOfTheGame);
        timeKeeper.setDaemon(true);
    }

    private void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    private void setDurationOfTheGame(int durationOfTheGame) throws InvalidDurationOfGameException {
        if (durationOfTheGame < 1) {
            throw new InvalidDurationOfGameException("A game should last at least 1 minute");
        }
        this.durationOfTheGame = durationOfTheGame;
    }


    public void addPlayers(Player... players) {
        for (Player player : players) {
            if (!listOfPlayers.contains(player)) {
                listOfPlayers.add(player);
                player.setGame(this);
                playersTokens.add(new TreeSet<>());
            }
        }
    }

    public void removePlayer(Player player) throws PlayerNotFoundException {
        if (!listOfPlayers.contains(player)) {
            throw new PlayerNotFoundException(player.getName());
        } else {
            listOfPlayers.remove(player);
            player.setGame(null);
        }
    }

    public void addTokenToPlayer(Player player, Token token) {
        int indexOfPlayer = listOfPlayers.indexOf(player);
        playersTokens.get(indexOfPlayer).add(token);
    }

    abstract protected void welcomeMessage();

    /**
     * Each player has his own thread
     */
    private void createThreadsForPlayers() {
        for (Player player : listOfPlayers) {
            new Thread(player).start();
        }
    }

    private void playerTurnMessage(int turn) {
        System.out.println("---------------------------------------------------------------");
        System.out.println(listOfPlayers.get(turn).getName() + "'s turn");
        System.out.println(getBoard());
        if (getPlayersTokens().size() > turn) {
            System.out.println("Your tokens: " + getPlayersTokens().get(turn));
        }
    }

    abstract protected int computePlayerScore(int index);

    /**
     * Shows the score of each player.
     */
    protected void showRanking() {
        System.out.println("Scores: ");
        for (int i = 0; i < listOfPlayers.size(); ++i) {
            int playerScore = computePlayerScore(i);
            System.out.println(listOfPlayers.get(i).getName() + ": " + playerScore + " points");
        }
    }

    /**
     * Objective of the game
     */
    protected abstract int getObjective();

    /**
     * The game ends when either a player achieves the game objective
     * or when all tokens have been removed from the board.
     * Also, the game ends if it exceeds a certain time limit.
     *
     * @return true if the game is over
     * false, otherwise
     */
    protected boolean gameOver() {
        if (board.getTokens().size() == 0 || !timeKeeper.isAlive()) {
            System.out.println("---------------------------------------------------------------");
            System.out.println("Game over");
            showRanking();
            return true;
        } else {
            int playerScore = computePlayerScore(getCurrentTurn());
            if (playerScore >= getObjective()) {
                System.out.println("---------------------------------------------------------------");
                System.out.println(listOfPlayers.get(getCurrentTurn()).getName() + " won");
                System.out.println("Game has ended");
                return true;
            }
        }
        return false;
    }

    /**
     * If the game is not over,
     * the next player will have the turn.
     * Otherwise, no player can make any move.
     */
    public void update() {
        if (gameOver()) {
            setCurrentTurn(-1);
        } else {
            int nextTurn = (getCurrentTurn() + 1) % getListOfPlayers().size();
            setCurrentTurn(nextTurn);
            playerTurnMessage(getCurrentTurn());
        }
    }

    /**
     * The player who will have
     * the first move is chosen at random
     */
    private int generateRandomTurn() {
        Random random = new Random();
        return random.nextInt(listOfPlayers.size());
    }

    /**
     * In order to start, the game needs at least two players.
     * A few settings are required before the game starts:
     * 1. which player will have the first turn
     * 2. threads for each player
     * 3. messages regarding rules
     * 4. start timeKeeper
     */
    public void start() {
        if (listOfPlayers.size() >= 2) {
            welcomeMessage();
            setCurrentTurn(generateRandomTurn());
            playerTurnMessage(getCurrentTurn());
            createThreadsForPlayers();
            timeKeeper.start();
        } else {
            System.out.println("The game needs at least two players in order to start");
        }
    }
}
