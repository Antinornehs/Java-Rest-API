package fr.univtln.mgajovski482.JPA.Entities.Player;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.univtln.mgajovski482.CustomConstraints.NickName;
import fr.univtln.mgajovski482.JPA.Entities.PersonalInformations.PersonalInformationsJPA;
//import org.codehaus.jackson.annotate.JsonTypeName;
//import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Maxime on 26/03/2016.
 */
@NamedQueries({
        @NamedQuery(name = "iplayer.findAll", query = "select p from PlayerJPA p"),
        @NamedQuery(name = "iplayer.removeAll", query = "delete from PlayerJPA "),
        @NamedQuery(name = "player.findByEmail", query = "select p from PlayerJPA p where p.personalInformations.email= :email"),
        @NamedQuery(name = "iperson.deleteByEmail", query = "delete from PlayerJPA P where p.personalInformations.email= :email"),
        @NamedQuery(name = "iperson.delete", query = "delete from PlayerJPA P where p.id= :id")}
)

@Entity
@Table( name = "PLAYER")
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
//@CascadeOnDelete
@JsonTypeName(value = "player")
public class PlayerJPA implements IPlayer, Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name ="ID")
    @XmlElement(name="ID")
    private int id;


    @Column(unique = true,name = "NICK_NAME")
    @NickName
    @XmlElement(name="nickName")
    private String nickName;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true ,name = "EMAIL")

    @XmlElement(name="personalInformations")
    private PersonalInformationsJPA personalInformations;

    public PlayerJPA(){

    }

    public int getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(@JsonProperty("nickName") String nickName) {
        this.nickName = nickName;
    }

    public PersonalInformationsJPA getPersonalInformations() {
        return personalInformations;
    }

    public void setPersonalInformations(@JsonProperty("personalInformations")PersonalInformationsJPA personalInformations) {
        this.personalInformations = personalInformations;
    }

    @Override
    public String toString() {
        return display();
    }
}
