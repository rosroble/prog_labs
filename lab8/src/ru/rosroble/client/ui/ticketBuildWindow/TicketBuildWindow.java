/*
 * Created by JFormDesigner on Tue May 04 18:04:53 MSK 2021
 */

package ru.rosroble.client.ui.ticketBuildWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import net.miginfocom.swing.*;
import ru.rosroble.client.Client;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.commandListWindow.CommandListWindow;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.commands.AbstractCommand;
import ru.rosroble.common.commands.AddCommand;
import ru.rosroble.common.commands.RemoveLowerCommand;
import ru.rosroble.common.data.*;
import ru.rosroble.common.exceptions.DomainViolationException;

/**
 * @author unknown
 */
public class TicketBuildWindow extends JPanel {

    private CommandListWindow commandListWindow;
    private Client client;
    private Map<String, Integer> nameIndexMap = new TreeMap<>();
    private boolean isAdd;
    public TicketBuildWindow(CommandListWindow commandListWindow, boolean isAdd)
    {
        this.isAdd = isAdd;
        nameIndexMap.put("id", 0);
        nameIndexMap.put("name", 1);
        nameIndexMap.put("XCoord", 2);
        nameIndexMap.put("YCoord", 3);
        nameIndexMap.put("price", 4);
        nameIndexMap.put("refundable", 5);
        nameIndexMap.put("TicketType", 6);
        nameIndexMap.put("venueId", 7);
        nameIndexMap.put("venueName", 8);
        nameIndexMap.put("venueCapacity", 9);
        nameIndexMap.put("venueType", 10);
        nameIndexMap.put("venueStreet", 11);
        nameIndexMap.put("VXCoord", 12);
        nameIndexMap.put("VYCoord", 13);
        nameIndexMap.put("VZCoord", 14);
        nameIndexMap.put("townName", 15);
        this.commandListWindow = commandListWindow;
        this.client = Client.getClient();
        initComponents();
    }

