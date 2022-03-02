package pl.pas.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class TablePostDTO {
    @NotNull
    @Positive
    private Integer tableNumber;
    @NotBlank
    private String description;
}
