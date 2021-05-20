/*
 * Created by JFormDesigner on Sat May 08 17:24:16 MSK 2021
 */

package ru.rosroble.client.ui.visualizeWindow;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import net.miginfocom.swing.*;
import ru.rosroble.client.Client;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.commandListWindow.CommandListWindow;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.common.Request;
import ru.rosroble.common.ShowcaseResponse;
import ru.rosroble.common.commands.TicketShowcaseCommand;
import ru.rosroble.common.data.Ticket;

/**
 * @author unknown
 */
public class VisualizeWindow extends JPanel {

    private float MAX_X_COORDINATE_VALUE = (float) -355;
    private float MIN_X_COORDINATE_VALUE = Float.MAX_VALUE;
    private long MAX_Y_COORDINATE_VALUE = Long.MIN_VALUE;
    private long MIN_Y_COORDINATE_VALUE = Long.MAX_VALUE;
    private boolean isActive = false;

    private CommandListWindow cmdListWindow;
    private ArrayList<Ticket> tickets;
    private ArrayList<TicketRectangle> rectangles = new ArrayList<>();
    Thread drawThread;

    public VisualizeWindow(CommandListWindow cmdListWindow) {
        this.cmdListWindow = cmdListWindow;
        initComponents();
    }


    public void initListening() {
        isActive = true;
        drawThread = new Thread(() -> {
            while (isActive) {
                panel.clearPanel();
                visualize();
                panel.repaint();
                UIControl.getFrame().validate();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        drawThread.start();
    }


    private void visualize() {
        synchronized (panel) {
            try {
                panel.removeAll();
                Client client = Client.getClient();
                client.sendRequest(new Request(new TicketShowcaseCommand()));
                ShowcaseResponse response = (ShowcaseResponse) client.getResponse();
                this.tickets = response.getList();
                rectangles.removeIf(rect -> !tickets.contains(rect.getTicket()));
                for (Ticket t : tickets) {
                    defineMaxAndMinCoordinates();
                    TicketRectangle rectangle = drawObject(t);
                    //panel.addRectangle(rectangle);
                    initAnimation(rectangle);
                }
            } catch (IOException | ClassNotFoundException exception) {
                JOptionPane.showMessageDialog(null, LocaleBundle.getValue("fail"));
            }
        }
    }

    private void initAnimation(TicketRectangle rectangle) {
        int initialWidth = rectangle.width;
        int initialHeight = rectangle.height;
        if (!rectangles.contains(rectangle)) {
            rectangle.width = 0;
            rectangle.height = 0;
            rectangles.add(rectangle);
        } else {
            int index = 0;
            for (TicketRectangle rect: rectangles) {
                if (rect.equals(rectangle)) break;
                index++;
            }
            rectangles.get(index).width += 5;
            if (rectangles.get(index).width > initialWidth) rectangles.get(index).width = initialWidth;
            rectangles.get(index).height += 3;
            if (rectangles.get(index).height > initialHeight) rectangles.get(index).height = initialHeight;
            rectangle.width = rectangles.get(index).width;
            rectangle.height = rectangles.get(index).height;
        }
        panel.addRectangle(rectangle);
    }

    private void defineMaxAndMinCoordinates() {
        for (Ticket t: tickets) {
            float x = t.getCoordinates().getX();
            long y = t.getCoordinates().getY();
            if (x > MAX_X_COORDINATE_VALUE) MAX_X_COORDINATE_VALUE = (int) x;
            if (x < MIN_X_COORDINATE_VALUE) MIN_X_COORDINATE_VALUE = (int) x;
            if (y > MAX_Y_COORDINATE_VALUE) MAX_Y_COORDINATE_VALUE = (int) y;
            if (y < MIN_Y_COORDINATE_VALUE) MIN_Y_COORDINATE_VALUE = (int) y;
        }
    }

    private TicketRectangle drawObject(Ticket ticket) {
        int width = (int) (0.8 * panel.getWidth());
        int height = (int) (0.8 * panel.getHeight());
        float x = ticket.getCoordinates().getX();
        long y = ticket.getCoordinates().getY();
        Color color = Client.getClient().getUsername().equals(ticket.getOwner()) ? new Color(0, 255, 0) : new Color(255, 0, 0);

        int screenX = (int) ((x - MIN_X_COORDINATE_VALUE) * (width / (MAX_X_COORDINATE_VALUE - MIN_X_COORDINATE_VALUE)) + panel.getWidth() * 0.05);
        int screenY = (int) ((y - MIN_Y_COORDINATE_VALUE) * (height / (MAX_Y_COORDINATE_VALUE - MIN_Y_COORDINATE_VALUE)) + panel.getHeight() * 0.05);
        Dimension rectangleSize = evaluateRectangleSize(ticket.getPrice());
        TicketRectangle rectangle = new TicketRectangle(screenX, screenY, rectangleSize.width, rectangleSize.height, color, ticket);
     //   panel.addRectangle(rectangle);
        return rectangle;
    }

    private Dimension evaluateRectangleSize(double price) {
        if (price > 10000) return new Dimension(panel.getWidth() / 40 + 110, panel.getHeight() / 40 + 55);
        if (price > 1000) return new Dimension(panel.getWidth() / 40 + 90, panel.getHeight() / 40 + 45);
        if (price > 100) return new Dimension(panel.getWidth() / 40 + 75, panel.getHeight() / 40 + 35);
        return new Dimension(panel.getWidth() / 20 + 60, panel.getHeight() / 20 + 30);
    }

    public void localize() {
        // TODO: localize
    }

    private void backButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), cmdListWindow);
        isActive = false;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        ticketMgLabel = new JLabel();
        panel = new GraphicsPanel();
        backButton = new JButton();

        //======== this ========
        setBackground(new Color(60, 96, 205));
        setLayout(new MigLayout(
            "hidemode 3,alignx center",
            // columns
            "[fill]" +
            "[grow,fill]" +
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
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[16,fill]",
            // rows
            "[]" +
            "[]" +
            "[30,grow]" +
            "[]" +
            "[]"));

        //---- ticketMgLabel ----
        ticketMgLabel.setText("Visualize Panel");
        ticketMgLabel.setFont(new Font("Droid Sans Mono Dotted", Font.PLAIN, 48));
        ticketMgLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        ticketMgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(ticketMgLabel, "cell 0 0 29 1,align center center,grow 0 0");

        //======== visualizePanel ========
        {
            panel.setBackground(new Color(102, 102, 255));
            panel.setForeground(new Color(255, 51, 51));
            panel.setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel.getComponentCount(); i++) {
                    Rectangle bounds = panel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel.setMinimumSize(preferredSize);
                panel.setPreferredSize(preferredSize);
            }
        }
        add(panel, "cell 1 1 27 2,grow");

        //---- backButton ----
        backButton.setText("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                backButtonMouseReleased(e);
            }
        });
        add(backButton, "cell 0 3 29 1,alignx center,growx 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel ticketMgLabel;
    private GraphicsPanel panel;
    private JButton backButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
