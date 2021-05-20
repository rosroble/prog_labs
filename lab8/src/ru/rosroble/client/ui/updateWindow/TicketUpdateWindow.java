/*
 * Created by JFormDesigner on Tue May 04 18:04:53 MSK 2021
 */

package ru.rosroble.client.ui.updateWindow;

import net.miginfocom.swing.MigLayout;
import ru.rosroble.client.Client;
import ru.rosroble.client.ui.commandListWindow.CommandListWindow;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.commands.AddCommand;
import ru.rosroble.common.commands.UpdateIdCommand;
import ru.rosroble.common.data.*;
import ru.rosroble.common.exceptions.DomainViolationException;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @author unknown
 */
public class TicketUpdateWindow extends JPanel{

    private CommandListWindow commandListWindow;
    private Client client;
    public TicketUpdateWindow(CommandListWindow commandListWindow)
    {
        this.commandListWindow = commandListWindow;
        this.client = Client.getClient();
        initComponents();
    }

    protected void submitButtonMouseReleased(MouseEvent e) {
        try {
            Request updateRequest = buildUpdateTicketRequest();
            client.sendRequest(updateRequest);
            JOptionPane.showMessageDialog(null, client.getResponse().getResponseInfo());
            UIControl.switchWindow(UIControl.getFrame(), commandListWindow);
        } catch (DomainViolationException ignored) {} catch (IOException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    public void localize() {
        this.headerLabel.setText(LocaleBundle.getValue("ticketUpdateHeader"));
        this.nameLabel.setText(LocaleBundle.getValue("name"));
        this.priceLabel.setText(LocaleBundle.getValue("price"));
        this.refundableLabel.setText(LocaleBundle.getValue("refundable"));
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


    protected Request buildUpdateTicketRequest() throws DomainViolationException {
        StringBuilder errorMessage = new StringBuilder("Found errors:\n");
        Ticket ticket = new Ticket();
        Venue venue = new Venue();
        Address address = new Address();
        Location location = new Location();
        boolean hasErrors = false;
        long id = 0;

        try {
            id = Long.parseLong(idTextField.getText());
        } catch (NumberFormatException e) {
            hasErrors = true;
            errorMessage.append(LocaleBundle.getValue("idInvalid")).append("\n");
        }

        boolean isUpdateName = ticketNameTextField.isEnabled();
        boolean isUpdatePrice = ticketPriceTextField.isEnabled();
        boolean isUpdateRefundable = ticketRefComboBox.isEnabled();
        boolean isUpdateVenue = venueCheckbox.isSelected();
        boolean isUpdateType = ticketTypeComboBox.isEnabled();
        boolean isUpdateCoordinates = CoordCheckBox.isSelected();

        if (isUpdateName) {
            try {
                String name = Parsers.parseTheName(this.ticketNameTextField.getText());
                ticket.setName(name);
            } catch (DomainViolationException e) {
                hasErrors = true;
                errorMessage.append(e.getMessage()).append("\n");
            }
        }
        if (isUpdatePrice) {
            try {
                double price = Parsers.parseThePrice(this.ticketPriceTextField.getText());
                ticket.setPrice(price);
            } catch (DomainViolationException e) {
                hasErrors = true;
                errorMessage.append(e.getMessage()).append("\n");
            }
        }
        if (isUpdateRefundable) {
            try {
                boolean refundable = Parsers.parseTheRefundable((String) this.ticketRefComboBox.getSelectedItem());
                ticket.setRefundable(refundable);
            } catch (DomainViolationException e) {
                hasErrors = true;
                errorMessage.append(e.getMessage()).append("\n");
            }
        }
        if (isUpdateCoordinates) {
            try {
                Coordinates coordinates = Parsers.parseTheCoordinates(this.ticketXTextField.getText(), this.ticketYTextField.getText());
                ticket.setCoordinates(coordinates);
            } catch (DomainViolationException e) {
                hasErrors = true;
                errorMessage.append(e.getMessage()).append("\n");
            }
        }
        if (isUpdateType) {
            try {
                TicketType ticketType = Parsers.parseTicketType((String) this.ticketTypeComboBox.getSelectedItem());
                ticket.setType(ticketType);
            } catch (DomainViolationException e) {
                hasErrors = true;
                errorMessage.append(e.getMessage()).append("\n");
            }
        }
        if (isUpdateVenue) {
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
                VenueType vType = Parsers.parseVenueType((String) this.venueTypeComboBox.getSelectedItem());
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
        }

        if (hasErrors) {
            JOptionPane.showMessageDialog(null, errorMessage.toString());
            throw new DomainViolationException("Cannot build a ticket");
        }

        address.setLocation(location);
        venue.setAddress(address);
        ticket.setVenue(venue);
        return new Request(new UpdateIdCommand(
                ticket,
                id,
                isUpdateName,
                isUpdatePrice,
                isUpdateRefundable,
                isUpdateVenue,
                isUpdateType,
                isUpdateCoordinates));
    }

    private void nameCheckboxMouseReleased(MouseEvent e) {
        ticketNameTextField.setEnabled(nameCheckbox.isSelected());
    }


    private void venueCheckboxMouseReleased(MouseEvent e) {
        boolean isSelected = nameCheckbox.isSelected();
        venueNameField.setEnabled(isSelected);
        venueCapacityField.setEnabled(isSelected);
        venueXField.setEnabled(isSelected);
        venueYField.setEnabled(isSelected);
        venueZField.setEnabled(isSelected);
        venueStreetNameField.setEnabled(isSelected);
        venueTownNameField.setEnabled(isSelected);
        venueTypeComboBox.setEnabled(isSelected);

    }

    private void CoordCheckBoxMouseReleased(MouseEvent e) {
        ticketXTextField.setEnabled(CoordCheckBox.isSelected());
        ticketYTextField.setEnabled(CoordCheckBox.isSelected());
    }

    private void priceCheckboxMouseReleased(MouseEvent e) {
        ticketPriceTextField.setEnabled(priceCheckbox.isSelected());
    }

    private void refCheckBoxMouseReleased(MouseEvent e) {
        ticketRefComboBox.setEnabled(refCheckBox.isSelected());
    }

    private void tTypeCheckBoxMouseReleased(MouseEvent e) {
        ticketTypeComboBox.setEnabled(tTypeCheckBox.isSelected());
    }

    private void backButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), commandListWindow);
    }




    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        headerLabel = new JLabel();
        ticketLabel = new JLabel();
        idLabel = new JLabel();
        idTextField = new JTextField();
        nameLabel = new JLabel();
        ticketNameTextField = new JTextField();
        nameCheckbox = new JCheckBox();
        xCoordLabel = new JLabel();
        ticketXTextField = new JTextField();
        CoordCheckBox = new JCheckBox();
        priceLabel = new JLabel();
        ticketPriceTextField = new JTextField();
        priceCheckbox = new JCheckBox();
        yCoordLabel = new JLabel();
        ticketYTextField = new JTextField();
        refundableLabel = new JLabel();
        ticketRefComboBox = new JComboBox<>();
        refCheckBox = new JCheckBox();
        typeLabel = new JLabel();
        ticketTypeComboBox = new JComboBox<>();
        tTypeCheckBox = new JCheckBox();
        venueLabel = new JLabel();
        venueCheckbox = new JCheckBox();
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
            "[]" +
            "[]" +
            "[]"));

        //---- headerLabel ----
        headerLabel.setText("Update a Ticket");
        headerLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
        add(headerLabel, "cell 0 0 32 3,align center center,grow 0 0");

        //---- ticketLabel ----
        ticketLabel.setText("Enter ID and basic ticket params:");
        ticketLabel.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 20));
        add(ticketLabel, "cell 0 3 32 1,alignx center,growx 0");

        //---- idLabel ----
        idLabel.setText("ID");
        add(idLabel, "cell 13 4 2 1,alignx right,growx 0");

        //---- idTextField ----
        idTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        idTextField.setBackground(new Color(83, 73, 176));
        add(idTextField, "cell 15 4 2 1,alignx left,growx 0,width 30:70:100,height 20:30:60");

        //---- nameLabel ----
        nameLabel.setText("name");
        add(nameLabel, "cell 4 5 5 1,alignx right,growx 0");

        //---- ticketNameTextField ----
        ticketNameTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketNameTextField.setBackground(new Color(83, 73, 176));
        ticketNameTextField.setEnabled(false);
        add(ticketNameTextField, "cell 9 5 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- nameCheckbox ----
        nameCheckbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                nameCheckboxMouseReleased(e);
            }
        });
        add(nameCheckbox, "cell 14 5");

        //---- xCoordLabel ----
        xCoordLabel.setText("X-coord");
        add(xCoordLabel, "cell 15 5 5 1,alignx right,growx 0");

        //---- ticketXTextField ----
        ticketXTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketXTextField.setBackground(new Color(83, 73, 176));
        ticketXTextField.setEnabled(false);
        add(ticketXTextField, "cell 20 5 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- CoordCheckBox ----
        CoordCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                CoordCheckBoxMouseReleased(e);
            }
        });
        add(CoordCheckBox, "cell 25 5 1 2");

        //---- priceLabel ----
        priceLabel.setText("price");
        add(priceLabel, "cell 6 6 3 1,alignx right,growx 0");

        //---- ticketPriceTextField ----
        ticketPriceTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketPriceTextField.setBackground(new Color(83, 73, 176));
        ticketPriceTextField.setEnabled(false);
        add(ticketPriceTextField, "cell 9 6 5 1,align center center,grow 0 0,width 100:150:200,height 20:30:60");

        //---- priceCheckbox ----
        priceCheckbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                priceCheckboxMouseReleased(e);
            }
        });
        add(priceCheckbox, "cell 14 6");

        //---- yCoordLabel ----
        yCoordLabel.setText("Y-coord");
        add(yCoordLabel, "cell 15 6 5 1,alignx right,growx 0");

        //---- ticketYTextField ----
        ticketYTextField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketYTextField.setBackground(new Color(83, 73, 176));
        ticketYTextField.setEnabled(false);
        add(ticketYTextField, "cell 20 6 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- refundableLabel ----
        refundableLabel.setText("refundable");
        add(refundableLabel, "cell 4 7 5 1,alignx right,growx 0");

        //---- ticketRefComboBox ----
        ticketRefComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "-",
            "true",
            "false"
        }));
        ticketRefComboBox.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketRefComboBox.setBackground(new Color(83, 73, 176));
        ticketRefComboBox.setEnabled(false);
        add(ticketRefComboBox, "cell 9 7 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- refCheckBox ----
        refCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                refCheckBoxMouseReleased(e);
            }
        });
        add(refCheckBox, "cell 14 7");

        //---- typeLabel ----
        typeLabel.setText("Type");
        add(typeLabel, "cell 15 7 5 1,alignx right,growx 0");

        //---- ticketTypeComboBox ----
        ticketTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "-",
            "VIP",
            "USUAL",
            "BUDGETARY",
            "CHEAP"
        }));
        ticketTypeComboBox.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        ticketTypeComboBox.setBackground(new Color(83, 73, 176));
        ticketTypeComboBox.setEnabled(false);
        add(ticketTypeComboBox, "cell 20 7 5 1,alignx center,growx 0,width 100:150:200,height 20:30:60");

        //---- tTypeCheckBox ----
        tTypeCheckBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                tTypeCheckBoxMouseReleased(e);
            }
        });
        add(tTypeCheckBox, "cell 25 7");

        //---- venueLabel ----
        venueLabel.setText("Enter Venue params:");
        venueLabel.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 20));
        add(venueLabel, "cell 0 8 32 1,alignx center,growx 0");

        //---- venueCheckbox ----
        venueCheckbox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                venueCheckboxMouseReleased(e);
            }
        });
        add(venueCheckbox, "cell 0 8 32 1");

        //---- venueNameLabel ----
        venueNameLabel.setText("name");
        add(venueNameLabel, "cell 4 9 5 1,alignx right,growx 0");

        //---- venueNameField ----
        venueNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueNameField.setBackground(new Color(83, 73, 176));
        venueNameField.setEnabled(false);
        add(venueNameField, "cell 9 9 5 1,width 100:150:200,height 20:30:60");

        //---- townNameLabel ----
        townNameLabel.setText("town name");
        add(townNameLabel, "cell 15 9 5 1,alignx right,growx 0");

        //---- venueTownNameField ----
        venueTownNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueTownNameField.setBackground(new Color(83, 73, 176));
        venueTownNameField.setEnabled(false);
        add(venueTownNameField, "cell 20 9 5 1,width 100:150:200,height 20:30:60");

        //---- capacityLabel ----
        capacityLabel.setText("capacity");
        add(capacityLabel, "cell 4 10 5 1,alignx right,growx 0");

        //---- venueCapacityField ----
        venueCapacityField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueCapacityField.setBackground(new Color(83, 73, 176));
        venueCapacityField.setEnabled(false);
        add(venueCapacityField, "cell 9 10 5 1,width 100:150:200,height 20:30:60");

        //---- townXlabel ----
        townXlabel.setText("town X-coord");
        add(townXlabel, "cell 15 10 5 1,alignx right,growx 0");

        //---- venueXField ----
        venueXField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueXField.setBackground(new Color(83, 73, 176));
        venueXField.setEnabled(false);
        add(venueXField, "cell 20 10 5 1,width 100:150:200,height 20:30:60");

        //---- streetNameLabel ----
        streetNameLabel.setText("street name");
        add(streetNameLabel, "cell 4 11 5 1,alignx right,growx 0");

        //---- venueStreetNameField ----
        venueStreetNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueStreetNameField.setBackground(new Color(83, 73, 176));
        venueStreetNameField.setEnabled(false);
        add(venueStreetNameField, "cell 9 11 5 1,width 100:150:200,height 20:30:60");

        //---- townYlabel ----
        townYlabel.setText("town Y-coord");
        add(townYlabel, "cell 15 11 5 1,alignx right,growx 0");

        //---- venueYField ----
        venueYField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueYField.setBackground(new Color(83, 73, 176));
        venueYField.setEnabled(false);
        add(venueYField, "cell 20 11 5 1,width 100:150:200,height 20:30:60");

        //---- venueTypeLabel ----
        venueTypeLabel.setText("Type");
        add(venueTypeLabel, "cell 4 12 5 1,alignx right,growx 0");

        //---- venueTypeComboBox ----
        venueTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "-",
            "PUB",
            "BAR",
            "THEATRE"
        }));
        venueTypeComboBox.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueTypeComboBox.setBackground(new Color(83, 73, 176));
        venueTypeComboBox.setEnabled(false);
        add(venueTypeComboBox, "cell 9 12 5 1,width 100:150:200,height 20:30:60");

        //---- townZlabel ----
        townZlabel.setText("town Z-coord");
        add(townZlabel, "cell 15 12 5 1,alignx right,growx 0");

        //---- venueZField ----
        venueZField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueZField.setBackground(new Color(83, 73, 176));
        venueZField.setEnabled(false);
        add(venueZField, "cell 20 12 5 1,width 100:150:200,height 20:30:60");

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
        add(backButton, "cell 9 15 5 1,alignx center,growx 0,width 100:125:175");

        //---- submitButton ----
        submitButton.setText("Submit");
        submitButton.setBackground(new Color(35, 118, 31));
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                submitButtonMouseReleased(e);
            }
        });
        add(submitButton, "cell 20 15 5 1,alignx center,growx 0,width 100:125:175");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel headerLabel;
    private JLabel ticketLabel;
    private JLabel idLabel;
    private JTextField idTextField;
    private JLabel nameLabel;
    private JTextField ticketNameTextField;
    private JCheckBox nameCheckbox;
    private JLabel xCoordLabel;
    private JTextField ticketXTextField;
    private JCheckBox CoordCheckBox;
    private JLabel priceLabel;
    private JTextField ticketPriceTextField;
    private JCheckBox priceCheckbox;
    private JLabel yCoordLabel;
    private JTextField ticketYTextField;
    private JLabel refundableLabel;
    private JComboBox<String> ticketRefComboBox;
    private JCheckBox refCheckBox;
    private JLabel typeLabel;
    private JComboBox<String> ticketTypeComboBox;
    private JCheckBox tTypeCheckBox;
    private JLabel venueLabel;
    private JCheckBox venueCheckbox;
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
