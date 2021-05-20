package ru.rosroble.common.commands;

import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.exceptions.DomainViolationException;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;

public class CountGreaterThanPriceCommand extends AbstractCommand{
    private double price;

    public CountGreaterThanPriceCommand() {
        super(CommandType.COUNT_GR_THAN_PRICE);
    }

    public CountGreaterThanPriceCommand(double price) {
        super(CommandType.COUNT_GR_THAN_PRICE);
        this.price = price;
    }

    @Override
    public Request execute(String[] commandSplit) {
        try {
            Parsers.verify(commandSplit, 1);
            this.price = Parsers.parseThePrice(commandSplit[1]);
            return getRequest();
        } catch (DomainViolationException e) {
            System.out.println("Поле price должно быть положительным числом, вмещающимся в double.");
        } catch (NumberFormatException e) {
            System.out.println("Поле price должно быть типа double");
        } catch (InvalidAmountOfArgumentsException e) {
            e.printMessage();
        }
        return null;
    }

    public double getPrice() {
        return price;
    }
}
