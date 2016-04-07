package fr.univtln.mgajovski482.JPA.DAO.Player;

import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;
import fr.univtln.mgajovski482.JPA.DAO.IDaoJPA;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import org.hibernate.validator.constraints.Email;


/**
 * Created by Maxime on 28/03/2016.
 */
public interface PlayerDAO  extends IDaoJPA<Integer, IPlayer> {

    IPlayer findByEmail(@Email String email) throws DAOException;

    int deleteByEmail(@Email String email) throws DAOException;
}
