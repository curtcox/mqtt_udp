package ru.dz.mqtt_udp.packets;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;

import java.io.IOException;

import static org.junit.Assert.*;

public class PacketsTest {

    @Test
    public void packets_written_to_net_can_be_read_from_net() throws IOException {
        IPacket.IO net = Packets.net;
        IPacket ping = new PingReqPacket();
        net.write(ping);
        IPacket read = net.read();

        assertEquals(ping,read);
    }
}
