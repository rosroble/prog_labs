package ru.rosroble.common.data;

import java.io.Serializable;

/**
 * Represents the Venue of a ticket.
 */
public class Venue implements Comparable<Venue>, Serializable {
    private static final long serialVersionUID = 3888450949293392826L;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private long capacity; //Значение поля должно быть больше 0
    private VenueType type; //Поле может быть null
    private Address address; //Поле может быть null

    public Venue(String name, long capacity, VenueType type, Address address) {
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.address = address;
    }

    public Venue(Long id, String name, long capacity, VenueType type, Address address) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.address = address;
    }

    public Venue() {}

    private static Long nextId;

    static {
        nextId = 1l;
    }

    {
        this.id = nextId;
        nextId += 1;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public VenueType getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(VenueType type) {
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public int compareTo(Venue o) {
        return Long.compare(this.capacity, o.getCapacity());
    }
}