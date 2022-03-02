package pl.pas.rest.mapper;

import pl.pas.rest.dto.TableAllocationGetDTO;
import pl.pas.rest.dto.TableAllocationPostDTO;
import pl.pas.rest.model.TableAllocation;

public class TableAllocationMapper {
    public TableAllocationGetDTO convertToTableAllocationGetDTO(TableAllocation tableAllocation){
        return new TableAllocationGetDTO(tableAllocation.getUuid(), tableAllocation.getUserUuid(),
                tableAllocation.getTableUuid(), tableAllocation.getStartDate(), tableAllocation.getEndDate());
    }

    public TableAllocation convertToTableAllocation(TableAllocationPostDTO tableAllocationPostDTO) {
        return new TableAllocation(tableAllocationPostDTO.getUserUuid(), tableAllocationPostDTO.getTableUuid(),
                tableAllocationPostDTO.getStartDate());
    }
}
