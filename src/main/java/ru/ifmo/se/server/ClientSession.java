package ru.ifmo.se.server;

import ru.ifmo.se.commands.*;
import ru.ifmo.se.manager.App;
import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.manager.FileManager;
import ru.ifmo.se.musicians.MusicBand;
import ru.ifmo.se.server.message.MessageReader;
import ru.ifmo.se.server.message.MessageWriter;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class ClientSession {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private final ServerData serverData;
    private Controller controller;
    private App app;
    private Collection collection;
    private MessageReader messageReader;
    private FileManager fileManager;
    private MessageWriter messageWriter;
    private byte[]buf = new byte[16000];

    public ClientSession(DatagramSocket socket, final ServerData serverData) throws IOException {
        this.socket = socket;
        this.serverData = serverData;
    }

    public void run(LinkedHashSet<MusicBand> collectionSet) throws IOException {
        this.messageReader = new MessageReader(socket);
        messageWriter = new MessageWriter(socket, messageReader);
        this.fileManager = new FileManager();
        this.collection = new Collection(collectionSet);
        this.app = new App(collection, fileManager);
        this.controller = new Controller(collection , app, messageReader, messageWriter, socket);
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                socket.close();
                fileManager.saveFile(collection);
                System.out.println("Все элементы коллекции сохранены");
            }));
            Scanner scanner = new Scanner(System.in);
            while(true){
                controller.executeCommand();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {

    }
}
