package ru.rosroble.common.commands;

import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.data.Coordinates;
import ru.rosroble.common.data.Ticket;
import ru.rosroble.common.data.TicketType;
import ru.rosroble.common.data.Venue;
import ru.rosroble.common.exceptions.DomainViolationException;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;
import ru.rosroble.common.Updater;

import java.util.NoSuchElementException;

public class AddCommand extends AbstractCommand {
    private Ticket ticket;
    private boolean ifMax;

    public AddCommand(boolean ifMax) {
        super(CommandType.ADD);
        this.ifMax = ifMax;
    }

    public AddCommand(Ticket t) {
        super(CommandType.ADD);
        ticket = t;
        ifMax = false;
    }

    public Request execute(String[] commandSplit) {
        try {
            Parsers.verify(commandSplit, 3);
            String name = commandSplit[1];
            double price = Parsers.parseThePrice(commandSplit[2]);
            boolean refundable = Parsers.parseTheRefundable(commandSplit[3]);
            Coordinates coordinates = Updater.updateCoordinates();
            TicketType ticketType = Updater.updateType();
            Venue venue = Updater.updateVenue();
            Ticket t = new Ticket(name,
                    coordinates,
                    price,
                    refundable,
                    ticketType,
                    venue);
            this.ticket = t;
            return getRequest();
        } catch (NoSuchElementException e) {
            System.out.println("Получен сигнал конца ввода. Завершение.");
        } catch (DomainViolationException dve) {
            dve.printMessage();
        } catch (InvalidAmountOfArgumentsException e) {
            e.printMessage();
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат данных.");
        }
        return new Request(null);
    }

    public Ticket getTicket() {
        return ticket;
    }

    public boolean ifMax() {
        return ifMax;
    }
}
