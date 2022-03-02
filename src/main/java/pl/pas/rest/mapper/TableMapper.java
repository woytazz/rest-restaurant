package pl.pas.rest.mapper;

import pl.pas.rest.dto.TableGetDTO;
import pl.pas.rest.dto.TablePostDTO;
import pl.pas.rest.model.Table;

public class TableMapper {
    public TableGetDTO convertToTableGetDTO(Table table) {
        return new TableGetDTO(table.getUuid(), table.getIsAllocated(), table.getTableNumber(), table.getDescription());
    }

    public Table convertToTable(TablePostDTO tablePostDTO) {
        return new Table(tablePostDTO.getTableNumber(), tablePostDTO.getDescription());
    }
}
