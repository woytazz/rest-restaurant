package pl.pas.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PasswordDTO {
    @NotBlank
    private String password;
    @NotBlank
    private String retryPassword;
}
