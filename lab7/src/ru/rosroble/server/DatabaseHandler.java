package ru.rosroble.server;

import ru.rosroble.common.data.*;
import ru.rosroble.common.exceptions.InsufficientPermissionException;
import ru.rosroble.common.exceptions.InvalidDBOutputException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;


public class DatabaseHandler {

    private static final String pepper = "2Hq@*!8fdAQl";
    private static final String ADD_USER_REQUEST = "INSERT INTO USERS (username, password) VALUES (?, ?)";
    private static final String VALIDATE_USER_REQUEST = "SELECT COUNT(*) AS count FROM USERS WHERE username = ? AND password = ?";
    private static final String FIND_USERNAME_REQUEST = "SELECT COUNT(*) AS count FROM USERS WHERE username = ?";
    private static final String ADD_TICKET_REQUEST = "INSERT INTO TICKETS (name, x_coord, y_coord, price, refundable, " +
            "ticketType, owner) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String ADD_VENUE_REQUEST = "INSERT INTO VENUES (venueId, venue_name, capacity, venueType, street," +
            "v_x_coord, v_y_coord, v_z_coord, streetName) VALUES (LASTVAL(), ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String JOIN_TICKET_VENUE_REQUEST = "SELECT * FROM TICKETS INNER JOIN VENUES ON " +
            "TICKETS.venueId = VENUES.venueId";
    private static final String GET_MAX_TICKET_ID_REQUEST = "SELECT last_value AS id FROM Tickets_id_seq";
    private static final String GET_MAX_VENUE_ID_REQUEST = "SELECT last_value AS id FROM Tickets_venueid_seq";
    private static final String CHECK_ID_PRESENT_REQUEST = "SELECT COUNT(*) AS count FROM TICKETS WHERE id = ?";
    private static final String REMOVE_BY_TICKET_ID_REQUEST = "DELETE FROM TICKETS WHERE id = ?";
    private static final String REMOVE_BY_VENUE_ID_REQUEST = "DELETE FROM VENUES WHERE venueid = ?";
    private static final String TICKETS_BY_OWNER_REQUEST = "SELECT (*) FROM TICKETS WHERE owner = ?";
    private static final String IS_OWNER_REQUEST = "SELECT COUNT(*) FROM TICKETS WHERE id = ? AND owner = ?";
    private static final String UPDATE_NAME_BY_ID_REQUEST = "UPDATE TICKETS SET name = ? WHERE id = ?";
    private static final String UPDATE_PRICE_BY_ID_REQUEST = "UPDATE TICKETS SET price = ? WHERE id = ?";
    private static final String UPDATE_COORD_BY_ID_REQUEST = "UPDATE TICKETS SET x_coord = ?, y_coord = ? WHERE id = ?";
    private static final String UPDATE_REF_BY_ID_REQUEST = "UPDATE TICKETS SET refundable = ? WHERE id = ?";
    private static final String UPDATE_TYPE_BY_ID_REQUEST = "UPDATE TICKETS SET tickettype = ? WHERE id = ?";
    private static final String UPDATE_VENUE_BY_ID_REQUEST = "UPDATE VENUES SET " +
            "venue_name = ?, " +
            "capacity = ?, " +
            "venuetype = ?, " +
            "street = ?, " +
            "v_x_coord = ?, " +
            "v_y_coord = ?, " +
            "v_z_coord = ?, " +
            "streetname = ? " +
            "WHERE venueId = ?";


    private String URL;
    private String username;
    private String password;
    private Connection connection;


    public DatabaseHandler(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(URL, username, password);
            System.out.println("Подключение к базе данных установлено.");
        } catch (SQLException e) {
            System.err.println("Не удалось выполнить подключение к базе данных. Завершение работы.");
            System.exit(-1);
        }
    }

    public ArrayList<Ticket> loadCollectionFromDB() {
        ArrayList<Ticket> collection = new ArrayList<>();
        try {
            PreparedStatement joinStatement = connection.prepareStatement(JOIN_TICKET_VENUE_REQUEST);
            ResultSet result = joinStatement.executeQuery();

            while (result.next()) {
                try {
                    Ticket t = extractTicketFromResult(result);
                    collection.add(t);
                    Ticket.addToIdMap(t);
                } catch (InvalidDBOutputException e) {
                    System.out.println("Неверный объект");
                    continue;
                }

            }

            joinStatement.close();
            System.out.println("Коллекция успешно загружена из базы данных. Количество элементов: " + collection.size());
        } catch (SQLException e) {
            System.out.println("Произошла ошибка при загрузке коллекции из базы данных. Завершение работы.");
            e.printStackTrace();
            System.exit(-1);
        }
        return collection;
    }

