package ru.rosroble.client.ui.visualizeWindow;

import ru.rosroble.client.Client;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.common.Request;
import ru.rosroble.common.commands.RemoveByIdCommand;
import ru.rosroble.common.data.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel {
    private ArrayList<TicketRectangle> rectangles;


    public GraphicsPanel() {
        rectangles = new ArrayList<>();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                checkForRectangleClick(e);
            }
        });
    }

    private void checkForRectangleClick(MouseEvent e) {
        ArrayList<TicketRectangle> list = new ArrayList<>(rectangles);
        System.out.println(e.getX() + " " + e.getY());
        for (TicketRectangle rectangle : list) {
            if (rectangle.contains(e.getX(), e.getY())) {
                Ticket t = rectangle.getTicket();
                StringBuilder message = new StringBuilder();
                String id = String.valueOf(t.getId());
                String name = t.getName();
                String price = String.valueOf(t.getPrice());
                String refundable = String.valueOf(t.getRefundable());
                String type = String.valueOf(t.getType());
                String tX = String.valueOf(t.getCoordinates().getX());
                String tY = String.valueOf(t.getCoordinates().getY());
                message
                        .append(LocaleBundle.getValue("id") + " - " + id + "\n")
                        .append(LocaleBundle.getValue("name") + " - " + name + "\n")
                        .append(LocaleBundle.getValue("price") + " - " + price + "\n")
                        .append(LocaleBundle.getValue("refundable") + " - " + refundable + "\n")
                        .append(LocaleBundle.getValue("type") + " - " + type + "\n")
                        .append(LocaleBundle.getValue("xCoord") + " - " + tX + "\n")
                        .append(LocaleBundle.getValue("yCoord") + " - " + tY + "\n")
                        .append(LocaleBundle.getValue("owner") + " - " + t.getOwner() + "\n");
                UIManager.put("OptionPane.cancelButtonText", LocaleBundle.getValue("remove"));
                int response;
                if(rectangle.getColor().equals(new Color(0, 255, 0))) {
                    response = JOptionPane.showConfirmDialog(null, message.toString(), "", 2);
                    if (response == 2) {
                        try {
                            synchronized (this) {
                                Client client = Client.getClient();
                                client.sendRequest(new Request(new RemoveByIdCommand(t.getId())));
                                JOptionPane.showMessageDialog(null, LocaleBundle.getValue(client.getResponse().getResponseInfo()));
                            }
                        } catch (IOException | ClassNotFoundException exception) {
                            JOptionPane.showMessageDialog(null, LocaleBundle.getValue("fail"));
                            UIControl.switchWindow(UIControl.getFrame(), UIControl.getStartWindow());
                        }
                    }
                }
                else JOptionPane.showMessageDialog(null, message.toString());
            }
        }
    }

    public void addRectangle(TicketRectangle rectangle) {
        rectangles.add(rectangle);
    }

    public void clearPanel() {
        rectangles.clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (TicketRectangle rectangle : rectangles) {
            g2.setColor(rectangle.getColor());
            g2.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            g2.setColor(new Color(0, 0, 0));
            g2.setFont(new Font("Times New Roman", Font.BOLD, 20));
            g2.drawString(rectangle.getText(), rectangle.x + (rectangle.width / 2) - 1, rectangle.y + (rectangle.height / 2) - 1);
        }
    }
}

