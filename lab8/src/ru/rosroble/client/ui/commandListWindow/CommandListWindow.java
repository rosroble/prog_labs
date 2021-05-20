/*
 * Created by JFormDesigner on Sun May 02 16:35:30 MSK 2021
 */

package ru.rosroble.client.ui.commandListWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

import jdk.nashorn.internal.scripts.JO;
import net.miginfocom.swing.*;
import ru.rosroble.client.Client;
import ru.rosroble.client.ui.consoleWindow.ConsoleWindow;
import ru.rosroble.client.ui.ticketBuildWindow.TicketBuildWindow;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.ticketShowcaseWindow.TicketShowcaseWindow;
import ru.rosroble.client.ui.venueBuildWindow.VenueBuildWindow;
import ru.rosroble.client.ui.visualizeWindow.VisualizeWindow;
import ru.rosroble.client.ui.welcomeWindow.WelcomeWindow;
import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.client.ui.updateWindow.TicketUpdateWindow;
import ru.rosroble.common.Parsers;
import ru.rosroble.common.Request;
import ru.rosroble.common.Response;
import ru.rosroble.common.commands.*;
import ru.rosroble.common.exceptions.DomainViolationException;

/**
 * @author unknown
 */
public class CommandListWindow extends JPanel {

    private WelcomeWindow welcomeWindow;
    private TicketBuildWindow ticketAddBuildWindow = new TicketBuildWindow(this, true);
    private TicketBuildWindow ticketRemoveLowerBuildWindow = new TicketBuildWindow(this, false);
    private ConsoleWindow consoleWindow = new ConsoleWindow(this);
    private TicketUpdateWindow ticketUpdateWindow= new TicketUpdateWindow(this);
    private TicketShowcaseWindow ticketShowcaseWindow = new TicketShowcaseWindow(this);
    private VenueBuildWindow venueBuildWindow = new VenueBuildWindow(this);
    private VisualizeWindow visualizeWindow = new VisualizeWindow(this);
    private String username;


    public CommandListWindow(WelcomeWindow welcomeWindow) {
        this.welcomeWindow = welcomeWindow;
        initComponents();
    }

    private void quitButtonMouseReleased(MouseEvent e) {
        System.exit(0);
    }

