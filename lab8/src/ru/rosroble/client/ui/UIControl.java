package ru.rosroble.client.ui;

import ru.rosroble.client.Client;
import ru.rosroble.client.ui.welcomeWindow.WelcomeWindow;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import java.awt.*;

public final class UIControl {

    private static JFrame frame = null;
    private static JPanel startWindow = null;

    public static void switchWindow(JFrame frame, JPanel newWindow) {
        SwingUtilities.invokeLater(() -> {
            frame.setContentPane(newWindow);
            frame.validate();
        });
    }

    public static JFrame initFrame() {
        if (frame == null) {
            frame = new JFrame();
            frame.setSize(1280, 720);
            frame.setMinimumSize(new Dimension(800, 600));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        return frame;
    }

    public static JFrame getFrame() {
        if (frame == null) throw new RuntimeException("Frame is not initialized. Use UIControl.initFrame()");
        return frame;
    }

    public static JPanel startWindow(Client client) {
        if (startWindow == null) {
            startWindow = new WelcomeWindow(client);
        }
        return startWindow;
    }

    public static JPanel getStartWindow() {
        return startWindow;
    }
}
