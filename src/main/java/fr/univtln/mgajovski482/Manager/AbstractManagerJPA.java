package fr.univtln.mgajovski482.Manager;

import fr.univtln.mgajovski482.JPA.DAO.AbstractDAOJPA;
import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.JPA.DAO.IDaoJPA;
import fr.univtln.mgajovski482.JPA.IdentifiableEntity;
import fr.univtln.mgajovski482.Rest.utils.Page;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Maxime on 31/03/2016.
 */
public class AbstractManagerJPA<PK extends Serializable, T extends IdentifiableEntity<PK>> implements IManager<PK, T> {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractManagerJPA.class);

    protected final EntityManager entityManager;

    private IDaoJPA<PK, T> DAO;

    public AbstractManagerJPA(AbstractDAOJPA abstractCrudJPA) {
        this.entityManager = abstractCrudJPA.getEntityManager();
        this.DAO = abstractCrudJPA;
    }


    @Override
    public T find(@Valid PK id) throws DAOException {
        return DAO.find(id);
    }

    @Override
    public void delete(@Valid PK id) throws DAOException {
        System.out.println("AbstractManagerJPA DELETE");
        DAO.delete(id);
    }

    @Override
    public List<T> findAll(boolean reverse, int first, int limit) throws DAOException {
        return DAO.findAll(reverse, first, limit);
    }

    @Override
    public long getSize() {
        return DAO.getSize();
    }

    @Override
    public Page findAllByPage(boolean reverse, int pagenumber, int pagesize, int limit) throws DAOException {
        return DAO.findAllByPage(reverse, pagenumber, pagesize, limit);
    }

    @Override
    public List<PK> getIds(boolean reverse, int first, int limit) throws DAOException {
        return DAO.getIds(reverse, first, limit);
    }

    @Override
    public void consumeAll(boolean reverse, int first, int limit, Consumer<T> consumer) throws DAOException {
        DAO.consumeAll(reverse, first, limit, consumer);
    }

    /**
     *
     * @param consumer
     * @throws DAOException
     */
    @Override
    public void consumeAll(Consumer<T> consumer) throws DAOException {
        DAO.consumeAll(consumer);

    }


    @Override
    public void consumeAll(boolean reverse, Consumer<T> consumer) throws DAOException {
        DAO.consumeAll(reverse, consumer);

    }

    @Override
    public void consumeAll(boolean reverse, int first, Consumer<T> consumer) throws DAOException {
        DAO.consumeAll(reverse, first, consumer);
    }

    @Override
    public List<T> findAll() throws DAOException {
        return DAO.findAll();
    }

    @Override
    public List<T> findAll(boolean reverse) throws DAOException {
        return DAO.findAll(reverse);
    }

    @Override
    public List<T> findAll(boolean reverse, int first) throws DAOException {
        return DAO.findAll(reverse, first);

    }

    @Override
    public List<T> findAll(int first, int limit) throws DAOException {
        return DAO.findAll(first, limit);
    }

    @Override
    public List<T> findAll(int first) throws DAOException {
        return DAO.findAll(first);
    }

    @Override
    public List<PK> getIds() throws DAOException {
        return DAO.getIds();

    }

    @Override
    public List<PK> getIds(boolean reverse) throws DAOException {
        return DAO.getIds(reverse);
    }

    @Override
    public List<PK> getIds(boolean reverse, int first) throws DAOException {
        return DAO.getIds(reverse, first);

    }

    /**
     *
     */

    protected static interface DAOIntUpdateSupplier {
        int getAsInt() throws DAOException;
    }

    private int complexeTransaction(DAOIntUpdateSupplier supplier) throws DAOException {
        EntityTransaction tx = null;
        int result;
        try {
            tx = entityManager.getTransaction();

            tx.begin();
            result = supplier.getAsInt();
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DAOException(e);
        }
        catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DAOException(e);
        }
        return result;
    }

    protected static interface DAOSimpleUpdateSupplier<T> {
        T get() throws DAOException;
    }

    protected T simpleTransaction(DAOSimpleUpdateSupplier<T> supplier) throws DAOException {
        EntityTransaction tx = null;
        T result;
        try {
            tx = entityManager.getTransaction();
            tx.begin();
            result = supplier.get();
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DAOException(e);
        }
        catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DAOException(e);
        }
        return result;
    }

    @Override
    public T create(@Valid T t) throws DAOException {
        return simpleTransaction(()->DAO.create(t));
    }

    @Override
    public int create(@Valid Collection<T> collection) throws DAOException {
        return complexeTransaction(()->DAO.create(collection));
    }

    @Override
    public int create(Iterator<T> iterator) throws DAOException {
        return complexeTransaction(()->DAO.create(iterator));
    }

    @Override
    public int create(Stream<T> stream) throws DAOException {
        return complexeTransaction(()->DAO.create(stream));
    }

    @Override
    public T update(@Valid T t) throws DAOException {
        return simpleTransaction(()->DAO.update(t));
    }

    @Override
    public void done() throws DAOException {
        entityManager.close();
    }

    @Override
    public int deleteAll() throws DAOException {
        return complexeTransaction(()->DAO.deleteAll());
    }

}
