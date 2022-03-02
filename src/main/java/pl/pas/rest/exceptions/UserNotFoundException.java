package pl.pas.rest.exceptions;

import javax.ejb.ObjectNotFoundException;

public class UserNotFoundException extends ObjectNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
