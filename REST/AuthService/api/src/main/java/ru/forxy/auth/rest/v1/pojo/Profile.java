package ru.forxy.auth.rest.v1.pojo;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.forxy.common.pojo.SimpleJacksonDateDeserializer;
import ru.forxy.common.pojo.SimpleJacksonDateSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Document(collection = "profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = -1828924339216439884L;

    @Id
    private String email;

    private Date birthDate;

    private Set<String> telephones;

    private Address address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonSerialize(using = SimpleJacksonDateSerializer.class)
    public Date getBirthDate() {
        return birthDate;
    }

    @JsonDeserialize(using = SimpleJacksonDateDeserializer.class)
    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(Set<String> telephones) {
        this.telephones = telephones;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
