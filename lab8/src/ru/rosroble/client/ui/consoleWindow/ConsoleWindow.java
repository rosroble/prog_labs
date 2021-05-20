/*
 * Created by JFormDesigner on Thu May 06 21:54:44 MSK 2021
 */

package ru.rosroble.client.ui.consoleWindow;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;
import ru.rosroble.client.ui.UIControl;
import ru.rosroble.client.ui.commandListWindow.CommandListWindow;
import ru.rosroble.client.ui.properties.LocaleBundle;

/**
 * @author unknown
 */
public class ConsoleWindow extends JPanel {
    private CommandListWindow cmdListWindow;
    public ConsoleWindow(CommandListWindow cmdListWindow) {
        this.cmdListWindow = cmdListWindow;
        initComponents();
    }

    private void backButtonMouseReleased(MouseEvent e) {
        UIControl.switchWindow(UIControl.getFrame(), cmdListWindow);
        this.consoleArea.setText("");
    }

    public void localize() {
        this.backButton.setText(LocaleBundle.getValue("back"));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        consoleArea = new JTextArea();
        backButton = new JButton();

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
            "[111,fill]",
            // rows
            "[]" +
            "[]" +
            "[119]" +
            "[]"));

        //---- consoleArea ----
        consoleArea.setEditable(false);
        consoleArea.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED, new Color(0, 84, 186), new Color(0, 55, 155), new Color(0, 49, 131), new Color(0, 70, 162)));
        consoleArea.setBackground(new Color(51, 153, 255));
        add(consoleArea, "cell 0 0 6 3,align center center,grow 0 0,width 200:500:700,height 200:400:600");

        //---- backButton ----
        backButton.setText("Back");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                backButtonMouseReleased(e);
            }
        });
        add(backButton, "cell 0 3 6 1,alignx center,growx 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JTextArea consoleArea;
    private JButton backButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public JTextArea getTextArea() {
        return consoleArea;
    }


}
