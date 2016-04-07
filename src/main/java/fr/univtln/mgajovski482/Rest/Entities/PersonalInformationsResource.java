package fr.univtln.mgajovski482.Rest.Entities;

import fr.univtln.mgajovski482.AppParameters;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.IPersonalInformations;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.Manager.Entities.PersonalInformationsManager.IPersonalInformationsManager;
import fr.univtln.mgajovski482.Rest.AbstractResourceRest;
import fr.univtln.mgajovski482.Rest.IRestManager;

import javax.ws.rs.Path;

/**
 * Created by Maxime on 02/04/2016.
 */
@Path("/players/personal_informations")
public class PersonalInformationsResource extends AbstractResourceRest<String, IPersonalInformations> {


    private final IPersonalInformationsManager personalInformationsManager;

    public PersonalInformationsResource () throws DAOException {
        personalInformationsManager = AppParameters.
                ELEMENTARY_FACTORY.getPersonalInformationsManager();
    }

    @Override
    public IRestManager<String, IPersonalInformations> getGenericManager() {
        return this.personalInformationsManager;
    }
}
