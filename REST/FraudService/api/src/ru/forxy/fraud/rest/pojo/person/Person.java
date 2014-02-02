package ru.forxy.fraud.rest.pojo.person;

import ru.forxy.fraud.rest.pojo.Entity;
import ru.forxy.fraud.rest.pojo.location.Address;

public class Person extends Entity {
    protected String firstName;
    protected String lastName;
    protected String middleName;
    protected String suffixName;
    protected Address address;

}