package ru.forxy.fraud.rest.pojo.product.travel;

import ru.forxy.fraud.rest.pojo.location.Location;
import ru.forxy.fraud.rest.pojo.person.Traveler;
import ru.forxy.fraud.rest.pojo.product.Product;

import java.util.Date;
import java.util.List;

public class TravelProduct extends Product {
    protected List<Traveler> travelers;
    protected Date dateStart;
    protected Date dateEnd;
    protected List<Location> locations;
    protected Type type;

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public List<Traveler> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<Traveler> travelers) {
        this.travelers = travelers;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public enum Type {
        Air,
        Hotel,
        DestinationExperience,
        OpaqueHotel,
        Auto,
        Cruise
    }
}
