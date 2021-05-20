package ru.rosroble.common.data;

import java.io.Serializable;

/**
 * Contains address properties of a Venue
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 6372612397367151353L;
    private String street; //Строка не может быть пустой, Поле может быть null
    private Location town; //Поле не может быть null

    public Address(String street, Location town) {
        this.street = street;
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public Location getTown() {
        return town;
    }
}