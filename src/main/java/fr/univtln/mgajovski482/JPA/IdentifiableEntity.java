package fr.univtln.mgajovski482.JPA;

import java.io.Serializable;

/**
 * Created by bruno on 01/12/14.
 * From http://www.adam-bien.com/roller/abien/entry/generic_crud_service_aka_dao
 * and http://theelitegentleman.blogspot.fr/2014/04/daos-as-ejbs-you-are-doing-it-wrong.html
 */
public interface IdentifiableEntity<PK extends Serializable> extends Serializable {
}
