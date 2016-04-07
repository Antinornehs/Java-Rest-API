package fr.univtln.mgajovski482.JPA.DAO.PersonalInformations;

import fr.univtln.mgajovski482.JPA.DAO.AbstractDAOJPA;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.IPersonalInformations;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.PersonalInformationsJPA;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;

import javax.persistence.EntityManager;

/**
 * Created by Maxime on 02/04/2016.
 */
public class PersonalInformationsDAOJPA
        extends AbstractDAOJPA<String, IPersonalInformations, PersonalInformationsJPA>
        implements PersonalInformationsDAO{

    public PersonalInformationsDAOJPA(EntityManager entityManager) throws DAOException {
        super(entityManager);
    }
}
