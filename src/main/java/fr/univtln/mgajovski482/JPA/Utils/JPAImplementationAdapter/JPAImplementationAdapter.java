package fr.univtln.mgajovski482.JPA.Utils.JPAImplementationAdapter;

import javax.persistence.Query;
import java.util.stream.Stream;

/**
 * Created by Maxime on 31/03/2016.
 */
public interface JPAImplementationAdapter {
    public Stream streamOf(Query typedQuery);
}
