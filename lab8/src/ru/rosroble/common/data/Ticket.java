package ru.rosroble.common.data;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a ticket. Objects of this class are stored in the collection.
 */
public class Ticket implements Comparable<Ticket>, Serializable {
    private static final long serialVersionUID = 7478347378541007490L;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private double price; //Значение поля должно быть больше 0
    private boolean refundable;
    private TicketType type; //Поле может быть null
    private Venue venue;
    private String owner;

    public Ticket(String name, Coordinates coordinates, double price, boolean refundable, TicketType type, Venue venue) {
        this.name = name;
        this.coordinates = coordinates;
        this.price = price;
        this.refundable = refundable;
        this.type = type;
        this.venue = venue;
    }

    public Ticket(long id, String name, Coordinates coordinates, double price, boolean refundable, TicketType type, Venue venue) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.price = price;
        this.refundable = refundable;
        this.type = type;
        this.venue = venue;
        // ticketIdMap.put(this.id, this);
    }

    public Ticket() {}

    private static HashMap<Long, Ticket> ticketIdMap;

    static {
        ticketIdMap = new HashMap<>();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) { this.id = id; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) { this.name = name; }

    public static Ticket getTicketById(long id) {
        return ticketIdMap.get(id);
    }

    public static void removeFromIdMap(long id) {
        ticketIdMap.remove(id);
    }

    public static void addToIdMap(Ticket t) {
        ticketIdMap.put(t.getId(), t);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate date) {
        this.creationDate = creationDate;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public boolean getRefundable() {
        return refundable;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return String.format("%s, id: %d, price: %.2f\nx: %.2f, y: %d", name, id, price, coordinates.getX(), coordinates.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Ticket o) {
        return Double.compare(this.price, o.getPrice());
    }
}