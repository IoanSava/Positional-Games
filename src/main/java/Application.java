import entities.Board;
import entities.Game;
import entities.players.ManualPlayer;
import entities.Token;
import entities.players.Player;
import entities.players.RandomPlayer;
import exceptions.InvalidDurationOfGameException;
import exceptions.InvalidSizeOfArithmeticProgressionException;
import exceptions.InvalidTimeException;
import exceptions.InvalidTokenValueException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Application {
    private static final int NUMBER_OF_TOKENS = 10;
    private static final int MAXIMUM_VALUE_OF_TOKEN = 15;
    private static final int NUMBER_OF_PLAYERS = 3;
    private static final int SIZE_OF_ARITHMETIC_PROGRESSION = 4;
    private static final int DURATION_OF_GAME = 1;

    public static void main(String[] args) {
        try {
            Set<Token> tokens = generateTokens(NUMBER_OF_TOKENS, MAXIMUM_VALUE_OF_TOKEN);
            Board board = new Board(tokens);
            Game game = new Game(board, SIZE_OF_ARITHMETIC_PROGRESSION, DURATION_OF_GAME);
            addMockPlayersToGame(game);
            game.start();
        } catch (InvalidDurationOfGameException | InvalidSizeOfArithmeticProgressionException | InvalidTokenValueException | InvalidTimeException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Generate a given number of tokens.
     * A token cannot have a value greater than
     * 'maximumValueOfToken'
     */
    public static Set<Token> generateTokens(int numberOfTokens, int maximumValueOfToken)
            throws InvalidTokenValueException {
        Set<Token> tokens = new TreeSet<>();

        List<Integer> listOfValues = IntStream.rangeClosed(0, maximumValueOfToken)
                .boxed().collect(Collectors.toList());
        Collections.shuffle(listOfValues);

        for (int i = 0; i < numberOfTokens; ++i) {
            tokens.add(new Token(listOfValues.get(i)));
        }

        return tokens;
    }

    public static void addPlayersToGame(Game game, int numberOfPlayer) {
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

    public static void addMockPlayersToGame(Game game) {
        Player manualPlayer = new ManualPlayer("john");
        Player randomPlayer = new RandomPlayer("ioan");
        game.addPlayers(manualPlayer, randomPlayer);
    }
}
