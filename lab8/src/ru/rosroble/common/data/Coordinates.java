package ru.rosroble.common.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Contains coordinates info of a Ticket
 */
public class Coordinates implements Serializable {
    private static final long serialVersionUID = -5107483809114366253L;
    private Float x; //Значение поля должно быть больше -355, Поле не может быть null
    private long y;



    public Coordinates(Float x, long y) {
        this.x = x;
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return y == that.y && Objects.equals(x, that.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}