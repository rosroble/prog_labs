package ru.rosroble.common;

import ru.rosroble.common.exceptions.DomainViolationException;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;

public class Parsers {
    /**
     * Verifies if the command has a proper amount of arguments.
     * @param cmdSplit Command split into pieces
     * @param argsAmount Amount of arguments required.
     * @return {@code true} if the command has a proper amount of arguments. Otherwise, the exception will be thrown.
     * @throws InvalidAmountOfArgumentsException thrown if the amount of arguments doesn't equal the specified number
     */
    public static boolean verify(String[] cmdSplit, int argsAmount) throws InvalidAmountOfArgumentsException {
        boolean ver = cmdSplit.length == argsAmount + 1;
        if (!ver) throw new InvalidAmountOfArgumentsException(argsAmount);
        return true;
    }

    /**
     * Parses the string representation of a price into double. Checks if the price entered meets the domain requirements.
     * @param s The string to be parsed.
     * @return The parsed double-type price.
     * @throws DomainViolationException thrown if the price doesn't match the domain
     */
    public static double parseThePrice(String s) throws DomainViolationException {
        double price = Double.parseDouble(s);
        if (!(price > 0)) throw new DomainViolationException("Цена должна быть положительной.");
        return price;
    }

    /**
     * Parses the string representation of a boolean into boolean. Checks if the string equals "true" or "false" (case-ignored).
     * @param s The string to be parsed.
     * @return Boolean refundable value parsed
     * @throws DomainViolationException - if the input cannot be parsed into boolean
     */
    public static boolean parseTheRefundable(String s) throws DomainViolationException {
        if (!(s.equalsIgnoreCase("True") || s.equalsIgnoreCase("False"))) {
            throw new DomainViolationException("Ошибка ввода refundable: ожидается true или false");
        }
        return Boolean.parseBoolean(s);
    }

    /**
     * Parses the string representation of an id field into long. Checks the domain conflict.
     * @param s - The string to be parsed
     * @return Long ID value parsed
     * @throws DomainViolationException - thrown if the number given doesn't match the requirements.
     */
    public static long parseTheId(String s) throws DomainViolationException {
        long id = Long.parseLong(s);
        if (!(id > 0)) throw new DomainViolationException("Поле id должно быть больше 0.");
        if (!(id < Long.MAX_VALUE)) throw new DomainViolationException("Число не входит в область типа long");
        return id;
    }
}
