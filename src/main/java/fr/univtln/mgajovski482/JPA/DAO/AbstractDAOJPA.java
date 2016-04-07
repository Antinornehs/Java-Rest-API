package fr.univtln.mgajovski482.JPA.DAO;

import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.JPA.IdentifiableEntity;
import fr.univtln.mgajovski482.JPA.Utils.Constants;
import fr.univtln.mgajovski482.JPA.Utils.JPAImplementationAdapter.JPAImplementationAdapterService;
import fr.univtln.mgajovski482.Rest.utils.Page;
import fr.univtln.mgajovski482.Rest.utils.PageNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Maxime on 31/03/2016.
 */
public class AbstractDAOJPA<PK extends Serializable, D extends IdentifiableEntity<PK>, R extends D> implements IDaoJPA<PK, D> {
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractDAOJPA.class);
    private final Class<D> declararedType;
    private final Class<R> entityBeanType;
    private final Class<PK> PKType;
    private final String idname;
    protected EntityManager entityManager;
    private int batchSize = 1000;

    private static List<DAOEventListener> daoEventListeners = new ArrayList<>();

    public static void addEventListener(DAOEventListener eventListener) {
        daoEventListeners.add(eventListener);
    }

    private static void notifyDAOEvent(DAOEvent daoEvent) {
        daoEventListeners.forEach(l -> l.onDAOEvent(daoEvent));
    }

    public AbstractDAOJPA(EntityManager entityManager) throws DAOException {

        this.entityManager = entityManager;
        Class[] parameters = getParametersClasses();
        PKType = parameters[0];
        declararedType = parameters[1];
        entityBeanType = parameters[2];


        idname = getInheritedFields(entityBeanType).stream().filter(f -> f.isAnnotationPresent(Id.class)).map(Field::getName).findFirst()
                .orElseGet(getInheritedMethods(entityBeanType).stream().filter(f -> f.isAnnotationPresent(Id.class)).map(Method::getName).findFirst()
                        ::get);
        if (idname == null)
            throw new DAOException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorCode.DAO_EXCEPTION, "No field or method annotated with @Id for class: " + entityBeanType.getName(), "", "");
        LOGGER.info("New DAO for" + PKType + " " + declararedType + " " + entityBeanType + " " + idname);
    }

    private Class[] getParametersClasses() {
        Class c = getClass();
        //In case of use with implementation class using raw types (mandatory with EJBs).
        while (!(c.getGenericSuperclass() instanceof ParameterizedType)) {
            c = c.getSuperclass();
        }

        Type[] types = ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments();
        Class[] classes = new Class[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] instanceof ParameterizedType)
                classes[i] = (Class) ((ParameterizedType) types[i]).getRawType();
            else
                classes[i] = (Class) types[i];
        }
        return classes;
    }

    public static List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    public static List<Method> getInheritedMethods(Class<?> type) {
        List<Method> methods = new ArrayList<Method>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        }
        return methods;
    }

    @Override
    public void close() {
        entityManager.close();
    }

    @Override
    public void consume(Query query, Consumer consumer) throws DAOException {
        JPAImplementationAdapterService.streamOf(query).forEach(consumer);
    }

    @Override
    public void consumeAll(boolean reverse, int first, int limit, Consumer<D> consumer) throws DAOException {
        consume(findAllQuery(reverse, first, limit), consumer);
    }

    @Override
    public int create(Stream<D> stream) throws DAOException {
        final int[] nbAdded = {0};
        FlushModeType flushmode = entityManager.getFlushMode();
        entityManager.setFlushMode(FlushModeType.COMMIT);
        stream.forEachOrdered(d -> {
                    entityManager.persist(d);
                    if (nbAdded[0]++ % batchSize == 0) {
                        //Push data to the database in the same transaction
                        entityManager.flush();
                        entityManager.clear();
                    }
                }
        );
        entityManager.flush();
        entityManager.clear();
        entityManager.setFlushMode(flushmode);
        return nbAdded[0];
    }

    @Override
    public D create(D t) throws DAOException {
        entityManager.persist(t);
        entityManager.flush();
        entityManager.refresh(t);

        notifyDAOEvent(new DAOEvent(DAOEvent.Type.CREATE, t));
        return t;
    }

    @Override
    public PK delete(PK id) throws DAOException {
        System.out.println("AbstractDAOJPA DELETE " + id);
        System.out.println(entityManager.getClass().getSimpleName());
        D objet = find(id);
        System.out.println(objet.toString());
        try {
            System.out.println(objet.getClass().getSimpleName());
            entityManager.remove(objet);
            notifyDAOEvent(new DAOEvent(DAOEvent.Type.DELETE, objet));
            System.out.println("lool");
        }catch (Exception e){
            e.printStackTrace();
        }
        //entityManager.comit();
        return id;
    }

    @Override
    public int deleteAll() throws DAOException {
        CriteriaDelete<R> cdelete = entityManager.getCriteriaBuilder().createCriteriaDelete(entityBeanType);
        System.out.println("CDELETE:" + cdelete);
//        Root<R> t = cdelete.from(entityBeanType);
        return entityManager.createQuery(cdelete).executeUpdate();
    }

    @Override
    public D find(PK id) {
        return entityManager.find(entityBeanType, id);
    }

    @Override
    public List<D> findAll(boolean reverse, int first, int limit) throws DAOException {
        return (List<D>) findAllQuery(reverse, first, limit).getResultList();
    }

    @Override
    public Stream<D> findAllAsStream(boolean reverse, int first, int limit) throws DAOException {
        return JPAImplementationAdapterService.streamOf(findAllQuery(reverse, first, limit));
    }

    @Override
    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int first, int limit) {
        return getNamedQuery(namedQueryName, parameters, first, limit).getResultList();
    }

    @Override
    public List findWithNativeQuery(String sql, Class type) {
        return entityManager.createNativeQuery(sql, type).getResultList();
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public List<PK> getIds(boolean reverse, int first, int limit) throws DAOException {
        return getIDsQuery(reverse, first, limit).getResultList();
    }

    private TypedQuery<PK> getIDsQuery(boolean reverse, int first, int limit) throws DAOException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PK> cq = cb.createQuery(PKType);
        Root<R> t = cq.from(entityBeanType);
        cq.select(t.get(idname));
        cq.orderBy(reverse ? cb.desc(t.get(idname)) : cb.asc(t.get(idname)));
        TypedQuery<PK> q = entityManager.createQuery(cq);
        if (first > -1) q.setFirstResult(first);
        if (limit > -1) q.setMaxResults(limit);
        return q;
    }

    @Override
    public Stream<PK> getIdsAsStream(boolean reverse, int first, int limit) throws DAOException {
        return JPAImplementationAdapterService.streamOf(getIDsQuery(reverse, first, limit));
    }

    private TypedQuery<Long> getSizeQuery() {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(entityBeanType)));
        return entityManager.createQuery(cq);
    }

    @Override
    public long getSize() {
        return getSizeQuery().getSingleResult();
    }

    @Override
    public Page queryByPage(int pagenumber, int pagesize, int totalItems, Query contentQuery) {
        return new Page(pagenumber, pagesize, totalItems, (int) Math.ceil(totalItems / (float) pagesize), contentQuery.getResultList());
    }

    @Override
    public Page findAllByPage(boolean reverse, int pagenumber, int pagesize, int limit) throws DAOException {
        int size = getSizeQuery().getSingleResult().intValue();
        if (limit > 0 && limit < size) size = limit;
        int first = pagenumber * pagesize;
        if (first >= size) throw new PageNotFoundException();
        return queryByPage(pagenumber, pagesize, size, findAllQuery(reverse, first, Integer.min(pagesize, size)));
    }

    @Override
    public boolean isOpen() {
        return entityManager.isOpen();
    }

    @Override
    public Stream<D> namedQueryAsStream(String namedQueryName, Map<String, Object> parameters, boolean reverse, int first, int limit) throws DAOException {
        return JPAImplementationAdapterService.streamOf(getNamedQuery(namedQueryName, parameters, first, limit));
    }

    private Query getNamedQuery(String namedQueryName, Map<String, Object> parameters, int first, int limit) {
        Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = entityManager.createNamedQuery(namedQueryName);
        if (limit > 0)
            query.setMaxResults(limit);

        if (first > 0)
            query.setFirstResult(first);

        for (Map.Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    @Override
    public Object singleWithNamedQuery(String namedQueryName) {
        return entityManager.createNamedQuery(namedQueryName).getSingleResult();
    }

    @Override
    public Stream<D> typedQueryAsStream(TypedQuery typedQuery, Map<String, Object> parameters, boolean reverse, int first, int limit) throws DAOException {
        return JPAImplementationAdapterService.streamOf(typedQuery);
    }

    @Override
    public D update(D t) {
        return entityManager.merge(t);
    }

    @Override
    public int updateWithNamedQuery(String namedQueryName, Map<String, Object> parameters) {
        Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = entityManager.createNamedQuery(namedQueryName);
        for (Map.Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.executeUpdate();
    }

    private TypedQuery<R> findAllQuery(boolean reverse, int first, int limit) throws DAOException {
        String queryName = declararedType.getSimpleName().toLowerCase() + ".findAll";
        TypedQuery<R> result = entityManager.createNamedQuery(queryName, entityBeanType);
        if (result == null) {
            LOGGER.warn("Falling back to auto generated findAll query, you should add a namedquery " + queryName + " to " + entityBeanType);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<R> cq = cb.createQuery(entityBeanType);
            Root<R> t = cq.from(entityBeanType);
            cq.select(t);
            cq.orderBy(reverse ? cb.desc(t.get(idname)) : cb.asc(t.get(idname)));
            result = entityManager.createQuery(cq);
        } else
            LOGGER.info("Using namedquery " + queryName + " for findAll");
        if (first > -1) result.setFirstResult(first);
        if (limit > -1) result.setMaxResults(limit);

        return result;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
    /*
    public Query fullTextSearchQuery(String text) {
        return entityManager.createNativeQuery
                ("SELECT * FROM publisher t WHERE t::text LIKE '%"+text+"%'", entityBeanType);
    }*/

    /*
    @Override
    public Stream<D> findAllAsStream() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<R> cq = cb.createQuery(entityBeanType);
        Root<R> t = cq.from(entityBeanType);
        cq.select(t);
        org.hibernate.Query query = (org.hibernate.Query) entityManager.createQuery(cq);

        if (fetchSize != null) {
            query.setFetchSize(fetchSize);
        }
        query.setReadOnly(true);

        ScrollableResults scroll = query.scroll(ScrollMode.FORWARD_ONLY);
        return (Stream<D>) StreamSupport.stream(toSplitIterator(scroll, entityBeanType), false)
                .onClose(scroll::close);
    }

    private Spliterator<R> toSplitIterator(ScrollableResults scroll, Class<R> type) {
        return Spliterators.spliteratorUnknownSize(
                new ScrollableResultIterator<>(scroll, type),
                Spliterator.DISTINCT | Spliterator.NONNULL |
                        Spliterator.CONCURRENT | Spliterator.IMMUTABLE
        );
    }

    private static class ScrollableResultIterator<R> implements Iterator<R> {

        private final ScrollableResults results;
        private final Class<R> type;

        ScrollableResultIterator(ScrollableResults results, Class<R> type) {
            this.results = results;
            this.type = type;
        }


        public boolean hasNext() {
            return results.next();
        }


        public R next() {
            return type.cast(results.get(0));
        }
    }
*/


