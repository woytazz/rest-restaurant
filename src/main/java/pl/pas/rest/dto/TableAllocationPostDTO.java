package pl.pas.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TableAllocationPostDTO {
    @NotNull
    private UUID userUuid;
    @NotNull
    private UUID tableUuid;
    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;
}
