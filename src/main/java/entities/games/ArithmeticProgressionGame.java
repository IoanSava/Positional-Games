package entities.games;

import entities.Board;
import entities.tokens.ArithmeticProgressionToken;
import entities.tokens.Token;
import exceptions.InvalidDurationOfGameException;
import exceptions.InvalidSizeOfArithmeticProgressionException;
import exceptions.InvalidTimeException;

import java.util.*;

/**
 * Arithmetic progression game
 *
 * @author Ioan Sava
 * @see <a href="https://en.wikipedia.org/wiki/Arithmetic_progression_game">https://en.wikipedia.org/wiki/Arithmetic_progression_game</a>
 */
public class ArithmeticProgressionGame extends Game {
    /**
     * Each player extracts tokens successively from the board
     * and must create with them a complete
     * arithmetic progression of a given size.
     */
    private int sizeOfArithmeticProgression;

    public ArithmeticProgressionGame(Board board, int durationOfTheGame, int sizeOfArithmeticProgression) throws
            InvalidDurationOfGameException, InvalidTimeException, InvalidSizeOfArithmeticProgressionException {
        super(board, durationOfTheGame);
        setSizeOfArithmeticProgression(sizeOfArithmeticProgression);
    }

    private void setSizeOfArithmeticProgression(int sizeOfArithmeticProgression) throws
            InvalidSizeOfArithmeticProgressionException {
        if (sizeOfArithmeticProgression < 1) {
            throw new InvalidSizeOfArithmeticProgressionException("The size of an arithmetic progression should be at least 1");
        }
        this.sizeOfArithmeticProgression = sizeOfArithmeticProgression;
    }

    @Override
    protected int getObjective() {
        return sizeOfArithmeticProgression;
    }

    protected void welcomeMessage() {
        System.out.println("Welcome to Arithmetic progression game");
        System.out.println("Your goal is to be the first to achieve" +
                " an arithmetic progression of length " + sizeOfArithmeticProgression);
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

        List<Map<Integer, Integer>> differences = new ArrayList<>();
        int result = 2;
        for (int i = 0; i < numbers.size(); ++i) {
            differences.add(new HashMap<>());
            for (int j = 0; j < i; ++j) {
                int difference = numbers.get(i) - numbers.get(j);
                differences.get(i).put(difference, differences.get(j).getOrDefault(difference, 1) + 1);
                result = Math.max(result, differences.get(i).get(difference));
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
            if (((ArithmeticProgressionToken) token).getValue() == 0) {
                return true;
            }
        }
        return false;
    }

    protected List<Integer> convertSetOfTokensToList(Set<Token> tokens) {
        List<Integer> numbers = new ArrayList<>();
        for (Token token : tokens) {
            if (((ArithmeticProgressionToken) token).getValue() != 0) {
                numbers.add(((ArithmeticProgressionToken) token).getValue());
            }
        }
        return numbers;
    }

    /**
     * A player receives a number of points equal
     * to the their largest arithmetic progression.
     */
    protected int computePlayerScore(int index) {
        int bonus = 0;
        if (containsBlankToken(playersTokens.get(index))) {
            bonus = 1;
        }

        return bonus + longestArithmeticProgression(convertSetOfTokensToList(playersTokens.get(index)));
    }
}
