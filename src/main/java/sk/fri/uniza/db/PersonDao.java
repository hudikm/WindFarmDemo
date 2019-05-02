package sk.fri.uniza.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.api.Person;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class PersonDao extends AbstractDAO<Person> implements BasicDao<Person, Long> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public PersonDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Person> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(get(id));
    }

    @Override
    public List<Person> getAll() {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = builder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        criteriaQuery.select(root);
        List<Person> list = list(criteriaQuery);
        return list;
    }

    @Override
    public Paged<List<Person>> getAll(int limit, int page) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        Root<Person> root = cq.from(Person.class);
        cq.select(builder.count(root));
        Long countResults = currentSession().createQuery(cq).getSingleResult();

        if (countResults == 0) return null;

        CriteriaQuery<Person> criteriaQuery = builder.createQuery(Person.class);
        criteriaQuery.select(criteriaQuery.from(Person.class));
        List<Person> list = currentSession().createQuery(criteriaQuery)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .list();

        return new Paged<>(page, limit, countResults, list);

    }

    @Override
    public Long save(Person person) throws HibernateException {
        persist(person);
        return person.getId();
    }

    @Override
    public Long update(Person person, String[] params) {

        // Find person in DB and copy salt, secrete so the values will not be affected
        Optional<Person> personOptional = findById(person.getId());
        personOptional.ifPresent(person1 -> {
            person.setSalt(person1.getSalt());
            person.setSecrete(person1.getSecrete());
            currentSession().detach(person1);
        });
        persist(person);
        return person.getId();
    }

    public void saveNewPassword(Long id, String password) {
        Person person = get(id);
        person.setNewPassword(password);
    }

    @Override
    public void delete(Person person) {
        currentSession().delete(person);
    }
}
