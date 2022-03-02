package pl.pas.rest.exceptions;

public class UserLoginTakenException extends Exception {
    public UserLoginTakenException(String message) {
        super(message);
    }
}
