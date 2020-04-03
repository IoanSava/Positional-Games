package entities.tokens.clique_token;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * A node of an undirected graph.
 *
 * @author Ioan Sava
 */
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Node {
    private int label;

    @Override
    public String toString() {
        return "N(" + label + ")";
    }
}
