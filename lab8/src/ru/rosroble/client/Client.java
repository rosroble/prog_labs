package ru.rosroble.client;

import ru.rosroble.client.ui.properties.LocaleBundle;
import ru.rosroble.common.CommandProcessor;
import ru.rosroble.common.Request;
import ru.rosroble.common.Response;
import ru.rosroble.common.commands.AuthCommand;
import ru.rosroble.common.commands.CommandType;
import ru.rosroble.common.commands.ExecuteScriptCommand;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private final static int MAX_RECONNECTION_ATTEMPTS = 3;
    private final static int RECONNECTION_TIMEOUT_IN_SECONDS = 1;
    private String host;
    private int port;
    private CommandProcessor cp;
    private String serverToken;
    private String username;
    private boolean isAuthed = false;
    private static Client client;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


    public Client(String host, int port, CommandProcessor cp) {
        this.host = host;
        this.port = port;
        this.cp = cp;
    }

    public static Client initClient(String host, int port, CommandProcessor cp) {
        if (client == null) {
            client = new Client(host, port, cp);
        }
        return client;
    }

    public static Client getClient() {
        if (client == null) throw new RuntimeException("Client is not initialized. Use initClient()");
        return client;
    }

    public String getUsername() {
        return username;
    }

    public void disconnect() {
        isAuthed = false;
        username = "";
        serverToken = "";
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        connect();
        System.out.println("Для выполнения команд требуется авторизация: auth reg/login <username>");
        exchangeDataWithServer();
        System.out.println("Клиент завершил свою работу.");
    }

    public boolean connect() {
        int reconnectionAttempt = 0;
        do {
            try {
                if (reconnectionAttempt > 0) {
                    Thread.sleep(RECONNECTION_TIMEOUT_IN_SECONDS * 1000);
                    System.out.printf("Пытаюсь переподключиться (попытка %d)\n", reconnectionAttempt);
                }
                socket = new Socket(host, port);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("Соединение установлено.");
                reconnectionAttempt = 0;
                return true;
            } catch (IOException e) {
                System.out.println("Ошибка подключения.");
                reconnectionAttempt++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (reconnectionAttempt <= MAX_RECONNECTION_ATTEMPTS);
        return false;
    }

    public void exchangeDataWithServer() {
        Request request = null;
        Response response = null;
        boolean latestRequestIsDelivered = true;
        while (true) {
            try {
                if (!latestRequestIsDelivered) {
                    request.addToken(serverToken);
                    sendRequest(request);
                    response = getResponse();
                    System.out.println(response.getResponseInfo());
                    latestRequestIsDelivered = true;
                }
                String[] commandSplit = cp.readCommand();
                request = cp.generateRequest(commandSplit);
                if (request.isEmpty()) continue;
                if (request.getCommand().getCommandType().equals(CommandType.EXIT)) break;
                if (isAuthed) {
                    request.addToken(serverToken);
                    request.addInitiator(username);
                }
                if (request.getCommand().getCommandType().equals(CommandType.EXECUTE_SCRIPT)) {
                    processExecuteScriptRequest(request);
                    continue;
                }
                sendRequest(request);
                response = getResponse();
                if (request.getCommand().getCommandType().equals(CommandType.AUTH)) {
                    AuthCommand auth = (AuthCommand) request.getCommand();
                    if (auth.getAuthType().equals(AuthCommand.AuthType.LOGIN) && response.isOK()) {
                        confirmAuthSuccess(response, request);
                    }
                }
                System.out.println(response.getResponseInfo());
            } catch (IOException e) {
                latestRequestIsDelivered = false;
                isAuthed = false;
                System.out.println("Потеряно соединение с сервером. Будет выполнена попытка переподключения.");
                boolean reconnectionSuccess = connect();
                if (reconnectionSuccess) {
                    // System.out.println("Требуется повторная авторизация: auth login <username>.");
                    continue;
                }
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void processExecuteScriptRequest(Request request) throws IOException, ClassNotFoundException {
        if (!isAuthed) {
            System.out.println("Требуется авторизация!");
            return;
        }
        ExecuteScriptCommand execScr = (ExecuteScriptCommand) request.getCommand();
        ArrayList<Request> script = cp.executeScript(execScr.getFilename());
        for (Request r: script) {
            if (r == null || r.isEmpty()) continue;
            r.addToken(serverToken);
            r.addInitiator(username);
            sendRequest(r);
            Response response = getResponse();
            if (response == null) continue;
            System.out.println(response.getResponseInfo());
        }
    }
    public void processExecuteScriptRequest(Request request, JTextArea output) throws IOException, ClassNotFoundException {
        if (!isAuthed) {
            System.out.println("Требуется авторизация!");
            return;
        }
        ExecuteScriptCommand execScr = (ExecuteScriptCommand) request.getCommand();
        ArrayList<Request> script = cp.executeScript(execScr.getFilename(), output);
        for (Request r: script) {
            if (r == null || r.isEmpty()) continue;
            r.addToken(serverToken);
            r.addInitiator(username);
            String commandType = r.getCommand().getCommandType().toString();
            sendRequest(r);
            Response response = getResponse();
            String ok = response.isOK() ? "OK" : "Fail";
            if (response == null) continue;
            output.append(commandType + ": " + ok + "\n");
            if (output == null) break;
            output.update(output.getGraphics());
        }
    }

    public boolean sendRequest(Request r) throws IOException {
        r.addToken(serverToken);
        r.addInitiator(username);
        oos.writeObject(r);
        oos.flush();
        return true;
    }


    public Response getResponse() throws IOException, ClassNotFoundException {
        Response response = (Response) ois.readObject();
        return response;
    }

    public void confirmAuthSuccess(Response response, Request request) {
        AuthCommand auth = (AuthCommand) request.getCommand();
        serverToken = response.getServerToken();
        username = auth.getUsername();
        isAuthed = true;
    }

    public String getServerToken() {
        return this.serverToken;
    }
}
