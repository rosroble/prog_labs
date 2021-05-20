package ru.rosroble.server;

import ru.rosroble.common.Request;
import ru.rosroble.common.Response;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerConnectionHandler implements Runnable {
    private Server server;
    private Socket clientSocket;
    private ServerRequestHandler requestHandler;
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);


    public ServerConnectionHandler(Server server, Socket clientSocket, ServerRequestHandler serverRequestHandler) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.requestHandler = serverRequestHandler;
    }

    @Override
    public void run() {
        Request request = null;
        Response response = null;
        try {
            ObjectInputStream clientInput = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream serverOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            while (true) {
                request = (Request) clientInput.readObject();
                response = requestHandler.processClientRequest(request);
                Response finalResponse = response;
                fixedThreadPool.submit(() -> {
                    try {
                        serverOutput.writeObject(finalResponse);
                        serverOutput.flush();
                    } catch (IOException e) {
                        System.out.println("Ошибка отправки данных на клиент!");
                    }
                });
            }
        } catch (IOException exception) {
            System.out.printf("Разрыв соединения с клиентом %s:%s\n", clientSocket.getInetAddress().toString(), clientSocket.getPort());
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
