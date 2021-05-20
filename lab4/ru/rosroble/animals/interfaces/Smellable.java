package ru.rosroble.animals.interfaces;

import ru.rosroble.entities.Entity;
import ru.rosroble.exceptions.SmellNotFoundException;

public interface Smellable {
    Entity smell() throws SmellNotFoundException;
}
