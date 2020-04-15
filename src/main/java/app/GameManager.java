package app;

import entities.Board;
import entities.games.ArithmeticProgressionGame;
import entities.games.CliqueGame;
import entities.games.Game;
import entities.players.ManualPlayer;
import entities.tokens.ArithmeticProgressionToken;
import entities.players.Player;
import entities.players.RandomPlayer;
import entities.tokens.Token;
import entities.tokens.clique_token.CliqueGameToken;
import exceptions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Positional games application
 *
 * @author Ioan Sava
 */
public class GameManager {
    private final int NUMBER_OF_TOKENS = 10;
    private final int MAXIMUM_VALUE_OF_TOKEN = 15;
    private final int NUMBER_OF_PLAYERS = 3;
    private final int SIZE_OF_ARITHMETIC_PROGRESSION = 4;
    private final int DURATION_OF_GAME = 1;
    private final int NUMBER_OF_NODES = 8;
    private final int SIZE_OF_CLIQUE = 3;

    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        int typeOfGame = gameManager.chooseGame();
        if (typeOfGame == 1) {
            gameManager.playArithmeticProgressionGame();
        } else {
            gameManager.playCliqueGame();
        }
    }

    public void showGameChooser() {
        System.out.println("1 - Arithmetic Progression Game");
        System.out.println("2 - Clique Game");
        System.out.println("Choose a game");
    }

    public int chooseGame() {
        showGameChooser();
        Scanner scanner = new Scanner(System.in);
        int typeOfGame = scanner.nextInt();
        while (typeOfGame != 1 && typeOfGame != 2) {
            System.out.println("Invalid game");
            showGameChooser();
            typeOfGame = scanner.nextInt();
        }
        return typeOfGame;
    }

    /**
     * Generate a random permutation of
     * the elements in the range [lowerBound, upperBound]
     */
    private List<Integer> generateRandomPermutation(int lowerBound, int upperBound) {
        List<Integer> permutation = IntStream.rangeClosed(lowerBound, upperBound)
                .boxed().collect(Collectors.toList());
        Collections.shuffle(permutation);
        return permutation;
    }

    /**
     * Generate a given number of tokens.
     * A token cannot have a value greater than
     * 'maximumValueOfToken'
     */
    public Set<Token> generateAPTokens(int numberOfTokens, int maximumValueOfToken)
            throws InvalidTokenValueException {
        Set<Token> tokens = new TreeSet<>();
        List<Integer> permutation = generateRandomPermutation(0, maximumValueOfToken);

        for (int i = 0; i < numberOfTokens; ++i) {
            tokens.add(new ArithmeticProgressionToken(permutation.get(i)));
        }

        return tokens;
    }

    /**
     * It generates tokens with all
     * the possibilities of edges between 'numberOfNodes' nodes
     */
    public Set<Token> generateCGTokens(int numberOfNodes) {
        Set<Token> tokens = new TreeSet<>();
        for (int i = 1; i < numberOfNodes; ++i) {
            for (int j = i + 1; j <= numberOfNodes; ++j) {

                tokens.add(new CliqueGameToken(i, j));
            }
        }

        return tokens;
    }

    public void addPlayersToGame(Game game, int numberOfPlayer) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 1; i <= numberOfPlayer; ++i) {
            System.out.print("Player " + i + " name: ");
            String name = scanner.nextLine();
            while (game.getListOfPlayers().contains(new ManualPlayer(name))) {
                System.out.println("Another player already took this name");
                System.out.print("Choose another name: ");
                name = scanner.nextLine();
            }
            game.addPlayers(new ManualPlayer(name));
        }
    }

    public void addMockPlayersToGame(Game game) {
        Player manualPlayer = new ManualPlayer("john");
        Player randomPlayer = new RandomPlayer("ioan");
        game.addPlayers(manualPlayer, randomPlayer);
    }

    public void playArithmeticProgressionGame() {
        try {
            Set<Token> tokens = generateAPTokens(NUMBER_OF_TOKENS, MAXIMUM_VALUE_OF_TOKEN);
            Board board = new Board(tokens);
            Game game = new ArithmeticProgressionGame(board, DURATION_OF_GAME, SIZE_OF_ARITHMETIC_PROGRESSION);
            addMockPlayersToGame(game);
            game.start();
        } catch (InvalidDurationOfGameException | InvalidTimeException | InvalidTokenValueException |
                InvalidSizeOfArithmeticProgressionException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void playCliqueGame() {
        try {
            Set<Token> tokens = generateCGTokens(NUMBER_OF_NODES);
            Board board = new Board(tokens);
            Game game = new CliqueGame(board, DURATION_OF_GAME, SIZE_OF_CLIQUE);
            addMockPlayersToGame(game);
            game.start();
        } catch (InvalidDurationOfGameException | InvalidCliqueSizeException | InvalidTimeException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
