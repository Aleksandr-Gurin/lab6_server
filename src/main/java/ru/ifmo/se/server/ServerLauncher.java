package ru.ifmo.se.server;

import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.manager.FileManager;
import ru.ifmo.se.musicians.MusicBand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedHashSet;

public class ServerLauncher {
    public static void main(String[] args) {
        try {
            FileManager fileManager = new FileManager();
            LinkedHashSet<MusicBand> collectionSet = fileManager.readFile(args[0]);
            int port = Integer.parseInt(args[1]);
            Collection collection = new Collection(collectionSet);
            Server server = new Server(port, collection);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                //server.disconnect();
                fileManager.saveFile(collection);
                System.out.println("Все элементы коллекции сохранены");
            }));

            server.run();
        } catch (FileNotFoundException var1) {
            System.out.println("Неправильно указан путь(java -jar Server.jar filepath port");
        } catch (SocketException var2) {
            var2.printStackTrace();
            System.exit(-1);
        } catch (IndexOutOfBoundsException var3) {
            System.out.println("Неправильно указаны аргументы(java -jar Server.jar filepath port)");
        }
    }
}
