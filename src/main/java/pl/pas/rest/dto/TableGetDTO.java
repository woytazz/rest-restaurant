package pl.pas.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableGetDTO {
    private UUID uuid;
    private Boolean isAllocated;
    private Integer tableNumber;
    private String description;
}
