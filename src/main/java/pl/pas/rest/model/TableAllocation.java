package pl.pas.rest.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TableAllocation {
    private UUID uuid;
    private UUID userUuid;
    private UUID tableUuid;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TableAllocation(UUID userUuid, UUID tableUuid, LocalDateTime startDate) {
        this.userUuid = userUuid;
        this.tableUuid = tableUuid;
        this.startDate = startDate;
    }
}
