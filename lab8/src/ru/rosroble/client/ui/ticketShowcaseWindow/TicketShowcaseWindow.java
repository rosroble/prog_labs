/*
 * Created by JFormDesigner on Fri May 07 17:07:47 MSK 2021
 */

package ru.rosroble.client.ui.ticketShowcaseWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import net.miginfocom.swing.*;
import ru.rosroble.client.Client;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.commandListWindow.CommandListWindow;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.ShowcaseResponse;
import ru.rosroble.common.commands.TicketShowcaseCommand;
import ru.rosroble.common.commands.UpdateIdCommand;
import ru.rosroble.common.data.Address;
import ru.rosroble.common.data.Location;
import ru.rosroble.common.data.Ticket;
import ru.rosroble.common.data.Venue;
import ru.rosroble.common.exceptions.DomainViolationException;

/**
 * @author unknown
 */
public class TicketShowcaseWindow extends JPanel {

    private CommandListWindow cmdListWindow;
    private ArrayList<Ticket> list;

    public TicketShowcaseWindow(CommandListWindow cmdListWindow) {
        this.cmdListWindow = cmdListWindow;
        initComponents();
        initTableModel();
        initFilterTextListener();
    }

    private void initTableModel() {
        ticketTable.setModel(new ExtendedTableModel(
                new Object[][] {
                },
                new String[] {
                        "id", "name", "XCoord", "YCoord", "price", "refundable", "TicketType", "venueId", "venueName", "venueCapacity", "venueType", "venueStreet", "VXCoord", "VYCoord", "VZCoord", "townName", "owner"
                }
        ));
    }

