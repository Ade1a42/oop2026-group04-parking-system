package parking.repository;

import java.util.List;
import java.util.Optional;

public interface  IRepository<T> {
    T save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    void delete(int id);
    T update(T entity);
}
