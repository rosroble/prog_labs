package ru.rosroble.client.ui.visualizeWindow;

import ru.rosroble.common.data.Ticket;

import java.awt.*;
import java.util.Objects;

public class TicketRectangle extends Rectangle {

    public int x;
    public int y;
    public int width;
    public int height;
    private Color color;
    private Ticket ticket;

    public TicketRectangle(int x, int y, int width, int height, Color color, Ticket ticket) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.ticket = ticket;
    }

    public Color getColor() {
        return color;
    }

    public String getText() {
        return String.valueOf(ticket.getId());
    }


    public boolean contains(int X, int Y) {
        if ((x < X) && (X < x + width) && (y < Y) && (Y < y + height)) return true;
        return false;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setColor(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
      //  if (!super.equals(o)) return false;
        TicketRectangle rectangle = (TicketRectangle) o;
        return ticket.equals(rectangle.ticket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket);
    }
}
