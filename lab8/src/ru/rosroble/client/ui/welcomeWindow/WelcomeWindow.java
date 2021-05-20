/*
 * Created by JFormDesigner on Sat May 01 15:24:06 MSK 2021
 */

package ru.rosroble.client.ui.welcomeWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;
import ru.rosroble.client.Client;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.commandListWindow.CommandListWindow;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.client.ui.registerWindow.RegisterWindow;
import ru.rosroble.common.Request;
import ru.rosroble.common.Response;
import ru.rosroble.common.commands.AuthCommand;
import ru.rosroble.common.exceptions.DomainViolationException;

/**
 * @author unknown
 */
public class WelcomeWindow extends JPanel {

    private Client client;
    private String language = "English";
    private JFrame frame;
    private RegisterWindow registerWindow = new RegisterWindow(this);
    private CommandListWindow commandListWindow = new CommandListWindow(this);

    public WelcomeWindow(Client client)
    {
        this.client = client;
        initComponents();
        frame = UIControl.getFrame();
    }

    private void registerButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(frame, registerWindow);
    }

    private void enterButtonMouseReleased(MouseEvent e) {
        boolean isSuccessfullyConnected = client.connect();
        if (isSuccessfullyConnected) {
            String username = loginField.getText();
            String password = String.valueOf(passwordField.getPassword());
            try {
                Request authRequest = new Request(new AuthCommand(username, password, AuthCommand.AuthType.LOGIN));
                client.sendRequest(authRequest);
                Response response = client.getResponse();
                if (response.isOK()) {
                    client.confirmAuthSuccess(response, authRequest);
                    UIControl.switchWindow(frame, commandListWindow);
                    commandListWindow.setUsername(username);
                } else {
                    JOptionPane.showMessageDialog(null, LocaleBundle.getValue(response.getResponseInfo()));
                }
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            } catch (DomainViolationException exception) {
                System.out.println(exception.getMessage());
                JOptionPane.showMessageDialog(null, LocaleBundle.getValue(exception.getMessage()));
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error while connecting.");
        }
    }

    public void setUsernameText(String username) {
        this.loginField.setText(username);
    }

    public void setPasswordText(String password) {
        this.passwordField.setText(password);
    }

    private void languageBoxItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String item = (String) e.getItem();
            localize(item);
        }
    }

    private void localize(String language) {
        LocaleBundle.setCurrentLanguage(language);
        this.loginLabel.setText(LocaleBundle.getValue("login"));
        this.passwordLabel.setText(LocaleBundle.getValue("password"));
        this.enterButton.setText(LocaleBundle.getValue("enter"));
        this.registerButton.setText(LocaleBundle.getValue("nregister"));
        commandListWindow.localize();
        registerWindow.localize();
    }


    private void submitKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_ENTER) {
            enterButtonMouseReleased(null);
        }
    }

    private void passwordFieldKeyPressed(KeyEvent e) {
        // TODO add your code here
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        hSpacer3 = new JPanel(null);
        headerTicket = new JLabel();
        headerManager = new JLabel();
        hSpacer4 = new JPanel(null);
        loginLabel = new JLabel();
        loginField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        hSpacer1 = new JPanel(null);
        enterButton = new JButton();
        hSpacer2 = new JPanel(null);
        registerButton = new JButton();
        vSpacer2 = new JPanel(null);
        label1 = new JLabel();
        vSpacer1 = new JPanel(null);
        languageBox = new JComboBox<>();

        //======== this ========
        setPreferredSize(new Dimension(1920, 1080));
        setBackground(new Color(60, 96, 205));
        setMinimumSize(new Dimension(640, 480));
        setLayout(new MigLayout(
            "hidemode 3,alignx center",
            // columns
            "[left]" +
            "[142,fill]" +
            "[395,fill]" +
            "[180,right]",
            // rows
            "[]" +
            "[94]" +
            "[40:80:100,grow 3,center]" +
            "[]" +
            "[2]" +
            "[10]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[35]" +
            "[28]" +
            "[36]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- hSpacer3 ----
        hSpacer3.setOpaque(false);
        hSpacer3.setPreferredSize(new Dimension(8, 8));
        add(hSpacer3, "cell 2 2,width 10:20:30");

        //---- headerTicket ----
        headerTicket.setText("Ticket");
        headerTicket.setFont(new Font("Consolas", Font.BOLD, 48));
        headerTicket.setForeground(new Color(118, 194, 39, 170));
        add(headerTicket, "cell 2 2,alignx right,grow 0 100");

        //---- headerManager ----
        headerManager.setText("Manager");
        headerManager.setFont(new Font("Consolas", Font.BOLD, 48));
        headerManager.setForeground(new Color(47, 227, 134, 170));
        headerManager.setHorizontalAlignment(SwingConstants.CENTER);
        add(headerManager, "cell 2 2,align left center,grow 0 0");

        //---- hSpacer4 ----
        hSpacer4.setOpaque(false);
        add(hSpacer4, "cell 2 2,width 10:20:30");

        //---- loginLabel ----
        loginLabel.setText("Username:");
        loginLabel.setFont(new Font("JetBrains Mono ExtraLight", Font.PLAIN, 22));
        add(loginLabel, "cell 2 6,width 30:60:90");

        //---- loginField ----
        loginField.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        loginField.setBackground(new Color(69, 95, 201));
        loginField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        loginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                submitKeyPressed(e);
            }
        });
        add(loginField, "cell 2 6,width 50:150:200,height 15:30:45");

        //---- passwordLabel ----
        passwordLabel.setText("Password");
        passwordLabel.setFont(new Font("JetBrains Mono Light", Font.PLAIN, 22));
        add(passwordLabel, "cell 2 7,width 30:60:90");

        //---- passwordField ----
        passwordField.setBackground(new Color(69, 95, 201));
        passwordField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        passwordField.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                passwordFieldKeyPressed(e);
            }
        });
        add(passwordField, "cell 2 7,width 100:150:200,height 15:30:45");

        //---- hSpacer1 ----
        hSpacer1.setOpaque(false);
        add(hSpacer1, "cell 2 8,width 30:60:90");

        //---- enterButton ----
        enterButton.setText("Enter");
        enterButton.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 16));
        enterButton.setSelectedIcon(new ImageIcon("D:\\github_repos\\prog_labs\\chart_graphic_data_analysis_statistics_icon_186866.ico"));
        enterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                enterButtonMouseReleased(e);
            }
        });
        add(enterButton, "cell 2 8,aligny center,grow 100 0,width 100:150:200,height 30:30:50");

        //---- hSpacer2 ----
        hSpacer2.setOpaque(false);
        add(hSpacer2, "cell 2 9,width 30:60:90");

        //---- registerButton ----
        registerButton.setText("Not registered yet?");
        registerButton.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 16));
        registerButton.setMaximumSize(new Dimension(216, 30));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                registerButtonMouseReleased(e);
            }
        });
        add(registerButton, "cell 2 9,growx,width 100:150:200,height 30:30:50");

        //---- vSpacer2 ----
        vSpacer2.setOpaque(false);
        add(vSpacer2, "cell 2 10 1 4,height 50:100:1000");

        //---- label1 ----
        label1.setText("Choose language:");
        label1.setFont(new Font("JetBrains Mono ExtraLight", Font.PLAIN, 22));
        add(label1, "cell 2 14,alignx center,growx 0");

        //---- vSpacer1 ----
        vSpacer1.setOpaque(false);
        add(vSpacer1, "cell 2 15 1 3");

        //---- languageBox ----
        languageBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "English",
            "\u0420\u0443\u0441\u0441\u043a\u0438\u0439",
            "\u010cesko",
            "Shqiptare"
        }));
        languageBox.addItemListener(e -> languageBoxItemStateChanged(e));
        add(languageBox, "cell 2 15,aligny bottom,grow 100 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public Client getClient() {
        return this.client;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel hSpacer3;
    private JLabel headerTicket;
    private JLabel headerManager;
    private JPanel hSpacer4;
    private JLabel loginLabel;
    private JTextField loginField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JPanel hSpacer1;
    private JButton enterButton;
    private JPanel hSpacer2;
    private JButton registerButton;
    private JPanel vSpacer2;
    private JLabel label1;
    private JPanel vSpacer1;
    private JComboBox<String> languageBox;

    public void localize() {
        this.loginLabel.setText(LocaleBundle.getValue("login"));
        this.passwordLabel.setText(LocaleBundle.getValue("password"));
        this.enterButton.setText(LocaleBundle.getValue("enter"));
        this.registerButton.setText(LocaleBundle.getValue("nregister"));
        registerWindow.localize();
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
