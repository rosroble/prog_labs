package ru.rosroble.common.commands;

import ru.rosroble.common.Request;

public class TicketShowcaseCommand extends AbstractCommand {

    public TicketShowcaseCommand() {
        super(CommandType.TICKET_SHOWCASE);
    }

    @Override
    public Request execute(String[] commandSplit) {
        return null; // this is for GUI-only, so this method doesn't need to be impl..
    }
}
