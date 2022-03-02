package pl.pas.rest.exceptions;

import javax.ejb.ObjectNotFoundException;

public class TableAllocationNotFoundException extends ObjectNotFoundException {
    public TableAllocationNotFoundException(String message) {
        super(message);
    }
}
