package ru.rosroble.common.data;


import java.io.Serializable;

/**
 * Contains a location info of an address
 */
public class Location implements Serializable {
    private static final long serialVersionUID = -205783213652228030L;
    private Long x; //Поле не может быть null
    private Integer y; //Поле не может быть null
    private long z;
    private String name; //Строка не может быть пустой, Поле не может быть null

    public Location() {
    }

    public Location(Long x, Integer y, long z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public Long getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    public void setX(long locX) {
        this.x = locX;
    }

    public void setY(int locY) {
        this.y = locY;
    }

    public void setZ(long locZ) {
        this.z = locZ;
    }

    public void setName(String name) {
        this.name = name;
    }
}