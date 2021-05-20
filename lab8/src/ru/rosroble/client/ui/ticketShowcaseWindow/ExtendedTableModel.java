package ru.rosroble.client.ui.ticketShowcaseWindow;

import ru.rosroble.client.Client;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.Response;
import ru.rosroble.common.commands.UpdateIdCommand;
import ru.rosroble.common.data.*;
import ru.rosroble.common.exceptions.DomainViolationException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class ExtendedTableModel extends DefaultTableModel {

    String oldValue;

    public ExtendedTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    Class<?>[] columnTypes = new Class<?>[] {
            Long.class, String.class, Float.class, Long.class, Double.class, Boolean.class, String.class, Long.class, String.class, Long.class, String.class, String.class, Long.class, Integer.class, Long.class, String.class, String.class
    };
    boolean[] columnEditable = new boolean[] {
            false, true, true, true, true, true, true, false, true, true, true, true, true, true, true, true, false
    };
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnEditable[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        oldValue = String.valueOf(getValueAt(row, column));
        if (String.valueOf(aValue).isEmpty()) return;
        try {
            Request request = generateUpdateRequest(aValue, row, column);
            Client client = Client.getClient();
            client.sendRequest(request);
            Response response = client.getResponse();
            JOptionPane.showMessageDialog(null, LocaleBundle.getValue(response.getResponseInfo()));
            if (!response.isOK()) return;
        } catch (DomainViolationException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
            return;
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, LocaleBundle.getValue("fail"));
        } catch (ClassNotFoundException ignored) { }
        super.setValueAt(aValue, row, column);
    }

    public Request generateUpdateRequest(Object newValue, int row, int column) throws DomainViolationException {
        long id = (long) getValueAt(row, 0);
        String errorMessage = "";
        boolean isValid = true;
        Ticket ticket = new Ticket();
        String cell = String.valueOf(newValue);


        switch (column) {
            case 1:
                String name = Parsers.parseTheName(cell);
                ticket.setName(name);
                UpdateIdCommand updName = new UpdateIdCommand(ticket, id);
                updName.setUpdateName(true);
                return new Request(updName);
            case 2:
                Coordinates coordinatesX = Parsers.parseTheCoordinates(cell, String.valueOf(getValueAt(row, 3)));
                ticket.setCoordinates(coordinatesX);
                UpdateIdCommand updCoordX = new UpdateIdCommand(ticket, id);
                updCoordX.setUpdateCoordinates(true);
                return new Request(updCoordX);
            case 3:
                Coordinates coordinatesY = Parsers.parseTheCoordinates(String.valueOf(getValueAt(row, 2)), cell);
                ticket.setCoordinates(coordinatesY);
                UpdateIdCommand updCoordY = new UpdateIdCommand(ticket, id);
                updCoordY.setUpdateCoordinates(true);
                return new Request(updCoordY);
            case 4:
                try {
                    double price = Parsers.parseThePrice(cell);
                    ticket.setPrice(price);
                    UpdateIdCommand upd = new UpdateIdCommand(ticket, id);
                    upd.setUpdatePrice(true);
                    return new Request(upd);
                } catch (DomainViolationException exception) {
                    errorMessage = exception.getMessage();
                    isValid = false;
                }
                break;
            case 5:
                ticket.setRefundable(Boolean.parseBoolean(cell));
                UpdateIdCommand updRef = new UpdateIdCommand(ticket, id);
                updRef.setUpdateRefundable(true);
                return new Request(updRef);
            case 6:
                try {
                    TicketType ticketType = Parsers.parseTicketType(cell);
                    ticket.setType(ticketType);
                    UpdateIdCommand updType = new UpdateIdCommand(ticket, id);
                    updType.setUpdateType(true);
                    return new Request(updType);
                } catch (DomainViolationException e) {
                    isValid = false;
                    errorMessage = e.getMessage();
                }
                break;
            case 8:
                Venue newName = buildVenue(row);
                String venueName = Parsers.parseTheName(cell);
                newName.setName(venueName);
                ticket.setVenue(newName);
                UpdateIdCommand updVName = new UpdateIdCommand(ticket, id);
                updVName.setUpdateVenue(true);
                return new Request(updVName);
            case 9:
                Venue newCapacity = buildVenue(row);
                long capacity = Long.parseLong(cell);
                newCapacity.setCapacity(capacity);
                ticket.setVenue(newCapacity);
                UpdateIdCommand updVCapacity = new UpdateIdCommand(ticket, id);
                updVCapacity.setUpdateVenue(true);
                return new Request(updVCapacity);
            case 10:
                try {
                    Venue newType = buildVenue(row);
                    VenueType venueType = Parsers.parseVenueType(cell);
                    newType.setType(venueType);
                    ticket.setVenue(newType);
                    UpdateIdCommand updVType = new UpdateIdCommand(ticket, id);
                    updVType.setUpdateVenue(true);
                    return new Request(updVType);
                } catch (DomainViolationException e) {
                    isValid = false;
                    errorMessage = e.getMessage();
                }
                break;
            case 11:
                Venue newStreet = buildVenue(row);
                String street = Parsers.parseTheName(cell);
                newStreet.getAddress().setName(street);
                ticket.setVenue(newStreet);
                UpdateIdCommand updVStreet = new UpdateIdCommand(ticket, id);
                updVStreet.setUpdateVenue(true);
                return new Request(updVStreet);
            case 12:
                Venue newVX = buildVenue(row);
                long venueX = Long.parseLong(cell);
                newVX.getAddress().getTown().setX(venueX);
                ticket.setVenue(newVX);
                UpdateIdCommand updVX = new UpdateIdCommand(ticket, id);
                updVX.setUpdateVenue(true);
                return new Request(updVX);
            case 13:
                Venue newVY = buildVenue(row);
                int venueY = Integer.parseInt(cell);
                newVY.getAddress().getTown().setY(venueY);
                ticket.setVenue(newVY);
                UpdateIdCommand updVY = new UpdateIdCommand(ticket, id);
                updVY.setUpdateVenue(true);
                return new Request(updVY);
            case 14:
                Venue newVZ = buildVenue(row);
                long venueZ = Long.parseLong(cell);
                newVZ.getAddress().getTown().setZ(venueZ);
                ticket.setVenue(newVZ);
                UpdateIdCommand updVZ = new UpdateIdCommand(ticket, id);
                updVZ.setUpdateVenue(true);
                return new Request(updVZ);
            case 15:
                Venue newTownName = buildVenue(row);
                String townName = Parsers.parseTheName(cell);
                newTownName.getAddress().getTown().setName(townName);
                ticket.setVenue(newTownName);
                UpdateIdCommand updTownName = new UpdateIdCommand(ticket, id);
                updTownName.setUpdateVenue(true);
                return new Request(updTownName);
        }
        if (!isValid) throw new DomainViolationException(errorMessage);
        return null; // not reachable
    }


    public String getOldValue() {
        return this.oldValue;
    }

    private Venue buildVenue(int row) {

        long id = (Long) getValueAt(row,7 );
        String name = String.valueOf(getValueAt(row, 8));
        long capacity = (Long) getValueAt(row, 9);
        VenueType type = VenueType.valueOf(String.valueOf(getValueAt(row, 10)));

        String street = (String) getValueAt(row, 11);
        long vX = (Long) getValueAt(row, 12);
        int vY = (Integer) getValueAt(row, 13);
        long vZ = (Long) getValueAt(row, 14);
        String townName = (String) getValueAt(row, 15);

        Location location = new Location(vX, vY, vZ, townName);
        Address address = new Address(street, location);

        return new Venue(id, name, capacity, type, address);

    }

}
