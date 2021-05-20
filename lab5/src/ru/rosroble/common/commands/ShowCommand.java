package ru.rosroble.common.commands;

import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;

public class ShowCommand extends AbstractCommand {

    public ShowCommand() {
        super(CommandType.SHOW);
    }

    @Override
    public Request execute(String[] commandSplit) {
        try {
            Parsers.verify(commandSplit, 0);
            return getRequest();
        } catch (InvalidAmountOfArgumentsException e) {
            e.printMessage();
        }
        return null;
    }
}
