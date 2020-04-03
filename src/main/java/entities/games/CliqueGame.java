package entities.games;

import entities.Board;
import entities.tokens.Token;
import entities.tokens.clique_token.CliqueGameToken;
import entities.tokens.clique_token.Node;
import exceptions.InvalidCliqueSizeException;
import exceptions.InvalidDurationOfGameException;
import exceptions.InvalidTimeException;
import org.jgrapht.Graph;
import org.jgrapht.alg.BronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The clique game is a positional game
 * where some players alternately pick edges,
 * trying to occupy a complete clique of a given size.
 *
 * @author Ioan Sava
 * @see <a href="https://en.wikipedia.org/wiki/Clique_game">https://en.wikipedia.org/wiki/Clique_game</a>
 * @see <a href="https://en.wikipedia.org/wiki/Clique_(graph_theory)">https://en.wikipedia.org/wiki/Clique_(graph_theory)</a>
 */
public class CliqueGame extends Game {
    /**
     * Each player extracts tokens successively from the board
     * and must create with them a clique
     * of a given size.
     */
    private int sizeOfClique;

    /**
     * Every player has his own graph
     * formed with his tokens.
     */
    private List<Graph<Node, DefaultEdge>> graphs = new ArrayList<>();

    public CliqueGame(Board board, int durationOfTheGame, int sizeOfClique) throws
            InvalidDurationOfGameException, InvalidTimeException, InvalidCliqueSizeException {
        super(board, durationOfTheGame);
        setSizeOfClique(sizeOfClique);
    }

    private void setSizeOfClique(int sizeOfClique) throws InvalidCliqueSizeException {
        if (sizeOfClique < 2) {
            throw new InvalidCliqueSizeException("A clique should have a size of at least 2");
        }
        this.sizeOfClique = sizeOfClique;
    }

    @Override
    protected void welcomeMessage() {
        System.out.println("Welcome to Clique game");
        System.out.println("Your goal is to be the first to achieve" +
                " a clique of length " + sizeOfClique);
    }

    private Token lastTokenOfPlayer(int index) {
        Token token = null;
        for (Token currentToken : playersTokens.get(index)) {
            token = currentToken;
        }
        return token;
    }

    /**
     * Include the last token picked by the player
     * to his graph.
     */
    private void updatePlayerGraph(int index) {
        while (graphs.size() <= index) {
            graphs.add(new SimpleGraph<>(DefaultEdge.class));
        }

        Token lastTokenPlayer = lastTokenOfPlayer(index);
        Node firstNode = ((CliqueGameToken) lastTokenPlayer).getFirstNode();
        Node secondNode = ((CliqueGameToken) lastTokenPlayer).getSecondNode();
        graphs.get(index).addVertex(firstNode);
        graphs.get(index).addVertex(secondNode);
        graphs.get(index).addEdge(firstNode, secondNode);
    }

    /**
     * A player receives a number of points equal
     * to the their largest clique size.
     */
    @Override
    protected int computePlayerScore(int index) {
        updatePlayerGraph(index);
        BronKerboschCliqueFinder<Node, DefaultEdge> cliqueFinder = new BronKerboschCliqueFinder<>(graphs.get(index));
        Collection<Set<Node>> cliques = cliqueFinder.getBiggestMaximalCliques();
        return cliques.iterator().next().size();
    }

    @Override
    protected int getObjective() {
        return sizeOfClique;
    }
}
