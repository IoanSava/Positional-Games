package entities.tokens;

import exceptions.InvalidTokenValueException;
import lombok.Getter;

/**
 * An instance of this class will hold a number
 * from 1 to m. A token may be blank (value is 0),
 * meaning that it can take the place of any value
 * (wildcard).
 *
 * @author Ioan Sava
 */
@Getter
public class ArithmeticProgressionToken extends Token {
    private int value;

    public ArithmeticProgressionToken(int value) throws InvalidTokenValueException {
        setValue(value);
    }

    private void setValue(int value) throws InvalidTokenValueException {
        if (value < 0) {
            throw new InvalidTokenValueException();
        }
        this.value = value;
    }

    public int compareTo(ArithmeticProgressionToken token) {
        return this.value - token.getValue();
    }

    @Override
    public int compareTo(Token token) {
        return compareTo((ArithmeticProgressionToken) token);
    }

    @Override
    public String toString() {
        return "APToken(" + value + ')';
    }
}
