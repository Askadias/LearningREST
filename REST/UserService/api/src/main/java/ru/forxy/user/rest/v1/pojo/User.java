package ru.forxy.user.rest.v1.pojo;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.forxy.common.pojo.SimpleJacksonDateDeserializer;
import ru.forxy.common.pojo.SimpleJacksonDateSerializer;

import java.util.Date;

@Document
public class User {

    @Id
    private String email;

    private byte[] password;

    private String login;

    private String firstName;

    private String lastName;

    private Character gender;

    private Date birthDate;

    private Date createDate = new Date();

    public User() {
    }

    public User(final String email, final byte[] password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(final byte[] password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(final Character gender) {
        this.gender = gender;
    }

    @JsonSerialize(using = SimpleJacksonDateSerializer.class)
    public Date getBirthDate() {
        return birthDate;
    }

    @JsonDeserialize(using = SimpleJacksonDateDeserializer.class)
    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

    @JsonSerialize(using = SimpleJacksonDateSerializer.class)
    public Date getCreateDate() {
        return createDate;
    }

    @JsonDeserialize(using = SimpleJacksonDateDeserializer.class)
    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return String.format("{email=%s, login=%s, firstName=%s, lastName=%s, gender=%c, birthDate=%s, createDate=%s}",
                email, login, firstName, lastName, gender, birthDate, createDate);
    }
}
