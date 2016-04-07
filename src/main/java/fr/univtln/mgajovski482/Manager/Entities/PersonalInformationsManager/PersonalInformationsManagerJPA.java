package fr.univtln.mgajovski482.Manager.Entities.PersonalInformationsManager;

import fr.univtln.mgajovski482.JPA.DAO.PersonalInformations.PersonalInformationsDAOJPA;
import fr.univtln.mgajovski482.JPA.DAO.Player.PlayerDAOJPA;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.IPersonalInformations;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.Manager.AbstractManagerJPA;
import fr.univtln.mgajovski482.Rest.utils.Page;

/**
 * Created by Maxime on 02/04/2016.
 */
public class PersonalInformationsManagerJPA
        extends AbstractManagerJPA<String, IPersonalInformations>
        implements IPersonalInformationsManager{


    PersonalInformationsDAOJPA personalInformationsDAOJPA;

    public PersonalInformationsManagerJPA (PersonalInformationsDAOJPA personalInformationsDAOJPA) {
        super(personalInformationsDAOJPA);
        this.personalInformationsDAOJPA= personalInformationsDAOJPA;
    }


    @Override
    public Page findAllByPage(boolean reverse, int pagenumber, int pagesize) throws DAOException {
        return super.findAllByPage(reverse, pagenumber, pagesize);

    }
}
