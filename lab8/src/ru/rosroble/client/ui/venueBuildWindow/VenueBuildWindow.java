/*
 * Created by JFormDesigner on Tue May 04 18:04:53 MSK 2021
 */

package ru.rosroble.client.ui.venueBuildWindow;

import net.miginfocom.swing.MigLayout;
import ru.rosroble.client.Client;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.commandListWindow.CommandListWindow;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.commands.AddCommand;
import ru.rosroble.common.commands.CountLessThenVenueCommand;
import ru.rosroble.common.data.*;
import ru.rosroble.common.exceptions.DomainViolationException;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author unknown
 */
public class VenueBuildWindow extends JPanel {

    private final CommandListWindow commandListWindow;
    private final Client client;
    private Map<String, Integer> nameIndexMap = new TreeMap<>();

    public VenueBuildWindow(CommandListWindow commandListWindow)
    {
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

    public void localize() {
        this.venueNameLabel.setText(LocaleBundle.getValue("name"));
        this.capacityLabel.setText(LocaleBundle.getValue("capacity"));
        this.streetNameLabel.setText(LocaleBundle.getValue("streetName"));
        this.venueTypeLabel.setText(LocaleBundle.getValue("type"));
        this.townNameLabel.setText(LocaleBundle.getValue("townName"));
        this.townXlabel.setText(LocaleBundle.getValue("xCoord"));
        this.townYlabel.setText(LocaleBundle.getValue("yCoord"));
        this.townZlabel.setText(LocaleBundle.getValue("zCoord"));

        this.venueLabel.setText(LocaleBundle.getValue("venueLabel"));
        this.buildVenueLabel.setText(LocaleBundle.getValue("venueBuildHeader"));

        this.backButton.setText(LocaleBundle.getValue("back"));
        this.submitButton.setText(LocaleBundle.getValue("submit"));
    }

    protected void backButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), commandListWindow);
    }

    protected void submitButtonMouseReleased(MouseEvent e) {
        try {
            Venue venue = buildVenue();
            client.sendRequest(new Request(new CountLessThenVenueCommand(venue)));
            JOptionPane.showMessageDialog(null,
                    LocaleBundle.getValue("countLsVenueResponse") + client.getResponse().getResponseInfo());
            UIControl.switchWindow(UIControl.getFrame(), commandListWindow);
        } catch (DomainViolationException ignored) {} catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    protected Venue buildVenue() throws DomainViolationException {
        StringBuilder errorMessage = new StringBuilder("Found errors:\n");
        Venue venue = new Venue();
        Address address = new Address();
        Location location = new Location();

        boolean hasErrors = false;
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
        return venue;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        buildVenueLabel = new JLabel();
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

        //---- buildVenueLabel ----
        buildVenueLabel.setText("Build a Venue");
        buildVenueLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
        add(buildVenueLabel, "cell 0 0 29 3,align center center,grow 0 0");

        //---- venueLabel ----
        venueLabel.setText("Enter Venue params:");
        venueLabel.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 20));
        add(venueLabel, "cell 0 3 29 1,alignx center,growx 0");

        //---- venueNameLabel ----
        venueNameLabel.setText("name");
        add(venueNameLabel, "cell 4 5 4 1,alignx right,growx 0");

        //---- venueNameField ----
        venueNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueNameField.setBackground(new Color(83, 73, 176));
        add(venueNameField, "cell 8 5 5 1,width 100:150:200,height 20:30:60");

        //---- townNameLabel ----
        townNameLabel.setText("town name");
        add(townNameLabel, "cell 15 5 3 1,alignx right,growx 0");

        //---- venueTownNameField ----
        venueTownNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueTownNameField.setBackground(new Color(83, 73, 176));
        add(venueTownNameField, "cell 18 5 5 1,width 100:150:200,height 20:30:60");

        //---- capacityLabel ----
        capacityLabel.setText("capacity");
        add(capacityLabel, "cell 4 7 4 1,alignx right,growx 0");

        //---- venueCapacityField ----
        venueCapacityField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueCapacityField.setBackground(new Color(83, 73, 176));
        add(venueCapacityField, "cell 8 7 5 1,width 100:150:200,height 20:30:60");

        //---- townXlabel ----
        townXlabel.setText("town X-coord");
        add(townXlabel, "cell 15 7 3 1,alignx right,growx 0");

        //---- venueXField ----
        venueXField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueXField.setBackground(new Color(83, 73, 176));
        add(venueXField, "cell 18 7 5 1,width 100:150:200,height 20:30:60");

        //---- streetNameLabel ----
        streetNameLabel.setText("street name");
        add(streetNameLabel, "cell 4 9 4 1,alignx right,growx 0");

        //---- venueStreetNameField ----
        venueStreetNameField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueStreetNameField.setBackground(new Color(83, 73, 176));
        add(venueStreetNameField, "cell 8 9 5 1,width 100:150:200,height 20:30:60");

        //---- townYlabel ----
        townYlabel.setText("town Y-coord");
        add(townYlabel, "cell 15 9 3 1,alignx right,growx 0");

        //---- venueYField ----
        venueYField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueYField.setBackground(new Color(83, 73, 176));
        add(venueYField, "cell 18 9 5 1,width 100:150:200,height 20:30:60");

        //---- venueTypeLabel ----
        venueTypeLabel.setText("Type");
        add(venueTypeLabel, "cell 4 11 4 1,alignx right,growx 0");

        //---- venueTypeComboBox ----
        venueTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "-",
            "PUB",
            "BAR",
            "THEATRE"
        }));
        venueTypeComboBox.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueTypeComboBox.setBackground(new Color(83, 73, 176));
        add(venueTypeComboBox, "cell 8 11 5 1,width 100:150:200,height 20:30:60");

        //---- townZlabel ----
        townZlabel.setText("town Z-coord");
        add(townZlabel, "cell 15 11 3 1,alignx right,growx 0");

        //---- venueZField ----
        venueZField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        venueZField.setBackground(new Color(83, 73, 176));
        add(venueZField, "cell 18 11 5 1,width 100:150:200,height 20:30:60");

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
    private JLabel buildVenueLabel;
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
