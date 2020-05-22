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

    public ClientSession(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run(Collection collection) {
        MessageReader messageReader = new MessageReader(socket);
        MessageWriter messageWriter = new MessageWriter(socket, messageReader);
        FileManager fileManager = new FileManager();
        App app = new App(collection, fileManager);
        Controller controller = new Controller(collection, app, messageReader, messageWriter);
        while (socket.isBound()) {
            controller.executeCommand();
        }
    }
}
