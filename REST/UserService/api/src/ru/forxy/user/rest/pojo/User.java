package ru.forxy.user.rest.pojo;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import ru.forxy.common.pojo.SimpleJacksonDateSerializer;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "user")
public class User {

    private String email;

    private byte[] password;

    private String login;

    private String firstName;

    private String lastName;

    private Character gender;

    @JsonSerialize(using = SimpleJacksonDateSerializer.class)
    private Date birthDate;

    public User() {
    }

    public User(final String email, final byte[] password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return String.format("{email=%s, login=%s, firstName=%s, lastName=%s, gender=%c, birthDate=%s}", email, login,
                firstName, lastName, gender, birthDate);
    }
}
