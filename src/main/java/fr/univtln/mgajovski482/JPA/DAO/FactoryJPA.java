package fr.univtln.mgajovski482.JPA.DAO;

import com.fasterxml.jackson.databind.JsonDeserializer;
import fr.univtln.mgajovski482.JPA.DAO.PersonalInformations.PersonalInformationsDAOJPA;
import fr.univtln.mgajovski482.JPA.DAO.Player.PlayerDAO;
import fr.univtln.mgajovski482.JPA.DAO.Player.PlayerDAOJPA;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.IPersonalInformations;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.PersonalInformationsJPA;
import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.Manager.Entities.PersonalInformationsManager.IPersonalInformationsManager;
import fr.univtln.mgajovski482.Manager.Entities.PersonalInformationsManager.PersonalInformationsManagerJPA;
import fr.univtln.mgajovski482.Manager.Entities.PlayerManager.IPlayerManager;
import fr.univtln.mgajovski482.Manager.Entities.PlayerManager.PlayerManagerJPA;

import javax.persistence.EntityManagerFactory;

/**
 * Created by Maxime on 30/03/2016.
 */
public class FactoryJPA {

    private final EntityManagerFactory entityManagerFactory;

    public FactoryJPA(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public PlayerDAO getPlayerDAO() throws DAOException {
        return new PlayerDAOJPA(entityManagerFactory.createEntityManager());
    }

//
//    @Override
//    public JsonDeserializer<IPlayer> getPersonDeserializer() {
//        return new PersonDeserializer(this);
//    }
//
//    @Override
//    public JsonDeserializer<IPersonalInformations> getPostDeserializer() {
//        return new PostDeserializer(this);
//    }

    public IPlayerManager getPersonManager() throws DAOException {
        return new PlayerManagerJPA(new PlayerDAOJPA(entityManagerFactory.createEntityManager()));
    }


    public IPersonalInformationsManager getPersonalInformationsManager() throws DAOException {
        return new PersonalInformationsManagerJPA(new PersonalInformationsDAOJPA(entityManagerFactory.createEntityManager()));
    }

}