    private Ticket extractTicketFromResult(ResultSet result) throws SQLException, InvalidDBOutputException {
        int ticketId = result.getInt("id");
        if (ticketId < 1) throw new InvalidDBOutputException();
        String ticketName = result.getString("name");
        if (ticketName == null || ticketName.isEmpty()) throw new InvalidDBOutputException();
        double x = result.getDouble("x_coord");
        long y = result.getLong("y_coord");
        LocalDate date = result.getDate("creationdate").toLocalDate();
        double price = result.getDouble("price");
        boolean refundable = result.getBoolean("refundable");
        TicketType ticketType = TicketType.valueOf(result.getString("tickettype"));

        long venueId = result.getLong("venueid");
        if (venueId < 1) throw new InvalidDBOutputException();
        String venueName = result.getString("venue_name");
        if (venueName == null || venueName.isEmpty()) throw new InvalidDBOutputException();
        long capacity = result.getLong("capacity");
        VenueType venueType = VenueType.valueOf(result.getString("venuetype"));
        String street = result.getString("street");
        long vX = result.getLong("v_x_coord");
        int vY = result.getInt("v_y_coord");
        long vZ = result.getInt("v_z_coord");
        String streetName = result.getString("streetname");

        Location location = new Location(vX, vY, vZ, streetName);
        Address address = new Address(street, location);
        Venue venue = new Venue(venueId, venueName, capacity, venueType, address);

        Coordinates coordinates = new Coordinates((float) x, y);
        Ticket ticket = new Ticket(ticketId, ticketName, coordinates, price, refundable, ticketType, venue);

        return ticket;
    }

    public boolean insertTicket(Ticket t, String owner) {
        String name = t.getName();
        Coordinates coordinates = t.getCoordinates();
        double price = t.getPrice();
        boolean refundable = t.getRefundable();
        TicketType type = t.getType();
        Venue venue = t.getVenue();

        try {
            connection.setAutoCommit(false);
            connection.setSavepoint();

            PreparedStatement addToTicketsStatement = connection.prepareStatement(ADD_TICKET_REQUEST);
            addToTicketsStatement.setString(1, name);
            addToTicketsStatement.setDouble(2, coordinates.getX());
            addToTicketsStatement.setLong(3, coordinates.getY());
            addToTicketsStatement.setDouble(4, price);
            addToTicketsStatement.setBoolean(5, refundable);
            addToTicketsStatement.setString(6, type.toString());
            addToTicketsStatement.setString(7, owner);
            addToTicketsStatement.executeUpdate();
            addToTicketsStatement.close();

            PreparedStatement addToVenuesStatement = connection.prepareStatement(ADD_VENUE_REQUEST);
            addToVenuesStatement.setString(1, venue.getName());
            addToVenuesStatement.setLong(2, venue.getCapacity());
            addToVenuesStatement.setString(3, venue.getType().toString());
            addToVenuesStatement.setString(4, venue.getAddress().getStreet());
            addToVenuesStatement.setLong(5, venue.getAddress().getTown().getX());
            addToVenuesStatement.setInt(6, venue.getAddress().getTown().getY());
            addToVenuesStatement.setLong(7, venue.getAddress().getTown().getZ());
            addToVenuesStatement.setString(8, venue.getAddress().getTown().getName());
            addToVenuesStatement.executeUpdate();
            addToVenuesStatement.close();

            connection.commit();
            connection.setAutoCommit(true);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }

        return false;
    }

    public void updateTicketName(String name, long id) throws SQLException{
        PreparedStatement updateName = connection.prepareStatement(UPDATE_NAME_BY_ID_REQUEST);
        updateName.setString(1, name);
        updateName.setLong(2, id);
        updateName.executeUpdate();
        updateName.close();
    }

    public void updateTicketPrice(double price, long id) throws SQLException {
        PreparedStatement updatePrice = connection.prepareStatement(UPDATE_PRICE_BY_ID_REQUEST);
        updatePrice.setDouble(1, price);
        updatePrice.setLong(2, id);
        updatePrice.executeUpdate();
        updatePrice.close();
    }

    public void updateTicketCoordinates(Coordinates coords, long id) throws SQLException {
        PreparedStatement updateCoordinates = connection.prepareStatement(UPDATE_COORD_BY_ID_REQUEST);
        updateCoordinates.setDouble(1, Double.valueOf(coords.getX()));
        updateCoordinates.setLong(2, coords.getY());
        updateCoordinates.setLong(3, id);
        updateCoordinates.executeUpdate();
        updateCoordinates.close();
    }

    public void updateTicketRefundable(boolean refundable, long id) throws SQLException {
        PreparedStatement updateRefundable = connection.prepareStatement(UPDATE_REF_BY_ID_REQUEST);
        updateRefundable.setBoolean(1, refundable);
        updateRefundable.setLong(2, id);
        updateRefundable.executeUpdate();
        updateRefundable.close();
    }

