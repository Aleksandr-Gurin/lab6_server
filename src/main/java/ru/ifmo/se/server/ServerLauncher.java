package ru.ifmo.se.server;

import ru.ifmo.se.manager.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.util.LinkedHashSet;

public class ServerLauncher {
    public static void main(String[] args) throws SocketException {
        try {
            FileManager fileManager = new FileManager();
            LinkedHashSet collection = fileManager.readFile(args[0]);
                    Server server = new Server();
            server.run(collection);
        }catch (FileNotFoundException var1){
        }
    }
}
