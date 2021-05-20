package ru.rosroble.common.commands;

import ru.rosroble.common.Request;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable {
    protected final CommandType commandType;
    public abstract Request execute(String[] commandSplit);

    public CommandType getCommandType() {
        return commandType;
    }



    protected AbstractCommand(CommandType commandType) {
        this.commandType = commandType;
    }


    public Request getRequest() {
        Request req = new Request(this);
        return req;
    }

}
