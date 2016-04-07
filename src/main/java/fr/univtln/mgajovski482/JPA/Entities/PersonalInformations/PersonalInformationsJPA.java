package fr.univtln.mgajovski482.JPA.Entities.PersonalInformations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.univtln.mgajovski482.CustomConstraints.Email;
import fr.univtln.mgajovski482.CustomConstraints.FirstName;
import fr.univtln.mgajovski482.CustomConstraints.LastName;
import fr.univtln.mgajovski482.JPA.Entities.Player.PlayerJPA;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Maxime on 26/03/2016.
 */

@Entity
@Table(name = "PERSONAL_INFORMATIONS")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PersonalInformationsJPA implements IPersonalInformations, Serializable {

    @Id
    @Email
    @Column(name = "EMAIL")

    @XmlElement(name="email")
    private String email;

    @FirstName
    @Column(name = "FIRST_NAME")
    @XmlElement(name="firstName")
    private String firstName;

    @LastName
    @Column(name = "LAST_NAME")
    @XmlElement(name="lastName")
    private String lastName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BIRTH_DATE")
    //@BirthDate
    @XmlElement(name="birthDate")
    @JsonIgnore
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd MMM yyyy", timezone="CET")
    private Date birthDate;

    @XmlTransient
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personalInformations")
    private PlayerJPA owner;

    public PersonalInformationsJPA() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@JsonProperty("email")String email){
        this.email = email;
    }

    public PlayerJPA getOwner() {
        return owner;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName( @JsonProperty("firstName")String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(@JsonProperty("lastName")String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public void setBirthDate(@JsonProperty("birthDate")Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return display();
    }

}
