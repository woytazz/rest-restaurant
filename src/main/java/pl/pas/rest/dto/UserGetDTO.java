package pl.pas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGetDTO {
    private UUID uuid;
    private Boolean isActive;
    private String login;
    private String role;
    private String name;
    private String surname;
}
