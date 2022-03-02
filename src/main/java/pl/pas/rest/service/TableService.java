package pl.pas.rest.service;

import pl.pas.rest.dto.TableGetDTO;
import pl.pas.rest.dto.TablePostDTO;
import pl.pas.rest.exceptions.TableNotFoundException;
import pl.pas.rest.exceptions.TableNumberTakenException;
import pl.pas.rest.mapper.TableMapper;
import pl.pas.rest.model.Table;
import pl.pas.rest.repository.TableRepository;

import javax.inject.Inject;
import java.util.*;

public class TableService {
    @Inject
    private TableRepository repository;

    @Inject
    private TableMapper mapper;

    public TableGetDTO createTable(TablePostDTO tablePostDTO) throws TableNumberTakenException, TableNotFoundException {
        if (repository.add(mapper.convertToTable(tablePostDTO))) {
            return findTableByNumber(tablePostDTO.getTableNumber());
        } else {
            throw new TableNumberTakenException("Table number is taken");
        }
    }

    public TableGetDTO readTable(UUID uuid) throws TableNotFoundException {
        Optional<Table> optionalTable = repository.get(uuid);

        if (optionalTable.isPresent()) {
            return mapper.convertToTableGetDTO(optionalTable.get());
        } else {
            throw new TableNotFoundException("Bad table UUID");
        }
    }

    public List<TableGetDTO> readAllTables() {
        List<TableGetDTO> tableGetDTOList = new ArrayList<>();

        for (Table table : repository.getList()) {
            tableGetDTOList.add(mapper.convertToTableGetDTO(table));
        }
        return Collections.unmodifiableList(tableGetDTOList);
    }

    public TableGetDTO updateTable(UUID uuid, TablePostDTO tablePostDTO) throws TableNotFoundException {
        if (repository.update(uuid, mapper.convertToTable(tablePostDTO))) {
            return readTable(uuid);
        } else {
            throw new TableNotFoundException("Bad table UUID");
        }
    }

    public boolean deleteTable(UUID uuid) throws TableNotFoundException {
        Optional<Table> optionalTable = repository.get(uuid);

        if (optionalTable.isPresent()) {
            if (!optionalTable.get().getIsAllocated()) {
                return repository.delete(uuid);
            } else {
                return false;
            }
        } else {
            throw new TableNotFoundException("Bad table UUID");
        }
    }

    public TableGetDTO findTableByNumber(Integer number) throws TableNotFoundException {
        Optional<Table> optionalTable = repository.findByNumber(number);

        if (optionalTable.isPresent()) {
            return mapper.convertToTableGetDTO(optionalTable.get());
        } else {
            throw new TableNotFoundException("Bad table UUID");
        }
    }
}
