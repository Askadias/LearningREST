package ru.forxy.fraud.rest.v1.check.product.travel

import ru.forxy.fraud.rest.v1.check.location.Location
import ru.forxy.fraud.rest.v1.check.person.Traveler
import ru.forxy.fraud.rest.v1.check.product.Product

class TravelProduct extends Product {
    List<Traveler> travelers;
    Date dateStart;
    Date dateEnd;
    List<Location> locations;
    Type type;

    enum Type {
        Air,
        Hotel,
        DestinationExperience,
        OpaqueHotel,
        Auto,
        Cruise
    }
}
