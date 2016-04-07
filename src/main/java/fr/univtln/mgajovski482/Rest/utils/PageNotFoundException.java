package fr.univtln.mgajovski482.Rest.utils;

import fr.univtln.mgajovski482.JPA.Utils.Constants;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;

import javax.ws.rs.core.Response;

/**
 * Created by Maxime on 31/03/2016.
 */
public class PageNotFoundException extends DAOException {
    public PageNotFoundException() {
        super(Response.Status.NOT_FOUND, Constants.ErrorCode.DAO_EXCEPTION, "Page doesn't exist", "Page doesn't exist", null);
    }
}