    private void changeUserButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), welcomeWindow);
        Client.getClient().disconnect();
    }

    private void addButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), ticketAddBuildWindow);
    }

    private void execScrButtonMouseReleased(MouseEvent e) {
        final JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            Client client = Client.getClient();
            try {
                UIControl.switchWindow(UIControl.getFrame(), consoleWindow);
                consoleWindow.validate();
                client.processExecuteScriptRequest(new ExecuteScriptCommand().execute(path), consoleWindow.getTextArea());
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void updateButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), ticketUpdateWindow);
    }

    private void showButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), ticketShowcaseWindow);
        ticketShowcaseWindow.initTable();
    }

    private void removeByRefButtonMouseReleased(MouseEvent e) {

        Client client = Client.getClient();
        UIManager.put("OptionPane.yesButtonText", "true");
        UIManager.put("OptionPane.noButtonText", "false");
        UIManager.put("OptionPane.cancelButtonText", LocaleBundle.getValue("cancel"));

        int choice = JOptionPane.showConfirmDialog(null,LocaleBundle.getValue("RemoveRefChoice"));
        try {
            if (choice == 0) {
                client.sendRequest(new Request(new RemoveAnyByRefundableCommand(true)));
                JOptionPane.showMessageDialog(null, client.getResponse().getResponseInfo());
            }
            if (choice == 1) {
                client.sendRequest(new Request(new RemoveAnyByRefundableCommand(false)));
                JOptionPane.showMessageDialog(null, client.getResponse().getResponseInfo());
            }
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private void countGrPriceMouseReleased(MouseEvent e) {
        while (true) {
            String input = JOptionPane.showInputDialog(LocaleBundle.getValue("countGrPrice"));
            if (input == null) break;
            try {
                double price = Parsers.parseThePrice(input);
                Client client = Client.getClient();
                client.sendRequest(new Request(new CountGreaterThanPriceCommand(price)));
                JOptionPane.showMessageDialog(null,
                        LocaleBundle.getValue("countGrPriceResponse") + client.getResponse().getResponseInfo());
                break;
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, LocaleBundle.getValue("priceDoubleExp"));
            } catch (DomainViolationException exception) {
                JOptionPane.showMessageDialog(null, LocaleBundle.getValue("priceError"));
            }
        }
    }

    private void clearButtonMouseReleased(MouseEvent e) {
        Client client = Client.getClient();
        try {
            client.sendRequest(new Request(new ClearCommand()));
            if (client.getResponse().isOK()) JOptionPane.showMessageDialog(null, LocaleBundle.getValue("success"));
        } catch (IOException | ClassNotFoundException exception) {
            JOptionPane.showMessageDialog(null, LocaleBundle.getValue("fail"));
        }
    }

    private void removeIdButtonMouseReleased(MouseEvent e) {
        Client client = Client.getClient();
        try {
            long id = Long.parseLong(JOptionPane.showInputDialog(null, LocaleBundle.getValue("enterId")));
            client.sendRequest(new Request(new RemoveByIdCommand(id)));
            Response response = client.getResponse();
            JOptionPane.showMessageDialog(null, LocaleBundle.getValue(response.getResponseInfo()));
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, LocaleBundle.getValue("idInvalid"));
        } catch (IOException | ClassNotFoundException exception) {
            JOptionPane.showMessageDialog(null, LocaleBundle.getValue("fail"));
        }
    }

    private void countLsVenueButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), venueBuildWindow);
    }

    private void removeLowerMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), ticketRemoveLowerBuildWindow);
    }

    private void visualizeButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), visualizeWindow);
        visualizeWindow.initListening();
    }

    private void helpButtonMouseReleased(MouseEvent e) {
        StringBuilder sb = new StringBuilder();
        sb
                .append(LocaleBundle.getValue("addButton") + " -- " + LocaleBundle.getValue("addDesc") + "\n")
                .append(LocaleBundle.getValue("clearButton") + " -- " + LocaleBundle.getValue("clearDesc") + "\n")
                .append(LocaleBundle.getValue("countGrPriceButton") +  " -- " +  LocaleBundle.getValue("countGrPriceDesc") + "\n")
                .append(LocaleBundle.getValue("countLsVenueButton") + " -- " + LocaleBundle.getValue("countLsVenueDesc") + "\n")
                .append(LocaleBundle.getValue("showButton") + " -- " +  LocaleBundle.getValue("showDesc"));
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void infoButtonMouseReleased(MouseEvent e) {
        Client client = Client.getClient();
        try {
            client.sendRequest(new Request(new InfoCommand()));
            String response = client.getResponse().getResponseInfo();
            String[] split = response.split(" ");
            JOptionPane.showMessageDialog(null, LocaleBundle.getValue("listType") + split[0]
            + "\n" + LocaleBundle.getValue("listSize") + split[1]);
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private void languageBoxItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String item = (String) e.getItem();
            localize(item);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        quitButton = new JButton();
        ticketMgLabel = new JLabel();
        changeUserButton = new JButton();
        addButton = new JButton();
        ticketIcon = new JLabel();
        removeIdButton = new JButton();
        clearButton = new JButton();
        removeLower = new JButton();
        countGrPrice = new JButton();
        removeByRefButton = new JButton();
        countLsVenueButton = new JButton();
        updateButton = new JButton();
        showButton = new JButton();
        loggedAsLabel = new JLabel();
        visualizeButton = new JButton();
        execScrButton = new JButton();
        helpButton = new JButton();
        infoButton = new JButton();
        languageBox = new JComboBox<>();

        //======== this ========
        setBackground(new Color(60, 96, 205));
        setLayout(new MigLayout(
            "hidemode 3,alignx center",
            // columns
            "[right]" +
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
            "[0,grow,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[0,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[left]" +
            "[left]" +
            "[37,left]" +
            "[11,left]",
            // rows
            "[]" +
            "[37]" +
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
            "[grow]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- quitButton ----
        quitButton.setText("Quit");
        quitButton.setBackground(Color.BLUE);
        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                quitButtonMouseReleased(e);
            }
        });
        add(quitButton, "cell 5 1 6 1,aligny center,growy 0,width 50:100:175");

        //---- ticketMgLabel ----
        ticketMgLabel.setText("Ticket Manager");
        ticketMgLabel.setFont(new Font("Droid Sans Mono Dotted", Font.PLAIN, 48));
        ticketMgLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        ticketMgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(ticketMgLabel, "cell 13 1,align center center,grow 0 0");

        //---- changeUserButton ----
        changeUserButton.setText("Change User");
        changeUserButton.setBackground(Color.yellow);
        changeUserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                changeUserButtonMouseReleased(e);
            }
        });
        add(changeUserButton, "cell 16 1 10 1,aligny center,growy 0,width 50:100:200");

        //---- addButton ----
        addButton.setText("Add");
        addButton.setBorder(new EtchedBorder());
        addButton.setBackground(new Color(204, 102, 0));
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                addButtonMouseReleased(e);
            }
        });
        add(addButton, "cell 5 5 6 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- ticketIcon ----
        ticketIcon.setMaximumSize(null);
        ticketIcon.setIcon(new ImageIcon("D:\\github_repos\\prog_labs\\lab8\\imgonline-com-ua-Resize-m6EGe2dOCz.png"));
        add(ticketIcon, "cell 13 5 1 7,align center center,grow 0 0");

        //---- removeIdButton ----
        removeIdButton.setText("Remove by ID");
        removeIdButton.setBorder(new EtchedBorder());
        removeIdButton.setBackground(new Color(19, 227, 146));
        removeIdButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                removeIdButtonMouseReleased(e);
            }
        });
        add(removeIdButton, "cell 16 5 10 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- clearButton ----
        clearButton.setText("Clear");
        clearButton.setBorder(new EtchedBorder());
        clearButton.setBackground(new Color(204, 102, 0));
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                clearButtonMouseReleased(e);
            }
        });
        add(clearButton, "cell 5 7 6 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- removeLower ----
        removeLower.setText("Remove Lower ");
        removeLower.setBorder(new EtchedBorder());
        removeLower.setBackground(new Color(19, 227, 146));
        removeLower.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                removeLowerMouseReleased(e);
            }
        });
        add(removeLower, "cell 16 7 10 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- countGrPrice ----
        countGrPrice.setText("Count > price");
        countGrPrice.setBorder(new EtchedBorder());
        countGrPrice.setBackground(new Color(204, 102, 0));
        countGrPrice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                countGrPriceMouseReleased(e);
            }
        });
        add(countGrPrice, "cell 5 9 6 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- removeByRefButton ----
        removeByRefButton.setText("Remove by ref");
        removeByRefButton.setBorder(new EtchedBorder());
        removeByRefButton.setBackground(new Color(19, 227, 146));
        removeByRefButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                removeByRefButtonMouseReleased(e);
            }
        });
        add(removeByRefButton, "cell 16 9 10 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- countLsVenueButton ----
        countLsVenueButton.setText("Count < venue");
        countLsVenueButton.setBorder(new EtchedBorder());
        countLsVenueButton.setBackground(new Color(204, 102, 0));
        countLsVenueButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                countLsVenueButtonMouseReleased(e);
            }
        });
        add(countLsVenueButton, "cell 5 11 6 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- updateButton ----
        updateButton.setText("Update by ID");
        updateButton.setBorder(new EtchedBorder());
        updateButton.setBackground(new Color(19, 227, 146));
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                updateButtonMouseReleased(e);
            }
        });
        add(updateButton, "cell 16 11 10 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- showButton ----
        showButton.setText("Show");
        showButton.setBorder(new EtchedBorder());
        showButton.setBackground(new Color(204, 102, 0));
        showButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showButtonMouseReleased(e);
            }
        });
        add(showButton, "cell 5 13 6 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- loggedAsLabel ----
        loggedAsLabel.setText("logged as:");
        loggedAsLabel.setFont(new Font("JetBrains Mono Thin", Font.PLAIN, 12));
        add(loggedAsLabel, "cell 13 13,alignx center,growx 0");

        //---- visualizeButton ----
        visualizeButton.setText("Visualize");
        visualizeButton.setBorder(new EtchedBorder());
        visualizeButton.setBackground(new Color(19, 227, 146));
        visualizeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                visualizeButtonMouseReleased(e);
            }
        });
        add(visualizeButton, "cell 16 13 10 1,aligny bottom,growy 0,width 50:150:150,height 30:40:50");

        //---- execScrButton ----
        execScrButton.setText("EXECUTE SCRIPT");
        execScrButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                execScrButtonMouseReleased(e);
            }
        });
        add(execScrButton, "cell 13 14,alignx center,growx 0,width 100:180:200");

        //---- helpButton ----
        helpButton.setText("HELP");
        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                helpButtonMouseReleased(e);
            }
        });
        add(helpButton, "cell 13 15,alignx center,growx 0,width 100:180:200");

        //---- infoButton ----
        infoButton.setText("INFO");
        infoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                infoButtonMouseReleased(e);
            }
        });
        add(infoButton, "cell 13 16,alignx center,growx 0,width 100:180:200");

        //---- languageBox ----
        languageBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "English",
            "\u0420\u0443\u0441\u0441\u043a\u0438\u0439",
            "\u010cesko",
            "Shqiptare"
        }));
        languageBox.addItemListener(e -> languageBoxItemStateChanged(e));
        add(languageBox, "cell 2 15 9 1,aligny bottom,grow 100 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

    }

    public void setUsername(String username) {
        this.username = username;
        this.loggedAsLabel.setText(LocaleBundle.getValue("logged_as") + username);
    }


    public void localize() {
        this.loggedAsLabel.setText(LocaleBundle.getValue("logged_as") + username);
        this.addButton.setText(LocaleBundle.getValue("addButton"));
        this.clearButton.setText(LocaleBundle.getValue("clearButton"));
        this.countGrPrice.setText(LocaleBundle.getValue("countGrPriceButton"));
        this.countLsVenueButton.setText(LocaleBundle.getValue("countLsVenueButton"));
        this.showButton.setText(LocaleBundle.getValue("showButton"));
        this.removeIdButton.setText(LocaleBundle.getValue("removeIdButton"));
        this.removeLower.setText(LocaleBundle.getValue("removeLowerButton"));
        this.removeByRefButton.setText(LocaleBundle.getValue("removeByRefButton"));
        this.updateButton.setText(LocaleBundle.getValue("updateByIdButton"));
        this.visualizeButton.setText(LocaleBundle.getValue("visualizeButton"));
        this.execScrButton.setText(LocaleBundle.getValue("execScrButton"));
        this.helpButton.setText(LocaleBundle.getValue("helpButton"));
        this.infoButton.setText(LocaleBundle.getValue("infoButton"));
        this.quitButton.setText(LocaleBundle.getValue("quitButton"));
        this.changeUserButton.setText(LocaleBundle.getValue("changeUserButton"));
        ticketAddBuildWindow.localize();
        ticketUpdateWindow.localize();
        ticketRemoveLowerBuildWindow.localize();
        consoleWindow.localize();
        ticketShowcaseWindow.localize();
        venueBuildWindow.localize();
        visualizeWindow.localize();

    }

    private void localize(String language) {
        LocaleBundle.setCurrentLanguage(language);
        this.loggedAsLabel.setText(LocaleBundle.getValue("logged_as") + username);
        this.addButton.setText(LocaleBundle.getValue("addButton"));
        this.clearButton.setText(LocaleBundle.getValue("clearButton"));
        this.countGrPrice.setText(LocaleBundle.getValue("countGrPriceButton"));
        this.countLsVenueButton.setText(LocaleBundle.getValue("countLsVenueButton"));
        this.showButton.setText(LocaleBundle.getValue("showButton"));
        this.removeIdButton.setText(LocaleBundle.getValue("removeIdButton"));
        this.removeLower.setText(LocaleBundle.getValue("removeLowerButton"));
        this.removeByRefButton.setText(LocaleBundle.getValue("removeByRefButton"));
        this.updateButton.setText(LocaleBundle.getValue("updateByIdButton"));
        this.visualizeButton.setText(LocaleBundle.getValue("visualizeButton"));
        this.execScrButton.setText(LocaleBundle.getValue("execScrButton"));
        this.helpButton.setText(LocaleBundle.getValue("helpButton"));
        this.infoButton.setText(LocaleBundle.getValue("infoButton"));
        this.quitButton.setText(LocaleBundle.getValue("quitButton"));
        this.changeUserButton.setText(LocaleBundle.getValue("changeUserButton"));
        ticketAddBuildWindow.localize();
        ticketUpdateWindow.localize();
        ticketRemoveLowerBuildWindow.localize();
        consoleWindow.localize();
        ticketShowcaseWindow.localize();
        venueBuildWindow.localize();
        visualizeWindow.localize();
        welcomeWindow.localize();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JButton quitButton;
    private JLabel ticketMgLabel;
    private JButton changeUserButton;
    private JButton addButton;
    private JLabel ticketIcon;
    private JButton removeIdButton;
    private JButton clearButton;
    private JButton removeLower;
    private JButton countGrPrice;
    private JButton removeByRefButton;
    private JButton countLsVenueButton;
    private JButton updateButton;
    private JButton showButton;
    private JLabel loggedAsLabel;
    private JButton visualizeButton;
    private JButton execScrButton;
    private JButton helpButton;
    private JButton infoButton;
    private JComboBox<String> languageBox;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
