package fr.univtln.mgajovski482.JPA.DAO.Player;

import fr.univtln.mgajovski482.JPA.DAO.AbstractDAOJPA;
import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;
import fr.univtln.mgajovski482.JPA.Entities.Player.PlayerJPA;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import org.hibernate.validator.constraints.Email;

import javax.persistence.EntityManager;

/**
 * Created by Maxime on 28/03/2016.
 */

public class PlayerDAOJPA extends AbstractDAOJPA<Integer, IPlayer, PlayerJPA> implements PlayerDAO {

    public PlayerDAOJPA(EntityManager entityManager) throws DAOException{
        super(entityManager);
    }

    @Override
    public IPlayer findByEmail(@Email String email) throws DAOException {
        return  (IPlayer) findWithNamedQuery("player.findByEmail",
                QueryParameter.with("email", email)
                .parameters()).get(0);
    }

    @Override
    public int deleteByEmail(@Email String email) {
        return super.updateWithNamedQuery("player.deleteByEmail",
                QueryParameter.with("email", email)
                        .parameters());
    }

}
