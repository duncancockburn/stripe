package stripe.exceptions;

public class InvalidAPIKeyException extends Throwable {

    public InvalidAPIKeyException(String message) {
        super(message);
    }
}
