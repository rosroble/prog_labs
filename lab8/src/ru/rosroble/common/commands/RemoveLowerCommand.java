package ru.rosroble.common.commands;

import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.data.Ticket;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;
import ru.rosroble.common.Updater;

public class RemoveLowerCommand extends AbstractCommand {

    private Ticket ticket;
    public RemoveLowerCommand() {
        super(CommandType.REMOVE_LOWER);
    }

    public RemoveLowerCommand(Ticket t) {
        super(CommandType.REMOVE_LOWER);
        ticket = t;
    }

    @Override
    public Request execute(String[] commandSplit) {
        try {
            Parsers.verify(commandSplit, 0);
            System.out.println("Укажите параметры сравниваемого объекта:");
            this.ticket = new Ticket(Updater.updateName(),
                    Updater.updateCoordinates(),
                    Updater.updatePrice(),
                    Updater.updateRefundable(),
                    Updater.updateType(),
                    Updater.updateVenue());
            return getRequest();
        } catch (InvalidAmountOfArgumentsException e) {
            e.printMessage();
        }
        return null;
    }

    public Ticket getTicket() {
        return this.ticket;
    }
}
