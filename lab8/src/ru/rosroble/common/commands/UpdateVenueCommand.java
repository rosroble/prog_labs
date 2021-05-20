package ru.rosroble.common.commands;

import ru.rosroble.common.Request;
import ru.rosroble.common.data.Venue;

public class UpdateVenueCommand extends AbstractCommand {

    private long ticketId;
    private Venue newVenue;

    private boolean isUpdateName;
    private boolean isUpdateCapacity;
    private boolean isUpdateType;
    private boolean isUpdateStreet;
    private boolean isUpdateX;
    private boolean isUpdateY;
    private boolean isUpdateZ;
    private boolean isUpdateTown;

    public UpdateVenueCommand(long ticketId, Venue newVenue) {
        super(CommandType.UPDATE_VENUE);
        this.ticketId = ticketId;
        this.newVenue = newVenue;

        //TODO: update venue fields from the table
    }

    @Override
    public Request execute(String[] commandSplit) {
        return null; // GUI-only command, not impl.
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public Venue getNewVenue() {
        return newVenue;
    }

    public void setNewVenue(Venue newVenue) {
        this.newVenue = newVenue;
    }

    public boolean isUpdateName() {
        return isUpdateName;
    }

    public void setUpdateName(boolean updateName) {
        isUpdateName = updateName;
    }

    public boolean isUpdateCapacity() {
        return isUpdateCapacity;
    }

    public void setUpdateCapacity(boolean updateCapacity) {
        isUpdateCapacity = updateCapacity;
    }

    public boolean isUpdateType() {
        return isUpdateType;
    }

    public void setUpdateType(boolean updateType) {
        isUpdateType = updateType;
    }

    public boolean isUpdateStreet() {
        return isUpdateStreet;
    }

    public void setUpdateStreet(boolean updateStreet) {
        isUpdateStreet = updateStreet;
    }

    public boolean isUpdateX() {
        return isUpdateX;
    }

    public void setUpdateX(boolean updateX) {
        isUpdateX = updateX;
    }

    public boolean isUpdateY() {
        return isUpdateY;
    }

    public void setUpdateY(boolean updateY) {
        isUpdateY = updateY;
    }

    public boolean isUpdateZ() {
        return isUpdateZ;
    }

    public void setUpdateZ(boolean updateZ) {
        isUpdateZ = updateZ;
    }

    public boolean isUpdateTown() {
        return isUpdateTown;
    }

    public void setUpdateTown(boolean updateTown) {
        isUpdateTown = updateTown;
    }
}
