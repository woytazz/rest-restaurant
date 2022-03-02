package pl.pas.rest.exceptions;

import javax.ejb.ObjectNotFoundException;

public class TableNotFoundException extends ObjectNotFoundException {
    public TableNotFoundException(String message) {
        super(message);
    }
}
