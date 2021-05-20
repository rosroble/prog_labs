package ru.rosroble.client.ui;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConsoleOutputStream extends OutputStream {
    private JTextArea textArea;

    public ConsoleOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        textArea.append(new String(String.valueOf((char)b).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
        textArea.setCaretPosition(textArea.getDocument().getLength());
        textArea.update(textArea.getGraphics());
    }
}