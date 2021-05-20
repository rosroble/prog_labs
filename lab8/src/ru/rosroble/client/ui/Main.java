package ru.rosroble.client.ui;

import ru.rosroble.client.Client;
import ru.rosroble.client.ui.welcomeWindow.WelcomeWindow;
import ru.rosroble.common.CommandProcessor;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = UIControl.initFrame();
        JPanel welcomeWindow = UIControl.startWindow(Client.initClient("localhost", 11321, new CommandProcessor()));
        frame.setContentPane(welcomeWindow);
        frame.setVisible(true);
    }
}
