package ru.rosroble.common.commands;

import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.data.Ticket;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;
import ru.rosroble.common.Updater;

public class UpdateIdCommand extends AbstractCommand {

    private long id;
    private Ticket newTicket;

    private boolean updateName;
    private boolean updatePrice;
    private boolean updateRefundable;
    private boolean updateVenue;
    private boolean updateType;
    private boolean updateCoordinates;

    public boolean isUpdateName() {
        return updateName;
    }

    public boolean isUpdatePrice() {
        return updatePrice;
    }

    public boolean isUpdateRefundable() {
        return updateRefundable;
    }

    public boolean isUpdateVenue() {
        return updateVenue;
    }

    public boolean isUpdateType() {
        return updateType;
    }

    public boolean isUpdateCoordinates() {
        return updateCoordinates;
    }

    public UpdateIdCommand() {
        super(CommandType.UPDATE_ID);
    }

    public long getId() {
        return id;
    }

    public Ticket getNewTicket() {
        return newTicket;
    }

    @Override
    public Request execute(String[] commandSplit) {
        try {
            Parsers.verify(commandSplit, 1);
            long id = Long.parseLong(commandSplit[1]);
            this.id = id;
            Ticket t = new Ticket();
            if (Updater.ask("Хотите изменить имя билета?")) {
                t.setName(Updater.updateName());
                updateName = true;
            }
            if (Updater.ask("Хотите изменить цену билета?")) {
                t.setPrice(Updater.updatePrice());
                updatePrice = true;
            }
            if (Updater.ask("Хотите изменить статус возвратности билета?")) {
                t.setRefundable(Updater.updateRefundable());
                updateRefundable = true;
            }
            if (Updater.ask("Хотите изменить параметр Venue билета?")) {
                t.setVenue(Updater.updateVenue());
                updateVenue = true;
            }
            if (Updater.ask("Хотите изменить тип билета?")) {
                t.setType(Updater.updateType());
                updateType = true;
            }
            if (Updater.ask("Хотите изменить координаты билета?")) {
                t.setCoordinates(Updater.updateCoordinates());
                updateCoordinates = true;
            }
            this.newTicket = t;
            return getRequest();
        } catch (InvalidAmountOfArgumentsException e) {
            e.printMessage();
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат аргумента.");
        }
        return null;
    }
}
