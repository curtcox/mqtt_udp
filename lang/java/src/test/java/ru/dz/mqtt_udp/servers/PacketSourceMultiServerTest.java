package ru.dz.mqtt_udp.servers;

import org.junit.Test;
import ru.dz.mqtt_udp.packets.PingReqPacket;

import static org.junit.Assert.assertNotNull;

public class PacketSourceMultiServerTest {

    @Test
    public void can_create() {
        assertNotNull(new PacketSourceMultiServer());
    }

    @Test
    public void processPacket_does_nothing_when_no_sinks() {
        PacketSourceMultiServer server = new PacketSourceMultiServer();
        server.processPacket(new PingReqPacket());
    }
}
