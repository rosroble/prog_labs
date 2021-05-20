package ru.rosroble.server;

import ru.rosroble.common.Request;
import ru.rosroble.common.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class ChannelConnection {

    private int port;
    private ServerRequestHandler requestHandler;
    private ServerInputHandler serverInputHandler;
    private Selector selector;

    public ChannelConnection(int port, ServerRequestHandler requestHandler, ServerInputHandler serverInputHandler) {
        this.port = port;
        this.requestHandler = requestHandler;
        this.serverInputHandler = serverInputHandler;
    }
//    public void startCh() {
//        try {
//            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//            serverSocketChannel.configureBlocking(false);
//            ServerSocket serverSocket = serverSocketChannel.socket();
//            serverSocket.bind(new InetSocketAddress(port));
//            Selector selector = Selector.open();
//            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//            Response currentResponse = null;
//
//            while (true) {
//                int num = selector.select();
//                if (num == 0) continue;
//                Set<SelectionKey> keys = selector.selectedKeys();
//
//                for (SelectionKey key : keys) {
//                    if (key.isAcceptable()) {
//                        SocketChannel clientChannel = serverSocketChannel.accept();
//                        if (clientChannel == null) continue;
//                        clientChannel.configureBlocking(false);
//                        clientChannel.register(selector, SelectionKey.OP_READ);// read
//                    } else {
//                        if (key.isReadable()) {
//                            SocketChannel client = null;
//                            client = (SocketChannel) key.channel();
//                            client.read(buffer);
//                            buffer.flip();
//                            client.register(selector, SelectionKey.OP_READ);
//                            byte[] arr = buffer.array();
//                            Request r = deserialize(arr);
//                            currentResponse = requestHandler.processClientRequest(r);
//                            client.register(selector, SelectionKey.OP_WRITE);
//                            buffer.clear();
//                            break;
//                        }
//                        if (key.isWritable()) {
//                            SocketChannel clientResponse = null;
//                            clientResponse = (SocketChannel) key.channel();
//                            byte[] responseBytes = serialize(currentResponse);
//                            buffer.put(responseBytes);
//                            buffer.flip();
//                            clientResponse.write(buffer);
//                            clientResponse.register(selector, SelectionKey.OP_READ);
//                            buffer.clear();
//                        }
//                    }
//                }
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//
//
//    }

    private Request deserialize(byte[] buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Request request = (Request) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return request;
    }

    private byte[] serialize(Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        objectOutputStream.flush();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return buffer;
    }

    public void sendResponse(Response r) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(65536);
        SocketChannel channel = null;
        while (channel == null) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey key: selectionKeys) {
                if (key.isWritable()) {
                    channel = (SocketChannel) key.channel();
                    byte[] responseBytes = serialize(r);
                    buffer.put(responseBytes);
                    buffer.flip();
                    channel.write(buffer);
                    channel.register(selector, SelectionKey.OP_READ);
                    buffer.clear();
                    selectionKeys.remove(key);
                }
            }
        }
    }

    public Request getRequest() throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(65336);
        SocketChannel channel = null;
        while (channel == null) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey key: selectionKeys) {
                if (key.isReadable()) {
                    channel = (SocketChannel) key.channel();
                    channel.read(buffer);
                    buffer.flip();
                    if (!buffer.hasRemaining()) continue;
                    Request r = deserialize(buffer.array());
                    channel.register(selector, SelectionKey.OP_WRITE);
                    buffer.clear();
                    selectionKeys.remove(key);
                    return r;
                }
            }
        }
        return null;
    }

    public void resolveConnection(ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel channel = null;
        while (channel == null) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey: selectionKeys) {
                if (selectionKey.isAcceptable()) {
                    channel = serverSocketChannel.accept();
                    selectionKeys.remove(selectionKey);
                    if (channel != null) {
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else break;
                }
            }
            return;
        }
    }

    public void start()  {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(port));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                resolveConnection(serverSocketChannel);
                Request request = getRequest();
                if (request == null) continue;
                Response response = requestHandler.processClientRequest(request);
                sendResponse(response);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
