package pl.pas.rest.model;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Table {
    private UUID uuid;
    private Boolean isAllocated;
    private Integer tableNumber;
    private String description;

    public Table(Integer tableNumber, String description) {
        this.isAllocated = false;
        this.tableNumber = tableNumber;
        this.description = description;
    }
}
