package ru.forxy.fraud.rest.v1.check.person

import ru.forxy.fraud.rest.v1.check.Entity
import ru.forxy.fraud.rest.v1.check.location.Address

class Person extends Entity {
    String prefixName;
    String firstName;
    String middleName;
    String lastName;
    String suffixName;
    Address address;
    String email;
    Character gender;
    Integer age;
    Date birthDate;
    List<Telephone> telephones;
}
