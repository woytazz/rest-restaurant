package pl.pas.rest.service;

import pl.pas.rest.dto.TableAllocationGetDTO;
import pl.pas.rest.dto.TableAllocationPostDTO;
import pl.pas.rest.exceptions.BadEndDateException;
import pl.pas.rest.exceptions.TableAllocationNotFoundException;
import pl.pas.rest.mapper.TableAllocationMapper;
import pl.pas.rest.model.Table;
import pl.pas.rest.model.TableAllocation;
import pl.pas.rest.model.User;
import pl.pas.rest.repository.TableAllocationRepository;
import pl.pas.rest.repository.TableRepository;
import pl.pas.rest.repository.UserRepository;

import javax.ejb.ObjectNotFoundException;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;
import java.util.*;

public class TableAllocationService {
    @Inject
    private TableAllocationRepository tableAllocationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TableRepository tableRepository;

    @Inject
    private TableAllocationMapper mapper;

    public boolean createTableAllocation(TableAllocationPostDTO tableAllocationPostDTO) throws ObjectNotFoundException {
        Optional<User> optionalUser = userRepository.get(tableAllocationPostDTO.getUserUuid());
        Optional<Table> optionalTable = tableRepository.get(tableAllocationPostDTO.getTableUuid());

        if (optionalUser.isPresent() && optionalTable.isPresent()) {
            if (optionalUser.get().getIsActive() && !optionalTable.get().getIsAllocated()) {
                optionalTable.get().setIsAllocated(true);
                return tableAllocationRepository.add(mapper.convertToTableAllocation(tableAllocationPostDTO));
            }
            return false;
        } else {
            throw new ObjectNotFoundException("Bad user or table UUID");
        }
    }

    public TableAllocationGetDTO readTableAllocation(UUID uuid) throws TableAllocationNotFoundException {
        Optional<TableAllocation> optionalTableAllocation = tableAllocationRepository.get(uuid);

        if (optionalTableAllocation.isPresent()) {
            return mapper.convertToTableAllocationGetDTO(optionalTableAllocation.get());
        } else {
            throw new TableAllocationNotFoundException("Bad table allocation UUID");
        }
    }

    public List<TableAllocationGetDTO> readAllTableAllocations() {
        List<TableAllocationGetDTO> tableAllocationGetDTOList = new ArrayList<>();

        for (TableAllocation tableAllocation : tableAllocationRepository.getList()) {
            tableAllocationGetDTOList.add(mapper.convertToTableAllocationGetDTO(tableAllocation));
        }
        return Collections.unmodifiableList(tableAllocationGetDTOList);
    }

    public boolean deleteTableAllocation(UUID uuid) throws TableAllocationNotFoundException {
        Optional<TableAllocation> optionalTableAllocation = tableAllocationRepository.get(uuid);

        if (optionalTableAllocation.isPresent()) {
            if (optionalTableAllocation.get().getEndDate() == null) {
                Optional<Table> optionalTable = tableRepository.get(optionalTableAllocation.get().getTableUuid());
                optionalTable.ifPresent(table -> table.setIsAllocated(false));
                return tableAllocationRepository.delete(uuid);
            } else {
                return false;
            }
        } else {
            throw new TableAllocationNotFoundException("Bad table allocation UUID");
        }
    }

    public List<TableAllocationGetDTO> queryTableAllocations(String type, String time, UUID uuid) {
        List<TableAllocationGetDTO> tableAllocationGetDTOList = new ArrayList<>();

        if (type.equals("user") && time.equals("past") && userRepository.get(uuid).isPresent()) {
            for (TableAllocation tableAllocation : tableAllocationRepository.getUserPast(uuid)) {
                tableAllocationGetDTOList.add(mapper.convertToTableAllocationGetDTO(tableAllocation));
            }
        } else if (type.equals("user") && time.equals("present") && userRepository.get(uuid).isPresent()) {
            for (TableAllocation tableAllocation : tableAllocationRepository.getUserPresent(uuid)) {
                tableAllocationGetDTOList.add(mapper.convertToTableAllocationGetDTO(tableAllocation));
            }
        } else if (type.equals("table") && time.equals("past") && tableRepository.get(uuid).isPresent()) {
            for (TableAllocation tableAllocation : tableAllocationRepository.getTablePast(uuid)) {
                tableAllocationGetDTOList.add(mapper.convertToTableAllocationGetDTO(tableAllocation));
            }
        } else if (type.equals("table") && time.equals("present") && tableRepository.get(uuid).isPresent()) {
            for (TableAllocation tableAllocation : tableAllocationRepository.geTablePresent(uuid)) {
                tableAllocationGetDTOList.add(mapper.convertToTableAllocationGetDTO(tableAllocation));
            }
        } else {
            throw new BadRequestException("Bad query param");
        }

        return Collections.unmodifiableList(tableAllocationGetDTOList);
    }

    public void endTableAllocation(UUID uuid) throws TableAllocationNotFoundException, BadEndDateException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Optional<TableAllocation> optionalTableAllocation = tableAllocationRepository.get(uuid);

        if (optionalTableAllocation.isPresent()) {
            if (localDateTime.isAfter(optionalTableAllocation.get().getStartDate())) {
                Optional<Table> optionalTable = tableRepository.get(optionalTableAllocation.get().getTableUuid());
                optionalTable.ifPresent(table -> table.setIsAllocated(false));
                optionalTableAllocation.get().setEndDate(localDateTime);
            } else {
                throw new BadEndDateException("Allocation has not started yet");
            }
        } else {
            throw new TableAllocationNotFoundException("Bad table allocation UUID");
        }
    }
}
