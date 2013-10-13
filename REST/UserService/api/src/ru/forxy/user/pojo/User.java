package ru.forxy.user.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.Date;

@XmlRootElement(name = "user")
public class User {

    private String email;

    private byte[] password;

    private String firstName;

    private String lastName;

    private Character gender;

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
