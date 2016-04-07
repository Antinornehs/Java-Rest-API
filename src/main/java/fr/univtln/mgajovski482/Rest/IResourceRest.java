package fr.univtln.mgajovski482.Rest;

import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.JPA.IdentifiableEntity;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.HEAD;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Maxime on 30/03/2016.
 */
public interface IResourceRest<PK extends Serializable, T extends IdentifiableEntity<PK>> {

        T find(@Valid PK id) throws DAOException;

        void delete(@Valid PK id) throws DAOException;

        long size() throws DAOException;

        @HEAD
        Response metadata() throws DAOException;

        Response findAll(boolean reverse, int pageNumber, int perPage, int limit, UriInfo uriInfo) throws DAOException;

        List<PK> getIds(boolean reverse) throws DAOException;

        @PUT
        @Consumes({"application/json", "application/xml"})
        T create(T item) throws DAOException;

        int create(List<T> list) throws DAOException;

        T update(@Valid T t) throws DAOException;

        int delete() throws DAOException;

        public void setMaxPageSize(int pageSize);

        public int getMaxPageSize();
    }

