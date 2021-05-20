package ru.rosroble.server;

import ru.rosroble.common.Response;
import ru.rosroble.common.data.*;
import ru.rosroble.common.exceptions.InsufficientPermissionException;


import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * A class used to make operations on the collection. Contains methods, which are executed after CommandProcessor request.
 */
public class CollectionManager {
    private long nextId;
    private ArrayList<Ticket> collection;
    private static HashMap<String, String> description;
    private String listType;
    private final Date initDate;
    private FileManager fileManager;
    private ReadWriteLock collectionLocker;
    private DatabaseHandler dbHandler;

    public CollectionManager(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    {
        initDate = new Date();
        description = new HashMap<String, String>();
        //fileManager = new FileManager(collection, "envXML");
        collectionLocker = new ReentrantReadWriteLock();
        // load();
        // nextId = collection.size();
        description.put("help", "вывести справку по доступным командам"); // done
        description.put("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д."); // done
        description.put("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении"); // done
        description.put("add", "добавить новый элемент в коллекцию | args: <element>"); // done
        description.put("update", "обновить значение элемента коллекции, id которого равен заданному | args: <id> <element>"); // done
        description.put("remove_by_id", "удалить элемент из коллекции по его id | args: <id>"); // done
        description.put("clear", "очистить коллекцию"); // done
        description.put("save", "сохранить коллекцию в файл"); // done
        description.put("execute_script", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме | args: <filename>"); // done
        description.put("exit", "завершить без сохранения в файл"); // done
        description.put("add_if_max", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции | args: <element>"); // done
        description.put("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный"); // done
        description.put("sort", "отсортировать в естественном порядке"); // done
        description.put("remove_any_by_refundable", "удалить из коллекции один элемент, значение поля refundable которого эквивалентно заданному | args: <refundable>"); // done
        description.put("count_less_than_venue", "вывести количество элементов, значение поля venue которых меньше заданного | args: <venue>"); // done
        description.put("count_greater_than_price", "вывести количество элементов, значение поля price которых больше заданного | args: <price>"); // done
    }

    public void init() {
        collection = dbHandler.loadCollectionFromDB();
        listType = collection.getClass().getSimpleName();
    }

    /**
     * Prints out the list of available commands with their descriptions.
     */
    public Response help() {
        StringBuilder msg = new StringBuilder();
        Set keySet = description.keySet();
        keySet.stream().forEach(key -> msg.append(key + " - " + description.get(key) + "\n"));
        //System.out.println(msg.toString());
        return new Response(msg.toString());
        // TODO: how to send this to a client? maybe make this client-side?
    }

//    /**
//     * @param cmd A command to show the description of.
//     */
//    public void help(String cmd) {
//        String desc = description.get(cmd);
//        if (desc == null)
//            System.out.println("Команды не существует. Введите help, чтобы отобразить список всех комманд");
//        else
//            System.out.println(cmd + ": " + desc);
//    }

    /**
     * @return String representation of an collection.
     */
    @Override
    public String toString() {
        return "Тип коллекции: " + listType + "\nДата инициализации: " + initDate + "\nКоличество объектов: " + collection.size();
    }

    /**
     * Shows the objects stored in collection with short description.
     */
    public Response showCollection() {
        collectionLocker.readLock().lock();
        StringBuilder sb;
        try {
            sb = new StringBuilder();
            collection.forEach(ticket -> sb.append(ticket.toString() + "\n"));
        } finally {
            collectionLocker.readLock().unlock();
        }
        return new Response(sb.toString());

    }


    public Response add(Ticket t, boolean ifMax) {
        collectionLocker.writeLock().lock();
        try {
            if ((!ifMax) || (collection.stream().filter(ticket -> ticket.compareTo(t) < 0).count() == collection.size())) {
                collection.add(t);
            }
        } finally {
            collectionLocker.writeLock().unlock();
        }
        return new Response("Добавлен объект: " + t.toString());
    }

    /**
     * Reads and loads the collection from the XML-File via FileManager.
     */
    @Deprecated
    public void load() {
        this.collection = fileManager.parseCollectionFromFile();
        fileManager.setCollection(collection);
    }


    /**
     * Consequently updates all the fields of an object with id specified. Asks user if they wish to update the field.
     * @param id - id of an object to update.
     */
    public Response updateId(long id, Ticket newT) {
        collectionLocker.writeLock().lock();
        try {
            collection.remove(Ticket.getTicketById(id));
            collection.add(newT);
            sort();
        } finally {
            collectionLocker.writeLock().unlock();
        }
        return new Response("Элемент с указанным id успешно обновлён.");
    }

    /**
     * @param id Id of an object to be removed.
     */
    public Response removeById(long id) {
        collectionLocker.writeLock().lock();
        try {
            collection.remove(collection.stream().filter(x -> x.getId() == id).findFirst().orElse(null)); // if the element is not found remove() returns false
            Ticket.removeFromIdMap(id); // remove the element from (id -> Ticket) hashmap
            return new Response("Элемент с id " + id + " успешно удалён.");
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Clears the collection
     */
    public Response clear() {
        collectionLocker.writeLock().lock();
        try {
            collection.clear(); // clear the collection
            return new Response("Коллекция успешно очищена.");
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Saves the collection to XML-File via FileManager
     */
    @Deprecated
    public void saveToFile() {
        if (fileManager.isRead()) {
            fileManager.saveCollectionToFile();
        } else {
            System.out.println("Файл не был прочитан. Запись невозможна.");
        }
    }


    /**
     * Asks the user to input the object and removes all the objects which are lower (according to compareTo() method)
     */
    public Response removeLower(Ticket ticket, String initiator) {
        StringBuilder sb = new StringBuilder();
        collectionLocker.writeLock().lock();
        try {
            ArrayList<Ticket> temp = new ArrayList<>(collection);
            Iterator it = temp.iterator();
            while (it.hasNext()) {
                Ticket t = (Ticket) it.next();
                if (t.compareTo(ticket) < 0) {
                    try {
                        dbHandler.removeTicketByID(t.getId(), initiator);
                        collection.remove(t);
                        Ticket.removeFromIdMap(t.getId());
                        sb.append("Удален объект: " + t.toString() + "\n");
                    } catch (InsufficientPermissionException e) {
                        sb.append("Объект с id " + t.getId() + " не был удален: недостаточно прав.\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
            return new Response(sb.toString());
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Sorts the collection.
     */
    public Response sort() {
        collectionLocker.writeLock().lock();
        try {
            Collections.sort(collection, Collections.reverseOrder());
            return new Response("Успешно отсортировано.");
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /** Removes first occurrence of an object with stated "refundable" param from the collection
     * @param refundable value of a "refundable" field to search for
     */
    public Response removeByRefundable(boolean refundable, String initiator) throws SQLException {
        collectionLocker.writeLock().lock();
        try {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                try {
                    Ticket t = (Ticket) it.next();
                    if (t.getRefundable() == refundable) {
                        dbHandler.removeTicketByID(t.getId(), initiator);
                        collection.remove(t);
                        return new Response("Удален элемент: " + t);
                    }
                } catch (InsufficientPermissionException e) {
                    continue;
                }
            }
            return new Response("Элемент с заданным refundable не найден.");
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * @param venue A "Venue" object to be compared with.
     */
    public Response countLessThanVenue(Venue venue) {
        collectionLocker.readLock().lock();
        try {
            long count = collection.stream().filter(ticket -> venue.compareTo(ticket.getVenue()) > 0).count();
            return new Response("Объектов в коллекции имеют значение venue меньше заданного: " + count);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * @param price A price number to be compared with.
     * @return
     */
    public Response countGreaterThanPrice(Double price) {
        collectionLocker.readLock().lock();
        try {
            long count = collection.stream().filter(ticket -> price < ticket.getPrice()).count();
            return new Response("Объектов в коллекции имеют значение price больше заданного: " + count);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    public Ticket getTicketById(long id) {
        return Ticket.getTicketById(id);
    }

    public Response getInfo() {
        return new Response(toString());
    }

    @Deprecated
    public long generateId() {
        return ++nextId;
    }
}