    private void backButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), cmdListWindow);
    }


    public void localize() {
        // TODO: localize combo-box
        this.ticketShowcaseLabel.setText(LocaleBundle.getValue("ticketShowcaseHeader"));
        this.backButton.setText(LocaleBundle.getValue("back"));

        TableColumnModel columnModel = ticketTable.getTableHeader().getColumnModel();
        columnModel.getColumn(1).setHeaderValue(LocaleBundle.getValue("name"));
        columnModel.getColumn(2).setHeaderValue(LocaleBundle.getValue("xCoord"));
        columnModel.getColumn(3).setHeaderValue(LocaleBundle.getValue("yCoord"));
        columnModel.getColumn(4).setHeaderValue(LocaleBundle.getValue("price"));
        columnModel.getColumn(5).setHeaderValue(LocaleBundle.getValue("refundable"));
        columnModel.getColumn(6).setHeaderValue(LocaleBundle.getValue("ticketType"));
        columnModel.getColumn(7).setHeaderValue(LocaleBundle.getValue("venueId"));
        columnModel.getColumn(8).setHeaderValue(LocaleBundle.getValue("venueName"));
        columnModel.getColumn(9).setHeaderValue(LocaleBundle.getValue("capacity"));
        columnModel.getColumn(10).setHeaderValue(LocaleBundle.getValue("venueType"));
        columnModel.getColumn(11).setHeaderValue(LocaleBundle.getValue("venueStreet"));
        columnModel.getColumn(12).setHeaderValue(LocaleBundle.getValue("vX"));
        columnModel.getColumn(13).setHeaderValue(LocaleBundle.getValue("vY"));
        columnModel.getColumn(14).setHeaderValue(LocaleBundle.getValue("vZ"));
        columnModel.getColumn(15).setHeaderValue(LocaleBundle.getValue("townName"));
        columnModel.getColumn(16).setHeaderValue(LocaleBundle.getValue("owner"));

        ticketTable.getTableHeader().repaint();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        ticketShowcaseLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        ticketTable = new JTable();
        filterKey = new JComboBox<>();
        backButton = new JButton();
        filterText = new JTextField();
        filterOperation = new JComboBox<>();

        //======== this ========
        setBackground(new Color(60, 96, 205));
        setLayout(new MigLayout(
            "hidemode 3,align center center",
            // columns
            "[grow,right]" +
            "[fill]" +
            "[fill]" +
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
            "[359,grow]" +
            "[40]" +
            "[grow]" +
            "[grow]" +
            "[grow]" +
            "[grow]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[grow]" +
            "[grow]" +
            "[]" +
            "[]" +
            "[grow]" +
            "[]" +
            "[grow]" +
            "[]" +
            "[]" +
            "[grow]" +
            "[]" +
            "[]" +
            "[]"));

        //---- ticketShowcaseLabel ----
        ticketShowcaseLabel.setText("Ticket Showcase");
        ticketShowcaseLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 48));
        add(ticketShowcaseLabel, "cell 0 0 31 3,align center center,grow 0 0");

        //======== scrollPane1 ========
        {

            //---- ticketTable ----
            ticketTable.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                    "id", "name", "XCoord", "YCoord", "price", "refundable", "TicketType", "venueId", "venueName", "venueCapacity", "venueType", "venueStreet", "VXCoord", "VYCoord", "VZCoord", "townName", "owner"
                }
            ) {
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
            });
            ticketTable.setShowHorizontalLines(false);
            scrollPane1.setViewportView(ticketTable);
        }
        add(scrollPane1, "cell 0 3 31 13,aligny center,grow 100 0,hmax 2000");

        //---- filterKey ----
        filterKey.setModel(new DefaultComboBoxModel<>(new String[] {
            "id",
            "name",
            "XCoord",
            "YCoord",
            "price",
            "refundable",
            "TicketType",
            "venueId",
            "venueName",
            "venueCapacity",
            "venueType",
            "venueStreet",
            "VXCoord",
            "VYCoord",
            "VZCoord",
            "townName",
            "owner"
        }));
        add(filterKey, "cell 5 16 6 8,alignx right,growx 0");

        //---- backButton ----
        backButton.setText("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                backButtonMouseReleased(e);
            }
        });
        add(backButton, "cell 12 16 8 8,alignx center,growx 0,width 100:150:200");
        add(filterText, "cell 23 16 6 8,growx,width 100:150:200");

        //---- filterOperation ----
        filterOperation.setModel(new DefaultComboBoxModel<>(new String[] {
            "=",
            ">",
            "<"
        }));
        add(filterOperation, "cell 2 16 3 8,alignx center,growx 0,width 30:60:90");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel ticketShowcaseLabel;
    private JScrollPane scrollPane1;
    private JTable ticketTable;
    private JComboBox<String> filterKey;
    private JButton backButton;
    private JTextField filterText;
    private JComboBox<String> filterOperation;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void addTicketToTable(Ticket ticket) {
        Object[] row = new Object[]{
                ticket.getId(),
                ticket.getName(),
                ticket.getCoordinates().getX(),
                ticket.getCoordinates().getY(),
                ticket.getPrice(),
                ticket.getRefundable(),
                ticket.getType().toString(),
                ticket.getVenue().getId(),
                ticket.getVenue().getName(),
                ticket.getVenue().getCapacity(),
                ticket.getVenue().getType().toString(),
                ticket.getVenue().getName(),
                ticket.getVenue().getAddress().getTown().getX(),
                ticket.getVenue().getAddress().getTown().getY(),
                ticket.getVenue().getAddress().getTown().getZ(),
                ticket.getVenue().getAddress().getTown().getName(),
                ticket.getOwner()
        };
        DefaultTableModel model = (DefaultTableModel) ticketTable.getModel();
        model.addRow(row);
    }

    public void initTable() {
        Client client = Client.getClient();
        ArrayList<Ticket> ticketArrayList = new ArrayList<>();
        try {
            client.sendRequest(new Request(new TicketShowcaseCommand()));
            ShowcaseResponse response = (ShowcaseResponse) client.getResponse();
            ticketArrayList = response.getList();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        DefaultTableModel model = (DefaultTableModel) ticketTable.getModel();
        model.setRowCount(0);
        for (Ticket t : ticketArrayList) {
            addTicketToTable(t);
            System.out.println(t);
        }
    }

    public void initFilterTextListener() {
        DefaultTableModel model = (DefaultTableModel) ticketTable.getModel();

        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
        initSortRules(sorter);
        ticketTable.setRowSorter(sorter);

        filterText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String textToFilter = filterText.getText();
                int index = filterKey.getSelectedIndex();
                String filterOperationValue = (String) filterOperation.getSelectedItem();
                if (textToFilter.isEmpty()) sorter.setRowFilter(null);
                else {
                    try {
                       // sorter.setRowFilter(RowFilter.regexFilter(textToFilter, index));
                        sorter.setRowFilter(chooseFilter(index, filterOperationValue));
                    } catch (PatternSyntaxException exception) {
                        exception.printStackTrace();
                    }
                }
            }


        });
    }

    private RowFilter chooseFilter(int index, String filterOperationValue) {
        RowFilter.ComparisonType type = null;
        if (filterOperationValue.equals("=")) type = RowFilter.ComparisonType.EQUAL;
        if (filterOperationValue.equals(">")) type = RowFilter.ComparisonType.AFTER;
        if (filterOperationValue.equals("<")) type = RowFilter.ComparisonType.BEFORE;

        switch (index) {
            case 0:
            case 3:
            case 7:
            case 12:
            case 14:
                try {
                    return RowFilter.numberFilter(type, Long.parseLong(filterText.getText()), index);
                } catch (NumberFormatException e) {
                    return null;
                }
            case 2:
            case 4:
            case 9:
            case 13:
                try {
                    return RowFilter.numberFilter(type, Double.parseDouble(filterText.getText()), index);
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                if (type == RowFilter.ComparisonType.EQUAL) return RowFilter.regexFilter("^" + filterText.getText() + "$", index);
                if (type == RowFilter.ComparisonType.AFTER) return RowFilter.regexFilter("[" + filterText.getText().toLowerCase(Locale.ROOT).charAt(0) + "-z]+", index);
                if (type == RowFilter.ComparisonType.BEFORE) return RowFilter.regexFilter("[a-" + filterText.getText().toLowerCase(Locale.ROOT).charAt(0) + "]+", index);

        }
        return null;
    }


    public void initSortRules(TableRowSorter<DefaultTableModel> sorter) {
        LongComparator comparator = new LongComparator();
        sorter.setComparator(0, comparator);
        sorter.setComparator(2, comparator);
        sorter.setComparator(3, comparator);
        sorter.setComparator(4, comparator);
        sorter.setComparator(7, comparator);
        sorter.setComparator(9, comparator);
        sorter.setComparator(12, comparator);
        sorter.setComparator(13, comparator);
        sorter.setComparator(14, comparator);
    }

    RowFilter<DefaultTableModel, Integer> GreaterThan = new RowFilter<DefaultTableModel, Integer>() {
        public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
            TableModel defaultModel = ticketTable.getModel();
            ticketTable.setModel(entry.getModel());

            //0 is the first column
            if ((long) ticketTable.getValueAt(entry.getIdentifier(), 0) > Long.parseLong(filterText.getText())) {
                ticketTable.setModel(defaultModel);
                return true;
            }
            return false;
        }
    };


}

class LongComparator implements Comparator<Number> {

    @Override
    public int compare(Number o1, Number o2) {
        return (int) (o1.doubleValue() - o2.doubleValue());
    }
}
