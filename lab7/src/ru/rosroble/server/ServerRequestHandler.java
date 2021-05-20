package ru.rosroble.server;

import ru.rosroble.common.commands.*;
import ru.rosroble.common.Request;
import ru.rosroble.common.Response;
import ru.rosroble.common.data.Ticket;
import ru.rosroble.common.exceptions.InsufficientPermissionException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ServerRequestHandler {

    private static final int TOKEN_LIST_MAX_SIZE = 100;
    private CollectionManager manager;
    private DatabaseHandler dbHandler;
    private List<String> tokenList = new LinkedList<>();
    private AuthManager authManager = new AuthManager();
    private ExecutorService forkJoinPool = ForkJoinPool.commonPool();
    private volatile long nextTicketId;
    private volatile long nextVenueId;

    public ServerRequestHandler(CollectionManager manager, DatabaseHandler dbHandler) {
        this.manager = manager;
        this.dbHandler = dbHandler;
        this.nextTicketId = dbHandler.getNextTicketId();
        this.nextVenueId = dbHandler.getNextVenueId();
    }


    public Response processClientRequest(Request r) {
        RequestExecutor executor = new RequestExecutor(r);
        Future<Response> responseFuture = forkJoinPool.submit(executor);
        while (true) {
            if (responseFuture.isDone()) {
                try {
                    return responseFuture.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            // Thread.yield();
        }
    }

    private synchronized Response executeRequest(AbstractCommand command, String initiator) {
        try {
            CommandType type = command.getCommandType();
            switch (type) {
                case ADD:
                    AddCommand addCommand = (AddCommand) command;
                    Ticket t = addCommand.getTicket();
                    t.setId(++nextTicketId);
                    t.getVenue().setId(++nextVenueId);
                    Ticket.addToIdMap(t);
                    if (dbHandler.insertTicket(t, initiator)) return manager.add(t, addCommand.ifMax()); //TODO: add_if_max
                    return new Response("Ошибка при добавлении объекта в базу данных.");
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
                    return manager.removeLower(removeLowerCommand.getTicket(), initiator);
                case REMOVE_BY_ID:
                    RemoveByIdCommand removeByIdCommand = (RemoveByIdCommand) command;
                    long idToRemove = removeByIdCommand.getId();
                    try {
                        if (dbHandler.removeTicketByID(idToRemove, initiator)) return manager.removeById(idToRemove);
                        else return new Response("Элемент с указанным id не существует.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        dbHandler.rollback();
                        return new Response("Ошибка выполнения запроса", false);
                    } catch (InsufficientPermissionException e) {
                        return new Response("Запрос отклонен: недостаточно прав.");
                    }
                case UPDATE_ID:
                    UpdateIdCommand updateIdCommand = (UpdateIdCommand) command;
                    long id = updateIdCommand.getId();
                    Ticket oldT = manager.getTicketById(updateIdCommand.getId());
                    if (oldT == null) return new Response("Элемент с указанным id не найден.");
                    if(!dbHandler.isOwnerOf(updateIdCommand.getId(), initiator)) return new Response("Недостаточно прав.");
                    Ticket newT = updateIdCommand.getNewTicket();
                    newT.setId(updateIdCommand.getId());
                    if (updateIdCommand.isUpdateName()) {
                        dbHandler.updateTicketName(newT.getName(), id);
                        oldT.setName(newT.getName());
                    }
                    if (updateIdCommand.isUpdatePrice()) {
                        dbHandler.updateTicketPrice(newT.getPrice(), id);
                        oldT.setPrice(newT.getPrice());
                    }
                    if (updateIdCommand.isUpdateCoordinates()) {
                        dbHandler.updateTicketCoordinates(newT.getCoordinates(), id);
                        oldT.setCoordinates(newT.getCoordinates());
                    }
                    if (updateIdCommand.isUpdateRefundable()) {
                        dbHandler.updateTicketRefundable(newT.getRefundable(), id);
                        oldT.setRefundable(newT.getRefundable());
                    }
                    if (updateIdCommand.isUpdateType()) {
                        dbHandler.updateTicketType(newT.getType(), id);
                        oldT.setType(newT.getType()) ;
                    }
                    if (updateIdCommand.isUpdateVenue()) {
                        newT.getVenue().setId(oldT.getVenue().getId());
                        dbHandler.updateTicketVenue(newT.getVenue(), newT.getVenue().getId());
                        oldT.setVenue(newT.getVenue());
                    }
                    return manager.updateId(updateIdCommand.getId(), oldT);
                case SORT:
                    SortCommand sortCommand = (SortCommand) command;
                    return manager.sort();
                case INFO:
                    InfoCommand infoCommand = (InfoCommand) command;
                    return manager.getInfo();
                case REMOVE_ANY_BY_RF:
                    RemoveAnyByRefundableCommand remAnyByRefCmd = (RemoveAnyByRefundableCommand) command;
                    return manager.removeByRefundable(remAnyByRefCmd.getRefundable(), initiator);
                default:
                    break;
            }
        } catch (NullPointerException e) {
            System.out.println("Получен неверный запрос от клиента.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Непредвиденная ошибка SQL.");
            e.printStackTrace();
        }
        return null;
    }


    private class AuthManager {
        public Response handleAuth(AbstractCommand command) {
            AuthCommand authCommand = (AuthCommand) command;
            try {
                String username = authCommand.getUsername();
                String password = authCommand.getPassword();
                if (authCommand.getAuthType().equals(AuthCommand.AuthType.REGISTER)) {
                    if(dbHandler.registerUser(username, password))
                        return new Response("Пользователь " + username + " успешно зарегистрирован", true);
                    else
                        return new Response("Пользователь с таким именем уже существует.", false);
                }
                if (authCommand.getAuthType().equals(AuthCommand.AuthType.LOGIN)) {
                    boolean isUserFound = dbHandler.validateUser(username, password);
                    if (isUserFound) return new Response("Авторизация успешна.", true, generateServerToken());
                    else return new Response("Ошибка авторизации. Проверьте правильность данных", false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response("Ошибка исполнения запроса. Повторите попытку позже.", false);
            }
            return null;
        }

        private String generateServerToken() {
            String token = Long.toHexString(Double.doubleToLongBits(Math.random()));
            tokenList.add(token);
            if (tokenList.size() > TOKEN_LIST_MAX_SIZE ) tokenList.remove(1);
            return token;
        }
    }

    private class RequestExecutor implements Callable<Response> {

        private Request r;

        public RequestExecutor(Request r) {
            this.r = r;
        }
        @Override
        public Response call() throws Exception {
            AbstractCommand command = r.getCommand();
            String initiator = r.getInitiator();
            System.out.println("Обрабатываю команду " + command.getCommandType().toString());
            if (command.getCommandType().equals(CommandType.AUTH)) return authManager.handleAuth(command);
            else if(tokenList.contains(r.getToken())) return executeRequest(command, initiator);
            else return new Response("Отказ в обработке: требуется авторизация");
        }
    }
}
