package pl.pas.rest.repository;

import pl.pas.rest.model.TableAllocation;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class TableAllocationRepository implements Repository<TableAllocation> {
    private final List<TableAllocation> tableAllocationList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean add(TableAllocation obj) {
        obj.setUuid(UUID.randomUUID());
        return tableAllocationList.add(obj);
    }

    @Override
    public Optional<TableAllocation> get(UUID uuid) {
        for (TableAllocation tableAllocation : tableAllocationList) {
            if (tableAllocation.getUuid().equals(uuid)) {
                return Optional.of(tableAllocation);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<TableAllocation> getList() {
        return Collections.unmodifiableList(this.tableAllocationList);
    }

    @Override
    public boolean update(UUID uuid, TableAllocation obj) {
        return false;
    }

    @Override
    public boolean delete(UUID uuid) {
        return tableAllocationList.removeIf(tableAllocation -> tableAllocation.getUuid().equals(uuid));
    }

    public List<TableAllocation> getUserPast(UUID uuid) {
        List<TableAllocation> pastList = new ArrayList<>();
        for (TableAllocation tableAllocation : tableAllocationList) {
            if (tableAllocation.getUserUuid().equals(uuid)) {
                if (tableAllocation.getEndDate() != null) {
                    pastList.add(tableAllocation);
                }
            }
        }
        return Collections.unmodifiableList(pastList);
    }

    public List<TableAllocation> getUserPresent(UUID uuid) {
        List<TableAllocation> presentList = new ArrayList<>();
        for (TableAllocation tableAllocation : tableAllocationList) {
            if (tableAllocation.getUserUuid().equals(uuid)) {
                if (tableAllocation.getEndDate() == null) {
                    presentList.add(tableAllocation);
                }
            }
        }
        return Collections.unmodifiableList(presentList);
    }

    public List<TableAllocation> getTablePast(UUID uuid) {
        List<TableAllocation> pastList = new ArrayList<>();
        for (TableAllocation tableAllocation : tableAllocationList) {
            if (tableAllocation.getTableUuid().equals(uuid)) {
                if (tableAllocation.getEndDate() != null) {
                    pastList.add(tableAllocation);
                }
            }
        }
        return Collections.unmodifiableList(pastList);
    }

    public List<TableAllocation> geTablePresent(UUID uuid) {
        List<TableAllocation> presentList = new ArrayList<>();
        for (TableAllocation tableAllocation : tableAllocationList) {
            if (tableAllocation.getTableUuid().equals(uuid)) {
                if (tableAllocation.getEndDate() == null) {
                    presentList.add(tableAllocation);
                }
            }
        }
        return Collections.unmodifiableList(presentList);
    }
}
