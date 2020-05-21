package ru.ifmo.se.server;

import java.net.SocketException;

public class ServerLauncher {
    public static void main(String[] args) throws SocketException {
        Server server =  new Server();
        server.run();
    }
}
