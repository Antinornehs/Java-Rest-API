package fr.univtln.mgajovski482;

import fr.univtln.mgajovski482.JPA.DAO.FactoryJPA;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Maxime on 31/03/2016.
 */
public class AppParameters {
    /**
     * An EntityManagerFactory shared by the whole application.
     */
    public static final EntityManagerFactory EMF = Persistence
            .createEntityManagerFactory("testpostgresqllocal");

    public static final FactoryJPA ELEMENTARY_FACTORY = new FactoryJPA(EMF);
}