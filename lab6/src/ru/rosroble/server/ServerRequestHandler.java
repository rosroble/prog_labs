package ru.rosroble.server;

import ru.rosroble.common.commands.*;
import ru.rosroble.common.Request;
import ru.rosroble.common.Response;
import ru.rosroble.common.data.Ticket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerRequestHandler {

    private ObjectInputStream clientReader;
    private ObjectOutputStream clientSender;
    private CollectionManager manager;

    public ServerRequestHandler(CollectionManager manager) {
        this.manager = manager;
    }

    public ObjectInputStream getClientReader() {
        return clientReader;
    }

    public void setClientReader(ObjectInputStream clientReader) {
        this.clientReader = clientReader;
    }

    public ObjectOutputStream getClientSender() {
        return clientSender;
    }

    public void setClientSender(ObjectOutputStream clientSender) {
        this.clientSender = clientSender;
    }

    public Response processClientRequest(Request r) {
        AbstractCommand command = r.getCommand();
        System.out.println("Обрабатываю команду " + command.getCommandType().toString());
        return executeRequest(command);
    }

    private Response executeRequest(AbstractCommand command) {
        try {
            CommandType type = command.getCommandType();


            switch (type) {
                case ADD:
                    AddCommand addCommand = (AddCommand) command;
                    Ticket t = addCommand.getTicket();
                    t.setId(manager.generateId());
                    return manager.add(t, addCommand.ifMax());
                case CLEAR:
                    return manager.clear();
                case COUNT_GR_THAN_PRICE:
                    CountGreaterThanPriceCommand countGrPrice = (CountGreaterThanPriceCommand) command;
                    return manager.countGreaterThanPrice(countGrPrice.getPrice());
                case COUNT_LESS_THAN_VENUE:
                    CountLessThenVenueCommand countLsVenue = (CountLessThenVenueCommand) command;
                    return manager.countLessThanVenue(countLsVenue.getVenue());
                case EXECUTE_SCRIPT:
                    // do later
                    break;
                case SHOW:
                    ShowCommand showCommand = (ShowCommand) command;
                    return manager.showCollection();
                case HELP:
                    HelpCommand helpCommand = (HelpCommand) command;
                    return manager.help();
                case REMOVE_LOWER:
                    RemoveLowerCommand removeLowerCommand = (RemoveLowerCommand) command;
                    return manager.removeLower(removeLowerCommand.getTicket());
                case REMOVE_BY_ID:
                    RemoveByIdCommand removeByIdCommand = (RemoveByIdCommand) command;
                    return manager.removeById(removeByIdCommand.getId());
                case UPDATE_ID:
                    UpdateIdCommand updateIdCommand = (UpdateIdCommand) command;
                    Ticket oldT = manager.getTicketById(updateIdCommand.getId());
                    Ticket newT = updateIdCommand.getNewTicket();
                    if (oldT == null) return new Response("Элемент с указанным id не найден.");
                    if (updateIdCommand.isUpdateName()) oldT.setName(newT.getName());
                    if (updateIdCommand.isUpdatePrice()) oldT.setPrice(newT.getPrice());
                    if (updateIdCommand.isUpdateCoordinates()) oldT.setCoordinates(newT.getCoordinates());
                    if (updateIdCommand.isUpdateRefundable()) oldT.setRefundable(newT.getRefundable());
                    if (updateIdCommand.isUpdateType()) oldT.setType(newT.getType());
                    if (updateIdCommand.isUpdateVenue()) oldT.setVenue(newT.getVenue());
                    return manager.updateId(updateIdCommand.getId(), oldT);
                case SORT:
                    SortCommand sortCommand = (SortCommand) command;
                    return manager.sort();
                case INFO:
                    InfoCommand infoCommand = (InfoCommand) command;
                    return manager.getInfo();
                case REMOVE_ANY_BY_RF:
                    RemoveAnyByRefundableCommand remAnyByRefCmd = (RemoveAnyByRefundableCommand) command;
                    return manager.removeByRefundable(remAnyByRefCmd.getRefundable());
                default:
                    break;
            }
        } catch (NullPointerException e) {
            System.out.println("Получен неверный запрос от клиента.");
        }
        return null;
    }

}