    public void updateTicketType(TicketType type, long id) throws SQLException {
        PreparedStatement updateTicketType = connection.prepareStatement(UPDATE_TYPE_BY_ID_REQUEST);
        updateTicketType.setString(1, type.name());
        updateTicketType.setLong(2, id);
        updateTicketType.executeUpdate();
        updateTicketType.close();
    }

    public void updateTicketVenue(Venue venue, long venueId) throws SQLException {
        PreparedStatement updateVenue = connection.prepareStatement(UPDATE_VENUE_BY_ID_REQUEST);
        updateVenue.setString(1, venue.getName());
        updateVenue.setLong(2, venue.getCapacity());
        updateVenue.setString(3, venue.getType().name());
        updateVenue.setString(4, venue.getAddress().getStreet());
        updateVenue.setLong(5, venue.getAddress().getTown().getX());
        updateVenue.setInt(6, venue.getAddress().getTown().getY());
        updateVenue.setLong(7, venue.getAddress().getTown().getZ());
        updateVenue.setString(8, venue.getAddress().getTown().getName());
        updateVenue.setLong(9, venueId);
        updateVenue.executeUpdate();
        updateVenue.close();
    }

    public boolean removeTicketByID(long id, String initiator) throws SQLException, InsufficientPermissionException {
        if (!checkIdExistence(id)) return false;
        if (!isOwnerOf(id, initiator)) throw new InsufficientPermissionException();
        PreparedStatement removeTicketStatement = connection.prepareStatement(REMOVE_BY_TICKET_ID_REQUEST);
        PreparedStatement removeVenueStatement = connection.prepareStatement(REMOVE_BY_VENUE_ID_REQUEST);
        connection.setAutoCommit(false);
        connection.setSavepoint();
        removeTicketStatement.setLong(1, id);
        removeTicketStatement.executeUpdate();
        removeTicketStatement.close();
        removeVenueStatement.setLong(1, Ticket.getTicketById(id).getVenue().getId());
        removeVenueStatement.executeUpdate();
        removeVenueStatement.close();
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    public boolean checkIdExistence(long id) throws SQLException {
        PreparedStatement checkId = connection.prepareStatement(CHECK_ID_PRESENT_REQUEST);
        checkId.setLong(1, id);
        ResultSet resultSet = checkId.executeQuery();
        resultSet.next();
        if (resultSet.getInt(1) == 0) return false;
        else return true;
    }
    public long getNextTicketId() {
        try {
            PreparedStatement getMaxId = connection.prepareStatement(GET_MAX_TICKET_ID_REQUEST);
            ResultSet result = getMaxId.executeQuery();
            if (result.next()) return result.getInt("id");
            else return 0;
        } catch (SQLException e) {
            System.out.println("Ошибка генерации id");
        }
        return 0;
    }

    public long getNextVenueId() {
        try {
            PreparedStatement getMaxId = connection.prepareStatement(GET_MAX_VENUE_ID_REQUEST);
            ResultSet result = getMaxId.executeQuery();
            if (result.next()) return result.getInt("id");
            else return 0;
        } catch (SQLException e) {
            System.out.println("Ошибка генерации venueid");
        }
        return 0;
    }


    public boolean registerUser(String username, String password) throws SQLException {
        if (userExists(username)) return false;
        PreparedStatement addStatement = connection.prepareStatement(ADD_USER_REQUEST);
        addStatement.setString(1, username);
        addStatement.setString(2, DataHasher.encryptStringMD2(password + pepper));
        addStatement.executeUpdate();
        addStatement.close();
        return true;
    }

    public boolean validateUser(String username, String password) throws SQLException {
        PreparedStatement findUserStatement = connection.prepareStatement(VALIDATE_USER_REQUEST);
        String hashedPassword = DataHasher.encryptStringMD2(password + pepper);
        findUserStatement.setString(1, username);
        findUserStatement.setString(2, hashedPassword);
        ResultSet result = findUserStatement.executeQuery();
        result.next();
        int count = result.getInt(1);
        findUserStatement.close();
        if (count == 1) return true;
        return false;
    }

    public boolean userExists(String username) throws SQLException {
        PreparedStatement findStatement = connection.prepareStatement(FIND_USERNAME_REQUEST);
        findStatement.setString(1, username);
        ResultSet result = findStatement.executeQuery();
        result.next();
        int count = result.getInt(1);
        findStatement.close();
        if (count == 1) return true;
        return false;
    }

    public boolean isOwnerOf(long id, String username) throws SQLException {
        PreparedStatement ownerStatement = connection.prepareStatement(IS_OWNER_REQUEST);
        ownerStatement.setLong(1, id);
        ownerStatement.setString(2, username);
        ResultSet result = ownerStatement.executeQuery();
        result.next();
        if (result.getInt(1) == 1) return true;
        return false;
    }

    public void rollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Не удалось откатить изменения.");
        }
    }
}
