package entities.tokens.clique_token;

import entities.tokens.ArithmeticProgressionToken;
import entities.tokens.Token;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.DefaultEdge;

/**
 * An instance of this class will hold
 * two nodes which form
 * an edge of an undirected graph.
 *
 * @author Ioan Sava
 */
@Getter
public class CliqueGameToken extends Token {
    private Node firstNode;
    private Node secondNode;

    public CliqueGameToken(Node firstNode, Node secondNode) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
    }

    public CliqueGameToken(int firstNodeLabel, int secondNodeLabel) {
        this.firstNode = new Node(firstNodeLabel);
        this.secondNode = new Node(secondNodeLabel);
    }

    public int compareTo(CliqueGameToken token) {
        if (this.firstNode.getLabel() == token.getFirstNode().getLabel()) {
            return this.secondNode.getLabel() - token.getSecondNode().getLabel();
        }
        return this.firstNode.getLabel() - token.getFirstNode().getLabel();
    }

    @Override
    public int compareTo(Token token) {
        return compareTo((CliqueGameToken) token);
    }

    @Override
    public String toString() {
        return "CGToken(" + firstNode + "," + secondNode + ')';
    }
}
