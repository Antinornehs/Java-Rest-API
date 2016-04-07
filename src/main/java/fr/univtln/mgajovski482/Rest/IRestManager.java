package fr.univtln.mgajovski482.Rest;

import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.JPA.IdentifiableEntity;
import fr.univtln.mgajovski482.Rest.utils.Page;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Maxime on 30/03/2016.
 */

public interface IRestManager<PK extends Serializable, T extends IdentifiableEntity<PK>> {

    public default void consumeAll(Consumer<T> consumer) throws DAOException {
        consumeAll(false, consumer);
    }

    public default void consumeAll(boolean reverse, Consumer<T> consumer) throws DAOException {
        consumeAll(reverse, 0, consumer);
    }

    public default void consumeAll(boolean reverse, int first, Consumer<T> consumer) throws DAOException {
        consumeAll(reverse, first, -1, consumer);
    }

    public void consumeAll(boolean reverse, int first, int limit, Consumer<T> consumer) throws DAOException;

    public T create(@Valid T t) throws DAOException;

    public int create(@Valid Collection<T> collection) throws DAOException;

    public int create(Iterator<T> iterator) throws DAOException;

    public int create(Stream<T> stream) throws DAOException;

    void delete(@Valid PK id) throws DAOException;

    public int deleteAll() throws DAOException;

    public void done() throws DAOException;

    T find(@Valid PK id) throws DAOException;

    public default List<T> findAll() throws DAOException {
        return findAll(false);
    }

    public default List<T> findAll(boolean reverse) throws DAOException {
        return findAll(reverse, 0);
    }

    public default List<T> findAll(boolean reverse, int first) throws DAOException {
        return findAll(reverse, first, -1);
    }

    public List<T> findAll(boolean reverse, int first, int limit) throws DAOException;

    public default List<T> findAll(int first, int limit) throws DAOException {
        return findAll(false, first, limit);
    }

    public default List<T> findAll(int first) throws DAOException {
        return findAll(false, first, -1);
    }

    public default List<PK> getIds() throws DAOException {
        return getIds(false);
    }

    public default List<PK> getIds(boolean reverse) throws DAOException {
        return getIds(reverse, 0);
    }

    public default List<PK> getIds(boolean reverse, int first) throws DAOException {
        return getIds(reverse, first, -1);
    }
    public List<PK> getIds(boolean reverse, int first, int limit) throws DAOException;

    public long getSize();

    public default Page findAllByPage(boolean reverse, int pagenumber, int pagesize) throws DAOException {
        return findAllByPage(reverse, pagenumber, pagesize, -1);
    }

    public Page findAllByPage(boolean reverse, int pagenumber, int pagesize, int limit) throws DAOException;

    public T update(@Valid T t) throws DAOException;
}

