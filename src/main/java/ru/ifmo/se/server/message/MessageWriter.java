package ru.ifmo.se.server.message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class MessageWriter {
    private DatagramSocket socket;
    private MessageReader reader;

    public MessageWriter(DatagramSocket socket, MessageReader reader) throws IOException {
        this.socket = socket;
        this.reader = reader;
    }

    public void writeAnswer(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            // get the byte array of the object
            ByteBuffer Buf = ByteBuffer.wrap(baos.toByteArray());
            byte[] buf = new byte[Buf.remaining()];
            Buf.get(buf);

            /**int number = Buf.length;;
             byte[] data = new byte[4];

             // int -> byte[]
             for (int i = 0; i < 4; ++i) {
             int shift = i << 3; // i * 8
             data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
             }



             DatagramPacket packet = new DatagramPacket(data, 4, client, socket.getPort());
             socket.send(packet);
             **/
            // now send the payload
            DatagramPacket packet = new DatagramPacket(buf, buf.length, reader.getAddress());
            socket.send(packet);

            System.out.println("DONE SENDING");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
