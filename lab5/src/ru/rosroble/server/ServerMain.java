package ru.rosroble.server;

public class ServerMain {
    public static void main(String[] args) {


        CollectionManager cm = new CollectionManager();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                cm.saveToFile();
            }
        });
        ServerConnectionHandler server = new ServerConnectionHandler(11321, new ServerRequestHandler(cm), new ServerInputHandler(cm));
        server.startCh();



    }
}
