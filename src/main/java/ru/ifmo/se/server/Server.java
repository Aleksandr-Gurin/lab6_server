package ru.ifmo.se.server;

import ru.ifmo.se.manager.Collection;
import ru.ifmo.se.musicians.MusicBand;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.*;
import java.util.LinkedHashSet;

public class Server {
    private final int port;
    private ServerData serverData;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[16000];

    public Server() throws SocketException {
        this.port = 5002;
        this.serverData = new ServerData();
        this.socket = new DatagramSocket(new InetSocketAddress(port));
    }

    public void run(LinkedHashSet<MusicBand> collection) {
        try {
            //Создается клиентская сессия
            ClientSession clientSession = new ClientSession(socket, this.serverData);
            this.serverData.getSessionsManger().addSession(clientSession);

            //Запуск логики работы с клиентом
            clientSession.run(collection);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
