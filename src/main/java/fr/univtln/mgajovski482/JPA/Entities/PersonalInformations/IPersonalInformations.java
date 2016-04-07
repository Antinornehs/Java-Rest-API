package fr.univtln.mgajovski482.JPA.Entities.PersonalInformations;

import fr.univtln.mgajovski482.CustomConstraints.FirstName;
import fr.univtln.mgajovski482.CustomConstraints.LastName;
import fr.univtln.mgajovski482.JPA.Entities.Player.PlayerJPA;
import fr.univtln.mgajovski482.JPA.IdentifiableEntity;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * Created by Maxime on 26/03/2016.
 */
public interface IPersonalInformations extends IdentifiableEntity<String>{

    String  getFirstName();
    void    setFirstName( @FirstName String firstName);

    String  getLastName();
    void    setLastName(@LastName String lastName);

    Date    getBirthDate();
    void    setBirthDate(@Past Date birthDate);

    void    setEmail(String email);
    String  getEmail();

    PlayerJPA getOwner() ;

    default String display(){
        return  "PersonalInformationsJPA{" + '\'' +
                "email ='" + getEmail() + '\'' +
                ", firstName ='" + getFirstName() + '\'' +
                ", lastName ='" + getLastName() + '\'' +
                ", birthDate =" + getBirthDate() +'}';
    }
}
