package ru.rosroble.common;

import ru.rosroble.common.data.Ticket;

import java.io.Serializable;
import java.util.ArrayList;

public class ShowcaseResponse extends Response implements Serializable {
    private static final long serialVersionUID = -207584844594971192L;
    private ArrayList<Ticket> list;

    public ShowcaseResponse(String responseInfo, boolean OK, ArrayList<Ticket> list) {
        super(responseInfo, OK);
        this.list = list;
    }

    public ArrayList<Ticket> getList() {
        return this.list;
    }

}
