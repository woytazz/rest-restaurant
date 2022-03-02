package pl.pas.rest.model;

public class Client extends User {
    public Client(Boolean isActive, String login, String password, String role, String name, String surname) {
        super(isActive, login, password, role, name, surname);
    }
}
