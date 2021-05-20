package ru.rosroble.common;

import ru.rosroble.common.CommandProcessor;
import ru.rosroble.common.Parsers;
import ru.rosroble.common.data.*;
import ru.rosroble.common.exceptions.DomainViolationException;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;

import java.util.Scanner;

/**
 * A class containing methods to update the info of objects stored both from the file or keyboard.
 */
public class Updater {

    /**
     * Updates the price of an object.
     * @return new price
     */
    public static double updatePrice() {
        while (true) {
            System.out.println("Введите цену билета (положительное число формата double): ");
            String data;
            String output;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim();
            else {
                output = CommandProcessor.fileScanner.nextLine();
                System.out.println(output);
                data = output.trim();
            }
            try {
                double price = Parsers.parseTheId(data);
                return price;
            } catch (DomainViolationException e) {
                e.printMessage();
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат числа. Повторите попытку ввода.");
            }
        }
    }

    /**
     * Updates the refundable field.
     * @return new refundable value
     */
    public static boolean updateRefundable() {
        while (true) {
            System.out.println("Введите статус возвратности билета (true или false)");
            String data;
            String output;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim();
            else {
                output = CommandProcessor.fileScanner.nextLine();
                System.out.println(output);
                data = output.trim();
            }
            try {
                boolean refundable = Parsers.parseTheRefundable(data);
                return refundable;
            } catch (DomainViolationException e) {
                e.printMessage();
            }
        }
    }
    /**
     * Updates the Coordinates field.
     * @return new Coordinate value
     */
    public static Coordinates updateCoordinates() {
        while (true) {
            System.out.println("Укажите координаты x, y через пробел (x > -355): ");
            String[] data;
            String output;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim().split(" ");
            else {
                output = CommandProcessor.fileScanner.nextLine();
                System.out.println(output);
                data = output.trim().split(" ");
            }
            try {
                if (data.length != 2) throw new InvalidAmountOfArgumentsException(2);
                float x = Float.parseFloat(data[0]);
                long y = Long.parseLong(data[1]);
                if (!(x > -355)) throw new DomainViolationException("x > 355 required");
                if (!(x < Float.MAX_VALUE)) throw new DomainViolationException("x is out of 'float' type bounds");
                if (!((y < Long.MAX_VALUE) && (y > Long.MIN_VALUE))) throw new DomainViolationException("y is out of 'long' type bounds");
                return new Coordinates(x, y);
            } catch (NumberFormatException nfe) {
                System.out.println("Неверный формат. Ожидается два числа x и y формата float и long соответственно (x > -355).");
            } catch (DomainViolationException dve) {
                System.out.println("Неверный формат. Координата должна быть > -355.");
            } catch (InvalidAmountOfArgumentsException e) {
                System.out.println("Ожидается два аргумента: x и у через пробел.");
            }
        }
    }
    /**
     * Updates the TicketType field.
     * @return new TicketType value
     */
    public static TicketType updateType() {
        while (true) {
            System.out.println("Выберите тип билета из предложенных: ");
            for (TicketType type: TicketType.values()) {
                System.out.println(type);
            }
            String data;
            String output;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim().toUpperCase();
            else {
                output = CommandProcessor.fileScanner.nextLine();
                System.out.println(output);
                data = output.trim().toUpperCase();
            }
            if (data.isEmpty()) {
                return null;
            }
            else {
                try {
                    return TicketType.valueOf(data);
                } catch (IllegalArgumentException iae) {
                    System.out.println("Неверный ввод.");
                }
            }
        }
    }
    /**
     * Updates the Venue field.
     * @return new Venue value
     */
    public static Venue updateVenue() {
        System.out.println("Ввод Venue~");
        return new Venue(updateName(), updateVenueCapacity(), updateVenueType(), updateVenueAddress());
    }

