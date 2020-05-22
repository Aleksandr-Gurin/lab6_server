package ru.ifmo.se.server;

import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.musicians.MusicBand;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.*;
import java.util.LinkedHashSet;

public class Server {
    private ServerData serverData;
    private DatagramSocket socket;
    private Collection collection;

    public Server(int port, Collection collection) throws SocketException {
        this.collection = collection;
        this.serverData = new ServerData();
        this.socket = new DatagramSocket(new InetSocketAddress(port));
    }

    public void run() {
        //Создается клиентская сессия
        ClientSession clientSession = new ClientSession(socket);
        this.serverData.getSessionsManger().addSession(clientSession);

        //Запуск логики работы с клиентом

        clientSession.run(collection);
        socket.close();
    }

    public void disconnect() {
        socket.disconnect();
    }
}
