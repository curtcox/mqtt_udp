package ru.dz.mqtt_udp.io;

import org.junit.Before;
import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.packets.PingReqPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static org.junit.Assert.*;
import static ru.dz.mqtt_udp.TestUtil.assertEqualBytes;

public class DatagramSocketIOTest {

    static class InputDatagramSocket extends DatagramSocket {
        DatagramPacket packet;
        public InputDatagramSocket() throws SocketException {}
        @Override
        public void receive(DatagramPacket p) {
            p.setData(packet.getData());
        }
    }

    static class OutputDatagramSocket extends DatagramSocket {
        public OutputDatagramSocket() throws SocketException {}
       DatagramPacket packet;

        @Override
        public void send(DatagramPacket p)  {
            packet = p;
        }
    }

    InputDatagramSocket inputSocket;
    OutputDatagramSocket outputSocket;

    @Before
    public void createSockets() throws SocketException {
        inputSocket = new InputDatagramSocket();
        outputSocket = new OutputDatagramSocket();
    }
    @Test
    public void packets_written_to_IO_can_be_read_from_IO() throws IOException {
        IPacket.IO net = DatagramSocketIO.newInstance();
        IPacket ping = new PingReqPacket();

        net.write(ping);
        IPacket read = net.read();

        assertEquals(ping,read);
    }

    @Test
    public void packets_written_are_sent_via_the_given_socket() throws IOException {
        DatagramSocketIO io = new DatagramSocketIO(inputSocket,outputSocket);
        IPacket ping = new PingReqPacket();

        io.write(ping);

        DatagramPacket actual = outputSocket.packet;
        assertNotNull(actual);
        assertEqualBytes(ping.toBytes(),actual.getData());
    }

    @Test
    public void packets_are_read_from_the_given_socket() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[0],0,0);
        inputSocket.packet = packet;
        DatagramSocketIO io = new DatagramSocketIO(inputSocket,outputSocket);

        IPacket actual = io.read();

        assertNotNull(actual);
        assertEqualBytes(packet.getData(),actual.toBytes());
    }

}
