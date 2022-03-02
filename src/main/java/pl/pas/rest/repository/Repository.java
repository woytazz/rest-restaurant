package pl.pas.rest.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {
    boolean add(T obj);
    Optional<T> get(UUID uuid);
    List<T> getList();
    boolean update(UUID uuid, T obj);
    boolean delete(UUID uuid);
}
