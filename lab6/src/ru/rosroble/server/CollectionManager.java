package ru.rosroble.server;

import ru.rosroble.common.Response;
import ru.rosroble.common.data.*;
//import ru.rosroble.common.Updater;


import java.util.*;


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



    {
        collection = new ArrayList<Ticket>();
        initDate = new Date();
        description = new HashMap<String, String>();
        listType = collection.getClass().getSimpleName();
        fileManager = new FileManager(collection, "envXML");
        load();
        nextId = collection.size();
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
        StringBuilder s = new StringBuilder();
        collection.forEach(ticket -> s.append(ticket.toString() + "\n"));
        return new Response(s.toString());
    }


    public Response add(Ticket t, boolean ifMax) {
        if ((!ifMax) || (collection.stream().filter(ticket -> ticket.compareTo(t) < 0).count() == collection.size())) {
            collection.add(t);
        }
        return new Response("Добавлен объект: " + t.toString());
    }

    /**
     * Reads and loads the collection from the XML-File via FileManager.
     */
    public void load() {
        this.collection = fileManager.parseCollectionFromFile();
        fileManager.setCollection(collection);
    }


    /**
     * Consequently updates all the fields of an object with id specified. Asks user if they wish to update the field.
     * @param id - id of an object to update.
     */
    public Response updateId(long id, Ticket newT) {
        collection.remove(Ticket.getTicketById(id));
        collection.add(newT);
        sort();
        return new Response("Элемент с указанным id успешно обновлён.");
    }

    /**
     * @param id Id of an object to be removed.
     */
    public Response removeById(long id) {

        if (!collection.remove(collection.stream().filter(x -> x.getId() == id).findFirst().orElse(null))) { // if the element is not found remove() returns false
            return new Response("Элемент с указанным id не найден.");
        }
        else {
            Ticket.removeFromIdMap(id); // remove the element from (id -> Ticket) hashmap
            return new Response("Элемент с id " + id + " успешно удалён.");
        }
    }

    /**
     * Clears the collection
     */
    public Response clear() {
        collection.clear(); // clear the collection
        Ticket.resetId(); // reset the id counter
        return new Response("Коллекция успешно очищена.");
    }

    /**
     * Saves the collection to XML-File via FileManager
     */
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
    public Response removeLower(Ticket ticket) {
        StringBuilder sb = new StringBuilder();

        collection.stream().filter(t -> t.compareTo(ticket) < 0).forEach(t -> {
            collection.remove(t);
            Ticket.removeFromIdMap(t.getId());
            sb.append("Удален объект: " + t.toString() + "\n");
        });
        return new Response(sb.toString());
    }

    /**
     * Sorts the collection.
     */
    public Response sort() {
        Collections.sort(collection, Collections.reverseOrder());
        return new Response("Успешно отсортировано.");
    }

    /** Removes first occurrence of an object with stated "refundable" param from the collection
     * @param refundable value of a "refundable" field to search for
     */
    public Response removeByRefundable(boolean refundable) {
        boolean isFound = false;
        Ticket toRemove = collection.stream().filter(ticket -> ticket.getRefundable() == refundable).findFirst().orElse(null);
        if (toRemove == null) return new Response("Элемент с заданным refundable не найден.");
        collection.remove(toRemove);
        return new Response("Удален элемент: " + toRemove.toString());
//        for (Ticket t: collection) {
//            if (t.getRefundable() == refundable) {
//                collection.remove(t);
//                Ticket.removeFromIdMap(t.getId());
//                isFound = true;
//                System.out.println("Удалён элемент: " + t.toString());
//                break;
//            }
//        }
    }

    /**
     * @param venue A "Venue" object to be compared with.
     */
    public Response countLessThanVenue(Venue venue) {
        long count = collection.stream().filter(ticket -> venue.compareTo(ticket.getVenue()) > 0).count();
//        int count = 0;
//        for (Ticket t: collection) {
//            if (venue.compareTo(t.getVenue()) > 0) {
//                count += 1;
//            }
//        }
        return new Response("Объектов в коллекции имеют значение venue меньше заданного: " + count);
    }

    /**
     * @param price A price number to be compared with.
     * @return
     */
    public Response countGreaterThanPrice(Double price) {
        long count = collection.stream().filter(ticket -> price < ticket.getPrice()).count();
        return new Response("Объектов в коллекции имеют значение price больше заданного: " + count);
    }

    public Ticket getTicketById(long id) {
        return Ticket.getTicketById(id);
    }

    public Response getInfo() {
        return new Response(toString());
    }

    public long generateId() {
        return ++nextId;
    }
}
