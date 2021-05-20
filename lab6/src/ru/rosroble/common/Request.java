package ru.rosroble.common;

import ru.rosroble.common.commands.AbstractCommand;

import java.io.Serializable;

public class Request implements Serializable {

    private static final long serialVersionUID = -6213323027290265345L;
    private AbstractCommand command;

    public Request(AbstractCommand command) {
        this.command = command;
    }

    public AbstractCommand getCommand() {
        return this.command;
    }

    public boolean isEmpty() {
        return command == null;
    }

}
