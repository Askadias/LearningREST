package ru.forxy.user.pojo;

import ru.forxy.common.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.Date;


@Entity
@Table(name = "user", schema = Constants.SCHEMA_NAME + "@user")
@XmlRootElement(name = "user")
public class User {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private byte[] password;

    @Column(name = "login")
    private String login;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "first_name")
    private String lastName;

    @Column(name = "gender")
    private Character gender;

    @Column(name = "birth_date")
    private Date birthDate;

    public User() {
    }

    public User(String email, byte[] password) {
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
        return String.format("{email=%s,password=%s}", email, Arrays.toString(password));
    }
}