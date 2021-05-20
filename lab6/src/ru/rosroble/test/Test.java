package ru.rosroble.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress ia = InetAddress.getLocalHost();
        System.out.println(ia);
    }
}