    protected void backButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), commandListWindow);
        this.ticketNameTextField.setText("");
        ticketPriceTextField.setText("");
        ticketXTextField.setText("");
        ticketYTextField.setText("");
        venueNameField.setText("");
        venueCapacityField.setText("");
        venueStreetNameField.setText("");
        venueXField.setText("");
        venueYField.setText("");
        venueZField.setText("");
        venueTownNameField.setText("");
    }

    protected void submitButtonMouseReleased(MouseEvent e) {
        try {
            Ticket ticket = buildTicket();
            AbstractCommand command = isAdd ? new AddCommand(ticket) : new RemoveLowerCommand(ticket);
            client.sendRequest(new Request(command));
            if (isAdd && client.getResponse().isOK())
                JOptionPane.showMessageDialog(null, LocaleBundle.getValue("success"));
            else
                JOptionPane.showMessageDialog(null, LocaleBundle.getValue("removeLowerResponse") + client.getResponse().getResponseInfo());

            UIControl.switchWindow(UIControl.getFrame(), commandListWindow);
            this.ticketNameTextField.setText("");
            ticketPriceTextField.setText("");
            ticketXTextField.setText("");
            ticketYTextField.setText("");
            venueNameField.setText("");
            venueCapacityField.setText("");
            venueStreetNameField.setText("");
            venueXField.setText("");
            venueYField.setText("");
            venueZField.setText("");
            venueTownNameField.setText("");
        } catch (DomainViolationException ignored) {} catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
    
    public void localize() {
        this.headerLabel.setText(LocaleBundle.getValue("ticketBuildHeader"));
        this.nameLabel.setText(LocaleBundle.getValue("name"));
        this.priceLabel.setText(LocaleBundle.getValue("price"));
        this.refunableLabel.setText(LocaleBundle.getValue("refundable"));
        this.xCoordLabel.setText(LocaleBundle.getValue("xCoord"));
        this.yCoordLabel.setText(LocaleBundle.getValue("yCoord"));
        this.typeLabel.setText(LocaleBundle.getValue("type"));
        this.venueNameLabel.setText(LocaleBundle.getValue("name"));
        this.capacityLabel.setText(LocaleBundle.getValue("capacity"));
        this.streetNameLabel.setText(LocaleBundle.getValue("streetName"));
        this.venueTypeLabel.setText(LocaleBundle.getValue("type"));
        this.townNameLabel.setText(LocaleBundle.getValue("townName"));
        this.townXlabel.setText(LocaleBundle.getValue("xCoord"));
        this.townYlabel.setText(LocaleBundle.getValue("yCoord"));
        this.townZlabel.setText(LocaleBundle.getValue("zCoord"));

        this.venueLabel.setText(LocaleBundle.getValue("venueLabel"));
        this.ticketLabel.setText(LocaleBundle.getValue("ticketLabel"));

        this.backButton.setText(LocaleBundle.getValue("back"));
        this.submitButton.setText(LocaleBundle.getValue("submit"));
        
    }

    // DRY-rule sucks
    protected Ticket buildTicket() throws DomainViolationException {
        StringBuilder errorMessage = new StringBuilder("Found errors:\n");
        Ticket ticket = new Ticket();
        Venue venue = new Venue();
        Address address = new Address();
        Location location = new Location();
        ticket.setOwner(Client.getClient().getUsername());

        boolean hasErrors = false;
        try {
            String name = Parsers.parseTheName(this.ticketNameTextField.getText());
            ticket.setName(name);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            double price = Parsers.parseThePrice(this.ticketPriceTextField.getText());
            ticket.setPrice(price);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            boolean refundable = Parsers.parseTheRefundable((String)this.ticketRefComboBox.getSelectedItem());
            ticket.setRefundable(refundable);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            Coordinates coordinates = Parsers.parseTheCoordinates(this.ticketXTextField.getText(), this.ticketYTextField.getText());
            ticket.setCoordinates(coordinates);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            TicketType ticketType = Parsers.parseTicketType((String) this.ticketTypeComboBox.getSelectedItem());
            ticket.setType(ticketType);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            String venueName = Parsers.parseTheName(this.venueNameField.getText());
            venue.setName(venueName);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            long venueCapacity = Parsers.parseCapacity(this.venueCapacityField.getText());
            venue.setCapacity(venueCapacity);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            VenueType vType = Parsers.parseVenueType((String)this.venueTypeComboBox.getSelectedItem());
            venue.setType(vType);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            String street = Parsers.parseTheName(this.venueStreetNameField.getText());
            address.setName(street);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
             long locX = Parsers.parseLocX(this.venueXField.getText());
             location.setX(locX);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            int locY = Parsers.parseLocY(this.venueYField.getText());
            location.setY(locY);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            long locZ = Parsers.parseLocZ(this.venueZField.getText());
            location.setZ(locZ);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }
        try {
            String street = Parsers.parseTheName(this.venueTownNameField.getText());
            location.setName(street);
        } catch (DomainViolationException e) {
            hasErrors = true;
            errorMessage.append(e.getMessage()).append("\n");
        }

        if (hasErrors) {
            JOptionPane.showMessageDialog(null, errorMessage.toString());
            throw new DomainViolationException("Cannot build a ticket");
        }

        address.setLocation(location);
        venue.setAddress(address);
        ticket.setVenue(venue);
        return ticket;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        headerLabel = new JLabel();
        ticketLabel = new JLabel();
        nameLabel = new JLabel();
        ticketNameTextField = new JTextField();
        priceLabel = new JLabel();
        ticketPriceTextField = new JTextField();
        xCoordLabel = new JLabel();
        ticketXTextField = new JTextField();
        yCoordLabel = new JLabel();
        ticketYTextField = new JTextField();
        refunableLabel = new JLabel();
        ticketRefComboBox = new JComboBox<>();
        typeLabel = new JLabel();
        ticketTypeComboBox = new JComboBox<>();
        venueLabel = new JLabel();
        venueNameLabel = new JLabel();
        venueNameField = new JTextField();
        townNameLabel = new JLabel();
        venueTownNameField = new JTextField();
        capacityLabel = new JLabel();
        venueCapacityField = new JTextField();
        townXlabel = new JLabel();
        venueXField = new JTextField();
        streetNameLabel = new JLabel();
        venueStreetNameField = new JTextField();
        townYlabel = new JLabel();
        venueYField = new JTextField();
        venueTypeLabel = new JLabel();
        venueTypeComboBox = new JComboBox<>();
        townZlabel = new JLabel();
        venueZField = new JTextField();
        backButton = new JButton();
        submitButton = new JButton();

        //======== this ========
        setBackground(new Color(60, 96, 205));
        setLayout(new MigLayout(
            "hidemode 3,align center center",
            // columns
            "[grow,right]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[::200,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[::200,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[27,fill]" +
            "[grow,left]" +
            "[grow,right]" +
            "[fill]" +
            "[fill]" +
            "[::200,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[grow,left]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[grow]" +
            "[grow]" +
            "[grow]" +
            "[grow]" +
            "[grow]" +
            "[grow]" +
            "[grow]" +
            "[grow]" +
            "[]" +
            "[]" +
            "[grow]" +
            "[]"));

        //---- headerLabel ----
        headerLabel.setText("Build a Ticket");
        headerLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
        add(headerLabel, "cell 0 0 29 3,align center center,grow 0 0");

        //---- ticketLabel ----
        ticketLabel.setText("Enter basic ticket params:");
        ticketLabel.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 20));
        add(ticketLabel, "cell 0 3 29 1,alignx center,growx 0");

        //---- nameLabel ----
        nameLabel.setText("name");
        add(nameLabel, "cell 4 5 4 1,alignx right,growx 0");

        //---- ticketNameTextField ----
        ticketNameTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketNameTextField.setBackground(new Color(238, 238, 238));
        add(ticketNameTextField, "cell 8 5 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- priceLabel ----
        priceLabel.setText("price");
        add(priceLabel, "cell 14 5 3 1,alignx right,growx 0");

        //---- ticketPriceTextField ----
        ticketPriceTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketPriceTextField.setBackground(Color.white);
        add(ticketPriceTextField, "cell 17 5 5 1,align center center,grow 0 0,width 100:150:200,height 20:30:60");

        //---- xCoordLabel ----
        xCoordLabel.setText("X-coord");
        add(xCoordLabel, "cell 4 6 4 1,alignx right,growx 0");

        //---- ticketXTextField ----
        ticketXTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketXTextField.setBackground(new Color(238, 238, 238));
        add(ticketXTextField, "cell 8 6 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- yCoordLabel ----
        yCoordLabel.setText("Y-coord");
        add(yCoordLabel, "cell 14 6 3 1,alignx right,growx 0");

        //---- ticketYTextField ----
        ticketYTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketYTextField.setBackground(Color.white);
        add(ticketYTextField, "cell 17 6 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- refunableLabel ----
        refunableLabel.setText("refundable");
        add(refunableLabel, "cell 4 7 4 1,alignx right,growx 0");

        //---- ticketRefComboBox ----
        ticketRefComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "-",
            "true",
            "false"
        }));
        ticketRefComboBox.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketRefComboBox.setBackground(new Color(238, 238, 238));
        add(ticketRefComboBox, "cell 8 7 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- typeLabel ----
        typeLabel.setText("Type");
        add(typeLabel, "cell 14 7 3 1,alignx right,growx 0");

        //---- ticketTypeComboBox ----
        ticketTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "-",
            "VIP",
            "USUAL",
            "BUDGETARY",
            "CHEAP"
        }));
        ticketTypeComboBox.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketTypeComboBox.setBackground(Color.white);
        add(ticketTypeComboBox, "cell 17 7 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- venueLabel ----
        venueLabel.setText("Enter Venue params:");
        venueLabel.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 20));
        add(venueLabel, "cell 0 8 29 1,alignx center,growx 0");

        //---- venueNameLabel ----
        venueNameLabel.setText("name");
        add(venueNameLabel, "cell 4 9 4 1,alignx right,growx 0");

        //---- venueNameField ----
        venueNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueNameField.setBackground(new Color(238, 238, 238));
        add(venueNameField, "cell 8 9 5 1,width 100:150:200,height 20:30:60");

        //---- townNameLabel ----
        townNameLabel.setText("town name");
        add(townNameLabel, "cell 14 9 3 1,alignx right,growx 0");

        //---- venueTownNameField ----
        venueTownNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueTownNameField.setBackground(Color.white);
        add(venueTownNameField, "cell 17 9 5 1,width 100:150:200,height 20:30:60");

        //---- capacityLabel ----
        capacityLabel.setText("capacity");
        add(capacityLabel, "cell 4 10 4 1,alignx right,growx 0");

        //---- venueCapacityField ----
        venueCapacityField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueCapacityField.setBackground(new Color(238, 238, 238));
        add(venueCapacityField, "cell 8 10 5 1,width 100:150:200,height 20:30:60");

        //---- townXlabel ----
        townXlabel.setText("town X-coord");
        add(townXlabel, "cell 14 10 3 1,alignx right,growx 0");

        //---- venueXField ----
        venueXField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueXField.setBackground(Color.white);
        add(venueXField, "cell 17 10 5 1,width 100:150:200,height 20:30:60");

        //---- streetNameLabel ----
        streetNameLabel.setText("street name");
        add(streetNameLabel, "cell 4 11 4 1,alignx right,growx 0");

        //---- venueStreetNameField ----
        venueStreetNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueStreetNameField.setBackground(new Color(238, 238, 238));
        add(venueStreetNameField, "cell 8 11 5 1,width 100:150:200,height 20:30:60");

        //---- townYlabel ----
        townYlabel.setText("town Y-coord");
        add(townYlabel, "cell 14 11 3 1,alignx right,growx 0");

        //---- venueYField ----
        venueYField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueYField.setBackground(Color.white);
        add(venueYField, "cell 17 11 5 1,width 100:150:200,height 20:30:60");

        //---- venueTypeLabel ----
        venueTypeLabel.setText("Type");
        add(venueTypeLabel, "cell 4 12 4 1,alignx right,growx 0");

        //---- venueTypeComboBox ----
        venueTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "-",
            "PUB",
            "BAR",
            "THEATRE"
        }));
        venueTypeComboBox.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueTypeComboBox.setBackground(new Color(238, 238, 238));
        add(venueTypeComboBox, "cell 8 12 5 1,width 100:150:200,height 20:30:60");

        //---- townZlabel ----
        townZlabel.setText("town Z-coord");
        add(townZlabel, "cell 14 12 3 1,alignx right,growx 0");

        //---- venueZField ----
        venueZField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueZField.setBackground(Color.white);
        add(venueZField, "cell 17 12 5 1,width 100:150:200,height 20:30:60");

        //---- backButton ----
        backButton.setText("Back");
        backButton.setBackground(new Color(168, 37, 75));
        backButton.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED, Color.red, new Color(177, 17, 56), new Color(78, 0, 0), new Color(136, 18, 5)));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                backButtonMouseReleased(e);
            }
        });
        add(backButton, "cell 8 15 5 1,alignx center,growx 0,width 100:125:175");

        //---- submitButton ----
        submitButton.setText("Submit");
        submitButton.setBackground(new Color(35, 118, 31));
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                submitButtonMouseReleased(e);
            }
        });
        add(submitButton, "cell 17 15 5 1,alignx center,growx 0,width 100:125:175");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel headerLabel;
    private JLabel ticketLabel;
    private JLabel nameLabel;
    private JTextField ticketNameTextField;
    private JLabel priceLabel;
    private JTextField ticketPriceTextField;
    private JLabel xCoordLabel;
    private JTextField ticketXTextField;
    private JLabel yCoordLabel;
    private JTextField ticketYTextField;
    private JLabel refunableLabel;
    private JComboBox<String> ticketRefComboBox;
    private JLabel typeLabel;
    private JComboBox<String> ticketTypeComboBox;
    private JLabel venueLabel;
    private JLabel venueNameLabel;
    private JTextField venueNameField;
    private JLabel townNameLabel;
    private JTextField venueTownNameField;
    private JLabel capacityLabel;
    private JTextField venueCapacityField;
    private JLabel townXlabel;
    private JTextField venueXField;
    private JLabel streetNameLabel;
    private JTextField venueStreetNameField;
    private JLabel townYlabel;
    private JTextField venueYField;
    private JLabel venueTypeLabel;
    private JComboBox<String> venueTypeComboBox;
    private JLabel townZlabel;
    private JTextField venueZField;
    private JButton backButton;
    private JButton submitButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
