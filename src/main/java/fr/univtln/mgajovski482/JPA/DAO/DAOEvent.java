package fr.univtln.mgajovski482.JPA.DAO;

import fr.univtln.mgajovski482.JPA.IdentifiableEntity;

/**
 * Created by Maxime on 31/03/2016.
 */
public class DAOEvent {
    private Type type;
    private IdentifiableEntity entity;
    public DAOEvent(Type type, IdentifiableEntity entity) {
        this(type);
        this.entity = entity;
    }

    public DAOEvent(Type type) {
        this.type = type;
    }

    public enum Type {CREATE, UPDATE, DELETE}

    public Type getType() {
        return type;
    }

    public IdentifiableEntity getEntity() {
        return entity;
    }
}