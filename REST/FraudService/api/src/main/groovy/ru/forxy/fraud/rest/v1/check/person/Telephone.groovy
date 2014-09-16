package ru.forxy.fraud.rest.v1.check.person;

import ru.forxy.fraud.rest.v1.check.Entity;

class Telephone extends Entity {
    String areaCode;
    String countryAccessCode;
    String phoneNumber;
    Type type;

    enum Type {
        Home,
        Mobile,
        Business
    }
}
