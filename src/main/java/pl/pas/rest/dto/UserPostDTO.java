package pl.pas.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserPostDTO {
    @NotNull
    private Boolean isActive;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
}
