package pl.pas.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class LoginDTO {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
}
