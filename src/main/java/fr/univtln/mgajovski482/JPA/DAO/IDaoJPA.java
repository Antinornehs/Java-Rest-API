package fr.univtln.mgajovski482.JPA.DAO;

import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.JPA.IdentifiableEntity;
import fr.univtln.mgajovski482.Rest.utils.Page;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by bruno on 01/12/14.
 * From http://www.adam-bien.com/roller/abien/entry/generic_crud_service_aka_dao
 * and http://theelitegentleman.blogspot.fr/2014/04/daos-as-ejbs-you-are-doing-it-wrong.html
 */

public interface IDaoJPA<PK extends Serializable, T extends IdentifiableEntity<PK>> {
    /**
     * Closes the underlaying datasource.
     */
    public void close();

    public void consume(Query query, Consumer consumer) throws DAOException;

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

    /**
     * Add the collection of items to the underlaying datasource.
     *
     * @param collection : the collection of items to create
     * @return the number created items.
     */
    public default int create(Collection<T> collection) throws DAOException {
        return create(collection.stream());
    }

    public int create(Stream<T> stream) throws DAOException;

    /**
     * Add the items given by the iterator to the underlaying datasource.
     * @param iterator : the iterator on the items to create.
     * @return the number of created items.
     * @throws DAOException
     */
    public default int create(Iterator<T> iterator) throws DAOException {
        return create(StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT | Spliterator.NONNULL), false));
    }

    /**
     * Add a item of type T to the underlaying datasource.
     * Datasource managed properties (id, ...) are updated.
     *
     * @param t
     * @return the added item.
     * @throws DAOException
     */
    public T create(T t) throws DAOException;

    /**
     * Deletes the Item with given id from the underlaying datasource.
     *
     * @param id The Id of the item to be remove.
     * @throws DAOException
     */
    public PK delete(PK id) throws DAOException;

    /**
     * Deletes all the item in the underlaying datasource.
     *
     * @return the number of deleted items.
     * @throws DAOException
     */
    public int deleteAll() throws DAOException;

    public T find(PK id) throws DAOException;

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

    public default Stream<T> findAllAsStream() throws DAOException {
        return findAllAsStream(false, 0);
    }

    public default Stream<T> findAllAsStream(boolean reverse, int first) throws DAOException {
        return findAllAsStream(false, 0, -1);
    }

    public Stream<T> findAllAsStream(boolean reverse, int first, int limit) throws DAOException;

    public default List findWithNamedQuery(String queryName) throws DAOException {
        return findWithNamedQuery(queryName, -1, 0);
    }

    public default List findWithNamedQuery(String queryName, int first, int resultLimit) throws DAOException {
        return findWithNamedQuery(queryName, null, first, resultLimit);
    }

    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int first, int limit)
            throws DAOException;

    public default List findWithNamedQuery(String queryName, int resultLimit) throws DAOException {
        return findWithNamedQuery(queryName, null, 0, resultLimit);
    }

    public default List findWithNamedQuery(String namedQueryName, Map parameters) throws DAOException {
        return findWithNamedQuery(namedQueryName, parameters, -1, 0);
    }

    public List findWithNativeQuery(String nativeQuery, Class type) throws DAOException;

    public int getBatchSize();

    public void setBatchSize(int i);

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


    public default Stream<PK> getIdsAsStream() throws DAOException {
        return getIdsAsStream(false);
    }

    public default Stream<PK> getIdsAsStream(boolean reverse) throws DAOException {
        return getIdsAsStream(reverse, 0, -1);
    }

    public Stream<PK> getIdsAsStream(boolean reverse, int first, int limit) throws DAOException;

    public Page<T> queryByPage(int pagenumber, int pagesize, int resultsize, Query contentQuery);

    public default Page findAllByPage(int pagenumber, int pagesize) throws DAOException {
        return (findAllByPage(false, pagenumber, pagesize, -1));
    }

    public default Page findAllByPage(int pagesize) throws DAOException {
        return (findAllByPage(false, 0, pagesize, -1));
    }

    public Page<T> findAllByPage(boolean reverse, int pagenumber, int pagesize, int limit) throws DAOException;

    long getSize();

    /**
     * @return true if the underlaying datasource is open.
     */
    boolean isOpen();

    public Stream<T> namedQueryAsStream(String namedQueryName, Map<String, Object> parameters, boolean reverse, int first, int limit) throws DAOException;

    public Object singleWithNamedQuery(String namedQueryName) throws DAOException;

    public Stream<T> typedQueryAsStream(TypedQuery typedQuery, Map<String, Object> parameters, boolean reverse, int first, int limit) throws DAOException;

    public T update(T t) throws DAOException;

    public default int updateWithNamedQuery(String namedQueryName) throws DAOException {
        return updateWithNamedQuery(namedQueryName, null);
    }

    public int updateWithNamedQuery(String namedQueryName, Map<String, Object> parameters) throws DAOException;

    public static class QueryParameter {

        private Map parameters = null;

        private QueryParameter(String name, Object value) {
            parameters = new HashMap();
            parameters.put(name, value);
        }

        public static QueryParameter with(String name, Object value) {
            return new QueryParameter(name, value);
        }

        public QueryParameter and(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        public Map parameters() {
            return parameters;
        }


    }

}