    public static String updateName() {
        while (true) {
            System.out.print("Введите имя: ");
            String data;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim();
            else {
                data = CommandProcessor.fileScanner.nextLine().trim();
                System.out.println(data);
            }
            if (data.isEmpty()) System.out.println("Строка не может быть пустой");
            else {
                return data;
            }
        }
    }
    /**
     * Updates the capacity field of a Venue.
     * @return new capacity value
     */
    public static long updateVenueCapacity() {
        while (true) {
            System.out.print("Введите вместимость зала: ");
            String data;
            String output;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim();
            else {
                output = CommandProcessor.fileScanner.nextLine();
                System.out.println(output);
                data = output.trim();
            }
            try {
                long capacity = Long.parseLong(data);
                if (!(capacity > 0)) throw new DomainViolationException("Вместимость зала должна быть больше нуля.");
                return capacity;
            } catch (DomainViolationException e) {
                e.printMessage();
            } catch (NumberFormatException nfe) {
                System.out.println("Неверный формат. Ожидается число формата long.");
            }
        }
    }
    /**
     * Updates the VenueType field.
     * @return new VenueType value
     */
    public static VenueType updateVenueType() {
        while (true) {
            System.out.println("Выберите тип билета из предложенных: ");
            for (VenueType type: VenueType.values()) {
                System.out.println(type);
            }
            String data;
            String output;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim().toUpperCase();
            else {
                output = CommandProcessor.fileScanner.nextLine();
                System.out.println(output);
                data = output.trim().toUpperCase();
            }
            if (data.isEmpty()) {
                return null;
            }
            else {
                try {
                    return VenueType.valueOf(data);
                } catch (IllegalArgumentException iae) {
                    System.out.println("Неверный ввод.");
                }
            }
        }
    }
    /**
     * Updates the address field of a Venue.
     * @return new Address value
     */
    public static Address updateVenueAddress() {
        System.out.println("Ввод Address~");
        return new Address(updateAddressStreet(), updateAddressTown());
    }
    /**
     * Updates the Street field.
     * @return new Street value
     */
    public static String updateAddressStreet() {
        while (true) {
            System.out.print("Введите название улицы: ");
            String data;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine();
            else {
                data = CommandProcessor.fileScanner.nextLine();
                System.out.println(data);
            }
            if (data.isEmpty()) return null;
            else {
                return data;
            }
        }
    }
    /**
     * Updates the Town field of an Address.
     * @return new Town value
     */
    public static Location updateAddressTown() {
        while (true) {
            System.out.print("Введите координаты x, y, z через пробел: ");
            String[] data;
            String output;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim().split(" ");
            else {
                output = CommandProcessor.fileScanner.nextLine();
                System.out.println(output);
                data = output.trim().split(" ");
            }
            try {
                if (data.length != 3) throw new InvalidAmountOfArgumentsException(3);
                long x = Long.parseLong(data[0]);
                int y = Integer.parseInt(data[1]);
                long z = Long.parseLong(data[2]);
                return new Location(x,y, z, updateTownName());
            } catch (InvalidAmountOfArgumentsException | NumberFormatException nfe) {
                System.out.println("Ожидается три аргумента: long, integer, long через пробел");
            }
        }
    }
    /**
     * Updates the name field of a Town.
     * @return new name value
     */
    public static String updateTownName() {
        while (true) {
            System.out.print("Введите имя локации: ");
            String data;
            if (!CommandProcessor.fileMode) data = new Scanner(System.in).nextLine().trim();
            else {
                data = CommandProcessor.fileScanner.nextLine().trim();
                System.out.println(data);
            }
            if (data.isEmpty()) System.out.println("Строка не может быть пустой");
            else {
                return data;
            }
        }
    }
    /**
     * Asks user if they want to proceed updating the field. The user types answers with "y" or "n" via keyboard.
     * @param question - the question printed before reading from the keyboard
     * @return {@code true} if the user wants to update the specified field (y) or {@code false} if the user doesn't want to update the specified field (n)
     *
     */
    public static boolean ask(String question) {
        while (true) {
            System.out.println(question + " (y/n)");
            Scanner answer = new Scanner(System.in);
            switch (answer.nextLine().trim().toLowerCase()) {
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    System.out.println("Введите y или n.");
            }
        }
    }

}
