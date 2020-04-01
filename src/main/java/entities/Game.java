package entities;

import entities.players.Player;
import exceptions.InvalidDurationOfGameException;
import exceptions.InvalidSizeOfArithmeticProgressionException;
import exceptions.InvalidTimeException;
import exceptions.PlayerNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Arithmetic progression game
 * (positional game)
 *
 * @author Ioan Sava
 * @see <a href="https://en.wikipedia.org/wiki/Positional_game">https://en.wikipedia.org/wiki/Positional_game</a>
 */
@NoArgsConstructor
@Getter
public class Game {
    private Board board;

    /**
     * Duration of the game in minutes.
     */
    private int durationOfTheGame;

    /**
     * Each player extracts tokens successively from the board
     * and must create with them a a complete
     * arithmetic progression of a given size.
     */
    private int sizeOfArithmeticProgression;

    /**
     * The order number of the player who will choose a token.
     */
    private int currentTurn;

    /**
     * The list of players who joined the game
     */
    private List<Player> listOfPlayers = new ArrayList<>();

    /**
     * The list of the tokens held by each player
     */
    private List<Set<Token>> playersTokens = new ArrayList<>();

    /**
     * This daemon thread will display the running time
     * of the game and it will stop the game if it exceeds a certain time limit
     */
    TimeKeeper timeKeeper;

    public Game(Board board, int sizeOfArithmeticProgression, int durationOfTheGame) throws
            InvalidSizeOfArithmeticProgressionException, InvalidDurationOfGameException, InvalidTimeException {
        this.board = board;
        setSizeOfArithmeticProgression(sizeOfArithmeticProgression);
        setDurationOfTheGame(durationOfTheGame);
        timeKeeper = new TimeKeeper(durationOfTheGame);
        timeKeeper.setDaemon(true);
    }

    private void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    private void setSizeOfArithmeticProgression(int sizeOfArithmeticProgression) throws
            InvalidSizeOfArithmeticProgressionException {
        if (sizeOfArithmeticProgression < 1) {
            throw new InvalidSizeOfArithmeticProgressionException("The size of an arithmetic progression should be at least 1");
        }
        this.sizeOfArithmeticProgression = sizeOfArithmeticProgression;
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

    private void welcomeMessage() {
        System.out.println("Welcome to Arithmetic progression game");
        System.out.println("Your goal is to be the first to achieve" +
                " an arithmetic progression of length " + sizeOfArithmeticProgression);
    }

    /**
     * Each player has his own thread
     */
    private void createThreadsForPlayers() {
        for (Player player : listOfPlayers) {
            new Thread(player).start();
        }
    }

    private void playerTurnMessage(int turn) {
        System.out.println(listOfPlayers.get(turn).getName() + "'s turn");
        System.out.println("Current board: " + getBoard());
        if (getPlayersTokens().size() > turn) {
            System.out.println("Your tokens: " + getPlayersTokens().get(turn));
        }
    }

    /**
     * Return the longest arithmetic progression size for
     * a given list of integers using a Dynamic Programming approach.
     */
    private int longestArithmeticProgression(List<Integer> numbers) {
        if (numbers == null) {
            return 0;
        } else if (numbers.size() < 3) {
            return numbers.size();
        }

        Map<Integer, Integer>[] matrix = new HashMap[numbers.size()];
        int result = 2;
        for (int i = 0; i < numbers.size(); ++i) {
            matrix[i] = new HashMap<>();
            for (int j = 0; j < i; ++j) {
                int difference = numbers.get(i) - numbers.get(j);
                matrix[i].put(difference, matrix[j].getOrDefault(difference, 1) + 1);
                result = Math.max(result, matrix[i].get(difference));
            }
        }

        return result;
    }

    /**
     * Check if a list of tokens contains a blank token
     * (a wildcard).
     */
    private boolean containsBlankToken(Set<Token> tokens) {
        for (Token token : tokens) {
            if (token.getValue() == 0) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> convertSetOfTokensToArray(Set<Token> tokens) {
        List<Integer> numbers = new ArrayList<>();
        for (Token token : tokens) {
            if (token.getValue() != 0) {
                numbers.add(token.getValue());
            }
        }
        return numbers;
    }

    /**
     * A player receives a number of points equal
     * to the their largest arithmetic progression.
     */
    private int computePlayerScore(int index) {
        int bonus = 0;
        if (containsBlankToken(playersTokens.get(index))) {
            bonus = 1;
        }

        return bonus + longestArithmeticProgression(convertSetOfTokensToArray(playersTokens.get(index)));
    }

    /**
     * Shows the score of each player.
     */
    private void showRanking() {
        System.out.println("Scores: ");
        for (int i = 0; i < listOfPlayers.size(); ++i) {
            int playerScore = computePlayerScore(i);
            System.out.println(listOfPlayers.get(i).getName() + ": " + playerScore + " points");
        }
    }

    /**
     * The game ends when either a player makes a complete
     * arithmetic progression or when all tokens have been removed from the board.
     * Also, the game ends if it exceeds a certain time limit.
     *
     * @return true if the game is over
     * false, otherwise
     */
    public boolean gameHasEnded() {
        if (board.getTokens().size() == 0 || !timeKeeper.isAlive()) {
            System.out.println("Game over");
            showRanking();
            return true;
        } else {
            int playerScore = computePlayerScore(getCurrentTurn());
            if (playerScore >= sizeOfArithmeticProgression) {
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
        if (gameHasEnded()) {
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
