package ru.dz.mqtt_udp.io;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.packets.PingReqPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static org.junit.Assert.*;
import static ru.dz.mqtt_udp.TestUtil.assertEqualBytes;

public class DatagramSocketIOTest {

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
        DatagramPacket[] packets = new DatagramPacket[1];
        DatagramSocket socket = new DatagramSocket() {
            @Override
            public void send(DatagramPacket p)  {
                packets[0] = p;
            }
        };
        DatagramSocketIO io = new DatagramSocketIO(socket);
        IPacket ping = new PingReqPacket();

        io.write(ping);

        DatagramPacket actual = packets[0];
        assertNotNull(actual);
        assertEqualBytes(ping.toBytes(),actual.getData());
    }

}
