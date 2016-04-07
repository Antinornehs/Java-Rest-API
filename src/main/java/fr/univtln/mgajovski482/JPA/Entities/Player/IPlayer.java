package fr.univtln.mgajovski482.JPA.Entities.Player;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.PersonalInformationsJPA;
import fr.univtln.mgajovski482.JPA.IdentifiableEntity;
//import org.codehaus.jackson.annotate.JsonTypeName;
//import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 * Created by Maxime on 26/03/2016.
 */
//@JsonDeserialize(as=PlayerJPA.class)
@JsonDeserialize(as=PlayerJPA.class)
public interface IPlayer extends IdentifiableEntity<Integer>{


    int    getId();

    String  getNickName();
    void    setNickName(String nickName);

    PersonalInformationsJPA getPersonalInformations();
    void setPersonalInformations(PersonalInformationsJPA personalInformations);

    default String display(){
        return  "Id = "         + getId()       + "\n" +
                "nickName = "   + getNickName() + "\n" +
                getPersonalInformations()       + "\n";
    }
}
