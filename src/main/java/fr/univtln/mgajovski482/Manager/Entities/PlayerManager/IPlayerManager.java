package fr.univtln.mgajovski482.Manager.Entities.PlayerManager;

import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.Rest.IRestManager;

import javax.ws.rs.QueryParam;

/**
 * Created by Maxime on 31/03/2016.
 */
public interface IPlayerManager extends IRestManager<Integer, IPlayer> {

    IPlayer findByEmail(@QueryParam("email") String email) throws DAOException ;
}
