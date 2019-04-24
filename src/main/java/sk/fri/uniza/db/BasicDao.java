package sk.fri.uniza.db;

import sk.fri.uniza.api.Paged;
import sk.fri.uniza.core.User;

import java.util.List;
import java.util.Optional;

public interface BasicDao<T, I> {
    Optional<T> findById(I id);

    List<T> getAll();

    /**
     * https://www.baeldung.com/hibernate-pagination
     * @param limit
     * @param page
     * @return
     */
    Paged<List<T>> getAll(int limit, int page);

    I save(T t);

    I update(T t, String[] params);

    void delete(T t);
}
