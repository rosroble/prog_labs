package ru.rosroble.client.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class Test2 {
    public static void main(String[] args) throws IOException {
        RoundedPanel loginPanel = new RoundedPanel();
        RoundedPanel registerPanel = new RoundedPanel();
        JFrame frame = new JFrame();
        frame.setSize(640, 480);

        JLabel imageLabel = new JLabel(new ImageIcon("img.jpg"));
        frame.setContentPane(imageLabel);

        JButton loginButton = new JButton("Вход");
        JButton registerButton = new JButton("Регистрация");
        loginButton.setSize(100, 100);
        registerButton.setSize(100, 100);
        loginButton.setBorder(new RoundedBorder(10));
        registerButton.setBorder(new RoundedBorder(10));
        loginPanel.add(loginButton);
        registerPanel.add(registerButton);
        frame.add(loginPanel);
        frame.add(registerPanel);
        loginButton.setContentAreaFilled(false);
        registerButton.setContentAreaFilled(false);
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }
}
