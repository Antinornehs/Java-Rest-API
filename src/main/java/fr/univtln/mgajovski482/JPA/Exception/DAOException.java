package fr.univtln.mgajovski482.JPA.Exception;

import fr.univtln.mgajovski482.JPA.Utils.Constants;

import javax.ws.rs.core.Response;

/**
 * Created by Maxime on 26/03/2016.
 */
public class DAOException extends BusinessException {
    public DAOException(Throwable e) {
        super(e);
    }

    public DAOException(Response.Status status, Constants.ErrorCode code, String message, String developerMessage, String link) {
        super(status, code, message, developerMessage, link);
    }
}
