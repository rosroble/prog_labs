package ru.rosroble.common.data;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.HashMap;

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
    private Venue venue; //Поле может быть null

    public Ticket(String name, Coordinates coordinates, double price, boolean refundable, TicketType type, Venue venue) {
        this.name = name;
        this.coordinates = coordinates;
        this.price = price;
        this.refundable = refundable;
        this.type = type;
        this.venue = venue;
    }

    public Ticket() {}

    private static long nextId;

    private static HashMap<Long, Ticket> ticketIdMap;

    static {
        nextId = 1;
        ticketIdMap = new HashMap<>();
    }

    {
        this.id = nextId;
        nextId += 1;
        creationDate = LocalDate.now();
        // coordinates = new Coordinates();
        ticketIdMap.put(this.id, this);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) { this.id = id; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) { this.name = name; }

    public static void resetId() {
        nextId = 1;
    }

    public static Ticket getTicketById(long id) {
        return ticketIdMap.get(id);
    }

    public static void removeFromIdMap(long id) {
        ticketIdMap.remove(id);
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

    @Override
    public String toString() {
        return String.format("%s, id: %d, price: %.2f\nx: %.2f, y: %d", name, id, price, coordinates.getX(), coordinates.getY());
    }


    @Override
    public int compareTo(Ticket o) {
        return Double.compare(this.price, o.getPrice());
    }
}