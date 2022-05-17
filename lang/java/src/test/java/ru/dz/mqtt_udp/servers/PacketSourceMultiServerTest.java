package ru.dz.mqtt_udp.servers;

import org.junit.Test;
import ru.dz.mqtt_udp.IPacket;
import ru.dz.mqtt_udp.items.AbstractItem;
import ru.dz.mqtt_udp.packets.PingReqPacket;

import java.util.function.Consumer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

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

    @Test
    public void processPacket_notifies_added_packet_sink() {
        PacketSourceMultiServer server = new PacketSourceMultiServer();

        IPacket[] packets = new IPacket[1];
        server.addPacketSink(iPacket -> packets[0] = iPacket);

        IPacket expected = new PingReqPacket();
        server.processPacket(expected);

        assertSame(expected,packets[0]);
    }

    @Test
    public void processPacket_notifies_added_item_sink() {
        PacketSourceMultiServer server = new PacketSourceMultiServer();

        AbstractItem[] items = new AbstractItem[1];
        server.addItemSink(item -> items[0] = item);

        IPacket packet = new PingReqPacket();
        server.processPacket(packet);

        AbstractItem item = items[0];
        assertSame(packet.getType(),item.toPacket().getType());
    }

}
