package pl.pas.rest.repository;

import pl.pas.rest.model.Table;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class TableRepository implements Repository<Table> {
    private final List<Table> tableList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean add(Table obj) {
        if (findByNumber(obj.getTableNumber()).isEmpty()) {
            obj.setUuid(UUID.randomUUID());
            return tableList.add(obj);
        } else {
            return false;
        }
    }

    @Override
    public Optional<Table> get(UUID uuid) {
        for (Table table : tableList) {
            if (table.getUuid().equals(uuid)) {
                return Optional.of(table);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Table> getList() {
        return Collections.unmodifiableList(this.tableList);
    }

    @Override
    public boolean update(UUID uuid, Table obj) {
        Optional<Table> optionalTable = get(uuid);
        if (optionalTable.isPresent()) {
            optionalTable.get().setDescription(obj.getDescription());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(UUID uuid) {
        return tableList.removeIf(table -> table.getUuid().equals(uuid));
    }

    public Optional<Table> findByNumber(Integer number) {
        for (Table table : tableList) {
            if (table.getTableNumber().equals(number)) {
                return Optional.of(table);
            }
        }
        return Optional.empty();
    }
}
