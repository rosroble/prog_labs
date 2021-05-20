/*
 * Created by JFormDesigner on Sat May 01 16:32:57 MSK 2021
 */

package ru.rosroble.client.ui.registerWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;
import ru.rosroble.client.Client;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.client.ui.welcomeWindow.WelcomeWindow;
import ru.rosroble.common.Request;
import ru.rosroble.common.Response;
import ru.rosroble.common.commands.AuthCommand;
import ru.rosroble.common.exceptions.DomainViolationException;

/**
 * @author unknown
 */
public class RegisterWindow extends JPanel {
    
    WelcomeWindow welcomeWindow;
    public RegisterWindow(WelcomeWindow welcomeWindow) {
        this.welcomeWindow = welcomeWindow;
        initComponents();
    }

    private void switchToWelcomeWindow() {
        JFrame parent = UIControl.getFrame();
        UIControl.switchWindow(parent, welcomeWindow);
    }

    public void localize() {
        this.loginLabel.setText(LocaleBundle.getValue("login"));
        this.registerLabel.setText(LocaleBundle.getValue("register"));
        this.passwordLabel.setText(LocaleBundle.getValue("password"));
        this.backButton.setText(LocaleBundle.getValue("back"));
        this.registerButton.setText(LocaleBundle.getValue("register"));
    }

    private void backButtonMouseReleased(MouseEvent e) {
        switchToWelcomeWindow();
    }

    private void registerButtonMouseReleased(MouseEvent e) {
        Client client = welcomeWindow.getClient();
        boolean isSuccessfullyConnected = client.connect();
        String responseMessage = "Error while connecting";
        boolean responseOK = false;
        String username = loginField.getText();
        String password = String.valueOf(passwordField.getPassword());
        AuthCommand register;
        try {
            register = new AuthCommand(username, password, AuthCommand.AuthType.REGISTER);
        } catch (DomainViolationException exception) {
            JOptionPane.showMessageDialog(null, LocaleBundle.getValue(exception.getMessage()));
            return;
        }
        if (isSuccessfullyConnected) {
            try {
                client.sendRequest(new Request(register));
                Response response = client.getResponse();
                responseMessage = response.getResponseInfo();
                responseOK = response.isOK();
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
        JOptionPane.showMessageDialog(null, LocaleBundle.getValue(responseMessage));
        if(responseOK) {
            welcomeWindow.setUsernameText(username);
            welcomeWindow.setPasswordText(password);
            switchToWelcomeWindow();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        registerLabel = new JLabel();
        loginLabel = new JLabel();
        loginField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        hSpacer1 = new JPanel(null);
        registerButton = new JButton();
        hSpacer2 = new JPanel(null);
        backButton = new JButton();
        vSpacer1 = new JPanel(null);

        //======== this ========
        setBackground(new Color(60, 96, 205));
        setLayout(new MigLayout(
            "hidemode 3,align center center",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[303,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[40:80:90,grow 23,center]" +
            "[9]" +
            "[3]" +
            "[4]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- registerLabel ----
        registerLabel.setText("Register");
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setFont(new Font("JetBrains Mono ExtraBold", Font.PLAIN, 48));
        add(registerLabel, "cell 5 2,align center center,grow 0 0");

        //---- loginLabel ----
        loginLabel.setText("Login");
        loginLabel.setFont(new Font("JetBrains Mono ExtraLight", Font.PLAIN, 18));
        add(loginLabel, "cell 5 6");

        //---- loginField ----
        loginField.setBackground(new Color(69, 95, 201));
        loginField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        loginField.setMinimumSize(new Dimension(49, 36));
        loginField.setOpaque(false);
        loginField.setPreferredSize(new Dimension(49, 36));
        add(loginField, "cell 5 6,growx,width 50:150:200,height 15:30:45");

        //---- passwordLabel ----
        passwordLabel.setText("Password");
        passwordLabel.setFont(new Font("JetBrains Mono ExtraLight", Font.PLAIN, 18));
        add(passwordLabel, "cell 5 7,width 30:50:100");

        //---- passwordField ----
        passwordField.setBackground(new Color(69, 95, 201));
        passwordField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        passwordField.setAlignmentX(1.0F);
        passwordField.setAlignmentY(1.0F);
        add(passwordField, "cell 5 7,growx,width 50:150:200,height 15:30:45");

        //---- hSpacer1 ----
        hSpacer1.setOpaque(false);
        add(hSpacer1, "cell 5 8,growx,width 30:50:70");

        //---- registerButton ----
        registerButton.setText("Register");
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                registerButtonMouseReleased(e);
            }
        });
        add(registerButton, "cell 5 8,aligny center,grow 100 0,width 100:150:200,height 30:30:50");

        //---- hSpacer2 ----
        hSpacer2.setOpaque(false);
        add(hSpacer2, "cell 5 9,growx,width 30:50:70");

        //---- backButton ----
        backButton.setText("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                backButtonMouseReleased(e);
            }
        });
        add(backButton, "cell 5 9,aligny center,grow 100 0,width 100:150:200,height 30:30:50");

        //---- vSpacer1 ----
        vSpacer1.setOpaque(false);
        add(vSpacer1, "cell 5 11,height 40:80:300");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel registerLabel;
    private JLabel loginLabel;
    private JTextField loginField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JPanel hSpacer1;
    private JButton registerButton;
    private JPanel hSpacer2;
    private JButton backButton;
    private JPanel vSpacer1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
