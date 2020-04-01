package entities;

import exceptions.InvalidTokenValueException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An instance of this class will hold a number
 * from 1 to m. A token may be blank (value is 0),
 * meaning that it can take the place of any value
 * (wildcard).
 *
 * @author Ioan Sava
 */
@Getter
@EqualsAndHashCode
@ToString
public class Token implements Comparable<Token> {
    private int value;

    public Token(int value) throws InvalidTokenValueException {
        setValue(value);
    }

    private void setValue(int value) throws InvalidTokenValueException {
        if (value < 0) {
            throw new InvalidTokenValueException();
        }
        this.value = value;
    }

    @Override
    public int compareTo(Token token) {
        return this.value - token.value;
    }
}
