package pl.pas.rest.model;

public class Administrator extends User {
    public Administrator(Boolean isActive, String login, String password, String role, String name, String surname) {
        super(isActive, login, password, role, name, surname);
    }
}
