package ru.rosroble.common.data;

import java.io.Serializable;

/**
 * Enum-Class to represent the type of a ticket.
 */
public enum TicketType implements Serializable {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP;
}