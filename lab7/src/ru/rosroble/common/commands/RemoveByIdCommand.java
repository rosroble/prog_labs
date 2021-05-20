package ru.rosroble.common.commands;

import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.exceptions.DomainViolationException;
import ru.rosroble.common.exceptions.InvalidAmountOfArgumentsException;

import static ru.rosroble.common.Parsers.parseTheId;

public class RemoveByIdCommand extends AbstractCommand{

    private long id;

    public RemoveByIdCommand() {
        super(CommandType.REMOVE_BY_ID);
    }


    @Override
    public Request execute(String[] commandSplit) {
        try {
            Parsers.verify(commandSplit, 1);
            this.id = parseTheId(commandSplit[1]);
            return getRequest();
        } catch (DomainViolationException e) {
            e.printMessage();
        } catch (NumberFormatException e) {
            System.out.println("Ожидается число типа double. Проверьте, что введенное число не нарушает границ double");
        } catch (InvalidAmountOfArgumentsException e) {
            e.printMessage();
        }
        return null;
    }

    public long getId() {
        return this.id;
    }
}
