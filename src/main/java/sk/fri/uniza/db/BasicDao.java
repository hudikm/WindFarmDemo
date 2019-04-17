package sk.fri.uniza.db;

import java.util.List;
import java.util.Optional;

public interface BasicDao<T, I> {
    Optional<T> get(I id);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
