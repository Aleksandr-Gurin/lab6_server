package ru.ifmo.se.server;

import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.manager.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.util.LinkedHashSet;

public class ServerLauncher {
    public static void main(String[] args)  {
        try {
            FileManager fileManager = new FileManager();
            LinkedHashSet collectionSet = fileManager.readFile(args[0]);
                    Server server = new Server();
            Collection collection = new Collection(collectionSet);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.disconnect();
                fileManager.saveFile(collection);
                System.out.println("Все элементы коллекции сохранены");
            }));
            server.run(collection);
        }catch (FileNotFoundException | ArrayIndexOutOfBoundsException var1){
        }
        catch (SocketException var10) {
            var10.printStackTrace();
            System.exit(-1);
        }
    }
}
