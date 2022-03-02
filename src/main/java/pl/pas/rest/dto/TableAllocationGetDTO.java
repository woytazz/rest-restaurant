package pl.pas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableAllocationGetDTO {
    private UUID uuid;
    private UUID userUuid;
    private UUID tableUuid;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
