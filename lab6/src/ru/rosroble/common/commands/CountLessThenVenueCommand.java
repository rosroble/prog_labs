package ru.rosroble.common.commands;

import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.data.Venue;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;
import ru.rosroble.common.Updater;

public class CountLessThenVenueCommand extends AbstractCommand{
    private Venue venue;

    public CountLessThenVenueCommand() {
        super(CommandType.COUNT_LESS_THAN_VENUE);
    }


    @Override
    public Request execute(String[] commandSplit) {
        try {
            Parsers.verify(commandSplit, 0);
            this.venue = new Venue(
                    Updater.updateName(),
                    Updater.updateVenueCapacity(),
                    Updater.updateVenueType(),
                    Updater.updateVenueAddress());
            return getRequest();
        } catch (InvalidAmountOfArgumentsException e) {
            e.printMessage();
        }
        return new Request(null);
    }

    public Venue getVenue() {
        return this.venue;
    }
}
