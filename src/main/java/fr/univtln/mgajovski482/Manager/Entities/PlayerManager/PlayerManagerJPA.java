package fr.univtln.mgajovski482.Manager.Entities.PlayerManager;

import fr.univtln.mgajovski482.JPA.DAO.Player.PlayerDAOJPA;
import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.Manager.AbstractManagerJPA;
import fr.univtln.mgajovski482.Rest.utils.Page;

/**
 * Created by Maxime on 31/03/2016.
 */
//TODO implements IPlayerManager ?
public class PlayerManagerJPA extends AbstractManagerJPA<Integer, IPlayer>  implements IPlayerManager{

    PlayerDAOJPA playerDAOJPA;

    public PlayerManagerJPA (PlayerDAOJPA playerDAOJPA) {
        super(playerDAOJPA);
        this.playerDAOJPA = playerDAOJPA;
    }


    @Override
    public Page findAllByPage(boolean reverse, int pagenumber, int pagesize) throws DAOException {
        return super.findAllByPage(reverse, pagenumber, pagesize);

    }

    @Override
    public IPlayer findByEmail(String email) throws DAOException {
        return playerDAOJPA.findByEmail(email);
    }

//    @Override
//    public IPlayer findByEmail(String email) throws DAOException {
//        entityManager.
//    }
}
