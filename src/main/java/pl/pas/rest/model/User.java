package pl.pas.rest.model;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class User {
    private UUID uuid;
    private Boolean isActive;
    private String login;
    private String password;
    private String role;
    private String name;
    private String surname;

    public User(Boolean isActive, String login, String password, String role, String name, String surname) {
        this.isActive = isActive;
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.surname = surname;
    }
}
