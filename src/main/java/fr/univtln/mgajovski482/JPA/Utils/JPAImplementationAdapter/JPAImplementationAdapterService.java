package fr.univtln.mgajovski482.JPA.Utils.JPAImplementationAdapter;

import javax.persistence.Query;
import java.util.ServiceLoader;
import java.util.stream.Stream;

/**
 * Created by Maxime on 31/03/2016.
 */
public class JPAImplementationAdapterService {
    private static ServiceLoader<JPAImplementationAdapter> jpaImplementationAdapterLoader
            = ServiceLoader.load(JPAImplementationAdapter.class);

    public static Stream streamOf(Query typedQuery) {
        for (JPAImplementationAdapter jpaImplementationAdapter : jpaImplementationAdapterLoader) {
            Stream enc = jpaImplementationAdapter.streamOf(typedQuery);
            if (enc != null)
                return enc;
        }
        return null;
    }
}
