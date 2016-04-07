package fr.univtln.mgajovski482.Rest.Entities;

import fr.univtln.mgajovski482.AppParameters;
import fr.univtln.mgajovski482.JPA.Entities.Player.IPlayer;
import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.Manager.Entities.PlayerManager.IPlayerManager;
import fr.univtln.mgajovski482.Rest.AbstractResourceRest;
import fr.univtln.mgajovski482.Rest.IRestManager;

import javax.ws.rs.*;

/**
 * Created by Maxime on 31/03/2016.
 */
@Path("/players")
public class PlayerResource extends AbstractResourceRest<Integer, IPlayer> {

    private final IPlayerManager playerManager;

    public PlayerResource() throws DAOException {
        playerManager = AppParameters.ELEMENTARY_FACTORY.getPersonManager();
    }

//    @GET
//    @Produces("application/json")
//    public IPlayer findByEmail(@QueryParam("email") @DefaultValue("non") String email) throws DAOException {
//        IPlayer result = playerManager.findByEmail(email);
//        getGenericManager().done();
//        return result;
//    }
    @Override
    public IRestManager<Integer, IPlayer> getGenericManager() {
        return this.playerManager;
    }
}
