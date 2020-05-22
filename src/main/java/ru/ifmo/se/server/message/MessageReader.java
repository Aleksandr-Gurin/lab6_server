package ru.ifmo.se.server.message;

import ru.ifmo.se.commands.ClassCommand;
import ru.ifmo.se.musicians.MusicBand;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.Buffer;

public class MessageReader {
    private DatagramSocket socket;
    private SocketAddress address;

    public MessageReader(DatagramSocket socket) {
        this.socket = socket;
    }

    public Object readCommand() {
        Object object = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(16000);
            this.getBuffer(buffer);
            ((Buffer) buffer).flip();
            byte[] petitionBytes = new byte[buffer.remaining()];
            buffer.get(petitionBytes);
            if (petitionBytes.length > 0) {
                ByteArrayInputStream bais = new ByteArrayInputStream(petitionBytes);
                try {
                    ObjectInputStream oos = new ObjectInputStream(bais);
                    try {
                        object = oos.readObject();
                    }catch (IOException var8){
                        var8.printStackTrace();
                    }finally {
                        if(oos!=null){
                            oos.close();
                        }
                    }
                }catch (IOException var7){
                    var7.printStackTrace();
                }finally {
                    if (bais!=null){
                        bais.close();
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public int readLength(byte[] data) {
        int len = 0;
        // byte[] -> int
        for (int i = 0; i < 4; ++i) {
            len |= (data[3 - i] & 0xff) << (i << 3);
        }
        return len;
    }

    public void getBuffer(ByteBuffer buffer) throws IOException {
        /**byte[] data = new byte[4];
         int length = 0;
         DatagramPacket packet = new DatagramPacket(data, data.length);
         socket.receive(packet);
         length = readLength(data);
         // now we know the length of the payload
         **/
        byte[] buf = new byte[buffer.remaining()];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        this.socket.receive(packet);
        address = packet.getSocketAddress();
        buffer.put(buf, 0, packet.getLength());
    }

    public SocketAddress getAddress() {
        return address;
    }
}
