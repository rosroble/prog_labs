package ru.rosroble.common.data;

import java.io.Serializable;

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


}