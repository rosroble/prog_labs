package ru.rosroble.common.commands;

import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.exceptions.DomainViolationException;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;

public class RemoveAnyByRefundableCommand extends AbstractCommand {

    private boolean refundable;

    public RemoveAnyByRefundableCommand() {
        super(CommandType.REMOVE_ANY_BY_RF);
    }

    @Override
    public Request execute(String[] commandSplit) {
        try {
            Parsers.verify(commandSplit, 1);
            this.refundable = Parsers.parseTheRefundable(commandSplit[1]);
        } catch (DomainViolationException e) {
            e.printMessage();
        } catch (InvalidAmountOfArgumentsException e) {
            e.printMessage();
        }
        return getRequest();
    }

    public boolean getRefundable() {
        return refundable;
    }
}
